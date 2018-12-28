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

package org.apache.guacamole.auth.reverse.conf;

import com.google.inject.Inject;
import java.io.File;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.FileGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 * Configuration service for the Reverse Registrar module.
 */
public class ConfigurationService {
    
    /**
     * The Guacamole server environment.
     */
    @Inject
    private Environment environment;
    
    /**
     * The file that specifies the authentication information to use for
     * the Reverse registration module.
     */
    private static final FileGuacamoleProperty AUTH_FILE = 
            new FileGuacamoleProperty() {
               
        @Override
        public String getName() { return "reverse-auth-file"; }
                
    };
    
    /**
     * The secret token that will be required for all systems that attempt
     * to register their connection with this module.
     */
    private static final StringGuacamoleProperty SECRET_TOKEN =
            new StringGuacamoleProperty() {
    
        @Override
        public String getName() { return "reverse-secret-token"; }
                
    };
    
    /**
     * Return the authentication file used to authenticate systems attempting
     * to register with this module.  The file will be located in the
     * GUACAMOLE_HOME directory.
     * 
     * @return
     *     The file used to store the authentication data for systems attempting
     *     to register with this module, within the GUACAMOLE_HOME directory.
     * 
     * @throws GuacamoleException
     *     If an error occurs parsing the guacamole.properties file.
     */
    public File getAuthFile() throws GuacamoleException {
        return environment.getProperty(
                AUTH_FILE,
                new File(environment.getGuacamoleHome(),"reverse_auth")
        );
    }
    
    /**
     * Return the secret token required for all systems attempting to register
     * with this module.
     * 
     * @return
     *     The secret token required for all systems attempting to register
     *     with this module.
     * 
     * @throws GuacamoleException
     *     If an error occurs parsing the guacamole.properties file.
     */
    public String getSecretToken() throws GuacamoleException {
        return environment.getRequiredProperty(SECRET_TOKEN);
    }
    
}
