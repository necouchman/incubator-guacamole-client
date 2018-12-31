/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.register.connection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DelegatingConnection;

/**
 * A connection class that delegates all functions to another connection.
 */
public class RegistrarConnection extends DelegatingConnection {
    
    /**
     * The attribute name of the UUID of the connection.
     */
    public static final String REGISTERED_CONNECTION_UUID_ATTRIBUTE_NAME =
            "registered-uuid";
    
    /**
     * Create a new RegistrarConnection that wraps an existing connection.
     * 
     * @param connection
     *     The connection to wrap.
     */
    public RegistrarConnection(Connection connection) {
        super(connection);
        this.setUUID(UUID.randomUUID());
    }
    
    /**
     * Create a new RegistrarConnection that wraps an existing connection and
     * assigns the given UUID.
     * 
     * @param connection
     *     The connection to wrap.
     * 
     * @param uuid 
     *     The UUID of the connection.
     */
    public RegistrarConnection(Connection connection, UUID uuid) {
        super(connection);
        this.setUUID(uuid);
    }
    
    /**
     * Get the underlying, undecorated connection object.
     * 
     * @return
     *     The underlying, undecorated connection object.
     */
    public Connection getUndecorated() {
        return getDelegateConnection();
    }
    
    @Override
    public Map<String, String> getAttributes() {
        
        Map<String, String> attributes = new HashMap<>(super.getAttributes());
        attributes.remove(REGISTERED_CONNECTION_UUID_ATTRIBUTE_NAME);
        
        return attributes;
        
    }
    
    @Override
    public void setAttributes(Map<String, String> attributes) {
        
        attributes = new HashMap<>(attributes);
        attributes.remove(REGISTERED_CONNECTION_UUID_ATTRIBUTE_NAME);
        
        super.setAttributes(attributes);
    }
    
    final public UUID getUUID() {
        return UUID.fromString(super
                .getAttributes()
                .get(REGISTERED_CONNECTION_UUID_ATTRIBUTE_NAME));
    }
    
    final public void setUUID(UUID uuid) {
        super.getAttributes().put(REGISTERED_CONNECTION_UUID_ATTRIBUTE_NAME,
                uuid.toString());
    }
    
}
