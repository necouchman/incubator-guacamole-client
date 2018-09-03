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

package org.apache.guacamole.auth.jdbc;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.jdbc.conf.RedisServerMode;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.auth.jdbc.security.PasswordPolicy;

/**
 * A JDBC-specific implementation of Environment that defines generic properties
 * intended for use within JDBC based authentication providers.
 */
public abstract class JDBCEnvironment extends LocalEnvironment {
    
    /**
     * The default Redis mode to use when connecting to Redis.
     */
    private static final RedisServerMode DEFAULT_REDIS_MODE = RedisServerMode.SINGLE;
    
    /**
     * The default URI to use for Redis support.
     */
    private static final List<URI> DEFAULT_REDIS_SERVER = Collections.singletonList(URI.create("redis://localhost:6379"));

    /**
     * The default database number to use for Redis support.
     */
    private static final int DEFAULT_REDIS_DB = 1;
    
    /**
     * Constructs a new JDBCEnvironment using an underlying LocalEnviroment to
     * read properties from the file system.
     * 
     * @throws GuacamoleException
     *     If an error occurs while setting up the underlying LocalEnvironment.
     */
    public JDBCEnvironment() throws GuacamoleException {
        super();
    }

    /**
     * Returns whether a database user account is required for authentication to
     * succeed, even if another authentication provider has already
     * authenticated the user.
     *
     * @return
     *     true if database user accounts are required for absolutely all
     *     authentication attempts, false otherwise.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the property.
     */
    public abstract boolean isUserRequired() throws GuacamoleException;

    /**
     * Returns the maximum number of concurrent connections to allow overall.
     * As this limit applies globally (independent of which connection is in
     * use or which user is using it), this setting cannot be overridden at the
     * connection level. Zero denotes unlimited.
     *
     * @return
     *     The maximum allowable number of concurrent connections.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the property.
     */
    public abstract int getAbsoluteMaxConnections() throws GuacamoleException;

    /**
     * Returns the default maximum number of concurrent connections to allow to 
     * any one connection, unless specified differently on an individual 
     * connection. Zero denotes unlimited.
     * 
     * @return
     *     The default maximum allowable number of concurrent connections 
     *     to any connection.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the property.
     */
    public abstract int getDefaultMaxConnections() throws GuacamoleException;

    /**
     * Returns the default maximum number of concurrent connections to allow to 
     * any one connection group, unless specified differently on an individual 
     * connection group. Zero denotes unlimited.
     * 
     * @return
     *     The default maximum allowable number of concurrent connections
     *     to any connection group.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the property.
     */
    public abstract int getDefaultMaxGroupConnections()
            throws GuacamoleException;
    
    /**
     * Returns the default maximum number of concurrent connections to allow to 
     * any one connection by an individual user, unless specified differently on
     * an individual connection. Zero denotes unlimited.
     * 
     * @return
     *     The default maximum allowable number of concurrent connections to
     *     any connection by an individual user.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the property.
     */
    public abstract int getDefaultMaxConnectionsPerUser()
            throws GuacamoleException;
    
    /**
     * Returns the default maximum number of concurrent connections to allow to 
     * any one connection group by an individual user, unless specified 
     * differently on an individual connection group. Zero denotes unlimited.
     * 
     * @return
     *     The default maximum allowable number of concurrent connections to
     *     any connection group by an individual user.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the property.
     */
    public abstract int getDefaultMaxGroupConnectionsPerUser()
            throws GuacamoleException;

    /**
     * Returns the policy which applies to newly-set passwords. Passwords which
     * apply to Guacamole user accounts will be required to conform to this
     * policy.
     *
     * @return
     *     The password policy which applies to Guacamole user accounts.
     */
    public abstract PasswordPolicy getPasswordPolicy();
    
    /**
     * Returns a boolean value that determines whether or not the JDBC module
     * will use Redis for storing active connections.  A boolean true is returned
     * if the module is configured to support it, otherwise file.  The default
     * is false.
     * 
     * @return
     *     Returns a boolean true if the JDBC module should use Redis for
     *     storing active connection data, or false otherwise.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property.
     */
    public boolean getUseRedis() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_USE_REDIS,
                false
        );
    }
    
    /**
     * Get the mode used to connect to the Redis server or servers as configured
     * in guacamole.properties.  If Redis support is disabled this is ignored.
     * 
     * @return
     *     The mode used to connect to the Redis server(s).
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property.
     */
    public RedisServerMode getRedisServerMode() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_REDIS_MODE,
                DEFAULT_REDIS_MODE
        );
    }
    
    /**
     * Returns the hostname or IP of the Redis server to use to store active
     * connection information.  The default is localhost.  If Redis support
     * is ont enabled this property is ignored.
     * 
     * @return
     *     The hostname or IP of the Redis server to use to store active
     *     connection information.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property.
     */
    public List<URI> getRedisServers() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_REDIS_SERVERS,
                DEFAULT_REDIS_SERVER
        );
    }
    
    /**
     * Retrieve the configured Redis master server URI when using Master-Slave
     * mode.  If Redis is disabled, or a mode other than master-slave is used,
     * this property will be ignored.
     * 
     * @return
     *     The URI of the Redis master server.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property, or an invalid URI
     *     is provided.
     */
    public URI getRedisMaster() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_REDIS_MASTER,
                null
        );
    }
    
    /**
     * Retrieve a list of Redis slave URIs as configured in the
     * guacamole.properties file.  If Redis is disabled or not in Master-Slave
     * mode this property is ignored.
     * 
     * @return
     *     A list of slave URIs to use with the Redis client in master-slave
     *     mode.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property, or an invalid URI
     *     is specified.
     */
    public List<URI> getRedisSlaves() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_REDIS_SLAVES,
                null
        );
    }
    
    /**
     * The number of the database to use when storing active connection data
     * in Redis.  If Redis is disabled this value is ignored.
     * 
     * @return
     *     The database number to use when storing active connection data
     *     in Redis.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property.
     */
    public int getRedisDB() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_REDIS_DB,
                DEFAULT_REDIS_DB
        );
    }
    
    /**
     * Returns the password to use when authenticating to Redis, or null if
     * no password is specified.  If Redis support is disabled this value is
     * ignored.
     * 
     * @return
     *     The password to use when authenticating to Redis.
     * 
     * @throws GuacamoleException
     *     If an error occurs retrieving the property.
     */
    public String getRedisPassword() throws GuacamoleException {
        return getProperty(
                JDBCGuacamoleProperties.JDBC_REDIS_PASSWORD,
                null
        );
    }

}
