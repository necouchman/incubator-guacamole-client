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

package org.apache.guacamole.auth.vdi.user;

import org.apache.guacamole.net.auth.UserContext;

/**
 * Factory for creating UserContext objects that implement hypervisor-specific
 * management of VDIConnectionGroups.
 */
public interface VDIUserContextFactory {
    
    /**
     * Returns a new instance of a UserContext which decorates another
     * UserContext and implements hypervisor-specific management of
     * VDIConnectionGroups and the connections associated with them.
     * 
     * @param userContext
     *     The original UserContext object that will be decorated.
     * 
     * @return 
     *     A new UserContext that manages Hypervisor-specific implementations
     *     of VDIConnectionGroups.
     */
    UserContext create(UserContext userContext);
    
}
