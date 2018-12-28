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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.reverse.conf.ConfigurationService;
import org.apache.guacamole.auth.reverse.rest.ReverseConnectionRegistrar;
import org.apache.guacamole.auth.reverse.user.ReverseUserContext;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.UserContext;

/**
 * This class provides the necessary hooks into the Guacamole Client authentication
 * process so that the QuickConnect functionality can be initialized and used
 * throughout the web client.
 */
public class ReverseAuthenticationProvider extends AbstractAuthenticationProvider {

    /**
     * The Guice injector for this authentication provider.
     */
    private final Injector injector;
    
    /**
     * The configuration service for this module.
     */
    @Inject
    private ConfigurationService confService;
    
    /**
     * The connection directory for this module.
     */
    private final ReverseConnectionDirectory directory;
    
    /**
     * Create a new ReverseAuthenticationProvider instance and configure
     * the Guice injector.
     * 
     * @throws GuacamoleException
     *     If an error occurs instantiating the Guice module.
     */
    public ReverseAuthenticationProvider() throws GuacamoleException {
        
        // Set up Guice injector.
        this.injector = Guice.createInjector(
            new ReverseAuthenticationProviderModule(this)
        );
        
        this.directory = new ReverseConnectionDirectory();
        
    }
    
    @Override
    public String getIdentifier() {
        return "reverse";
    }
    
    @Override
    public Object getResource() throws GuacamoleException {
        return new ReverseConnectionRegistrar(directory,
                confService.getSecretToken());
    }
    
    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser)
            throws GuacamoleException {
        
        return new ReverseUserContext(
                this,
                authenticatedUser.getIdentifier(),
                directory);
        
    }

}
