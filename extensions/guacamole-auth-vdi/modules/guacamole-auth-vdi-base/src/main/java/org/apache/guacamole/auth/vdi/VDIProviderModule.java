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
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.vdi.user.VDIUserContext;
import org.apache.guacamole.auth.vdi.user.VDIUserContextFactory;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.UserContext;

/**
 *
 * @author nick_couchman
 */
public abstract class VDIProviderModule extends AbstractModule {
    
    /**
     * The Guacamole Server environment.
     */
    private final Environment environment;
    
    /**
     * Creates a new VDIProviderModule, configuring dependency injection for
     * any implementing modules.
     * 
     * @throws GuacamoleException
     *     If the local server environment cannot be retrieved.
     */
    public VDIProviderModule() throws GuacamoleException {
        this.environment = new LocalEnvironment();
    }
    
    /**
     * Configures injection for implementation-specific interfaces for the
     * various supported VDI hypervisor modules.  Any sub-class must provide
     * an implementation of this method.
     */
    protected abstract void configureVdiProvider();
    
    /**
     * Returns the instance of the local server environment that will be
     * exposed to other classes via dependency injection.
     * 
     * @return 
     *     The instance of the local server environment that will be available
     *     to implementing classes.
     */
    protected Environment getEnvironment() {
        return environment;
    }
    
    @Override
    protected void configure() {
        
        // Bind the server environment
        bind(Environment.class).toInstance(environment);
        
        install(new FactoryModuleBuilder()
            .implement(UserContext.class, VDIUserContext.class)
            .build(VDIUserContextFactory.class));
        
        configureVdiProvider();
        
    }
    
}
