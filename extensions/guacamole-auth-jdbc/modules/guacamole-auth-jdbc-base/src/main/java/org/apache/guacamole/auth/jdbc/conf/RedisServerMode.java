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

package org.apache.guacamole.auth.jdbc.conf;

/**
 * The modes that can be used to connect to the Redis server.
 */
public enum RedisServerMode {
    
    /**
     * Connect to a Redis cluster.
     */
    CLUSTER,
    
    /**
     * Connect to a master and set of slave Reds nodes.
     */
    MASTER_SLAVE,
    
    /**
     * Connect to a set of replicated Redis servers.
     */
    REPLICATED,
    
    /**
     * Connect to a set of Redis servers in sentinel mode.
     */
    SENTINEL,
    
    /**
     * Connect to a single Redis server.
     */
    SINGLE;

}
