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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.apache.guacamole.form.Field;
import org.apache.guacamole.form.NumericField;
import org.apache.guacamole.form.PasswordField;
import org.apache.guacamole.form.TextField;
import org.apache.guacamole.form.UsernameField;

/**
 * Information which describes a set of valid parameters.
 */
public class ParametersInfo {

    /**
     * All fields required for valid parameters.
     */
    private final Collection<Field> fields;

    /**
     * Creates a new ParametersInfo object which requires the given fields as
     * parameters.
     *
     * @param fields
     *     The fields to require.
     */
    public ParametersInfo(Collection<Field> fields) {
        this.fields = fields;
    }
    
    /**
     * Returns all fields required needed for connection parameters.
     *
     * @return
     *     All parameter fields required for a connection.
     */
    public Collection<Field> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    /**
     * ParametersInfo object which describes empty parameters. No fields are
     * required.
     */
    public static final ParametersInfo EMPTY = new ParametersInfo(Collections.<Field>emptyList());

    /**
     * A field which describes a hostname prompt.
     */
    public static final Field HOSTNAME = new TextField("hostname");

    /**
     * A field which describes a port prompt.
     */
    public static final Field PORT = new NumericField("port");

    /**
     * A field which describes a username prompt.
     */
    public static final Field USERNAME = new UsernameField("username");

    /**
     * A field which describes a password prompt.
     */
    public static final Field PASSWORD = new PasswordField("password");

    /**
     * A field which describes a passphrase prompt for a certificate or SSH key.
     */
    public static final Field PASSPHRASE = new PasswordField("passphrase");

    /**
     * A field which describes a certificate or SSH key prompt.
     */
    public static final Field PRIVATE_KEY = new TextField("privatekey");

    /**
     * A field which describes a domain name prompt.
     */
    public static final Field DOMAIN = new TextField("domain");

    /**
     * ParametersInfo object that describes standard username and password
     * prompt.
     */
    public static final ParametersInfo USERNAME_PASSWOD = new ParametersInfo(
        Arrays.asList(USERNAME, PASSWORD)
    );

    /**
     * ParametersInfo object that describes windows credentials prompt.
     */
    public static final ParametersInfo WINDOWS_LOGIN = new ParametersInfo(
        Arrays.asList(USERNAME, PASSWORD, DOMAIN)
    );

    /**
     * ParametersInfo object that describes SSH key authentication prompt.
     */
    public static final ParametersInfo KEY_LOGIN = new ParametersInfo(
        Arrays.asList(USERNAME, PRIVATE_KEY, PASSPHRASE)
    );

}
