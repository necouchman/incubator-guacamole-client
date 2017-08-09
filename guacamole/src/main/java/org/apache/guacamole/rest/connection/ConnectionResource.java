/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.rest.connection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.form.Field;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleSecurityException;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionRecord;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.rest.directory.DirectoryView;
import org.apache.guacamole.net.auth.SharingProfile;
import org.apache.guacamole.net.auth.User;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.permission.ObjectPermission;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.permission.SystemPermission;
import org.apache.guacamole.net.auth.permission.SystemPermissionSet;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.rest.history.APIConnectionRecord;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.protocols.ProtocolInfo;
import org.apache.guacamole.rest.APIRequest;
import org.apache.guacamole.rest.directory.DirectoryObjectResource;
import org.apache.guacamole.rest.directory.DirectoryObjectTranslator;
import org.apache.guacamole.rest.directory.DirectoryResource;
import org.apache.guacamole.rest.directory.DirectoryResourceFactory;
import org.apache.guacamole.rest.sharingprofile.APISharingProfile;
import org.apache.guacamole.token.StandardTokens;
import org.apache.guacamole.token.TokenFilter;
import org.apache.guacamole.tunnel.TunnelRequestService;
import org.apache.guacamole.tunnel.http.HTTPTunnelRequest;

/**
 * A REST resource which abstracts the operations available on an existing
 * Connection.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConnectionResource extends DirectoryObjectResource<Connection, APIConnection> {

    /**
     * The UserContext associated with the Directory which contains the
     * Connection exposed by this resource.
     */
    private final UserContext userContext;

    /**
     * The Connection object represented by this ConnectionResource.
     */
    private final Connection connection;

    /**
     * A factory which can be used to create instances of resources representing
     * SharingProfiles.
     */
    @Inject
    private DirectoryResourceFactory<SharingProfile, APISharingProfile>
            sharingProfileDirectoryResourceFactory;

    /**
     * Service for handling tunnel requests.
     */
    @Inject
    private TunnelRequestService tunnelRequestService;

    /**
     * Creates a new ConnectionResource which exposes the operations and
     * subresources available for the given Connection.
     *
     * @param userContext
     *     The UserContext associated with the given Directory.
     *
     * @param directory
     *     The Directory which contains the given Connection.
     *
     * @param connection
     *     The Connection that this ConnectionResource should represent.
     *
     * @param translator
     *     A DirectoryObjectTranslator implementation which handles the type of
     *     object given.
     */
    @AssistedInject
    public ConnectionResource(@Assisted UserContext userContext,
            @Assisted Directory<Connection> directory,
            @Assisted Connection connection,
            DirectoryObjectTranslator<Connection, APIConnection> translator) {
        super(directory, connection, translator);
        this.userContext = userContext;
        this.connection = connection;
    }

    /**
     * Retrieves the parameters associated with a single connection.
     * 
     * @return
     *     A map of parameter name/value pairs.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the connection parameters.
     */
    @GET
    @Path("parameters")
    public Map<String, String> getConnectionParameters()
            throws GuacamoleException {

        User self = userContext.self();

        // Retrieve permission sets
        SystemPermissionSet systemPermissions = self.getSystemPermissions();
        ObjectPermissionSet connectionPermissions = self.getConnectionPermissions();

        // Deny access if adminstrative or update permission is missing
        String identifier = connection.getIdentifier();
        if (!systemPermissions.hasPermission(SystemPermission.Type.ADMINISTER)
         && !connectionPermissions.hasPermission(ObjectPermission.Type.UPDATE, identifier))
            throw new GuacamoleSecurityException("Permission to read connection parameters denied.");

        // Retrieve connection configuration
        GuacamoleConfiguration config = connection.getConfiguration();

        // Return parameter map
        return config.getParameters();

    }

    /**
     * Retrieves the prompts for the connection and matches them with the type of field, and returns
     * a list of fields to be prompted for.
     *
     * @return
     *     A list of fields to prompt for.
     *
     * @throws GuacamoleException
     *     If an error occurs retrieving prompts or the environment.
     */
    @GET
    @Path("prompts")
    public List<Field> getConnectionPrompts()
            throws GuacamoleException {

        List<Field> promptFields = new ArrayList<Field>();
        TokenFilter tokenFilter = new TokenFilter();
        List<String> connectionPrompts = tokenFilter.getPrompts(connection.getConfiguration().getParameters());
        if (connectionPrompts != null) {
            Environment env = new LocalEnvironment();
            Collection<Form> protocolForms = env.getProtocol(connection.getConfiguration().getProtocol()).getConnectionForms();
            paramLoop: for (String parameter : connectionPrompts) {
                for (Form form : protocolForms) {
                    Collection<Field> formFields = form.getFields();
                    for (Field field : formFields) {
                        if (parameter.equals(field.getName())) {
                            promptFields.add(field);
                            continue paramLoop;
                        }
                    }
                }
            }
        }

        return promptFields;

    }

    /**
     * Retrieves the usage history of a single connection.
     * 
     * @return
     *     A list of connection records, describing the start and end times of
     *     various usages of this connection.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the connection history.
     */
    @GET
    @Path("history")
    public List<APIConnectionRecord> getConnectionHistory()
            throws GuacamoleException {

        // Retrieve the requested connection's history
        List<APIConnectionRecord> apiRecords = new ArrayList<APIConnectionRecord>();
        for (ConnectionRecord record : connection.getHistory())
            apiRecords.add(new APIConnectionRecord(record));

        // Return the converted history
        return apiRecords;

    }

    /**
     * Returns a resource which provides read-only access to the subset of
     * SharingProfiles that the current user can use to share this connection.
     *
     * @return
     *     A resource which provides read-only access to the subset of
     *     SharingProfiles that the current user can use to share this
     *     connection.
     *
     * @throws GuacamoleException
     *     If the SharingProfiles associated with this connection cannot be
     *     retrieved.
     */
    @Path("sharingProfiles")
    public DirectoryResource<SharingProfile, APISharingProfile>
            getSharingProfileDirectoryResource() throws GuacamoleException {

        // Produce subset of all SharingProfiles, containing only those which
        // are associated with this connection
        Directory<SharingProfile> sharingProfiles = new DirectoryView<SharingProfile>(
            userContext.getSharingProfileDirectory(),
            connection.getSharingProfileIdentifiers()
        );

        // Return a new resource which provides access to only those SharingProfiles
        return sharingProfileDirectoryResourceFactory.create(userContext, sharingProfiles);

    }

    /**
     * Create a new tunnel for the specified connection, returning the UUID or
     * throwing a GuacamoleException if the tunnel cannot be created.
     *
     * @return
     *   The UUID of the newly-created tunnel in a string.
     *
     * @throws GucamoleException
     *     If the tunnel cannot be created.
     */
    @POST
    @Path("createTunnel")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Map<String, String> acquireConnectionTunnel(
            @Context HttpServletRequest consumedRequest,
            MultivaluedMap<String, String> parameters
            )
            throws GuacamoleException {

        // Reconstitute the HTTP request with the map of parameters
        HttpServletRequest request = new APIRequest(consumedRequest, parameters);

        GuacamoleTunnel tunnel = tunnelRequestService.createTunnel(new HTTPTunnelRequest(request));
        return Collections.<String, String>singletonMap("tunnelUUID", tunnel.getUUID().toString());

    }

}
