/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.register.rest;

import java.util.Map;
import org.apache.guacamole.auth.register.connection.RegistrarConnection;

/**
 * A connection used in the REST endpoint to represent connection data
 * to the REST client.
 */
public class APIRegistrarConnection {
    
    /**
     * The name of the connection.
     */
    private String name;
    
    /**
     * The protocol of the connection.
     */
    private String protocol;
    
    /**
     * The identifier of the connection.
     */
    private String identifier;
    
    /**
     * Parameters for the connection.
     */
    private Map<String, String> parameters;
    
    /**
     * Attributes of the connection.
     */
    private Map<String, String> attributes;
    
    /**
     * Create an empty APIRegistrarConnection.
     */
    public APIRegistrarConnection() {}
    
    public APIRegistrarConnection(RegistrarConnection srcConnection) {
        this.name = srcConnection.getName();
        this.identifier = srcConnection.getIdentifier();
        this.protocol = srcConnection.getConfiguration().getProtocol();
        this.attributes = srcConnection.getAttributes();
        this.parameters = srcConnection.getConfiguration().getParameters();
    }
    
    /**
     * Create a new APIRegistrarConnection and populate it with the specified
     * data.
     * 
     * @param name
     *     The name of the connection.
     * 
     * @param protocol
     *     The protocol to use for the connection.
     * 
     * @param identifier
     *     The identifier of the connection.
     * 
     * @param parameters
     *     A map of the connection parameters.
     * 
     * @param attributes 
     *     A map of the attributes of the connection.
     */
    public APIRegistrarConnection(String name, String protocol, String identifier,
            Map<String, String> parameters, Map<String, String> attributes) {
        this.name = name;
        this.protocol = protocol;
        this.identifier = identifier;
        this.parameters = parameters;
        this.attributes = attributes;
    }
    
    /**
     * Return the name of the connection.
     * 
     * @return 
     *     The name of the connection.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the name of the connection.
     * 
     * @param name 
     *     The name of the connection.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Return the protocol of the connection.
     * 
     * @return 
     *     The protocol of the connection.
     */
    public String getProtocol() {
        return protocol;
    }
    
    /**
     * Set the protocol of the connection.
     * 
     * @param protocol 
     *    The protocol of the connection.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    /**
     * Return the identifier of the connection.
     * 
     * @return 
     *     The identifier of the connection.
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Set the identifier of the connection.
     * 
     * @param identifier 
     *     The identifier of the connection.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Return the attributes of the connection.
     * 
     * @return 
     *     A map of the connection attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    /**
     * Set the connection attributes.
     * 
     * @param attributes 
     *     A map of attributes for the connection.
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Return the parameters of the connection.
     * 
     * @return 
     *     A map of connection parameters.
     */
    public Map<String, String> getParameters() {
        return parameters;
    }
    
    /**
     * Set the connection parameters.
     * 
     * @param parameters 
     *     A map of connection parameters.
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
}
