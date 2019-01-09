/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.event.snmp;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.event.snmp.conf.ConfigurationService;
import org.apache.guacamole.event.snmp.conf.SnmpVersion;
import org.apache.guacamole.net.event.listener.Listener;
import org.snmp4j.AbstractTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

/**
 * Event listener class which sends events to an SNMP destination.
 */
public class SnmpEventListener implements Listener {
    
    private final ConfigurationService confService;
    
    public SnmpEventListener() throws GuacamoleException {
        this.confService = new ConfigurationService(new LocalEnvironment());
    }
    
    @Override
    public void handleEvent(Object event) throws GuacamoleException {
        Snmp snmp = new Snmp();
        AbstractTarget target = confService.getSnmpTarget();
        SnmpVersion version = confService.getSnmpDestinationVersion();
        PDU trapPdu;
        if (version == SnmpVersion.V3)
            trapPdu = new ScopedPDU();
        else
            trapPdu = new PDU();
       
    }
    
}