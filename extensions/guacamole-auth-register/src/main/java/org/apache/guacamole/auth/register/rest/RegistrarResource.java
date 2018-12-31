/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.register.rest;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.register.connection.RegistrarConnection;
import org.apache.guacamole.auth.register.connection.RegistrarConnectionDirectory;

/**
 * Class which provides REST endpoints for the module.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegistrarResource {
    
    /**
     * The directory that this REST endpoint resource will manipulate.
     */
    private final RegistrarConnectionDirectory directory;
    
    /**
     * The secret for allowing clients to talk to the REST endpoint.
     */
    private final String secret;
    
    /**
     * Create an instance of this REST endpoint resource with the given
     * directory.
     * 
     * @param directory
     *     The directory that will be manipulated from within this
     *     resource.
     * 
     * @param secret
     *     The secret to use for the client to talk to the REST endpoint.
     */
    public RegistrarResource(RegistrarConnectionDirectory directory,
            String secret) {
        this.directory = directory;
        this.secret = secret;
    }
    
    /**
     * Register the provided connection with the directory.
     * 
     * @param secret
     *     The secret token allowing the endpoints to register.
     * 
     * @param connection
     *     The connection to register.
     * 
     * @return
     *     The UUID of the new connection.
     */
    @POST
    public String registerConnection(@QueryParam("secret") String secret,
            APIRegistrarConnection connection) {
        
    }
    
    /**
     * Update the connection matching the provided UUID with the new
     * data in the connection.
     * 
     * @param secret
     *     The secret token allowing the agent to register.
     * 
     * @param uuid
     *     The UUID of the connection that is being updated.
     * 
     * @param connection
     *     The new connection data to update.
     * 
     * @return
     *     The UUID of the connection that is updated.
     */
    @PUT
    public String updateConnection(@QueryParam("secret") String secret,
            @QueryParam("uuid") String uuid,
            APIRegistrarConnection connection) {
        
    }
    
    /**
     * Delete the connection with the specified UUID.
     * 
     * @param secret
     *     The secret token allowing the remote system to update the
     *     directory.
     * 
     * @param uuid
     *     The UUID of the connection to delete.
     * 
     * @return
     *     The UUID of the connection that was deleted.
     */
    @DELETE
    public String deleteConnection(@QueryParam("secret") String secret,
            @QueryParam("uuid") String uuid) {
        
    }
    
    /**
     * Retrieve a Map of all connections by UUID within the directory.
     * 
     * @param secret
     *     The secret token to allow the remote system to talk to the
     *     REST endpoint.
     * 
     * @return
     *     A Map of all connections by UUID in the directory.
     */
    @GET
    public Map<String, APIRegistrarConnection> getAllConnections(
            @QueryParam("secret") String secret) {
        
    }
    
    /**
     * Retrieve a Map with a single entry - a UUID and the connection
     * that is specified by the UUID.
     * 
     * @param secret
     *     The secret token allowing the remote system to talk to the
     *     REST endpoint.
     * 
     * @param uuid
     *     The UUID of the connection to retrieve.
     * 
     * @return
     *     A Map with a single entry with the UUID as key and the
     *     connection specified by the UUID as the value.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the connection from the directory.
     */
    @GET
    @Path("{uuid}")
    public Map<String, APIRegistrarConnection> getConnection(
            @QueryParam("secret") String secret,
            @PathParam("uuid") String uuid) 
            throws GuacamoleException {
        
        if (this.secret.equals(secret))
            return Collections.<String, APIRegistrarConnection>singletonMap(uuid,
                    new APIRegistrarConnection(directory.getByUUID(UUID.fromString(uuid))));
        
        return null;
        
    }
    
}
