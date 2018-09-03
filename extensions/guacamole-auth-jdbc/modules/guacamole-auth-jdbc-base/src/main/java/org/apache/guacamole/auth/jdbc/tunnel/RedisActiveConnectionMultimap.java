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
package org.apache.guacamole.auth.jdbc.tunnel;

import com.google.inject.Inject;
import java.net.URI;
import java.util.List;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.auth.jdbc.JDBCEnvironment;
import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.api.RSetMultimap;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mapping of object identifiers to lists of connection records stored within a
 * Redis backend. Records are added or removed individually, and the overall
 * list of current records associated with a given object can be retrieved at
 * any time. The public methods of this class are all threadsafe.
 */
public class RedisActiveConnectionMultimap extends ActiveConnectionMultimap {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The current JDBC environment.
     */
    @Inject
    private JDBCEnvironment jdbcEnvironment;

    /**
     * All active connections to a connection having a given identifier.
     */
    private final RSetMultimap<String, ActiveConnectionRecord> records;

    /**
     * Set up a new RedisActiveConnectionMultimap, connecting to the Redis
     * server and retrieving the records from the Redis back-end.
     *
     * @param mapName The name of the Map to retrieve from the Redis back-end.
     *
     * @throws GuacamoleException If an error occurs retrieving configuration
     * items.
     */
    public RedisActiveConnectionMultimap(String mapName) throws GuacamoleException {

        // Configure the Redisson client
        Config redisConfig = getRedisConfig();
        RedissonClient client = Redisson.create(redisConfig);

        // Retrieve the records map
        records = client.getSetMultimap(mapName);

    }

    private Config getRedisConfig() throws GuacamoleException {

        // Set up configuration items common to all modes.
        Config redisConfig = new Config();
        String redisPassword = jdbcEnvironment.getRedisPassword();

        switch (jdbcEnvironment.getRedisServerMode()) {

            // Redis cluster connection mode
            case CLUSTER:
                ClusterServersConfig clusterConfig = redisConfig.useClusterServers();

                // Set password, if present
                if (redisPassword != null) {
                    clusterConfig.setPassword(redisPassword);
                }

                // Get all server URIs and add to the configuration.
                for (URI server : jdbcEnvironment.getRedisServers()) {
                    clusterConfig.addNodeAddress(server.toString());
                }

                break;

            // Redis Master-Slave mode
            case MASTER_SLAVE:
                MasterSlaveServersConfig masterSlaveConfig = redisConfig.useMasterSlaveServers();

                // Set password, if present
                if (redisPassword != null) {
                    masterSlaveConfig.setPassword(redisPassword);
                }

                // Set the master address
                masterSlaveConfig.setMasterAddress(jdbcEnvironment.getRedisMaster());

                // Loop through slave addresses and add them
                for (URI slave : jdbcEnvironment.getRedisSlaves()) {
                    masterSlaveConfig.addSlaveAddress(slave);
                }

                // Set the database number
                masterSlaveConfig.setDatabase(jdbcEnvironment.getRedisDB());

                break;

            // Redis replicated mode
            case REPLICATED:
                ReplicatedServersConfig replicatedConfig = redisConfig.useReplicatedServers();

                // Set the password, if present
                if (redisPassword != null) {
                    replicatedConfig.setPassword(redisPassword);
                }

                // Loop through specified servers and add to the config
                for (URI server : jdbcEnvironment.getRedisServers()) {
                    replicatedConfig.addNodeAddress(server.toString());
                }

                // Set the databaes number
                replicatedConfig.setDatabase(jdbcEnvironment.getRedisDB());

                break;

            // Redis sentinel HA mode
            case SENTINEL:
                SentinelServersConfig sentinelConfig = redisConfig.useSentinelServers();

                // Set the password, if present
                if (redisPassword != null) {
                    sentinelConfig.setPassword(redisPassword);
                }

                // Loop through specified servers and add them to configuration
                for (URI server : jdbcEnvironment.getRedisServers()) {
                    sentinelConfig.addSentinelAddress(server.toString());
                }

                // Set the datansae number
                sentinelConfig.setDatabase(jdbcEnvironment.getRedisDB());

                break;

            // Redis single server mode
            case SINGLE:
                SingleServerConfig singleConfig = redisConfig.useSingleServer();

                // Set the password, if present
                if (redisPassword != null) {
                    singleConfig.setPassword(redisPassword);
                }

                List<URI> addresses = jdbcEnvironment.getRedisServers();

                // If more than one server, warn that only one will be used.
                if (addresses.size() > 1) {
                    logger.debug("More than one Redis server has been specified in single mode - only the first one will be used.");
                }

                // Pick first entry in list
                singleConfig.setAddress(jdbcEnvironment.getRedisServers().get(0).toString());

                // Set the databse number
                singleConfig.setDatabase(jdbcEnvironment.getRedisDB());

                break;

            // Should never get here, but throw exception if anything else
            default:
                throw new GuacamoleServerException("Unable to get a valid Redis mode.");

        }

        return redisConfig;

    }

    @Override
    public void put(String identifier, ActiveConnectionRecord record) {
        records.put(identifier, record);
    }

    @Override
    public void remove(String identifier, ActiveConnectionRecord record) {

        // Get set of active connection records
        RSet<ActiveConnectionRecord> connections = records.get(identifier);
        assert (connections != null);

        // Remove old record
        records.remove(identifier, record);

    }

}
