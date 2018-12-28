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

import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.reverse.ReverseConnectionDirectory;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * This is a class that provides the RESTful endpoints for registering
 * connections dynamically.
 */
public class ReverseConnectionRegistrar {
    
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
     * @param secret
     *     The secret token that must be provided in order to register a
     *     connection.  This is required.
     * 
     * @param name
     *     The name of the connection.  This is required.
     * 
     * @param protocol
     *     The protocol to use for the connection.  This is required.
     * 
     * @param hostname
     *     The host name of the system.  This is required.
     * 
     * @param port
     *     The port to use for the connection.  This is required.
     * 
     * @param username
     *     The username to be used in the connection.  This is optional -
     *     behavior of the connection if this parameter is omitted will depend
     *     upon the protocol and how the remote system behaves.
     * 
     * @param password
     *     The password to be used for the connection.  This is optional -
     *     behavior of the connection if this parameter is omitted will depend
     *     upon the protocol in use and how the remote system handles it.
     * 
     * @return
     *     The identifier of the registered connection.
     */
    @POST
    public String registerConnection(@FormParam("secret") String secret,
            @FormParam("name") String name, @FormParam("protocol") String protocol,
            @FormParam("hostname") String hostname, @FormParam("port") int port,
            @FormParam("username") String username,
            @FormParam("password") String password) {
        
        GuacamoleConfiguration registerConfig = new GuacamoleConfiguration();
        registerConfig.setConnectionID(name);
        registerConfig.setProtocol(protocol);
        registerConfig.setParameter("port", Integer.toString(port));
        registerConfig.setParameter("hostname", hostname);
        if (username != null && !username.isEmpty())
            registerConfig.setParameter("username", username);
        if (password != null && !password.isEmpty())
            registerConfig.setParameter("password", password);
        
        directory.create(name, registerConfig);
        
        return name;
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
     */
    @DELETE
    public String deleteConnection(@FormParam("secret") String secret,
            @FormParam("id") String id) {
        directory.delete(id);
        return id;
    }
    
    /**
     * Update the specified connection with new parameters.
     * 
     * @param secret
     *     The secret token required for interacting with this module.
     * 
     * @param id
     *     The identifier of the connection to update.
     * 
     * @param parameters
     *     The parameters to update in the connection.
     * 
     * @return
     *     The identifier of the connection that has been updated.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the existing connection to be updated.
     */
    @PUT
    public String updateConnection(@FormParam("secret") String secret,
            @FormParam("id") String id,
            @FormParam("params") Map<String, String> parameters)
            throws GuacamoleException {
        
        GuacamoleConfiguration config = directory.get(id).getConfiguration();
        if (parameters.containsKey("protocol"))
            config.setProtocol(parameters.remove("protocol"));
        
        parameters.entrySet().forEach((param) -> {
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
    public Connection getConnection(@FormParam("secret") String secret,
            @FormParam("id") String id)
            throws GuacamoleException {
        
        return directory.get(id);
        
    }
    
}
