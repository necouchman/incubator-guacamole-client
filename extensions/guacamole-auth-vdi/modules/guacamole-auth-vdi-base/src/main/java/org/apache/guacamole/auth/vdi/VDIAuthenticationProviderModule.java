/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
