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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Filtering class which looks for the "${GUAC_PROMPT}" and "-1" values
 * and generates a list of parameters that should be sent to the user
 * for prompting.
 */
public class PromptFilter {

    /**
     * String token that denotes a field that should be prompted.
     */
    public final static String PROMPT_TOKEN = "${GUAC_PROMPT}";

    /**
     * Numeric token that denotes a field that should be prompted.
     */
    public final static String PROMPT_NUMERIC = "-1";

    /**
     * Given an arbitrary map containing String values, replace each non-null
     * value with the corresponding filtered value.
     *
     * @param map
     *     The map whose values should be filtered.
     */
    public List<String> filterPrompts(Map<?, String> map) {

        List<String> prompts = new ArrayList<String>();

        // For each map entry
        for (Map.Entry<?, String> entry : map.entrySet()) {

            // If value is non-null, filter value through this TokenFilter
            String value = entry.getValue();
            if (value != null && (value.equals(PROMPT_TOKEN) || value.equals(PROMPT_NUMERIC)))
                prompts.add(entry.getKey().toString());
            
        }

        return prompts;
        
    }

}
