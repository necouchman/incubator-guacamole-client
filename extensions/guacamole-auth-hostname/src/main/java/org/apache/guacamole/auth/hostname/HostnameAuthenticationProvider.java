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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.apache.guacamole.xml.DocumentHandler;
import org.apache.guacamole.properties.FileGuacamoleProperty;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Map hostnames to connections
 *
 * @author Michael Jumper, Michal Kotas
 */
public class HostnameAuthenticationProvider extends SimpleAuthenticationProvider {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(HostnameAuthenticationProvider.class);

    /**
     * The time the host mapping file was last modified. If the file has never
     * been read, and thus no modification time exists, this will be
     * Long.MIN_VALUE.
     */
    private long lastModified = Long.MIN_VALUE;

    /**
     * The parsed HostMapping read when the host mapping file was last parsed.
     */
    private HostMapping cachedHostMapping;

    /**
     * Guacamole server environment.
     */
    private final Environment environment;

    /**
     * The filename to use for the host mapping.
     */
    public static final String HOST_MAPPING_FILENAME = "host-mapping.xml";
    
    /**
     * Creates a new HostnameAuthenticationProvider that maps host names to
     * connection IDs.
     *
     * @throws GuacamoleException
     *     If a required property is missing, or an error occurs while parsing
     *     a property.
     */
    public HostnameAuthenticationProvider() throws GuacamoleException {
        environment = new LocalEnvironment();
    }

    @Override
    public String getIdentifier() {
        return "hostname-auth";
    }

    /**
     * Returns a HostMapping containing all authorization data given within
     * the host-mapping.xml file. If the XML file has been modified or has not
     * yet been read, this function may reread the file.
     *
     * @return
     *     A HostMapping containing all authorization data within the host
     *     mapping XML file, or null if the file cannot be found/parsed.
     */
    private HostMapping getHostMapping() {

        // Get host mapping file GUACAMOLE_HOME/host-mapping.xml
        File hostMappingFile = new File(environment.getGuacamoleHome(), HOST_MAPPING_FILENAME);

        // Abort if host mapping does not exist
        if (!hostMappingFile.exists()) {
            logger.debug("Host mapping file \"{}\" does not exist and will not be read.", hostMappingFile);
            return null;
        }

        // Refresh host mapping if file has changed
        if (lastModified < hostMappingFile.lastModified()) {

            logger.debug("Updating host mapping file: \"{}\"", hostMappingFile);

            // Parse document
            try {

                // Get handler for root element
                HostMappingTagHandler hostMappingHandler =
                        new HostMappingTagHandler();

                // Set up document handler
                DocumentHandler contentHandler = new DocumentHandler(
                        "host-mapping", hostMappingHandler);

                // Set up XML parser
                XMLReader parser = XMLReaderFactory.createXMLReader();
                parser.setContentHandler(contentHandler);

                // Read and parse file
                InputStream input = new BufferedInputStream(new FileInputStream(hostMappingFile));
                parser.parse(new InputSource(input));
                input.close();

                // Store mod time and host mapping
                lastModified = hostMappingFile.lastModified();
                cachedHostMapping = hostMappingHandler.asHostMapping();

            }

            // If the file is unreadable, return no mapping
            catch (IOException e) {
                logger.warn("Unable to read host mapping file \"{}\": {}", hostMappingFile, e.getMessage());
                logger.debug("Error reading host mapping file.", e);
                return null;
            }

            // If the file cannot be parsed, return no mapping
            catch (SAXException e) {
                logger.warn("Host mapping file \"{}\" is not valid: {}", hostMappingFile, e.getMessage());
                logger.debug("Error parsing host mapping file.", e);
                return null;
            }

        }

        // Return (possibly cached) host mapping
        return cachedHostMapping;

    }

    @Override
    public Map<String, GuacamoleConfiguration>
            getAuthorizedConfigurations(Credentials credentials)
            throws GuacamoleException {

        // Abort authorization if no host mapping exists
        HostMapping hostMapping = getHostMapping();
        if (hostMapping == null)
            return null;
        if (credentials.getUsername() == null || credentials.getUsername().isEmpty())
            return null;

        try {
	    URL myURL = new URL(credentials.getRequest().getRequestURL().toString());
            logger.debug("URL is {}", myURL.toString());
            Authorization auth = hostMapping.getAuthorization(myURL.getHost());
	    
            if(auth != null)
                return auth.getConfigurations();

        }
        catch (Exception e) {
            logger.warn("Cannot parse the URL: {}", e.getMessage());
            logger.debug("Error parsing URL.", e);
            return null;
       }
        // Unauthorized
        return null;

    }

}
