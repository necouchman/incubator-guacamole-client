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

package org.apache.guacamole.auth.saml.user;

import com.google.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.guacamole.net.auth.AbstractAuthenticatedUser;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.pac4j.saml.profile.SAML2Profile;

/**
 * A user authenticated via a SAML IdP.
 */
public class SamlAuthenticatedUser extends AbstractAuthenticatedUser {
    
    /**
     * Reference to the authentication provider associated with this
     * authenticated user.
     */
    @Inject
    private AuthenticationProvider authProvider;

    /**
     * The credentials provided when this user was authenticated.
     */
    private Credentials credentials;
    
    /**
     * The SAML profile that results from successful authentication.
     */
    private SAML2Profile samlProfile;
    
    /**
     * Initializes this AuthenticatedUser using the given username and
     * credentials.
     *
     * @param samlProfile
     *     The SAML profile used to authenticate the user.
     * 
     * @param request
     *     The HTTP request associated with the authentication attempt.
     */
    public void init(SAML2Profile samlProfile, HttpServletRequest request) {
        this.samlProfile = samlProfile;
        setIdentifier(samlProfile.getUsername());
        credentials = new Credentials(samlProfile.getUsername(), null, request);
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authProvider;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }
    
    /**
     * Get the SAML profile associated with this authenticated user.
     * 
     * @return 
     *     The SAML profile associated with this authenticated user.
     */
    public SAML2Profile getSamlProfile() {
        return samlProfile;
    }
    
}
