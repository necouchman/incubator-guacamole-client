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

package org.apache.guacamole.auth.saml.conf;

import com.google.inject.Inject;
import java.io.File;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.FileGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;
import org.pac4j.saml.config.SAML2Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author nick_couchman
 */
public class ConfigurationService {
    
    /**
     * Guacamole property which stores the location of the SAML keystore
     * for the client, relative to GUACAMOLE_HOME.
     */
    public static final FileGuacamoleProperty SAML_KEYSTORE =
            new FileGuacamoleProperty() {
            
        @Override
        public String getName() { return "saml-keystore"; }
                
    };
    
    /**
     * Property which stores the password used to access the SAML keystore.
     */
    public static final StringGuacamoleProperty SAML_KEYSTORE_PASSWORD =
            new StringGuacamoleProperty() {

        @Override
        public String getName() { return "saml-keystore-password"; }

    };
    
    /**
     * Property which stores the password for the private key file for the
     * SAML client.
     */
    public static final StringGuacamoleProperty SAML_PRIVATE_KEY_PASSWORD =
            new StringGuacamoleProperty() {
            
        @Override
        public String getName() { return "saml-private-key-password"; }
                
    };
    
    /**
     * Property which specifies the file that contains the IdP Metadata for the
     * SAML client, relative to GUACAMOLE_HOME.
     */
    public static final FileGuacamoleProperty SAML_IDP_METADATA =
            new FileGuacamoleProperty() {
    
        @Override
        public String getName() { return "saml-idp-metadata"; }
                
    };
    
    /**
     * The Guacamole server environment.
     */
    @Inject
    private Environment environment;
    
    /**
     * Return the file that contains the SAML client keystore, as configured
     * in guacamole.properties.
     * 
     * @return
     *     The file that contains the SAML client keystore.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be parsed, or the property is not
     *     specified in the file.
     */
    public File getSamlKeystore() throws GuacamoleException {
        return environment.getRequiredProperty(SAML_KEYSTORE);
    }
    
    /**
     * Return the password used to access the SAML keystore, as configured in
     * guacamole.properties.
     * 
     * @return
     *     The password used to access the SAML keystore.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be parsed, or the property is not
     *     specified in the file.
     */
    public String getSamlKeystorePassword() throws GuacamoleException {
        return environment.getRequiredProperty(SAML_KEYSTORE_PASSWORD);
    }
    
    /**
     * Return the password used to access the SAML private key file, as
     * configured in guacamole.properties.
     * 
     * @return
     *     The password used to access the SAML private key file.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be parsed, or the property is not
     *     specified in the file.
     */
    public String getSamlPrivateKeyPassword() throws GuacamoleException {
        return environment.getRequiredProperty(SAML_PRIVATE_KEY_PASSWORD);
    }
    
    /**
     * Return the file that contains the SAML IdP metadata, as configured in
     * guacamole.properties.
     * 
     * @return
     *     The file that contains the SAML IdP metadata.
     * 
     * @throws GuacamoleException 
     *     If guacamole.properties cannot be parsed, or the property is not
     *     specified in the file.
     */
    public File getSamlIdpMetadata() throws GuacamoleException {
        return environment.getRequiredProperty(SAML_IDP_METADATA);
    }
    
    public SAML2Configuration getSamlConfiguration() throws GuacamoleException {
        File guacHome = environment.getGuacamoleHome();
        File samlKeystore = new File(guacHome, getSamlKeystore().toString());
        File samlMetadata = new File(guacHome, getSamlIdpMetadata().toString());
        
        return new SAML2Configuration(
                new FileSystemResource(samlKeystore),
                getSamlKeystorePassword(),
                getSamlPrivateKeyPassword(),
                new FileSystemResource(samlMetadata)
        );
    }
}
