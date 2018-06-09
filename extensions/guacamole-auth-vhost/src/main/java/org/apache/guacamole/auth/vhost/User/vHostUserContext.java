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

package org.apache.guacamole.auth.vhost.User;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.vhost.Connection.vHostConnectionDirectory;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DelegatingUserContext;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.credentials.CredentialsInfo;
import org.apache.guacamole.net.auth.credentials.GuacamoleInvalidCredentialsException;

/**
 * User context that delegates to another context for the purposes
 * of vHost-based authentication and routing.
 */
public class vHostUserContext extends DelegatingUserContext {
    
    /**
     * The virtual host associated with this user context.
     */
    private final String vHost;
    
    /**
     * The directory for this context.
     */
    private final Directory<Connection> directory = 
            new vHostConnectionDirectory(super.getConnectionDirectory());
    
    /**
     * Create a new user context that decorates the existing user context
     * and adds the virtual host passed in from the request URI.
     * 
     * @param userContext
     *     The original userContext to decorate.
     * 
     * @param vHost
     *     The virtual host to add to the context.
     * 
     * @throws GuacamoleException
     *     If an error occurs locating a connection with the specified
     *     virtual host.
     */
    public vHostUserContext(UserContext userContext, String vHost) 
            throws GuacamoleException {
        super(userContext);
        this.vHost = vHost;
        
        if(!((vHostConnectionDirectory) directory).getAllvHosts().contains(vHost)) {
            throw new GuacamoleInvalidCredentialsException(
                    "Virtual Host not found.",
                    CredentialsInfo.USERNAME_PASSWORD);
        }
        
    }
    
    @Override
    public final Directory<Connection> getConnectionDirectory()
            throws GuacamoleException {
        
        return new vHostConnectionDirectory(super.getConnectionDirectory());

    }
    
    public String getVHost() {
        return vHost;
    }
    
}
