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

package org.apache.guacamole.event.syslog.conf;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.MessageFormat;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.sender.AbstractSyslogMessageSender;
import com.cloudbees.syslog.sender.TcpSyslogMessageSender;
import com.cloudbees.syslog.sender.UdpSyslogMessageSender;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.IntegerGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 * ConfigurationService class for the SyslogEventListener module that
 * pulls configuration data from guacamole.properties and provides
 * methods for retrieving the data.
 */
public class ConfigurationService {
    
    /**
     * The Guacamole server environment.
     */
    private final Environment environment;
    
    /**
     * The default application name for Syslog.
     */
    public final static String DEFAULT_APP_NAME = "Guacamole";
    
    /**
     * A property for specifying the syslog server to which events will
     * be forwarded.
     */
    public final static StringGuacamoleProperty SYSLOG_SERVER =
            new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "syslog-server"; }
                
    };
    
    /**
     * Specify the syslog port on the server to which events will be forwarded.
     */
    public final static IntegerGuacamoleProperty SYSLOG_PORT =
            new IntegerGuacamoleProperty() {
    
        @Override
        public String getName() { return "syslog-port"; }
                
    };
    
    /**
     * Specify the protocol - TCP or UDP - that will be used to send the syslog
     * messages.
     */
    public final static SyslogProtocolProperty SYSLOG_PROTOCOL =
            new SyslogProtocolProperty() {
    
        @Override
        public String getName() { return "syslog-protocol"; }
                
    };
    
    /**
     * Configure whether or not SSL/TLS will be used to communicate with the
     * syslog server.
     */
    public final static BooleanGuacamoleProperty SYSLOG_SSL =
            new BooleanGuacamoleProperty() {
           
        @Override
        public String getName() { return "syslog-ssl"; }
                
    };
    
    /**
     * The syslog facility to which messages will be logged.
     */
    public final static SyslogFacilityProperty SYSLOG_FACILITY =
            new SyslogFacilityProperty() {

        @Override
        public String getName() { return "syslog-facility"; }
                
    };
    
    /**
     * The name of the sender for the syslog messages.
     */
    public final static StringGuacamoleProperty SYSLOG_SENDER =
            new StringGuacamoleProperty() {
    
        @Override
        public String getName() { return "syslog-sender"; }
                
    };
    
    /**
     * The application name used in the syslog messages.
     */
    public final static StringGuacamoleProperty SYSLOG_APP =
            new StringGuacamoleProperty() {

        @Override
        public String getName() { return "syslog-app"; }
                
    };
    
    /**
     * The format of the syslog messages - either RFC3614, RC5424, or RFC5425.
     */
    public final static SyslogFormatProperty SYSLOG_FORMAT =
            new SyslogFormatProperty() {
    
        @Override
        public String getName() { return "syslog-format"; }
                
    };
    
    /**
     * Instantiate a new ConfigurationService class with the given Guacamole
     * Server environment.
     * 
     * @param environment 
     *     The environment in which this class is running, used to retrieve
     *     values from the guacamole.properties.
     */
    public ConfigurationService(Environment environment) {
        this.environment = environment;
    }
    
    /**
     * Retrieve the value of the syslog server where messages will be sent.
     * The default is to send to localhost.
     * 
     * @return
     *     The syslog server where messages will be sent.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    private String getSyslogServer() throws GuacamoleException {
        return environment.getProperty(SYSLOG_SERVER,
                "localhost"
        );
    }
    
    /**
     * Retrieve the value of the port number on the syslog server to use to
     * send syslog messages.  The default is 514, the default syslog port.
     * 
     * @return
     *     The port number on the remote server to send syslog messages to.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    private int getSyslogPort() throws GuacamoleException {
        return environment.getProperty(SYSLOG_PORT,
                514
        );
    }
    
    /**
     * Return true if SSL/TLS should be used to communicate with the syslog
     * server, otherwise false.  The default is false, SSL/TLS will not
     * be used.
     * 
     * @return
     *     True if SSL/TLS should be used to communicate with syslog, otherwise
     *     false.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    private boolean getSyslogSsl() throws GuacamoleException {
        return environment.getProperty(SYSLOG_SSL,
                false
        );
    }
    
    /**
     * Return the IP protocol - TCP or UDP - that should be used to communicate
     * with the remote syslog server.
     * 
     * @return
     *     The protocol - TCP or UDP - that should be used to communicate with
     *     the remote syslog server.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    private SyslogProtocol getSyslogProtocol() throws GuacamoleException {
        return environment.getProperty(SYSLOG_PROTOCOL,
                SyslogProtocol.UDP
        );
    }
    
    /**
     * Return the syslog facility where the Guacamole messages will be sent.
     * 
     * @return
     *     The syslog facility where Guacamole messages will be sent.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    public Facility getSyslogFacility() throws GuacamoleException {
        return environment.getProperty(SYSLOG_FACILITY,
                Facility.DAEMON
        );
    }
    
    /**
     * Return the format of syslog messages that will be sent.  Valid values
     * are RFC_3164, RFC_5424, and RFC_5425.  The default is RFC_3164.
     * 
     * @return
     *     The format of syslog messages that are sent to the remote server.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    private MessageFormat getSyslogMessageFormat() throws GuacamoleException {
        return environment.getProperty(SYSLOG_FORMAT,
                MessageFormat.RFC_3164
        );
    }
    
    /**
     * Return the application name that should be used for the syslog messages.
     * 
     * @return
     *     The application name that should be used for the syslog messages.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    public String getSyslogApp() throws GuacamoleException {
        return environment.getProperty(SYSLOG_APP,
                DEFAULT_APP_NAME
        );
    }
    
    /**
     * Return the name of the sender that should be sent with the syslog messages,
     * which is generally the hostname of the sending system.  The default is to
     * get the FQDN of the local server.
     * 
     * @return
     *     The name of the sender for syslog messages.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    private String getSyslogSender() throws GuacamoleException {
        try {
            return environment.getProperty(SYSLOG_SENDER,
                    InetAddress.getLocalHost().getCanonicalHostName()
            );
        }
        catch (UnknownHostException e) {
                throw new GuacamoleServerException("Error getting hostname of local host.", e);
        }
    }
    
    /**
     * Generate the SyslogMessageSender used to send messages to a remote syslog
     * server based on values in the gaucamole.properties file.
     * 
     * @return
     *     The SyslogMessageSender that will be used to send messages to a
     *     remote syslog server.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be parsed.
     */
    public AbstractSyslogMessageSender getSyslogMessageSender()
            throws GuacamoleException {
        
        AbstractSyslogMessageSender syslogSender;
        
        // Generate a TCP sender.
        if (getSyslogProtocol() == SyslogProtocol.TCP) {
            syslogSender = new TcpSyslogMessageSender();
            ((TcpSyslogMessageSender) syslogSender).setSyslogServerHostname(getSyslogServer());
            ((TcpSyslogMessageSender) syslogSender).setSyslogServerPort(getSyslogPort());
            if(getSyslogSsl())
                ((TcpSyslogMessageSender) syslogSender).setSsl(true);
        }
        
        // Generate a UDP sender.
        else {
            syslogSender = new UdpSyslogMessageSender();
            ((UdpSyslogMessageSender) syslogSender).setSyslogServerHostname(getSyslogServer());
            ((UdpSyslogMessageSender) syslogSender).setSyslogServerPort(getSyslogPort());
        }
        
        // Set up common properties
        syslogSender.setDefaultAppName(getSyslogSender());
        syslogSender.setDefaultFacility(getSyslogFacility());
        syslogSender.setDefaultAppName(getSyslogApp());
        syslogSender.setDefaultSeverity(Severity.INFORMATIONAL);
        syslogSender.setMessageFormat(getSyslogMessageFormat());
        
        return syslogSender;
    }
    
}
