/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.event.snmp;

import java.net.SocketException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.event.snmp.conf.ConfigurationService;
import org.apache.guacamole.net.event.listener.Listener;
import org.snmp4j.AbstractTarget;
import org.snmp4j.CertifiedTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.smi.OctetString;
import org.snmp4j.Snmp;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UdpAddress;
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
       
    }
    
    private void sendTrap() throws GuacamoleException {
        AbstractTarget target = confService.getSnmpTarget();
        PDU trapPdu = new PDU();
        trapPdu.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise, 
                new OID(ConfigurationService.GUACAMOLE_OID)));
        trapPdu.setType(PDU.V1TRAP);
        
    }
    
    private void sendV1Trap() throws GuacamoleException {

    }
    
    private void sendV2Trap() {
        
    }
    
    private void sendV3Trap() {
        
    }
}
