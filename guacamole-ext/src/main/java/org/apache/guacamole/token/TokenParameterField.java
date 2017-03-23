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

package org.apache.guacamole.token;

import org.apache.guacamole.form.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A form used to prompt the user for additional information when
 * a parameter is configured to prompt in a connection specification.
 */
public class TokenParameterField extends Field {

    /**
     * The field returned by the parameter entry
     */
    public static final String PARAMETER_NAME = "guac-token-parameter-response";

    /**
     * The type of field to initialize for the parameter entry.
     */
    private static final String PARAMETER_FIELD_TYPE = "GUAC_TOKEN_PARAMETER";

    /**
     * The parameter field that is being prompted for.
     */
    private final String fieldName;

    /**
     * Initialize the field with the reply message and the state.
     */
    public TokenParameterField(String fieldName) {
        super(PARAMETER_NAME, PARAMETER_FIELD_TYPE);
        this.fieldName = fieldName;

    }

    /**
     * Get the value of the fieldName variable.
     */
    public String getFieldName() {
        return fieldName;
    }
}
