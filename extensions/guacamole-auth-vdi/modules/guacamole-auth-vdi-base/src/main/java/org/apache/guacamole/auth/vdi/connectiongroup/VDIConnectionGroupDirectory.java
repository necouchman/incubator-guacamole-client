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

package org.apache.guacamole.auth.vdi.connectiongroup;

import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.DecoratingDirectory;
import org.apache.guacamole.net.auth.Directory;

/**
 * A directory of ConnectionGroups that contains groups that decorate base
 * ConnectionGroup objects from another directory, hosting VDI workloads.
 */
public class VDIConnectionGroupDirectory extends DecoratingDirectory<ConnectionGroup> {
    
    public VDIConnectionGroupDirectory(Directory<ConnectionGroup> connectionGroups) {
        super(connectionGroups);
    }
    
    @Override
    public ConnectionGroup decorate(ConnectionGroup object) {
        if (!(object instanceof VDIConnectionGroup))
            return new VDIConnectionGroup(object);
        return object;
    }
    
    @Override
    public ConnectionGroup undecorate(ConnectionGroup object) {
        if (object instanceof VDIConnectionGroup)
            return ((VDIConnectionGroup) object).getUndecorated();
        return object;
    }
    
}
