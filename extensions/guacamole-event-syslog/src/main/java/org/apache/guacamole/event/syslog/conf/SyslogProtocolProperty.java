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

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.properties.GuacamoleProperty;

/**
 * A GuacamoleProperty which is parsed for a SyslogProtocol value.  The
 * String values "tcp" and "udp" are parsed to their corresponding
 * SyslogProtocol enum values.
 */
public abstract class SyslogProtocolProperty
        implements GuacamoleProperty<SyslogProtocol> {

    @Override
    public SyslogProtocol parseValue(String value) throws GuacamoleException {
        
        // No value, return nothing
        if (value == null)
            return null;
        
        if (value.equals("tcp"))
            return SyslogProtocol.TCP;
        
        else if (value.equals("udp"))
            return SyslogProtocol.UDP;
        
        throw new GuacamoleServerException("Syslog protocol must be one of \"tcp\" or \"udp\".");
        
    }
    
}
