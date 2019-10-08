/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.vdi.vmware;

import java.util.UUID;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.vdi.VDIMachine;

/**
 * An implementation of the VDIMachine interface specific to the VMware
 * hypervisor, that represents VMware Virtual Machines and provides methods for
 * controlling those items.
 */
public class VMwareVDIMachine implements VDIMachine {

    private final UUID uuid;
    
    /**
     * Create a new instance of this class with the given VMware Virtual Machine
     * UUID.
     * 
     * @param uuid
     *     The UUID of the Virtual Machine that this instance represents.
     */
    public VMwareVDIMachine(UUID uuid) {
        this.uuid = uuid;
    }
    
    /**
     * Create a new instance of this class with the VMware Virtual Machine name.
     * The configured VMware instance will be searched for a VM matching that
     * name, and, if found, the instance will be created with the UUID that is
     * located.  If either none or more than one UUID are found, an exception
     * will be thrown and instantiation of the class will fail.
     * 
     * @param name
     *     The name of the virtual machine.
     * 
     * @throws GuacamoleException
     *     If either zero or more than one virtual machine are located.
     */
    public VMwareVDIMachine(String name) throws GuacamoleException {
        this(getVMUUID(name));
    }
    
    /**
     * Get the UUID of the virtual machine with the given name, if one is found.
     * If either zero or more than one UUIDs are located, an exception will be
     * thrown.
     * 
     * @param name
     *     The name of the virtual machine.
     * 
     * @return
     *     The UUID of the virtual machine.
     * 
     * @throws GuacamoleException 
     *     If either zero or more than one virtual machine with the given name
     *     are located.
     */
    private static UUID getVMUUID(String name) throws GuacamoleException {
        
        /* ToDo:
         * 0 Connect to VMware Hypervisor
         * - Search for VMs with the given name
         * - Return exactly one (1) UUID.
         */
        
    }
    
    @Override
    public String getUUID() {
        return uuid.toString();
    }

    @Override
    public void powerOn() {
        
        /* ToDo:
         * - Connect to VMware HyperVisor
         * - Execute PowerOn method for current VM via UUID.
         */
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void powerOff() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void restartGuest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void shutdownGuest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
