/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.event.snmp.conf;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.properties.GuacamoleProperty;

/**
 * A GuacamoleProperty whose value is a SnmpVersion.
 */
public abstract class SnmpVersionProperty implements GuacamoleProperty<SnmpVersion> {
    
    @Override
    public SnmpVersion parseValue(String value) throws GuacamoleException {
        
        // Nothing in, nothing out.
        if (value == null)
            return null;
        
        switch(value) {
            case "v1":
                return SnmpVersion.V1;
            case "v2c":
                return SnmpVersion.V2C;
            case "v3":
                return SnmpVersion.V3;
            default:
                throw new GuacamoleServerException("SNMP version must be one of \"v1\", \"v2c\", or \"v3\".");
        }
        
    }
    
}
