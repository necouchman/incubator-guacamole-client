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

package org.apache.guacamole.event.syslog;

import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;
import com.cloudbees.syslog.sender.AbstractSyslogMessageSender;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.IOException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.event.syslog.conf.ConfigurationService;
import org.apache.guacamole.net.event.AuthenticationFailureEvent;
import org.apache.guacamole.net.event.AuthenticationSuccessEvent;
import org.apache.guacamole.net.event.TunnelCloseEvent;
import org.apache.guacamole.net.event.TunnelConnectEvent;
import org.apache.guacamole.net.event.listener.Listener;

/**
 * An event listener for sending Guacamole events to a syslog server.
 */
public class SyslogEventListener implements Listener {
    
    /**
     * Injector which will manage the object graph of this event listener.
     */
    private final Injector injector;
    
    /**
     * The message sender used to generate Syslog messages.
     */
    private final AbstractSyslogMessageSender syslogSender;

    public SyslogEventListener() throws GuacamoleException {
        this.injector = Guice.createInjector(new SyslogEventListenerModule(this));
        
        ConfigurationService confService = injector.getInstance(ConfigurationService.class);
        
        this.syslogSender = confService.getSyslogMessageSender();
        
        try {
            this.syslogSender.sendMessage("Syslog Event Listener loaded.");
        }
        catch (IOException e) {
            throw new GuacamoleServerException("Could not send message to syslog.", e);
        }
    }
    
    @Override
    public void handleEvent(Object event) throws GuacamoleException {
        StringBuilder sb = new StringBuilder();
        SyslogMessage eventMessage = new SyslogMessage();
        
        if (event == null)
            throw new GuacamoleServerException("Cannot handle non-existent event.");
        
        if (event instanceof AuthenticationSuccessEvent) {
            eventMessage.withMsg("User "
                    + ((AuthenticationSuccessEvent) event).getAuthenticatedUser().getIdentifier()
                    + " has successfully logged on.");
        }
        else if (event instanceof AuthenticationFailureEvent) {
            eventMessage.withMsg("Failed login for "
                    + ((AuthenticationFailureEvent) event).getCredentials().getUsername()
                    + " from host "
                    + ((AuthenticationFailureEvent) event).getCredentials().getRemoteAddress()
                    + ".");
            eventMessage.setSeverity(Severity.ALERT);
        }
        else if (event instanceof TunnelConnectEvent) {
            eventMessage.withMsg("Tunnel opened for "
                    + ((TunnelConnectEvent) event).getCredentials().getUsername()
                    + " with UUID "
                    + ((TunnelConnectEvent) event).getTunnel().getUUID().toString()
                    + ".");
        }
        else if (event instanceof TunnelCloseEvent) {
            eventMessage.withMsg("Tunnel closed for "
                    + ((TunnelCloseEvent) event).getCredentials().getUsername()
                    + " with UUID "
                    + ((TunnelCloseEvent) event).getTunnel().getUUID().toString()
                    + ".");
        }
        else {
            eventMessage.withMsg("Unknown Guacamole event: "
                    + event.toString());
        }
        
        try {
            this.syslogSender.sendMessage(eventMessage);
        }
        catch (IOException e) {
            throw new GuacamoleServerException("Error sending syslog message.", e);
        }
        
    }
    
}
