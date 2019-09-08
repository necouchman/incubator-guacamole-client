/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.vdi;

/**
 *
 * @author nick_couchman
 */
public interface VDIMachine {
    
    public String getUUID();
    
    public void powerOn();
    
    public void powerOff();
    
    public void restartGuest();
    
    public void shutdownGuest();
    
    public void destroy();
    
}
