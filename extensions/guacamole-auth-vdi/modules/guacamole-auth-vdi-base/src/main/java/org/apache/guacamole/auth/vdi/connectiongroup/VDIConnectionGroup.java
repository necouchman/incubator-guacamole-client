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

package org.apache.guacamole.auth.vdi.connectiongroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.guacamole.form.BooleanField;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.form.MultilineField;
import org.apache.guacamole.form.NumericField;
import org.apache.guacamole.form.TextField;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.DelegatingConnectionGroup;

/**
 * An implementation of a DelegatingConnection group that stores information
 * about the group in another directory.
 */
public class VDIConnectionGroup extends DelegatingConnectionGroup {
    
    /**
     * The name of the attribute that holds whether or not this
     * connection group should behave as a VDI connection group.
     */
    public static final String VDI_ENABLED_ATTRIBUTE_NAME =
            "vdi-enabled";
    
    /**
     * The name of the attribute that contains the name of the template
     * that will be used to create additional connections.
     */
    public static final String VDI_TEMPLATE_ATTRIBUTE_NAME =
            "vdi-template-name";
    
    /**
     * The name of the attribute that stores the name or path of the snapshot
     * of the template to use for generating new systems.
     */
    public static final String VDI_SNAPSHOT_ATTRIBUTE_NAME =
            "vdi-snapshot-name";
    
    /**
     * The name of the attribute that contains the number of minimum
     * spare systems that should be kept running.
     */
    public static final String VDI_MIN_SPARE_ATTRIBUTE_NAME =
            "vdi-minimum-spare";
    
    /**
     * The name of the attribute that contains the upper limit of
     * systems that should be created to support this VDI group.
     */
    public static final String VDI_MAX_SYSTEMS_ATTRIBUTE_NAME =
            "vdi-max-systems";
    
    /**
     * The name of the attribute that contains a comma-separated list of
     * destination data stores, storage repositories, etc., that will store
     * the virtual machines created for this group.
     */
    public static final String VDI_DS_ATTRIBUTE_NAME =
            "vdi-dest-storage";
    
    /**
     * The list of attributes, minus the enabling attribute, that make up all
     * of the available attributes for VDI connection groups.
     */
    public static final List<String> VDI_CONNECTION_GROUP_ATTRIBUTES =
            Arrays.asList(
                    VDI_TEMPLATE_ATTRIBUTE_NAME,
                    VDI_SNAPSHOT_ATTRIBUTE_NAME,
                    VDI_MIN_SPARE_ATTRIBUTE_NAME,
                    VDI_MAX_SYSTEMS_ATTRIBUTE_NAME,
                    VDI_DS_ATTRIBUTE_NAME);
    
    /**
     * The form fields that are used to represent the attributes related to
     * this type of connection group to the end user.
     */
    public static final Form VDI_CONNECTION_GROUP_FORM = new Form("vdi-attributes",
            Arrays.asList(
                    new BooleanField(VDI_ENABLED_ATTRIBUTE_NAME, "true"),
                    new TextField(VDI_TEMPLATE_ATTRIBUTE_NAME),
                    new TextField(VDI_SNAPSHOT_ATTRIBUTE_NAME),
                    new NumericField(VDI_MIN_SPARE_ATTRIBUTE_NAME),
                    new NumericField(VDI_MAX_SYSTEMS_ATTRIBUTE_NAME),
                    new MultilineField(VDI_DS_ATTRIBUTE_NAME)
            )
    );
    
    /**
     * The unmodifiable collection of all forms associated with this
     * connection group class.
     */
    public static final Collection<Form> VDI_FORMS =
            Collections.unmodifiableCollection(
                    Arrays.asList(VDI_CONNECTION_GROUP_FORM));
    
    /**
     * Whether or not the current user is allowed to update the attributes
     * for this connection group.
     */
    private final boolean canUpdate;
    
    /**
     * Create a new instance of a VDIConnectionGroup that delegates storage
     * to another class.
     * 
     * @param connectionGroup 
     *     The ConnectionGroup to decorate.
     * 
     * @param canUpdate
     *     If the current user is allowed to update the group.
     */
    public VDIConnectionGroup(ConnectionGroup connectionGroup,
            boolean canUpdate) {
        super(connectionGroup);
        this.canUpdate = canUpdate;
        
        // VDI groups are always BALANCING groups.
        super.setType(Type.BALANCING);
    }
    
    @Override
    public Map<String, String> getAttributes() {
        
        Map<String, String> attributes = new HashMap<>(super.getAttributes());
        List<String> vdiAttributes = new ArrayList<>(VDI_CONNECTION_GROUP_ATTRIBUTES);
        
        if (canUpdate && !attributes.containsKey(VDI_ENABLED_ATTRIBUTE_NAME))
            attributes.put(VDI_ENABLED_ATTRIBUTE_NAME, null);
        
        else if (canUpdate && 
                "true".equals(attributes.get(VDI_ENABLED_ATTRIBUTE_NAME))) {
            for (String attr : vdiAttributes) {
                if (!attributes.containsKey(attr))
                    attributes.put(attr, null);
            }
        }
        
        return attributes;
        
    }
    
    public ConnectionGroup getUndecorated() {
        return super.getDelegateConnectionGroup();
    }
    
    @Override
    public void setAttributes(Map<String, String> attributes) {
        if (!canUpdate
                ||!attributes.containsKey(VDI_ENABLED_ATTRIBUTE_NAME)
                || !("true".equals(VDI_ENABLED_ATTRIBUTE_NAME))) {
            
            attributes.remove(VDI_ENABLED_ATTRIBUTE_NAME);
            for (String attr : VDI_CONNECTION_GROUP_ATTRIBUTES)
                attributes.remove(attr);
            
        }
        
        super.setAttributes(attributes);
    }
    
}
