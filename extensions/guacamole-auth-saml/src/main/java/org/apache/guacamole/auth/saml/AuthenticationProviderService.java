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

package org.apache.guacamole.auth.saml;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.saml.conf.ConfigurationService;
import org.apache.guacamole.auth.saml.user.SamlAuthenticatedUser;
import org.apache.guacamole.form.Field;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.credentials.CredentialsInfo;
import org.apache.guacamole.net.auth.credentials.GuacamoleInvalidCredentialsException;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.config.SAML2Configuration;
import org.pac4j.saml.profile.SAML2Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides injected authentication services for the SAML module.
 */
public class AuthenticationProviderService {
    
    /**
     * The logger for this class.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(AuthenticationProviderService.class);
    
    /**
     * Service for retrieving OpenID configuration information.
     */
    @Inject
    private ConfigurationService confService;
    
    /**
     * Provider for AuthenticatedUser objects.
     */
    @Inject
    private Provider<SamlAuthenticatedUser> authenticatedUserProvider;
    
    /**
     * Returns an AuthenticatedUser representing the user authenticated by the
     * given credentials.
     *
     * @param credentials
     *     The credentials to use for authentication.
     *
     * @return
     *     An AuthenticatedUser representing the user authenticated by the
     *     given credentials.
     *
     * @throws GuacamoleException
     *     If an error occurs while authenticating the user, or if access is
     *     denied.
     */
    public AuthenticatedUser authenticateUser(Credentials credentials)
            throws GuacamoleException {

        HttpServletRequest request = credentials.getRequest();
        SAML2Configuration cfg = confService.getSamlConfiguration();
        cfg.setAuthnRequestBindingType(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
        SAML2Client client = new SAML2Client(cfg);
        
        return new SamlAuthenticatedUser();

    }
    
    public AuthenticatedUser authenticateUser(SAML2Profile samlProfile,
            HttpServletRequest request) throws GuacamoleException {
        
        SamlAuthenticatedUser user = new SamlAuthenticatedUser();
        user.init(samlProfile, request);
        return user;
        
    }
    
}
