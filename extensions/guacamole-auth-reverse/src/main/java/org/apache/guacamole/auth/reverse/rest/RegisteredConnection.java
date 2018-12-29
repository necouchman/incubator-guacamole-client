/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.reverse.rest;

import java.util.UUID;
import org.apache.guacamole.net.auth.simple.SimpleConnection;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * A class that extends SimpleConnection and adds items necessary
 * for registered connections.
 */
public class RegisteredConnection extends SimpleConnection {
    
    private final UUID uuid;
    
    public RegisteredConnection(UUID uuid) {
        super();
        this.uuid = uuid;
    }
    
    public RegisteredConnection(String name, String identifier,
            GuacamoleConfiguration config, UUID uuid) {
        super(name, identifier, config);
        this.uuid = uuid;
    }
    
    public UUID getUUID() {
        return uuid;
    }
    
}
