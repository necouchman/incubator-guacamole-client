/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.register.connection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DelegatingDirectory;
import org.apache.guacamole.net.auth.Directory;

/**
 *
 * @author nick_couchman
 */
public class RegistrarConnectionDirectory
        extends DelegatingDirectory<Connection> {
    
    public RegistrarConnectionDirectory(Directory<Connection> directory) {
        super(directory);
    }
    
    @Override
    public void add(Connection connection) throws GuacamoleException {
        super.add(connection);
    }
    
    @Override
    public void remove(String identifier) throws GuacamoleException {
        super.remove(identifier);
    }
    
    @Override
    public void update(Connection connection) throws GuacamoleException {
        super.update(connection);
    }
    
    @Override
    public Set<String> getIdentifiers() throws GuacamoleException {
        Set<String> myIds = new HashSet<>();
        for(String id : super.getIdentifiers()) {
            if (!(super.get(id) instanceof RegistrarConnection))
                myIds.add(id);
        }
        return myIds;
    }
    
    @Override
    public Collection<Connection> getAll(Collection<String> identifiers) throws GuacamoleException {
        Collection<Connection> myConns = new HashSet<>();
        for (Connection connection : super.getAll(identifiers)) {
            if (connection instanceof RegistrarConnection)
                myConns.add(connection);
        }
        return myConns;
    }
    
    public Collection<Connection> getAll() throws GuacamoleException {
        return getAll(getIdentifiers());
    }
    
    public RegistrarConnection getByUUID(UUID uuid) throws GuacamoleException
    {
        Collection<Connection> connections = getAll(getIdentifiers());
        for (Connection connection : connections) {
            if (connection instanceof RegistrarConnection
                    && ((RegistrarConnection) connection).getUUID().equals(uuid))
                return (RegistrarConnection)connection;
        }
        return null;
    }
    
}
