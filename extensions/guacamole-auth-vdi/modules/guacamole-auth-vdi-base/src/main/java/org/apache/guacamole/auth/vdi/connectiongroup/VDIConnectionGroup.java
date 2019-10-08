/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi.connectiongroup;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
import org.apache.guacamole.net.auth.DelegatingConnectionGroup;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocols.ProtocolInfo;

/**
 *
 * @author nick_couchman
 */
public abstract class VDIConnectionGroup extends DelegatingConnectionGroup {
    
    @Inject
    public Environment environment;
    
    public static final String VDI_TYPE_ATTR_NAME = "vdi-pool-type";
    
    public static final Collection<String> VDI_TYPE_ATTR_VALUES = 
            Arrays.asList("application", "dedicated", "floating");
    
    public static final String VDI_TEMPLATE_ATTR_NAME = "vdi-pool-template";
    
    public static final String VDI_SNAPSHOT_ATTR_NAME = "vdi-pool-snapshot";
    
    public static final String VDI_BASENAME_ATTR_NAME = "vdi-pool-basename";
    
    public static final String VDI_MAX_VMS_ATTR_NAME = "vdi-pool-max-vms";
    
    public static final String VDI_MIN_VMS_ATTR_NAME = "vdi-pool-min-vms";
    
    public static final String VDI_SPARE_VMS_ATTR_NAME = "vdi-pool-spare-vms";
    
    public static final String VDI_AUTO_GROW_ATTR_NAME = "vdi-pool-auto-grow";
    
    public static final String VDI_AUTO_SHRINK_ATTR_NAME = "vdi-pool-auto-shrink";
    
    public static final String VDI_PROTOCOL_ATTR_NAME = "vdi-pool-protocol";
    
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
    
    public VDIConnectionGroup(ConnectionGroup object) {
        super(object);
        
        Collection<Field> fields = VDI_POOL_FORM.getFields();
        fields.add(new EnumField(VDI_PROTOCOL_ATTR_NAME, environment.getProtocols().keySet()));
    }
    
    public ConnectionGroup getUndecorated() {
        return super.getDelegateConnectionGroup();
    }
    
    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info,
            Map<String, String> tokens) throws GuacamoleException {
        
        // TODO: Code to control pool size.
        int numConnections = super.getActiveConnections();
        
        return super.connect(info, tokens);
        
    }
    
}
