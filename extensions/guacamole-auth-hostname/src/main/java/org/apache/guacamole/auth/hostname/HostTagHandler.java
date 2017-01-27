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

import org.apache.guacamole.xml.TagHandler;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * TagHandler for the "host" element.
 *
 * @author Mike Jumper
 */
public class HostTagHandler implements TagHandler {

    /**
     * The Authorization corresponding to the "authorize" tag being handled
     * by this tag handler. The data of this Authorization will be populated
     * as the tag is parsed.
     */
    private Authorization authorization = new Authorization();

    /**
     * The default GuacamoleConfiguration to use if "param" or "protocol"
     * tags occur outside a "connection" tag.
     */
    private GuacamoleConfiguration default_config = null;

    /**
     * The HostMapping this authorization belongs to.
     */
    private HostMapping parent;

    /**
     * Creates a new HostTagHandler that parses an Authorization owned
     * by the given HostMapping.
     *
     * @param parent The HostMapping that owns the Authorization this handler
     *               will parse.
     */
    public HostTagHandler(HostMapping parent) {
        this.parent = parent;
    }

    @Override
    public void init(Attributes attributes) throws SAXException {

        // Init hostname
        authorization.setHostname(attributes.getValue("hostname"));

        parent.addAuthorization(this.asAuthorization());

    }

    @Override
    public TagHandler childElement(String localName) throws SAXException {

        // "connection" tag
        if (localName.equals("connection"))
            return new ConnectionTagHandler(authorization);

        // "param" tag
        if (localName.equals("param")) {

            // Create default config if it doesn't exist
            if (default_config == null) {
                default_config = new GuacamoleConfiguration();
                authorization.addConfiguration("DEFAULT", default_config);
            }

            return new ParamTagHandler(default_config);
        }

        // "protocol" tag
        if (localName.equals("protocol")) {

            // Create default config if it doesn't exist
            if (default_config == null) {
                default_config = new GuacamoleConfiguration();
                authorization.addConfiguration("DEFAULT", default_config);
            }

            return new ProtocolTagHandler(default_config);
        }

        return null;

    }

    @Override
    public void complete(String textContent) throws SAXException {
        // Do nothing
    }

    /**
     * Returns an Authorization backed by the data of this authorize tag
     * handler. This Authorization is guaranteed to at least have the hostname.
     * Any associated configurations will be added dynamically as the host tag
     * is parsed.
     *
     * @return An Authorization backed by the data of this authorize tag
     *         handler.
     */
    public Authorization asAuthorization() {
        return authorization;
    }

}
