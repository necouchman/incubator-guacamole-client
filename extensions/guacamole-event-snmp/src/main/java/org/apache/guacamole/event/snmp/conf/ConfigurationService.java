/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.event.snmp.conf;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.IntegerGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;
import org.snmp4j.AbstractTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

/**
 *
 * @author nick_couchman
 */
public class ConfigurationService {
    
    /**
     * Guacamole server environment.
     */
    private final Environment environment;
    
    public static final String GUACAMOLE_OID = "1.3.6.1.4.1.38971";
    
    public static final InetAddressProperty SNMP_DEST_HOST =
            new InetAddressProperty() {
        
        @Override
        public String getName() { return "snmp-dest-host"; }
                
    };
    
    public static final IntegerGuacamoleProperty SNMP_DEST_PORT =
            new IntegerGuacamoleProperty() {
    
        @Override
        public String getName() { return "snmp-dest-port"; }
                
    };
    
    public static final SnmpVersionProperty SNMP_DEST_VERSION =
            new SnmpVersionProperty() {
    
        @Override
        public String getName() { return "snmp-dest-version"; }
                
    };
    
    public static final StringGuacamoleProperty SNMP_DEST_COMMUNITY =
            new StringGuacamoleProperty() {
 
        @Override
        public String getName() { return "snmp-dest-community"; }
                
    };
    
    public static final StringGuacamoleProperty SNMP_DEST_USER =
            new StringGuacamoleProperty() {
    
        @Override
        public String getName() { return "snmp-dest-user"; }
                
    };
    
    public static final StringGuacamoleProperty SNMP_DEST_PASSWORD =
            new StringGuacamoleProperty() {

        @Override
        public String getName() { return "snmp-dest-password"; }
                
    };
    
    public ConfigurationService(Environment environment) {
        this.environment = environment;
    }
    
    private InetAddress getSnmpDestinationHost() throws GuacamoleException {
        try {
            return environment.getProperty(SNMP_DEST_HOST,
                    InetAddress.getLocalHost()
            );
        }
        catch (UnknownHostException e) {
            throw new GuacamoleServerException("Unable to find address for local host.", e);
        }
    }
    
    private int getSnmpDestinationPort() throws GuacamoleException {
        return environment.getProperty(SNMP_DEST_PORT,
                162
        );
    }
    
    public SnmpVersion getSnmpDestinationVersion() throws GuacamoleException {
        return environment.getProperty(SNMP_DEST_VERSION,
                SnmpVersion.V3
        );
    }
    
    private String getSnmpDestinationCommunity() throws GuacamoleException {
        return environment.getProperty(SNMP_DEST_COMMUNITY,
                "public"
        );
    }
    
    private String getSnmpDestinationUser() throws GuacamoleException {
        return environment.getProperty(SNMP_DEST_USER,
                "guest"
        );
    }
    
    public String getSnmpDestinationPassword() throws GuacamoleException {
        return environment.getProperty(SNMP_DEST_PASSWORD,
                "public"
        );
    }
    
    private Address getSnmpDestination() throws GuacamoleException {
        
        return new UdpAddress(getSnmpDestinationHost(), getSnmpDestinationPort());
        
    }
    
    public AbstractTarget getSnmpTarget() throws GuacamoleException {
        
        AbstractTarget target = null;
        SnmpVersion version = getSnmpDestinationVersion();
        if (version == SnmpVersion.V1 || version == SnmpVersion.V2C) {
            target = new CommunityTarget(getSnmpDestination(),
                new OctetString(getSnmpDestinationCommunity()));
            if (version == SnmpVersion.V1)
                target.setVersion(SnmpConstants.version1);
            else
                target.setVersion(SnmpConstants.version2c);
        }
        else if (version == SnmpVersion.V3) {
            target = new UserTarget(getSnmpDestination(),
                    new OctetString(getSnmpDestinationUser()),
                    null,
                    SecurityLevel.AUTH_NOPRIV
            );
            target.setVersion(SnmpConstants.version3);
        }
        return target;
    }
    
    public PDU getSnmpPdu() throws GuacamoleException {
        SnmpVersion version = getSnmpDestinationVersion();
        PDU snmpPdu;
        if (version == SnmpVersion.V3) {
            snmpPdu = new ScopedPDU();
            snmpPdu.setType(ScopedPDU.NOTIFICATION);
        }
        else {
            snmpPdu = new PDU();
            if (version == SnmpVersion.V1)
                snmpPdu.setType(PDU.V1TRAP);
            else
                snmpPdu.setType(PDU.TRAP);
        }
        return snmpPdu;
    }
    
}
