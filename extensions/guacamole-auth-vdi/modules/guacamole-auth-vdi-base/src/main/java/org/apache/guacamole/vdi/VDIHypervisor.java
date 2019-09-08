/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.vdi;

import java.util.List;
import java.util.Map;

/**
 *
 * @author nick_couchman
 */
public interface VDIHypervisor {
    
    public List<String> getVDIMachines();
    
    public String getVDIMachine(String uuid);
    
    public Map<String, String> getTemplates();
    
    public Map<String, String> getStorage();
    
    public Map<String, String> getNetworks();
    
    public void createMachine();
    
    public void startMachine(String uuid);
    
    public void stopMachine(String uuid, Boolean clean);
    
    public void restartMachine(String uuid, Boolean clean);
    
    public void rebuildMachine(String uuid);
    
}
