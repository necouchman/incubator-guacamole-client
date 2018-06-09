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

package org.apache.guacamole.auth.vhost.Connection;

import java.util.HashMap;
import java.util.Map;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.DelegatingConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nick_couchman
 */
public class vHostConnection extends DelegatingConnection {
    
    /**
     * The logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(vHostConnection.class);
    
    /**
     * The attribute used to store the vHost.
     */
    public static final String VHOST_ATTRIBUTE = "vhost-hostname";
    
    /**
     * The undecorated version of the connection being
     * extended by this connection.
     */
    private final Connection undecorated;
    
    /**
     * The attributes specific to this vHostConnection class.
     */
    private final Map<String, String> attributes;
    
    /**
     * Create a new vHostConnection which decorates the
     * Connection specified by the undecorated attribute.
     * 
     * @param undecorated 
     *     The connection to decorate.
     */
    public vHostConnection(Connection undecorated) {
        super(undecorated);
        this.undecorated = undecorated;
        this.attributes = super.getAttributes();
    }
    
    /**
     * Get the original, undecorated connection.
     * 
     * @return
     *     The original, undecorated Connection object.
     */
    public Connection getUndecorated() {
        return undecorated;
    }
    
    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> uAttributes = new HashMap<String, String>(attributes);
        uAttributes.remove(VHOST_ATTRIBUTE);
        return uAttributes;
    }
    
    @Override
    public void setAttributes(Map<String, String> attributes) {
        Map<String, String> uAttributes = new HashMap<String, String>(attributes);
        uAttributes.remove(VHOST_ATTRIBUTE);
        super.setAttributes(uAttributes);
    }
    
    /**
     * Return the virtual host attribute associated with this
     * Connection object.
     * 
     * @return
     *     The virtual host attribute associated with this connection
     *     object.
     */
    public String getVHost() {
        return attributes.get(VHOST_ATTRIBUTE);
    }
    
    /**
     * Set the virtual host attribute to the specified parameter.
     * 
     * @param vhost
     *     The value to set the virtual host attribute to.
     */
    public void setVHost(String vhost) {
        attributes.put(VHOST_ATTRIBUTE, vhost);
    }
}
