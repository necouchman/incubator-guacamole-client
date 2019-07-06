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

package org.apache.guacamole.auth.vdi.aws.conf;

import com.google.inject.Inject;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 * Configuration management for the AWS VDI authentication module.
 */
public class ConfigurationService {
    
    /**
     * The local Guacamole Server environment.
     */
    @Inject
    private Environment environment;
    
    public static final StringGuacamoleProperty VDI_AWS_REGION = new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "vdi-aws-region"; }
        
    };
            
    public static final StringGuacamoleProperty VDI_AWS_ACCESS_KEY = new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "vdi-aws-access-key"; }
        
    };
    
    public static final StringGuacamoleProperty VDI_AWS_ACCESS_SECRET = new StringGuacamoleProperty() {
        
        @Override
        public String getName() { return "vdi-aws-access-secret"; }
        
    };
    
    /**
     * Return the AWS region as configured in the guacamole.properties file,
     * throwing an exception if an error occurs reading the file or if the
     * property is not present.
     * 
     * @return
     *     The AWS region as configured in the guacamole.properties file.
     * 
     * @throws GuacamoleException
     *     If guacamole.properties cannot be read or the property is not
     *     specified in the file.
     */
    public String getVdiAwsRegion() throws GuacamoleException {
        return environment.getRequiredProperty(VDI_AWS_REGION);
    }
    
    /**
     * Return the AWS access key as configured in the guacamole.properties file,
     * or throw an exception if the file cannot be read or the property is not
     * specified in the file.
     * 
     * @return
     *     The access key used to log in to AWS, as specified in the
     *     guacamole.properties file.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be read or the property is not
     *     specified in the file.
     */
    public String getVdiAwsAccessKey() throws GuacamoleException {
        return environment.getRequiredProperty(VDI_AWS_ACCESS_KEY);
    }
    
    /**
     * Return the access secret used to log in to AWS, as configured in the
     * guacamole.properties file, or throw an exception if the file cannot
     * be read or the property is not specified.
     * 
     * @return
     *     The access secret used to log in to AWS, as specified in the
     *     guacamole.properties file.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be read, or if the property is not
     *     specified in the file.
     */
    public String getVdiAwsAccessSecret() throws GuacamoleException {
        return environment.getRequiredProperty(VDI_AWS_ACCESS_SECRET);
    }
    
}
