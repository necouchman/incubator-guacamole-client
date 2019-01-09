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
import java.io.IOException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.environment.LocalEnvironment;
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
     * The message sender used to generate syslog messages.
     */
    private final AbstractSyslogMessageSender syslogSender;

    /**
     * Construct a new SyslogEventListener that gathers configuration information
     * and generates a new syslog sender with the configured parameters.
     * 
     * @throws GuacamoleException 
     *     If an error occurs retrieving configuration information.
     */
    public SyslogEventListener() throws GuacamoleException {
        
        // Get the configuration service
        ConfigurationService confService = new ConfigurationService(new LocalEnvironment());
        
        // Set up the syslog sender
        this.syslogSender = confService.getSyslogMessageSender();
        
        // Send an initial test message
        try {
            this.syslogSender.sendMessage("Syslog Event Listener loaded.");
        }
        catch (IOException e) {
            throw new GuacamoleServerException("Could not send message to syslog.", e);
        }
    }
    
    @Override
    public void handleEvent(Object event) throws GuacamoleException {
        
        SyslogMessage eventMessage = new SyslogMessage();
        
        // If for some reason the event is null, bail out.
        if (event == null)
            throw new GuacamoleServerException("Cannot handle non-existent event.");
        
        // Event is an authentication success
        if (event instanceof AuthenticationSuccessEvent) {
            eventMessage.withMsg("User "
                    + ((AuthenticationSuccessEvent) event).getAuthenticatedUser().getIdentifier()
                    + " has successfully logged on.");
        }
        
        // Event is an authentication failure
        else if (event instanceof AuthenticationFailureEvent) {
            eventMessage.withMsg("Failed login for "
                    + ((AuthenticationFailureEvent) event).getCredentials().getUsername()
                    + " from host "
                    + ((AuthenticationFailureEvent) event).getCredentials().getRemoteAddress()
                    + ".");
            eventMessage.setSeverity(Severity.ALERT);
        }
        
        // Event is a tunnel connection
        else if (event instanceof TunnelConnectEvent) {
            eventMessage.withMsg("Tunnel opened for "
                    + ((TunnelConnectEvent) event).getCredentials().getUsername()
                    + " with UUID "
                    + ((TunnelConnectEvent) event).getTunnel().getUUID().toString()
                    + ".");
        }
        
        // Event is a tunnel close
        else if (event instanceof TunnelCloseEvent) {
            eventMessage.withMsg("Tunnel closed for "
                    + ((TunnelCloseEvent) event).getCredentials().getUsername()
                    + " with UUID "
                    + ((TunnelCloseEvent) event).getTunnel().getUUID().toString()
                    + ".");
        }
        
        // Shouldn't ever get here, but some other kind of event
        else {
            eventMessage.withMsg("Unknown Guacamole event: "
                    + event.toString());
        }
        
        // Send the message to the syslog server
        try {
            this.syslogSender.sendMessage(eventMessage);
        }
        catch (IOException e) {
            throw new GuacamoleServerException("Error sending syslog message.", e);
        }
        
    }
    
}
