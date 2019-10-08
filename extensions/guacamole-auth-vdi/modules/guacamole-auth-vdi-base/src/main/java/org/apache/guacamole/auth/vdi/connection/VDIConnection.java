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

package org.apache.guacamole.auth.vdi.connection;

import java.util.Collections;
import java.util.Map;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.TokenInjectingConnection;
import org.apache.guacamole.protocol.GuacamoleClientInformation;

/**
 *
 * @author nick_couchman
 */
public class VDIConnection extends TokenInjectingConnection {
    
    public VDIConnection(Connection object, Map<String, String> tokens) {
        super(object, tokens);
    }
    
    public VDIConnection(Connection object) {
        this(object, Collections.<String, String>emptyMap());
    }
    
    public Connection getUndecorated() {
        return super.getDelegateConnection();
    }
    
    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info,
            Map<String, String> tokens) throws GuacamoleException {
        
        // ToDo: Code to check number of current pool connections and
        // expand pool if necessary
        
        return super.connect(info, tokens);
    }
    
}
