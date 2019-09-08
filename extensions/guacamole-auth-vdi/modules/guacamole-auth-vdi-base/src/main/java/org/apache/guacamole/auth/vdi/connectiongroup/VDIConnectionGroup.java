/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi.connectiongroup;

/**
 *
 * @author nick_couchman
 */
public abstract class VDIConnectionGroup {
    
    public static final String VDI_TYPE_ATTR_NAME = "vdi-pool-type";
    
    public static final String VDI_TEMPLATE_ATTR_NAME = "vdi-pool-template";
    
    public static final String VDI_SNAPSHOT_ATTR_NAME = "vdi-pool-snapshot";
    
    public static final String VDI_MAX_VMS_ATTR_NAME = "vdi-pool-max-vms";
    
    public static final String VDI_MIN_VMS_ATTR_NAME = "vdi-pool-min-vms";
    
    public static final String VDI_SPARE_VMS_ATTR_NAME = "vdi-pool-spare-vms";
    
    public static final String VDI_AUTO_GROW_ATTR_NAME = "vdi-pool-auto-grow";
    
    public static final String VDI_AUTO_SHRINK_ATTR_NAME = "vdi-pool-auto-shrink";
    
    public static final String VDI_PROTOCOL_ATTR_NAME = "vdi-pool-protocol";
    
}
