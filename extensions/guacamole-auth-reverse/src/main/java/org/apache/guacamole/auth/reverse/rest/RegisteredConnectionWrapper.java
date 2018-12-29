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

package org.apache.guacamole.auth.reverse.rest;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionRecord;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * A wrapper to make an APIConnection look like a Connection. Useful where a
 * org.apache.guacamole.net.auth.Connection is required.
 */
public class RegisteredConnectionWrapper implements Connection {

    /**
     * The wrapped APIConnection.
     */
    private final APIRegisteredConnection registeredConnection;

    /**
     * Creates a new APIConnectionWrapper which wraps the given APIConnection
     * as a Connection.
     *
     * @param registeredConnection
     *     The APIConnection to wrap.
     */
    public RegisteredConnectionWrapper(APIRegisteredConnection registeredConnection) {
        this.registeredConnection = registeredConnection;
    }

    @Override
    public String getName() {
        return registeredConnection.getName();
    }

    @Override
    public void setName(String name) {
        registeredConnection.setName(name);
    }

    @Override
    public String getIdentifier() {
        return registeredConnection.getIdentifier();
    }

    @Override
    public void setIdentifier(String identifier) {
        registeredConnection.setIdentifier(identifier);
    }

    @Override
    public String getParentIdentifier() {
        return registeredConnection.getParentIdentifier();
    }

    @Override
    public void setParentIdentifier(String parentIdentifier) {
        registeredConnection.setParentIdentifier(parentIdentifier);
    }

    @Override
    public int getActiveConnections() {
        return registeredConnection.getActiveConnections();
    }

    @Override
    public GuacamoleConfiguration getConfiguration() {
        
        // Create the GuacamoleConfiguration with current protocol
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol(registeredConnection.getProtocol());

        // Add parameters, if available
        Map<String, String> parameters = registeredConnection.getParameters();
        if (parameters != null)
            configuration.setParameters(parameters);
        
        return configuration;
    }

    @Override
    public void setConfiguration(GuacamoleConfiguration config) {
        
        // Set protocol and parameters
        registeredConnection.setProtocol(config.getProtocol());
        registeredConnection.setParameters(config.getParameters());

    }

    @Override
    public Map<String, String> getAttributes() {
        return registeredConnection.getAttributes();
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        registeredConnection.setAttributes(attributes);
    }

    @Override
    public Set<String> getSharingProfileIdentifiers() {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info,
            Map<String, String> tokens) throws GuacamoleException {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    public Date getLastActive() {
        return null;
    }

    @Override
    public List<? extends ConnectionRecord> getHistory() throws GuacamoleException {
        return Collections.<ConnectionRecord>emptyList();
    }
    
}
