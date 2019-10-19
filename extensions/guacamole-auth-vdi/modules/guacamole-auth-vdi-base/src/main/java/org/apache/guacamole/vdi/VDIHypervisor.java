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

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

/**
 * Defines the standardized way that VDI hypervisors should interact with the
 * upstream Guacamole Client classes.  The methods defined here will be built
 * out in concrete fashion in the implementing classes for various supported
 * hypervisors.
 */
public interface VDIHypervisor {
    
    /**
     * Get a list of the identifiers of the machines from the hypervisor with
     * the given tags, or an empty list if no machines are found.
     * 
     * @param tags
     *     An array of tags to use to filter the machines.
     * 
     * @return 
     *     The list of virtual machine identifiers located.
     */
    public List<String> getMachines(String[] tags);
    
    /**
     * Return the virtual machine with the given identifier from the hypervisor,
     * or null if the machine cannot be found.
     * 
     * @param id
     *     The identifier of the machine to retrieve.
     * 
     * @return 
     *     The virtual machine, or null if no machine was found.
     */
    public VDIMachine getMachine(String id);
    
    /**
     * Get the available virtual machine templates for the hypervisor.
     * 
     * @return 
     *     A list of the available templates.
     */
    public List<VDITemplate> getTemplates();
    
    /**
     * Get the available virtual machine templates for the hypervisor in the
     * given folder.
     * 
     * @param folder
     *     The folder in which to look for templates.
     * 
     * @return 
     *    A list of the available templates.
     */
    public List<VDITemplate> getTemplates(String folder);
    
    /**
     * Get a map of the identifier and name of the available storage
     * repositories for the hypervisor.
     * 
     * @return 
     *     A map of identifiers and names of storage repositories.
     */
    public Map<String, String> getStorage();
    
    /**
     * Return a map of the available network identifiers and names for the
     * given hypervisor.
     * 
     * @return 
     *     A map of the available network identifiers and names.
     */
    public Map<String, String> getNetworks();
    
    /**
     * Create a new machine with the provided information, using DHCP to obtain
     * an IP address, and return the identifier of the machine upon completion,
     * or null if the virtual machine cannot be created.
     * 
     * @param pool
     *     The identifier of the VDI pool the machine will be a part of.
     * 
     * @param template
     *     The template to use as a base image for the machine.
     * 
     * @param name
     *     The name to assign to the machine.
     * 
     * @param network
     *     The identifier of the network the machine will be attached to.
     * 
     * @return 
     *     The identifier of the newly-created machine.
     */
    public String createMachine(String pool, VDITemplate template, String name,
            String network);
    
        /**
     * Create a new machine with the provided information, using DHCP to obtain
     * an IP address, join the domain to AD, and return the identifier of the
     * machine upon completion, or null if the virtual machine cannot be created.
     * 
     * @param pool
     *     The identifier of the VDI pool the machine will be a part of.
     * 
     * @param template
     *     The template to use as a base image for the machine.
     * 
     * @param name
     *     The name to assign to the machine.
     * 
     * @param network
     *     The identifier of the network the machine will be attached to.
     * 
     * @param domain
     *     The name of the AD domain to join.
     * 
     * @param username
     *     The username to use when joining the domain.
     * 
     * @param password
     *     The password to use when joining the domain.
     * 
     * @param ou
     *     The OU within the AD tree to place the machine object in.
     * 
     * @return 
     *     The identifier of the newly-created machine.
     */
    public String createMachine(String pool, VDITemplate template, String name,
            String network, String domain, String username, String password,
            String ou);
    
    /**
     * Create a new virtual machine as part of a VDI pool using the provided
     * information, and returning the identifier of the machine upon successful
     * completion, or null if the machine could not be created.  This method
     * takes static network information to assign to the machine.
     * 
     * @param pool
     *     The identifier of the VDI pool this machine will be a part of.
     * 
     * @param template
     *     The template to use as the base for this machine.
     * 
     * @param name
     *     The name of the virtual machine.
     * 
     * @param network
     *     The identifier of the network to attach to the virtual machine.
     * 
     * @param ip
     *     The IP address of the VM.
     * 
     * @param mask
     *     The subnet mask of the VM.
     * 
     * @param gw
     *     The default gateway address for the VM.
     * 
     * @param dns
     *     An array of DNS server IP addresses.
     * 
     * @param domain
     *     The primary domain for the virtual machine.  If the machine is
     *     to be joined to a domain, this will also be used as the domain the
     *     system will be joined to.
     * 
     * @param joinDomain
     *     Whether or not the system should join the domain as an
     *     Active Directory member.
     * 
     * @param username
     *     The username to use when joining a domain.
     * 
     * @param password
     *     The password to use when joining a domain.
     * 
     * @param ou
     *     The OU to place the machine object in within a joined domain.
     * 
     * @param searchDomains
     *     A list of DNS search domains.
     * 
     * @return 
     *     The identifier of the created machine.
     */
    public String createMachine(String pool, VDITemplate template, String name,
            String network, InetAddress ip, InetAddress mask, InetAddress gw,
            InetAddress[] dns, String domain, Boolean joinDomain,
            String username, String password, String ou, String[] searchDomains);
    
    /**
     * Destroy the machine with the given identifier, returning true if the
     * system is successfully destroyed, or false if an error occurs.  This will
     * uncleanly halt the system and delete it.
     * 
     * @param id
     *     The identifier of the virtual machine to destroy.
     * 
     * @return 
     *     True if the machine is destroyed successfully, otherwise false.
     */
    public Boolean destroyMachine(String id);
    
    /**
     * Boot up the machine with the given identifier, returning true if the
     * virtual machine was successfully booted, or false otherwise.
     * 
     * @param id
     *     The identifier of the virtual machine to boot.
     * 
     * @return
     *     True if the machine is started, otherwise false.
     */
    public Boolean startMachine(String id);
    
    /**
     * Stop the virtual machine with the given identifier, and return true if
     * the machine is successfully stopped, otherwise false.  If the clean
     * flag is true the machine will be cleanly shut down, otherwise it will
     * be powered off.
     * 
     * @param id
     *     The identifier of the machine to boot.
     * 
     * @param clean
     *     True if the machine should be shut down cleanly, false if the machine
     *     should simply be powered off.
     * 
     * @return 
     *     True if the machine was successfully stopped, false if an error occurs.
     */
    public Boolean stopMachine(String id, Boolean clean);
    
    /**
     * Restart the machine with the given identifier, cleanly if the clean
     * parameter is set to true.
     * 
     * @param id
     *     The identifier of the machine to restart.
     * 
     * @param clean 
     *     True if the restart should be done cleanly, false if it should simply
     *     be reset.
     * 
     * @return
     *     True if the machine is successfully restarted, false if an error
     *     occurs during the restart.
     */
    public Boolean restartMachine(String id, Boolean clean);
    
    /**
     * Rebuild the machine with the given identifier from its base template
     * or snapshot, returning true if the machine is successfully rebuilt, or
     * false if an error occurs.
     * 
     * @param id 
     *     The identifier of the virtual machine to rebuild.
     * 
     * @return 
     *     True if the rebuild succeeds, otherwise false.
     */
    public Boolean rebuildMachine(String id);
    
}
