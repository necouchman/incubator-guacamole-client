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

package org.apache.guacamole.auth.hostname;

import java.util.Map;
import java.util.TreeMap;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

/**
 * Mapping of hostname configuration set.
 *
 * @author Mike Jumper
 */
public class Authorization {

    /**
     * The hostname being authorized.
     */
    private String hostname;

    /**
     * Map of all authorized configurations, indexed by configuration name.
     */
    private Map<String, GuacamoleConfiguration> configs = new
            TreeMap<String, GuacamoleConfiguration>();

    /**
     * Returns the hostname associated with this authorization.
     *
     * @return The hostname associated with this authorization.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the hostname associated with this authorization.
     *
     * @param hostname The hostname to associate with this authorization.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Returns whether a given hostname is authorized based on
     * the stored hostname.
     *
     * @param hostname The hostname to validate.
     * @return true if the hostname given is authorized, false
     *         otherwise.
     */
    public boolean validate(String hostname) {

        // If hostname matches
        if (hostname != null)
            return hostname.equals(this.hostname);

        return false;

    }

    /**
     * Returns the GuacamoleConfiguration having the given name and associated
     * with the hostname stored within this authorization.
     *
     * @param name The name of the GuacamoleConfiguration to return.
     * @return The GuacamoleConfiguration having the given name, or null if no
     *         such GuacamoleConfiguration exists.
     */
    public GuacamoleConfiguration getConfiguration(String name) {
        return configs.get(name);
    }

    /**
     * Adds the given GuacamoleConfiguration to the set of stored configurations
     * under the given name.
     *
     * @param name The name to associate this GuacamoleConfiguration with.
     * @param config The GuacamoleConfiguration to store.
     */
    public void addConfiguration(String name, GuacamoleConfiguration config) {
        configs.clear();
        configs.put(name, config);
    }

    /**
     * Returns a Map of all stored GuacamoleConfigurations associated with the
     * hostname stored within this authorization, indexed by
     * configuration name.
     *
     * @return A Map of all stored GuacamoleConfigurations.
     */
    public Map<String, GuacamoleConfiguration> getConfigurations() {
        return configs;
    }

}
