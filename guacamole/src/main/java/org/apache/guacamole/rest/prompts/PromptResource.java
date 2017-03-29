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

package org.apache.guacamole.rest.prompts;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.ConnectionRecord;
import org.apache.guacamole.net.auth.ConnectionRecordSet;
import org.apache.guacamole.net.auth.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A REST resource for handling prompting of parameters for
 * Guacamole connections.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PromptResource {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(PromptResource.class);

    /**
     * The UserContext whose associated connection history is being exposed.
     */
    private final UserContext userContext;

    /**
     * Creates a new HistoryResource which exposes the connection history
     * associated with the given UserContext.
     *
     * @param userContext
     *     The UserContext whose connection history should be exposed.
     */
    public PromptResource(UserContext userContext) {
        this.userContext = userContext;
        logger.debug(">>>>>rest/prompts/PromptResource");
    }

    /**
     * Retrieves the prompts necessary for the specified connection.
     *
     * @param identifier
     *     The directory identifier of the connection for which to retrieve
     *     prompts.
     *
     * @return
     *     A list of prompts necessary to successfully complete the
     *     specified connection.
     *
     * @throws GuacamoleException
     *     If an error occurs while retrieving the connection history.
     */
    @GET
    @Path("{identifier}")
    public List<String> getConnectionPrompts(
            @PathParam("identifier") String identifier)
            throws GuacamoleException {

        logger.debug(">>>>>rest/prompts/{}", identifier);

        return userContext.getConnectionDirectory().get(identifier).getPrompts();

    }

}
