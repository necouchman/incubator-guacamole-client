/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi.connection;

import java.util.Collection;
import java.util.Set;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DecoratingDirectory;
import org.apache.guacamole.net.auth.Directory;

/**
 *
 * @author nick_couchman
 */
public class VDIConnectionDirectory extends DecoratingDirectory<Connection> {
    
    public VDIConnectionDirectory(Directory<Connection> connections) {
        super(connections);
    }
    
    @Override
    public Connection decorate(Connection object) {
        if (!(object instanceof VDIConnection))
            return new VDIConnection(object);
        return object;
    }
    
    @Override
    public Connection undecorate(Connection object) {
        if (object instanceof VDIConnection)
            return ((VDIConnection) object).getUndecorated();
        return object;
    }
    
}
