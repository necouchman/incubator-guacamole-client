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

package org.apache.guacamole.auth.saml;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;

/**
 * An authentication provider that delegates authentication and authorization
 * to a SAML Identity Provider (IdP) service.
 */
public class SamlAuthenticationProvider extends AbstractAuthenticationProvider {

    /**
     * Injector which will manage the object graph of this authentication
     * provider.
     */
    private final Injector injector;
    
    public SamlAuthenticationProvider() throws GuacamoleException {
        injector = Guice.createInjector(new SamlAuthenticationProviderModule(this));
    }
    
    @Override
    public String getIdentifier() {
        return "saml";
    }
    
    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) 
            throws GuacamoleException {
        
        // Authenticate the user via SAML
        AuthenticationProviderService authProviderService =
                injector.getInstance(AuthenticationProviderService.class);
        return authProviderService.authenticateUser(credentials);
    }
    
}
