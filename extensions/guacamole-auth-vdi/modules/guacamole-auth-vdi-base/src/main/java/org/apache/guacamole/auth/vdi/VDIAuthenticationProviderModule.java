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

package org.apache.guacamole.auth.vdi;

import com.google.inject.AbstractModule;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.AuthenticationProvider;

/**
 * Base Guice module for dependency loading for VDI-related modules, which will
 * be concretely implemented by each of the Hyprevisor modules.
 */
public abstract class VDIAuthenticationProviderModule extends AbstractModule {
    
    protected final Environment environment;
    
    protected final AuthenticationProvider authProvider;
    
    public VDIAuthenticationProviderModule(AuthenticationProvider authProvider)
            throws GuacamoleException {
        this.authProvider = authProvider;
        this.environment = new LocalEnvironment();
    }
    
    @Override
    protected void configure() {
        
        // Bind core implementations of guacamole-ext classes
        bind(AuthenticationProvider.class).toInstance(authProvider);
        bind(Environment.class).toInstance(environment);
        
    }
    
}
