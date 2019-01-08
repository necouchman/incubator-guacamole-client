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
import org.apache.guacamole.properties.GuacamoleProperty;

/**
 *
 * @author nick_couchman
 */
public abstract class InetAddressProperty implements GuacamoleProperty<InetAddress> {
    
    @Override
    public InetAddress parseValue(String value) throws GuacamoleException {
        try {
            return InetAddress.getByName(value);
        }
        catch (UnknownHostException e) {
            throw new GuacamoleServerException("Invalid host \"" + value + "\" specified.", e);
        }
    }
    
}
