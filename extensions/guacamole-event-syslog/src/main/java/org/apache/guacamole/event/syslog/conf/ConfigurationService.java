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
import com.google.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.IntegerGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 *
 * @author nick_couchman
 */
public class ConfigurationService {
    
    /**
     * The Guacamole server environment.
     */
    @Inject
    private Environment environment;
    
    public final static String DEFAULT_APP_NAME = "Guacamole";
    
    public final static StringGuacamoleProperty SYSLOG_SERVER =
            new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "syslog-server"; }
                
    };
    
    public final static IntegerGuacamoleProperty SYSLOG_PORT =
            new IntegerGuacamoleProperty() {
    
        @Override
        public String getName() { return "syslog-port"; }
                
    };
    
    public final static SyslogProtocolProperty SYSLOG_PROTOCOL =
            new SyslogProtocolProperty() {
    
        @Override
        public String getName() { return "syslog-protocol"; }
                
    };
    
    public final static BooleanGuacamoleProperty SYSLOG_SSL =
            new BooleanGuacamoleProperty() {
           
        @Override
        public String getName() { return "syslog-ssl"; }
                
    };
    
    public final static SyslogFacilityProperty SYSLOG_FACILITY =
            new SyslogFacilityProperty() {

        @Override
        public String getName() { return "syslog-facility"; }
                
    };
    
    public final static StringGuacamoleProperty SYSLOG_SENDER =
            new StringGuacamoleProperty() {
    
        @Override
        public String getName() { return "syslog-sender"; }
                
    };
    
    public final static StringGuacamoleProperty SYSLOG_APP =
            new StringGuacamoleProperty() {

        @Override
        public String getName() { return "syslog-app"; }
                
    };
    
    public final static SyslogFormatProperty SYSLOG_FORMAT =
            new SyslogFormatProperty() {
    
        @Override
        public String getName() { return "syslog-format"; }
                
    };
    
    private String getSyslogServer() throws GuacamoleException {
        return environment.getProperty(SYSLOG_SERVER,
                "localhost"
        );
    }
    
    private int getSyslogPort() throws GuacamoleException {
        return environment.getProperty(SYSLOG_PORT,
                514
        );
    }
    
    private boolean getSyslogSsl() throws GuacamoleException {
        return environment.getProperty(SYSLOG_SSL,
                false
        );
    }
    
    private SyslogProtocol getSyslogProtocol() throws GuacamoleException {
        return environment.getProperty(SYSLOG_PROTOCOL,
                SyslogProtocol.UDP
        );
    }
    
    public Facility getSyslogFacility() throws GuacamoleException {
        return environment.getProperty(SYSLOG_FACILITY,
                Facility.DAEMON
        );
    }
    
    private MessageFormat getSyslogMessageFormat() throws GuacamoleException {
        return environment.getProperty(SYSLOG_FORMAT,
                MessageFormat.RFC_3164
        );
    }
    
    public String getSyslogApp() throws GuacamoleException {
        return environment.getProperty(SYSLOG_APP,
                DEFAULT_APP_NAME
        );
    }
    
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
    
    public AbstractSyslogMessageSender getSyslogMessageSender()
            throws GuacamoleException {
        
        AbstractSyslogMessageSender syslogSender;
        
        if (getSyslogProtocol() == SyslogProtocol.TCP) {
            syslogSender = new TcpSyslogMessageSender();
            ((TcpSyslogMessageSender) syslogSender).setSyslogServerHostname(getSyslogServer());
            ((TcpSyslogMessageSender) syslogSender).setSyslogServerPort(getSyslogPort());
            if(getSyslogSsl())
                ((TcpSyslogMessageSender) syslogSender).setSsl(true);
        }
        else {
            syslogSender = new UdpSyslogMessageSender();
            ((UdpSyslogMessageSender) syslogSender).setSyslogServerHostname(getSyslogServer());
            ((UdpSyslogMessageSender) syslogSender).setSyslogServerPort(getSyslogPort());
        }
        
        syslogSender.setDefaultAppName(getSyslogSender());
        syslogSender.setDefaultFacility(getSyslogFacility());
        syslogSender.setDefaultAppName(getSyslogApp());
        syslogSender.setDefaultSeverity(Severity.INFORMATIONAL);
        syslogSender.setMessageFormat(getSyslogMessageFormat());
        
        return syslogSender;
    }
    
}
