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

package org.apache.guacamole.parameters;

import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.protocol.GuacamoleStatus;


/**
 * An exception which is thrown when the client is taking too long to respond.
 */
public class GuacamoleInsufficientParametersException extends GuacamoleClientException {

    /**
     * Information regarding acceptable parameters.
     */
    private final ParametersInfo parameters;

    /**
     * Creates a new GuacamoleInsufficientParametersException with the given message, cause,
     * and required parameters.
     *
     * @param message A human readable description of the exception that
     *                occurred.
     * @param cause The cause of this exception.
     *
     * @param parameters The parameters required to satisfy the exception.
     */
    public GuacamoleInsufficientParametersException(String message, Throwable cause,
            ParametersInfo parameters) {
        super(message, cause);
        this.parameters = parameters;
    }

    /**
     * Creates a new GuacamoleInsufficientParametersException with the given message and cause.
     *
     * @param message A human readable description of the exception that
     *                occurred.
     * @param parameters The parameters acceptable for resolving this exception.
     */
    public GuacamoleInsufficientParametersException(String message, ParametersInfo parameters) {
        super(message);
        this.parameters = parameters;
    }

    /**
     * Creates a new GuacamoleInsufficientParametersException with the given cause.
     *
     * @param cause The cause of this exception.
     *
     * @param parameters The parameters acceptable to resolve this exception.
     */
    public GuacamoleInsufficientParametersException(Throwable cause, ParametersInfo parameters) {
        super(cause);
        this.parameters = parameters;
    }

    @Override
    public GuacamoleStatus getStatus() {
        return GuacamoleStatus.CLIENT_INSUFFICIENT_PARAMETERS;
    }

    /**
     * Returns the currently defined set of acceptable parameters.
     *
     * @return Information describing acceptable parameters.
     */
    public ParametersInfo getParametersInfo() {
        return parameters;
    }

}
