/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi.user;

import java.util.Set;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.DelegatingUserContext;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.vdi.connection.VDIConnectionDirectory;
import org.apache.guacamole.net.auth.vdi.connectiongroup.VDIConnectionGroupDirectory;

/**
 *
 * @author nick_couchman
 */
public class VDIUserContext extends DelegatingUserContext {
    
    private final Directory<Connection> connections;
    
    private final Directory<ConnectionGroup> connectionGroups;
    
    public VDIUserContext(UserContext userContext) throws GuacamoleException {
        super(userContext);
        connections = new VDIConnectionDirectory(super.getConnectionDirectory());
        connectionGroups = new VDIConnectionGroupDirectory(super.getConnectionGroupDirectory());
    }
    
    @Override
    public Directory<Connection> getConnectionDirectory() {
        return connections;
    }
    
    @Override
    public Directory<ConnectionGroup> getConnectionGroupDirectory() {
        return connectionGroups;
    }
    
    @Override
    public Set<Form> getConnectionAttributes() {
        
    }
    
    @Override
    public Set<Form> getConnectionGroupAttributes() {
        
    }
    
}
