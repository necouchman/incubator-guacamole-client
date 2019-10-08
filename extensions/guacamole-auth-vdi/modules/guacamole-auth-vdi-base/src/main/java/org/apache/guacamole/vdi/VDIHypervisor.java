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
