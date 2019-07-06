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

package org.apache.guacamole.auth.vdi.ovirt.conf;

import com.google.inject.Inject;
import java.net.URI;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.StringGuacamoleProperty;
import org.apache.guacamole.properties.URIGuacamoleProperty;

/**
 *
 * @author nick_couchman
 */
public class ConfigurationService {
    
    /**
     * The local Guacamole server environment.
     */
    @Inject
    private Environment environment;
    
    public static final URIGuacamoleProperty VDI_OVIRT_URL = new URIGuacamoleProperty() {
        
        @Override
        public String getName() { return "vdi-ovirt-url"; }
        
    };
    
    public static final StringGuacamoleProperty VDI_OVIRT_USERNAME = new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "vdi-ovirt-username"; }
        
    };
    
    public static final StringGuacamoleProperty VDI_OVIRT_PASSWORD = new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "vdi-ovirt-password"; }
        
    };
    
    /**
     * Get the URI for accessing the oVirt system, as configured in the
     * guacamole.properties file.
     * 
     * @return
     *     The URI for accessing the oVirt system, as configured in the
     *     guacamole.properties file.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be read or the property is not
     *     present in the file.
     */
    public URI getVdiOvirtUri() throws GuacamoleException {
        return environment.getRequiredProperty(VDI_OVIRT_URL);
    }
    
    /**
     * Get the username to log in to the oVirt system, as configured in the
     * guacamole.properties file.
     * 
     * @return
     *     The username for logging in to the oVirt system, as configured in
     *     the guacamole.properties file.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be read, or the property is not
     *     present in the file.
     */
    public String getVdiOvirtUsername() throws GuacamoleException {
        return environment.getRequiredProperty(VDI_OVIRT_USERNAME);
    }
    
    /**
     * Get the password to log in to the oVirt system, as configured in the
     * guacamole.properties file.
     * 
     * @return
     *     The password for logging in to the oVirt system, as configured in the
     *     guacamole.properties file.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be read, or the property is not
     *     present in the file.
     */
    public String getVdiOvirtPassword() throws GuacamoleException {
        return environment.getRequiredProperty(VDI_OVIRT_PASSWORD);
    }
    
}
