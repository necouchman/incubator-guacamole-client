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

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.form.BooleanField;
import org.apache.guacamole.form.EnumField;
import org.apache.guacamole.form.Field;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.form.NumericField;
import org.apache.guacamole.form.TextField;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.TokenInjectingConnectionGroup;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocols.ProtocolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ConnectionGroup that decorates base connection groups, and contains
 * attributes requires to host VDI workloads in Guacamole.
 */
public class VDIConnectionGroup extends TokenInjectingConnectionGroup {
    
    /**
     * Logger for 
     */
    private static final Logger logger = LoggerFactory.getLogger(VDIConnectionGroup.class);
    
    /**
     * The server environment for this module.
     */
    @Inject
    public Environment environment;
    
    /**
     * The name of the attribute that stores the type of this VDI pool.
     */
    public static final String VDI_TYPE_ATTR_NAME = "vdi-pool-type";
    
    /**
     * The list of possible values for the type of this VDI pool.
     */
    public static final Collection<String> VDI_TYPE_ATTR_VALUES = 
            VDIConnectionGroupType.getStringValues();
    
    /**
     * The name of the attribute that stores the identifier of the template
     * for this VDI pool.
     */
    public static final String VDI_TEMPLATE_ATTR_NAME = "vdi-pool-template";
    
    /**
     * The name of the attribute that stores the snapshot name on the template
     * that this VDI pool should be based on.
     */
    public static final String VDI_SNAPSHOT_ATTR_NAME = "vdi-pool-snapshot";
    
    /**
     * The name of the attribute that stores the base name to be used for
     * naming the virtual machines.
     */
    public static final String VDI_BASENAME_ATTR_NAME = "vdi-pool-basename";
    
    /**
     * The name of the attribute that stores the maximum number of VMs that
     * can be created for this pool.
     */
    public static final String VDI_MAX_VMS_ATTR_NAME = "vdi-pool-max-vms";
    
    /**
     * The name of the attribute that stores the minimum base size of this
     * VDI Pool.
     */
    public static final String VDI_MIN_VMS_ATTR_NAME = "vdi-pool-min-vms";
    
    /**
     * The name of the attribute that stores the minimum number of spare VMs
     * that should be active in this pool, up to the max pool size.
     */
    public static final String VDI_SPARE_VMS_ATTR_NAME = "vdi-pool-spare-vms";
    
    /**
     * The name of the attribute that stores whether or not the pool should
     * automatically grow to reach the max size.
     */
    public static final String VDI_AUTO_GROW_ATTR_NAME = "vdi-pool-auto-grow";
    
    /**
     * The name of the attribute that stores whether or not the pool should
     * automatically shrink as connections are released.
     */
    public static final String VDI_AUTO_SHRINK_ATTR_NAME = "vdi-pool-auto-shrink";
    
    /**
     * The name of the attribute that stores the protocol to be used for the
     * connections that are created within the pool.
     */
    public static final String VDI_PROTOCOL_ATTR_NAME = "vdi-pool-protocol";
    
    /**
     * The field that presents the possible protocols during configuration.
     */
    public static final Field VDI_PROTOCOL_FIELD = new EnumField(VDI_PROTOCOL_ATTR_NAME, Collections.emptySet());
    
    /**
     * A list of all possible VDI attributes.
     */
    public static final List<String> VDI_ATTRIBUTES = Arrays.asList(
            VDI_TYPE_ATTR_NAME,
            VDI_TEMPLATE_ATTR_NAME,
            VDI_SNAPSHOT_ATTR_NAME,
            VDI_BASENAME_ATTR_NAME,
            VDI_MAX_VMS_ATTR_NAME,
            VDI_MIN_VMS_ATTR_NAME,
            VDI_SPARE_VMS_ATTR_NAME,
            VDI_AUTO_GROW_ATTR_NAME,
            VDI_AUTO_SHRINK_ATTR_NAME,
            VDI_PROTOCOL_ATTR_NAME
        );
    
    /**
     * The form that contains the fields used to configure the VDI pool.
     */
    public static final Form VDI_POOL_FORM = new Form("vdi-pool-form", Arrays.asList(
            new EnumField(VDI_TYPE_ATTR_NAME, VDI_TYPE_ATTR_VALUES),
            new TextField(VDI_TEMPLATE_ATTR_NAME),
            new TextField(VDI_SNAPSHOT_ATTR_NAME),
            new TextField(VDI_BASENAME_ATTR_NAME),
            new NumericField(VDI_MAX_VMS_ATTR_NAME),
            new NumericField(VDI_MIN_VMS_ATTR_NAME),
            new NumericField(VDI_SPARE_VMS_ATTR_NAME),
            new BooleanField(VDI_AUTO_GROW_ATTR_NAME, "true"),
            new BooleanField(VDI_AUTO_SHRINK_ATTR_NAME, "true")
        ));
    
    /**
     * The map of attributes associated with this VDI Connection Group.
     */
    private final Map<String, String> attributes;
    
    /**
     * Whether or not the current user can update this group.
     */
    private final Boolean canUpdate;
    
    /**
     * Create a new VDIConnectionGroup that decorates the provided group, and
     * contains the specified tokens.
     * 
     * @param object
     *     The ConnectionGroup object to decorate.
     * 
     * @param tokens 
     *     Any tokens that should be available to the connection.
     * 
     * @param canUpdate
     *     True if the current user can update this group; otherwise false.
     */
    public VDIConnectionGroup(ConnectionGroup object, Map<String, String> tokens,
            Boolean canUpdate) {
        super(object, tokens);
        
        /**
         * Gather the supported protocols and add them to the field.
         */
        Collection<Field> fields = VDI_POOL_FORM.getFields();
        VDI_PROTOCOL_FIELD.setOptions(environment.getProtocols().keySet());
        fields.add(VDI_PROTOCOL_FIELD);
        
        this.attributes = super.getAttributes();
        this.canUpdate = canUpdate;
    }
    
    /**
     * Create a new VDIConnectionGroup that decorates the specified group.
     * 
     * @param object 
     *     The ConnectinoGroup object to decorate.
     */
    public VDIConnectionGroup(ConnectionGroup object) {
        this(object, Collections.<String, String>emptyMap(), false);
    }
    
    /**
     * Get the underlying ConnectionGroup object that is being decorated.
     * 
     * @return 
     *     The underlying ConnectionGroup object.
     */
    public ConnectionGroup getUndecorated() {
        return super.getDelegateConnectionGroup();
    }
    
    @Override
    public Map<String, String> getAttributes() {
        
        // Create a mutable copy of the attributes
        Map<String, String> effectiveAttributes = new HashMap<>(attributes);

        // Check to see if any need to be added or removed
        for (String attr : VDI_ATTRIBUTES) {
            if (canUpdate && !effectiveAttributes.containsKey(attr))
                effectiveAttributes.put(attr, null);
            else if (!canUpdate && effectiveAttributes.containsKey(attr))
                effectiveAttributes.remove(attr);
        }

        return effectiveAttributes;
    }
    
    @Override
    public void setAttributes(Map<String, String> setAttributes) {
        
        // Create a multable copy of the attributes
        setAttributes =  new HashMap<>(setAttributes);

        if (!canUpdate)
            for (String attr : VDI_ATTRIBUTES)
                setAttributes.remove(attr);

        // Pass attributes on up
        super.setAttributes(setAttributes);
    }
    
    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info,
            Map<String, String> tokens) throws GuacamoleException {
        
        // TODO: Code to control pool size.
        int active = getActiveConnections();
        int up = getMemberConnections();
        int spare = getSpareVMs();
        int max = getMaxVMs();
        int deficit = up - (active + spare + 1);
        
        if (up - active < spare && up < max) {
            
        }
        
        /**
         * Max: 40
         * Up: 20
         * Active: 5
         * Spare: 5
         */
        
        return super.connect(info, tokens);
        
    }
    
    @Override
    public int getActiveConnections() {
        return super.getDelegateConnectionGroup().getActiveConnections();
    }
    
    /**
     * Returns the number of connections currently in the VDI pool.
     * 
     * @return
     *     The number of connections in the VDI pool.
     * 
     * @throws GuacamoleException 
     *     If the current list of connection identifiers cannot be retrieved.
     */
    public int getMemberConnections() throws GuacamoleException {
        return super.getConnectionIdentifiers().size();
    }
    
    /**
     * Return the type of VDI pool.
     * 
     * @return 
     *     The type of VDI pool.
     */
    public VDIConnectionGroupType getPoolType() {
        return VDIConnectionGroupType.valueOf(attributes.get(VDI_TYPE_ATTR_NAME));
    }
    
    /**
     * Return the identifier of the template to use for systems in this VDI pool.
     * 
     * @return 
     *     The identifier of the template to use for systems in this pool.
     */
    public String getTemplate() {
        return attributes.get(VDI_TEMPLATE_ATTR_NAME);
    }
    
    /**
     * Return the identifier of the snapshot to use for systems in this VDI Pool.
     * 
     * @return
     *     The identifier of the snapshot to use for systems in this pool.
     */
    public String getSnapshot() {
        return attributes.get(VDI_SNAPSHOT_ATTR_NAME);
    }
    
    /**
     * Return the base name to use for new VMs in this pool.
     * 
     * @return 
     *     The base name to use for new VMs in this pool.
     */
    public String getBasename() {
        return attributes.get(VDI_BASENAME_ATTR_NAME);
    }
    
    /**
     * Return the maximum number of virtual machines that will be created in
     * this pool.
     * 
     * @return 
     *     The maximum number of virtual machines that will be created in this
     *     pool.
     */
    public int getMaxVMs() {
        return Integer.parseInt(attributes.get(VDI_MAX_VMS_ATTR_NAME));
    }
    
    /**
     * Return the minimum number of virtual machines that should be created
     * for this pool.
     * 
     * @return 
     *     The minimum number of virtual machines that will be created for this
     *     pool.
     */
    public int getMinVMs() {
        return Integer.parseInt(attributes.get(VDI_MIN_VMS_ATTR_NAME));
    }
    
    /**
     * Return the number of spare VMs that should be available in the pool.
     * 
     * @return 
     *     The number of spare VMs that should be available in this pool.
     */
    public int getSpareVMs() {
        return Integer.parseInt(attributes.get(VDI_SPARE_VMS_ATTR_NAME));
    }
    
    /**
     * Return whether or not the pool should automatically grow as demand
     * increases.
     * 
     * @return
     *     A boolean true if the pool should grow automatically, otherwise
     *     false.
     */
    public Boolean getAutogrow() {
        return Boolean.getBoolean(attributes.get(VDI_AUTO_GROW_ATTR_NAME));
    }
    
    /**
     * Return whether or not the pool should shrink as users log out of the
     * pool.
     * 
     * @return 
     *     A boolean true if the pool should automatically shrink, otherwise
     *     false.
     */
    public Boolean getAutoshrink() {
        return Boolean.getBoolean(attributes.get(VDI_AUTO_SHRINK_ATTR_NAME));
    }
    
    /**
     * Return the ProtocolInfo object associated with the protocol used for this
     * pool.
     * 
     * @return 
     *     The ProtocolInfo object associated with the protocol used for this
     *     pool.
     */
    public ProtocolInfo getProtocol() {
        return environment.getProtocol(attributes.get(VDI_PROTOCOL_ATTR_NAME));
    }
    
}
