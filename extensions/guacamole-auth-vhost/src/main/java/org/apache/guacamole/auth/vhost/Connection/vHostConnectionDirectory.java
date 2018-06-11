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

package org.apache.guacamole.auth.vhost.Connection;

import java.util.Collection;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DecoratingDirectory;
import org.apache.guacamole.net.auth.Directory;

/**
 *
 * @author nick_couchman
 */
public class vHostConnectionDirectory extends DecoratingDirectory<Connection> {

    public vHostConnectionDirectory(Directory<Connection> directory, String vHost) 
            throws GuacamoleException {
        super(directory);
        
        Collection<Connection> connections = this.getAll(this.getIdentifiers());
        for (Connection connection : connections) {
            if (vHost != null && !vHost.isEmpty() &&
                    vHost.equals(((vHostConnection) connection).getVHost())) {
                continue;
            }
            directory.remove(connection.getIdentifier());
        }
    }
    
    @Override
    protected Connection decorate(Connection connection) {
        return new vHostConnection(connection);
    }
    
    @Override
    protected Connection undecorate(Connection connection) {
        assert(connection instanceof vHostConnection);
        return ((vHostConnection) connection).getUndecorated();
    }
    
}
