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

package org.apache.guacamole.auth.hostname.form;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import org.apache.guacamole.form.Field;


/**
 * Field definition which represents the ticket returned by an CAS service.
 * Within the user interface, this will be rendered as an appropriate "Log in
 * with ..." button which links to the CAS service.
 *
 * @author Nick Couchman
 */
public class HostnameClientField extends Field {

    /**
     * The standard HTTP parameter which will be included within the URL by all
     * CAS services upon successful authentication and redirect.
     */
    public static final String PARAMETER_NAME = "clientid";

    /**
     * The trailing part of the URL to route the client directly to the
     * connection.
     */
    private final String clientRoute;

    /**
     * Creates a new "client" field which redirects the client to the connection
     * mapped through from this authentication extension so that the connection
     * starts automatically.
     *
     * @param clientid
     *     The Base64 encoded ID of the connection to which the client
     *     should automatically connect.
     *
     */
    public HostnameClientField(String clientid) {

        // Init base field properties
        super(PARAMETER_NAME, "GUAC_HOSTNAME_CLIENT");

        // Build route URI from given values
        this.clientRoute = "client/" + clientid;
    }

    /**
     * Returns the route that this field should redirect to.
     *
     * @return
     *     The route that this field should redirect to.
     */
    public String getClientRoute() {
        return clientRoute;
    }

}
