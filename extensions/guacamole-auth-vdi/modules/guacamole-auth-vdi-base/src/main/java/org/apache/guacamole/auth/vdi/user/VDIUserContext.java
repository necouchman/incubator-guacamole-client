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

package org.apache.guacamole.auth.vdi.user;

import java.util.Collections;
import java.util.Set;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.DelegatingUserContext;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.auth.vdi.connection.VDIConnectionDirectory;
import org.apache.guacamole.auth.vdi.connectiongroup.VDIConnectionGroupDirectory;

/**
 * A UserContext that delegates functionality to a base object, for the purposes
 * of hosting VDI workloads.
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
        return Collections.emptySet();
    }
    
    @Override
    public Set<Form> getConnectionGroupAttributes() {
        return Collections.emptySet();
    }
    
}
