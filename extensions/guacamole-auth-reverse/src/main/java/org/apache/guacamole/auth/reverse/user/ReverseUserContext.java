/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.reverse.user;

import java.util.Collections;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.reverse.ReverseConnectionDirectory;
import org.apache.guacamole.net.auth.AbstractUserContext;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.User;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleUser;

/**
 *
 * @author nick_couchman
 */
public class ReverseUserContext extends AbstractUserContext {
    
    /**
     * The unique identifier of the root connection group.
     */
    public static final String ROOT_IDENTIFIER = DEFAULT_ROOT_CONNECTION_GROUP;
    
    /**
     * The AuthenticationProvider that created this UserContext.
     */
    private final AuthenticationProvider authProvider;

    /**
     * Reference to the user whose permissions dictate the configurations
     * accessible within this UserContext.
     */
    private final User self;
    
    /**
     * The directory associated with this user context.
     */
    private final ReverseConnectionDirectory directory;
    
    /**
     * Construct a QuickConnectUserContext using the authProvider and
     * the username.
     *
     * @param authProvider
     *     The authentication provider module instantiating this
     *     this class.
     *
     * @param username
     *     The name of the user logging in that will be associated
     *     with this UserContext.
     * 
     * @param directory
     *     The connection directory associated with this user context.
     * 
     * @throws GuacamoleException
     *     If errors occur initializing the ConnectionGroup,
     *     ConnectionDirectory, or User.
     */
    public ReverseUserContext(AuthenticationProvider authProvider,
            String username, ReverseConnectionDirectory directory)
            throws GuacamoleException {

        // Initialize the connection directory
        this.directory = directory;

        // Initialize the user to a SimpleUser with the provided username,
        // no connections, and the single root group.
        this.self = new SimpleUser(username) {

            @Override
            public ObjectPermissionSet getConnectionPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(directory.getIdentifiers());
            }

            @Override
            public ObjectPermissionSet getConnectionGroupPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(Collections.singleton(ROOT_IDENTIFIER));
            }

        };

        // Set the authProvider to the calling authProvider object.
        this.authProvider = authProvider;

    }
    
    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authProvider;
    }
    
    @Override
    public User self() {
        return self;
    }
    
    @Override
    public ReverseConnectionDirectory getConnectionDirectory() {
        return directory;
    }
    
}
