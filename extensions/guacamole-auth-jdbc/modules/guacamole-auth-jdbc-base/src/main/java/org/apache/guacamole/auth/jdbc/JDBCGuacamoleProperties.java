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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.jdbc;

import org.apache.guacamole.auth.jdbc.conf.RedisServerModeProperty;
import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.IntegerGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;
import org.apache.guacamole.properties.UriGuacamoleProperty;
import org.apache.guacamole.properties.UriListGuacamoleProperty;

/**
 * Common properties for all JDBC modules.
 */
public class JDBCGuacamoleProperties {

    /**
     * This class should not be instantiated.
     */
    private JDBCGuacamoleProperties() {}
    
    /**
     * If active connections and logins should be stored in Redis.
     */
    public static final BooleanGuacamoleProperty JDBC_USE_REDIS = 
            new BooleanGuacamoleProperty() {
    
        @Override
        public String getName() { return "jdbc-use-redis"; }
                
    };
    
    /**
     * The Redis mode to use to connect to the server.
     */
    public static final RedisServerModeProperty JDBC_REDIS_MODE =
            new RedisServerModeProperty() {
      
        @Override
        public String getName() { return "jdbc-redis-mode"; }
                
    };
    
    /**
     * The hostname of the Redis server.
     */
    public static final UriListGuacamoleProperty JDBC_REDIS_SERVERS =
            new UriListGuacamoleProperty() {
    
        @Override
        public String getName() { return "jdbc-redis-servers"; }
                
    };
    
    /**
     * When Redis is in Master-Slave mode, the master Redis server.
     */
    public static final UriGuacamoleProperty JDBC_REDIS_MASTER =
            new UriGuacamoleProperty() {
      
        @Override
        public String getName() { return "jdbc-redis-master"; }
                
    };
    
    /**
     * When Redis is in Master-Slave mode, the slave Redis nodes.
     */
    public static final UriListGuacamoleProperty JDBC_REDIS_SLAVES =
            new UriListGuacamoleProperty() {

        @Override
        public String getName() { return "jdbc-redis-slaves"; }

    };
    
    /**
     * The password to use to connect to Redis.
     */
    public static final StringGuacamoleProperty JDBC_REDIS_PASSWORD =
            new StringGuacamoleProperty() {

        @Override
        public String getName() { return "jdbc-redis-password"; }
                
    };
    
    /**
     * The database number to use for Redis.
     */
    public static final IntegerGuacamoleProperty JDBC_REDIS_DB =
            new IntegerGuacamoleProperty() {
    
        @Override
        public String getName() { return "jdbc-redis-db"; }
                
    };
}
