/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi.connectiongroup;

import org.apache.guacamole.auth.vdi.connection.*;
import java.util.Collection;
import java.util.Set;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.DecoratingDirectory;
import org.apache.guacamole.net.auth.Directory;

/**
 *
 * @author nick_couchman
 */
public class VDIConnectionGroupDirectory extends DecoratingDirectory<ConnectionGroup> {
    
    public VDIConnectionGroupDirectory(Directory<ConnectionGroup> connectionGroups) {
        super(connectionGroups);
    }
    
    @Override
    public ConnectionGroup decorate(ConnectionGroup object) {
        if (!(object instanceof VDIConnectionGroup))
            return new VDIConnectionGroup(object);
        return object;
    }
    
    @Override
    public ConnectionGroup undecorate(ConnectionGroup object) {
        if (object instanceof VDIConnectionGroup)
            return ((VDIConnectionGroup) object).getUndecorated();
        return object;
    }
    
}
