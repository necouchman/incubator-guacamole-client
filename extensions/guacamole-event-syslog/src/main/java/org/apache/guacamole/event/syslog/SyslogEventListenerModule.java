/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.event.syslog;

import com.google.inject.AbstractModule;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.event.syslog.conf.ConfigurationService;
import org.apache.guacamole.net.event.listener.Listener;

/**
 *
 * @author nick_couchman
 */
public class SyslogEventListenerModule extends AbstractModule {
    
    /**
     * Guacamole server environment.
     */
    private final Environment environment;
    
    /**
     * A reference to the Listener on behalf of which injection is being
     * performed.
     */
    private final Listener listener;
    
    public SyslogEventListenerModule(Listener listener)
        throws GuacamoleException {
        
        // Set up a new Guacamole Server environment.
        this.environment = new LocalEnvironment();
        
        // Assign the reference to the listener module.
        this.listener = listener;
    }
    
    @Override
    public void configure() {
        
        // Bind core implementations of guacamole-ext classes
        bind(Listener.class).toInstance(listener);
        bind(Environment.class).toInstance(environment);
        
        // Bind Syslog-specific classes
        bind(ConfigurationService.class);
        
    }
    
}
