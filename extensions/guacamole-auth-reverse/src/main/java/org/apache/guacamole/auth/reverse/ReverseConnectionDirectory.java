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

package org.apache.guacamole.auth.reverse;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.guacamole.auth.reverse.rest.RegisteredConnection;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.simple.SimpleDirectory;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * Connection directory that stores connections registered through the
 * REST endpoint.
 */
public class ReverseConnectionDirectory extends SimpleDirectory<Connection> {
    
    /**
     * The Map to store all connections in this directory.
     */
    private final Map<String, Connection> connections =
            new ConcurrentHashMap<>();
    
    /**
     * The root connection group for this directory, under which all connections
     * will be placed.
     */
    private final ReverseConnectionGroup rootGroup;
    
    /**
     * Internal connection identifier counter;
     */
    private final AtomicInteger connectionId;
    
    /**
     * Create a new connection directory with an empty connection group,
     * a new connection ID, and an empty set of connections.
     */
    public ReverseConnectionDirectory() {
        this.rootGroup = new ReverseConnectionGroup();
        this.connectionId = new AtomicInteger();
        super.setObjects(connections);
    }
    
    /**
     * Create a new connection directory using the specified root
     * connection group, a new connection ID, and an empty set of connections.
     * 
     * @param rootGroup
     *     The root group to use for the directory.
     */
    public ReverseConnectionDirectory(ConnectionGroup rootGroup) {
        this.rootGroup = (ReverseConnectionGroup)rootGroup;
        this.connectionId = new AtomicInteger();
        super.setObjects(connections);
    }
    
    /**
     * Get a new, random UUID to be used to identify a connection, and
     * convert it to a String before returning it.
     * 
     * @return 
     *     A String of the UUID of the connection to use.
     */
    private UUID getConnectionUuid() {
        return UUID.randomUUID();
    }
    
    /**
     * Get the next connection ID and increment the counter.
     * 
     * @return 
     *     The next available connection identifier.
     */
    private String getConnectionId() {
        return Integer.toString(connectionId.getAndIncrement());
    }
    
    /**
     * Given a name to identify the connection and a GuacamoleConfiguration,
     * create the Connection and add it to this directory, and return the
     * String value of the identifier of the new connection.
     * 
     * @param name
     *     The name to use for the connection.
     * 
     * @param config
     *     The GuacamoleConfiguration that should be associated with this
     *     connection.
     * 
     * @return 
     *     The String value of the identifier assigned to the new connection.
     */
    public String create(String name, GuacamoleConfiguration config) {

        UUID newUuid = getConnectionUuid();
        String newId = getConnectionId();
        Connection connection = new RegisteredConnection(name, newId, config, newUuid);
        connection.setParentIdentifier(rootGroup.getIdentifier());
        add(connection);
        
        rootGroup.addConnectionIdentifier(newId);
        
        return newUuid.toString();
    }
    
    /**
     * Return the number of items in the directory.
     * 
     * @return 
     *     The number of items in the directory.
     */
    public int size() {
        return connections.size();
    }
    
    /**
     * Return whether or not the directory is empty;
     * 
     * @return 
     *     Return true if the directory is empty, or false if there are items
     *     in the directory.
     */
    public boolean isEmpty() {
        return connections.isEmpty();
    }
    
    /**
     * Delete the specified connection from the directory.
     * 
     * @param id 
     *     The identifier of the connection to delete.
     */
    public void delete(String id) {
        connections.remove(id);
    }
    
    /**
     * Get the root connection group of this directory.
     * 
     * @return 
     *     The root connection group.
     */
    public ConnectionGroup getRootConnectionGroup() {
        return rootGroup;
    }
    
    @Override
    public void add(Connection connection) {
        connections.put(connection.getIdentifier(), connection);
    }
    
    /**
     * Look through connections and return the one that matches the
     * provided UUID.
     * 
     * @param uuid
     *     The UUID of the connection to search for.
     * 
     * @return 
     *     The connection that matches the UUID.
     */
    public RegisteredConnection getByUUID(UUID uuid) {
        for(Entry connection : connections.entrySet()) {
            RegisteredConnection tempConn = (RegisteredConnection)connection.getValue();
            if (tempConn.getUUID().equals(uuid))
                return tempConn;
        }
        return null;
    }
    
    public String getIdByUUID(UUID uuid) {
        for(Entry connection : connections.entrySet()) {
            RegisteredConnection tempConn = (RegisteredConnection)connection.getValue();
            if (tempConn.getUUID().equals(uuid))
                return connection.getKey().toString();
        }
        return null;
    }
    
}
