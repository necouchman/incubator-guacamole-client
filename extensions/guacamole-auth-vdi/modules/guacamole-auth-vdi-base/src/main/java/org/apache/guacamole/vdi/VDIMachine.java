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

package org.apache.guacamole.vdi;

/**
 * A representation of a machine provided by the hypervisor.
 */
public interface VDIMachine {
    
    /**
     * Return a string representation of the identifier of the virtual machine.
     * 
     * @return 
     *     The identifier of the virtual machine as a string.
     */
    public String getIdentifier();
    
    /**
     * Return the identifier of the VDI pool of which the machine is part.
     * 
     * @return 
     *     The identifier of the VDI pool.
     */
    public String getPool();
    
    /**
     * Power the virtual machine on.
     */
    public void powerOn();
    
    /**
     * Power the virtual machine off.
     */
    public void powerOff();
    
    /**
     * Restart the guest O/S of the virtual machine.
     */
    public void restartGuest();
    
    /**
     * Reset the virtual machine.
     */
    public void reset();
    
    /**
     * Shut down the guest O/S of the virtual machine.
     */
    public void shutdownGuest();
    
    /**
     * Destroy the virtual machine.
     */
    public void destroy();
    
}
