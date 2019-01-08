/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.event.snmp.conf;

/**
 * Simple enum to handle versions of SNMP
 */
public enum SnmpVersion {
    
    /**
     * SNMPv1, really, really old.
     */
    V1,
    
    /**
     * SNMPv2c, old, but still commonly used.
     */
    V2C,
    
    /**
     * SNMPv3, new, secure, etc.
     */
    V3;
    
}
