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

package org.apache.guacamole.form;

import java.net.URI;

/**
 * A field that loads options from a URI and presents those options to the
 * user.
 */
public class RemoteDataField extends Field {
    
    /**
     * An enum type used to distinguish the type of data provided by the
     * remote service.
     */
    public enum RemoteDataType {
        
        /**
         * Remote service provides JSON-style data (e.g. REST API).
         */
        JSON,
        
        /**
         * Remote service provide XML-style data (e.g. AJAX, SOAP)
         */
        XML;
        
    }
    
    /**
     * The URI from which to retrieve options.
     */
    private final URI uri;
    
    /**
     * Creates a new RemoteDataField with the given name and URI from which to
     * load possible values, and the type of data provided by the remote
     * endpoint.
     *
     * @param name
     *     The unique name to associate with this field.
     *
     * @param uri
     *     The URI from which to retrieve options.
     * 
     * @param type
     *     The type of data provided by the remote service.
     */
    public RemoteDataField(String name, URI uri, RemoteDataType type) {
        super(name, Field.Type.ENUM);
        this.uri = uri;
    }
    
    /**
     * Creates a new RemoteDataField with the given name and URI from which to
     * load possible values, and defaults to JSON as the type of data provided
     * by the remote URI.
     * 
     * @param name
     *     The unique name to associate with this field.
     * 
     * @param uri 
     *     The URI from which to retrieve options.
     */
    public RemoteDataField(String name, URI uri) {
        this(name, uri, RemoteDataType.JSON);
    }
    
}
