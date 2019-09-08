/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.guacamole.auth.vdi;

import com.google.inject.AbstractModule;

/**
 * Base Guice module for dependency loading for VDI-related modules, which will
 * be concretely implemented by each of the Hyprevisor modules.
 */
public abstract class VDIAuthenticationProviderModule extends AbstractModule {
    
}
