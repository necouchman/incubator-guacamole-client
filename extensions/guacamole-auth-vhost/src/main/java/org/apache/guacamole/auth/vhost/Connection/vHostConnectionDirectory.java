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
import org.apache.guacamole.net.auth.simple.SimpleDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A directory which decorates connections from another directory.  This one
 * is a bit...special...since it actually masks all connections except the ones
 * that are referred to by the specific virtual host attribute.
 */
public class vHostConnectionDirectory extends DecoratingDirectory<Connection> {
    
    /**
     * The logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(vHostConnectionDirectory.class);
    
    /**
     * 
     * @param directory
     * @param vHost
     * @throws GuacamoleException 
     */
    public vHostConnectionDirectory(Directory<Connection> directory, String vHost) 
            throws GuacamoleException {
        super(new SimpleDirectory<Connection>());
        
        Collection<Connection> connections = directory.getAll(this.getIdentifiers());
        for (Connection connection : connections) {
            if (vHost != null && !vHost.isEmpty() &&
                    vHost.equals(((vHostConnection) connection).getVHost())) {
                logger.debug(">>>VHOST<<< Match connection {} for vHost {}", connection.getName(), vHost);
                this.add(connection);
            }
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
