/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.vdi.user.VDIUserContext;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.UserContext;

/**
 * Base VDI authentication provider class, which will be implemented by each
 * of the Hypervisor-specific modules.
 */
public abstract class VDIAuthenticationProvider extends AbstractAuthenticationProvider {
    
    @Override
    public UserContext decorate(UserContext context,
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {
        if (context instanceof VDIUserContext)
            return context;
        return new VDIUserContext(context);
    }
    
    @Override
    public UserContext redecorate(UserContext decorated, UserContext context,
            AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {
        if (context instanceof VDIUserContext)
            return context;
        return new VDIUserContext(context);
    }
    
}
