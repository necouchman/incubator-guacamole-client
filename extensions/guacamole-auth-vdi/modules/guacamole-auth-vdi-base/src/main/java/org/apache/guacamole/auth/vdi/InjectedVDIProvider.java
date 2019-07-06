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

package org.apache.guacamole.auth.vdi;

import com.google.inject.Injector;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.AuthenticatedUser;

/**
 * Provides a base implementation of an AuthenticationProvider which delegates
 * the various function calls to an underlying VDIProviderService
 * implementation. As such a service is injectable by Guice, this provides a
 * means for Guice to (effectively) apply dependency injection to an
 * AuthenticationProvider, even though it is the AuthenticationProvider that
 * serves as the entry point.
 */
public abstract class InjectedVDIProvider extends AbstractAuthenticationProvider {

    /**
     * The VDIProviderService to which all AuthenticationProvider calls will be
     * delegated.
     */
    private final VDIProviderService vdiProviderService;

    /**
     * Creates a new InjectedVDIProvider that delegates all calls to an
     * underlying VDIProviderService. The behavior of the InjectedVDIProvider
     * is defined by the given VDIProviderService implementation, which will be
     * injected by the Guice Injector provided by the given VDIInjectorProvider.
     *
     * @param injectorProvider
     *     A VDIInjectorProvider instance which provides singleton instances
     *     of a Guice Injector, pre-configured to set up all injections and
     *     access to the underlying Hypervisor.
     *
     * @param vdiProviderServiceClass
     *    The VDIProviderService implementation which defines the behavior of
     *    this AuthenticationProvider.
     *
     * @throws GuacamoleException
     *     If the Injector cannot be created due to an error.
     */
    public InjectedVDIProvider(VDIInjectorProvider injectorProvider,
            Class<? extends VDIProviderService> vdiProviderServiceClass)
        throws GuacamoleException {

        Injector injector = injectorProvider.get();
        vdiProviderService = injector.getInstance(vdiProviderServiceClass);

    }

    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser)
            throws GuacamoleException {
        return vdiProviderService.getUserContext(this, authenticatedUser);
    }

    @Override
    public UserContext updateUserContext(UserContext context,
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {
        return vdiProviderService.updateUserContext(this, context,
                authenticatedUser, credentials);
    }

}
