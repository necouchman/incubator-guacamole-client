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

package org.apache.guacamole.auth.vdi.aws;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.vdi.VDIProvider;

/**
 * A new VDI provider implementation that access AWS to execute the
 * VDI management.
 */
public class AWSVDIProvider extends VDIProvider {
    
    /**
     * Create a new VDI Provider instance that implements AWS-specific
     * methods for managing VDI systems.
     * 
     * @throws GuacamoleException 
     *     If configuration details cannot be read from guacamole.properties.
     */
    public AWSVDIProvider() throws GuacamoleException {
        super(new AWSVDIProviderModule());
    }
    
    @Override
    public String getIdentifier() {
        return "aws-vdi";
    }
    
}
