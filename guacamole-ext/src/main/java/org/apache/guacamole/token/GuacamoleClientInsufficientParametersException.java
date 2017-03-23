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

import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.protocol.GuacamoleStatus;

/**
 * An exception which is thrown when a connection is attempted but there
 * are insufficient parameters to complete the connection, and additional
 * information is required.
 */
public class GuacamoleClientInsufficientParametersException extends GuacamoleClientException {

    /**
     * Information describing the required parameters.
     */
    private final ParametersInfo parametersInfo;

    /**
     * Creates a new GuacamoleClientInsufficientParametersException with the given message and cause.
     *
     * @param message A human readable description of the exception that
     *                occurred.
     * @param cause The cause of this exception.
     */
    public GuacamoleClientInsufficientParametersException(String message, Throwable cause, ParametersInfo parametersInfo) {
        super(message, cause);
        this.parametersInfo = parametersInfo;
    }

    /**
     * Creates a new GuacamoleClientInsufficientParametersException with the given message.
     *
     * @param message A human readable description of the exception that
     *                occurred.
     */
    public GuacamoleClientInsufficientParametersException(String message, ParametersInfo parametersInfo) {
        super(message);
        this.parametersInfo = parametersInfo;
    }

    /**
     * Creates a new GuacamoleClientInsufficientParametersException with the given cause.
     *
     * @param cause The cause of this exception.
     */
    public GuacamoleClientInsufficientParametersException(Throwable cause, ParametersInfo parametersInfo) {
        super(cause);
        this.parametersInfo = parametersInfo;
    }

    public ParametersInfo getParametersInfo() {
        return parametersInfo;
    }

    @Override
    public GuacamoleStatus getStatus() {
        return GuacamoleStatus.CLIENT_INSUFFICIENT_PARAMETERS;
    }

}
