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

package org.apache.guacamole.auth.http;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.http.user.HTTPAuthenticatedUser;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.UserContext;

/**
 * Guacamole authentication backend which authenticates users using an
 * arbitrary external HTTP basic. No storage for connections is
 * provided - only authentication. Storage must be provided by some other
 * extension.
 */
public class HTTPBasicAuthenticationProvider implements AuthenticationProvider {

    /**
     * The name of the application when generating the 401 request.
     */
    private static final String AUTH_NAME = "Guacamole HTTP Basic Authentication";

    /**
     * The name of the HTTP parameter to get the username from.
     */
    private static final String USERNAME_HEADER = "REMOTE_USER";

    /**
     * Injector which will manage the object graph of this authentication
     * provider.
     */
    private final Injector injector;

    /**
     * Provider for AuthenticatedUser objects.
     */
    @Inject
    private Provider<HTTPAuthenticatedUser> authenticatedUserProvider;

    /**
     * Creates a new HTTPBasicAuthenticationProvider that authenticates users
     * using HTTP basic.
     *
     * @throws GuacamoleException
     *     If a required property is missing, or an error occurs while parsing
     *     a property.
     */
    public HTTPBasicAuthenticationProvider() throws GuacamoleException {

        // Set up Guice injector.
        injector = Guice.createInjector(
            new HTTPBasicAuthenticationProviderModule(this)
        );

    }

    @Override
    public String getIdentifier() {
        return "http";
    }

    @Override
    public Object getResource() {
        return null;
    }

    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials)
            throws GuacamoleException {

        // Pull HTTP header from request if present
        HttpServletRequest request = credentials.getRequest();
        if (request != null) {

            // Get the username from the header configured in guacamole.properties
            String username = request.getHeader(USERNAME_HEADER);

            if (username != null) {
                HTTPAuthenticatedUser authenticatedUser = authenticatedUserProvider.get();
                authenticatedUser.init(username, credentials);
                return authenticatedUser;
            }

        }

        throw new GuacamoleUnauthorizedException(AUTH_NAME);

    }

    @Override
    public AuthenticatedUser updateAuthenticatedUser(
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {

        // No update necessary
        return authenticatedUser;

    }

    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser)
            throws GuacamoleException {

        // No associated data whatsoever
        return null;

    }

    @Override
    public UserContext updateUserContext(UserContext context,
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {

        // No update necessary
        return context;

    }

    @Override
    public UserContext decorate(UserContext context,
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {
        return context;
    }

    @Override
    public UserContext redecorate(UserContext decorated, UserContext context,
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {
        return context;
    }

    @Override
    public void shutdown() {
        // Do nothing
    }

}
