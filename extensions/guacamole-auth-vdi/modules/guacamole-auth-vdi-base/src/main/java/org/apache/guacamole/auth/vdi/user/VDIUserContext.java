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

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.auth.vdi.connectiongroup.VDIConnectionGroup;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.DecoratingDirectory;
import org.apache.guacamole.net.auth.DelegatingUserContext;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.net.auth.Permissions;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.permission.ObjectPermission;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.permission.SystemPermission;
import org.apache.guacamole.net.auth.permission.SystemPermissionSet;

/**
 * A user context that decorates another user context, delegating storage
 * of objects within the context to an underlying data store.
 */
public class VDIUserContext extends DelegatingUserContext {
    
    /**
     * 
     * @param userContext 
     */
    @AssistedInject
    public VDIUserContext(@Assisted UserContext userContext) {
        super(userContext);
    }
    
    @Override
    public Directory<ConnectionGroup> getConnectionGroupDirectory()
            throws GuacamoleException {
        return new DecoratingDirectory<ConnectionGroup>(
                super.getConnectionGroupDirectory()) {
            
            @Override
            protected ConnectionGroup decorate(ConnectionGroup object)
                    throws GuacamoleException {
                
                Permissions effective = self().getEffectivePermissions();
                SystemPermissionSet sysPermissions = effective.getSystemPermissions();
                ObjectPermissionSet objPermissions = effective.getConnectionGroupPermissions();
                
                boolean canUpdate = sysPermissions.hasPermission(SystemPermission.Type.ADMINISTER)
                        || objPermissions.hasPermission(ObjectPermission.Type.UPDATE, object.getIdentifier());
                
                return new VDIConnectionGroup(object, canUpdate);
                
            }
            
            @Override
            protected ConnectionGroup undecorate(ConnectionGroup object) {
                assert(object instanceof VDIConnectionGroup);
                return ((VDIConnectionGroup) object).getUndecorated();
            }
                    
        };
    }
    
    @Override
    public Collection<Form> getConnectionGroupAttributes() {
        Collection<Form> allAttrs = new HashSet<>(super.getConnectionGroupAttributes());
        allAttrs.addAll(VDIConnectionGroup.VDI_FORMS);
        return Collections.unmodifiableCollection(allAttrs);
    }
    
}
