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

import com.google.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleSecurityException;
import org.apache.guacamole.auth.reverse.ReverseConnectionDirectory;
import org.apache.guacamole.auth.reverse.conf.ConfigurationService;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * This is a class that provides the RESTful endpoints for registering
 * connections dynamically.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReverseConnectionRegistrar {
    
    /**
     * The environment of the server.
     */
    @Inject
    private ConfigurationService confService;
    
    /**
     * The connection directory to use for this REST endpoint.  Connections
     * will be registered, updated, deleted, and retrieved from this directory.
     */
    private final ReverseConnectionDirectory directory;
    
    /**
     * Create a new REST endpoint against which to register, update, delete,
     * and retrieve connections.
     * 
     * @param directory 
     *     The directory to use for connections for this endpoint.
     */
    public ReverseConnectionRegistrar(ReverseConnectionDirectory directory) {
        this.directory = directory;
    }
    
    /**
     * Register a connection with the directory with the provided information.
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
    public String registerConnection(RegisteredConnection connection)
            throws GuacamoleException {
        
        if (!connection.getSecret().equals(confService.getSecretToken()))
            throw new GuacamoleSecurityException("Invalid secret specified");
        
        GuacamoleConfiguration registerConfig = new GuacamoleConfiguration();
        registerConfig.setConnectionID(connection.getName());
        registerConfig.setProtocol(connection.getProtocol());
        connection.getParameters().entrySet().forEach((param) -> {
            registerConfig.setParameter(param.getKey(), param.getValue());
        });
        
        return directory.create(connection.getName(), registerConfig);
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
    public String deleteConnection(String secret, String id)
            throws GuacamoleException {
        
        if(!secret.equals(confService.getSecretToken()))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        directory.delete(id);
        return id;
    }
    
    /**
     * Update the specified connection with new parameters.
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
    public String updateConnection(RegisteredConnection connection)
            throws GuacamoleException {
        
        if(!connection.getSecret().equals(confService.getSecretToken()))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        String id = connection.getIdentifier();
        GuacamoleConfiguration config =
                directory.get(id).getConfiguration();
        config.setProtocol(connection.getProtocol());
        
        connection.getParameters().entrySet().forEach((param) -> {
            config.setParameter(param.getKey(), param.getValue());
        });
        
        return id;
        
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
    public RegisteredConnection getConnection(@FormParam("secret") String secret,
            @FormParam("id") String id)
            throws GuacamoleException {
        
        if(!secret.equals(confService.getSecretToken()))
            throw new GuacamoleSecurityException("Invalid secret specified.");
        
        return new RegisteredConnection(directory.get(id));
        
    }
    
}
