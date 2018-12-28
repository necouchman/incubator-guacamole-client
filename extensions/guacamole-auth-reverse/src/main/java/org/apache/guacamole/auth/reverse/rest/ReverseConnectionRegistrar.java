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

package org.apache.guacamole.auth.reverse.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleSecurityException;
import org.apache.guacamole.GuacamoleResourceNotFoundException;
import org.apache.guacamole.auth.reverse.ReverseConnectionDirectory;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * This is a class that provides the RESTful endpoints for registering
 * connections dynamically.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReverseConnectionRegistrar {
    
    /**
     * The connection directory to use for this REST endpoint.  Connections
     * will be registered, updated, deleted, and retrieved from this directory.
     */
    private final ReverseConnectionDirectory directory;
    
    /**
     * The secret token to use when authenticating registrations.
     */
    private final String secret;

    /**
     * Create a new REST endpoint against which to register, update, delete,
     * and retrieve connections.
     * 
     * @param directory 
     *     The directory to use for connections for this endpoint.
     * 
     * @param secret
     *     The secret to use to authenticate registrations.
     */
    public ReverseConnectionRegistrar(ReverseConnectionDirectory directory,
            String secret) {
        this.directory = directory;
        this.secret = secret;
    }
    
    /**
     * Register a connection with the directory with the provided information.
     * 
     * @param secret
     *     The secret to use when registering connections.
     * 
     * @param connection
     *     The connection to register.
     * 
     * @return
     *     The identifier of the registered connection.
     * 
     * @throws GuacamoleException
     *     If the secret doesn't match the configured secret.
     */
    @POST
    public Map<String,String> registerConnection(@QueryParam("secret") String secret,
            RegisteredConnection connection)
            throws GuacamoleException {
        
        if (!this.secret.equals(secret))
            throw new GuacamoleSecurityException("Invalid secret specified");
        
        GuacamoleConfiguration registerConfig = new GuacamoleConfiguration();
        registerConfig.setConnectionID(connection.getName());
        registerConfig.setProtocol(connection.getProtocol());
        connection.getParameters().entrySet().forEach((param) -> {
            registerConfig.setParameter(param.getKey(), param.getValue());
        });
        
        return Collections.<String, String>singletonMap("id",
                directory.create(connection.getName(), registerConfig));
    }
    
    /**
     * Delete or un-register the specified connection.
     * 
     * @param secret
     *     The secret token required for interacting with this module.
     * 
     * @param id
     *     The identifier of the connection to remove from the directory.
     * 
     * @return
     *     The identifier of the connection removed from the directory.
     * 
     * @throws GuacamoleException
     *     if an invalid secret is specified.
     */
    @DELETE
    public Map<String, String> deleteConnection(@QueryParam("secret") String secret,
            @QueryParam("id") String id)
            throws GuacamoleException {
        
        if(!this.secret.equals(secret))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        directory.delete(id);
        return Collections.<String, String>singletonMap("id", id);
    }
    
    /**
     * Update the specified connection with new parameters.
     * 
     * @param secret
     *     The secret to use to register connections.
     * 
     * @param id
     *     The identifier of the connection to update.
     * 
     * @param connection
     *     The connection to update.
     * 
     * @return
     *     The identifier of the connection that has been updated.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the existing connection to be updated.
     */
    @PUT
    public Map<String, String> updateConnection(@QueryParam("secret") String secret,
            @QueryParam("id") String id,
            RegisteredConnection connection)
            throws GuacamoleException {
        
        if(!this.secret.equals(secret))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        GuacamoleConfiguration config =
                directory.get(id).getConfiguration();
        config.setProtocol(connection.getProtocol());
        
        connection.getParameters().entrySet().forEach((param) -> {
            config.setParameter(param.getKey(), param.getValue());
        });
        
        return Collections.<String, String>singletonMap("id", id);
        
    }
    
    /**
     * Retrieve the specified connection.
     * 
     * @param secret
     *     The secret token to use when interacting with this module.
     * 
     * @param id
     *     The identifier of the connection to be retrieved.
     * 
     * @return
     *     The requested connection.
     * 
     * @throws GuacamoleException
     *     If the specified connection cannot be found or retrieved.
     */
    @GET
    public RegisteredConnection getConnection(@QueryParam("secret") String secret,
            @QueryParam("id") String id)
            throws GuacamoleException {
        
        if (!this.secret.equals(secret))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        if (directory.isEmpty())
            throw new GuacamoleResourceNotFoundException("Directory is empty.");
        
        Connection connection = directory.get(id);
        if (connection == null)
            throw new GuacamoleResourceNotFoundException("Connection does not exist.");
        
        return new RegisteredConnection(connection);
        
    }
    
    @GET
    public Map<String, RegisteredConnection> getAllConnections(
            @QueryParam("secret") String secret)
            throws GuacamoleException {
        
        if (!this.secret.equals(secret))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        if (directory.isEmpty())
            throw new GuacamoleResourceNotFoundException("Directory is empty.");
        
        Set<String> identifiers = directory.getIdentifiers();
        Map<String, RegisteredConnection> connections = new HashMap<>();
        identifiers.forEach((String id) -> {
            try {
                connections.put(id, new RegisteredConnection(directory.get(id)));
            }
            catch (GuacamoleException e) {
            }
        });
        
        return connections;
        
    }
    
}
