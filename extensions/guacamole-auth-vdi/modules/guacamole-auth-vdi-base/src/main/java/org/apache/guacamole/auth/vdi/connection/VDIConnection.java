/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi.connection;

import java.util.Map;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DelegatingConnection;
import org.apache.guacamole.protocol.GuacamoleClientInformation;

/**
 *
 * @author nick_couchman
 */
public class VDIConnection extends DelegatingConnection {
    
    public VDIConnection(Connection object) {
        super(object);
    }
    
    public Connection getUndecorated() {
        return super.getDelegateConnection();
    }
    
    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info,
            Map<String, String> tokens) throws GuacamoleException {
        
        // ToDo: Code to check number of current pool connections and
        // expand pool if necessary
        
        return super.connect(info, tokens);
    }
    
}
