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

package org.apache.guacamole.dashboard.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides various utility access to system-level metrics, including
 * CPU utilization, memory usage, and disk usage.
 */
public class SystemUtilities {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(SystemUtilities.class);
    
    public static final File MEMORY_FILE = new File("/proc/meminfo");
    
    public static final File LOAD_FILE = new File("/proc/loadavg");
    
    public static final File DISK_FILE = new File("/proc/diskstats");
    
    public static final File CPU_FILE = new File("/proc/stat");
    
    private static final Map<String, Double> CPU_STATS =  new HashMap<>();
    
    /**
     * Executor service which runs the periodic cleanup task
     */
    private static final ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(1);
    
    public static long getTotalMemory() {
        return readMemoryStats().get("MemTotal");
    }
    
    public static long getUsedMemory() {
        Map<String, Long> stats = readMemoryStats();
        return stats.get("MemTotal") - stats.get("MemFree");
    }
    
    public static long getFreeMemory() {
        return readMemoryStats().get("MemFree");
    }
    
    public static long getAvailMemory() {
        return readMemoryStats().get("MemAvailable");
    }
    
    public static long getCacheMemory() {
        return readMemoryStats().get("Cached");
    }
    
    public static long getBufferMemory() {
        return readMemoryStats().get("Buffers");
    }
    
    public static long getActiveMemory() {
        return readMemoryStats().get("Active");
    }
    
    public static long getInactiveMemory() {
        return readMemoryStats().get("Inactive");
    }
    
    public static Map<String, Long> readMemoryStats() {
        
        Map<String, Long> MemoryInfo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(MEMORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                String metric = tokens[0].replace(":", "");
                long value = Long.parseLong(tokens[1]);
                MemoryInfo.put(metric, value);
            }
            
            return Collections.unmodifiableMap(MemoryInfo);
            
        }
        catch (IOException e) {
            return Collections.emptyMap();
        }
        
    }
    
    public static double getSystemCPU() {
        return 0.00;
    }
    
    public static double getUserCPU() {
        return 0.00;
    }
    
    public static double getNiceCPU() {
        return 0.00;
    }
    
    public static double getIdleCPU() {
        return 0.00;
    }
    
    public static double getIOCPU() {
        return 0.00;
    }
    
    private static Map<String, Long> readCPUStats() {
        
        Map<String, Long> ProcessorInfo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CPU_FILE))) {
            String line = reader.readLine();
            String[] tokens = line.split("\\s+");
            long total = 0;
            for (int i = 1; i <= 9; i++) {
                total += Long.parseLong(tokens[i]);
            }
            
            ProcessorInfo.put("total", total);
            ProcessorInfo.put("user", Long.parseLong(tokens[1]));
            ProcessorInfo.put("nice", Long.parseLong(tokens[2]));
            ProcessorInfo.put("system", Long.parseLong(tokens[3]));
            ProcessorInfo.put("idle", Long.parseLong(tokens[4]));
            ProcessorInfo.put("iowait", Long.parseLong(tokens[5]));
            ProcessorInfo.put("hwirq", Long.parseLong(tokens[6]));
            ProcessorInfo.put("swirq", Long.parseLong(tokens[7]));
            ProcessorInfo.put("steal", Long.parseLong(tokens[8]));
            ProcessorInfo.put("guest", Long.parseLong(tokens[9]));
            ProcessorInfo.put("timestamp", System.currentTimeMillis());
            
            return Collections.unmodifiableMap(ProcessorInfo);
            
        }
        catch (IOException e) {
            return Collections.emptyMap();
        }
        
    }
    
    public static Map<String, Double> getCPUStats() {
        return CPU_STATS;
    }
    
    public static double getOneMinLoadAve() {
        return readLoadAve().get(1);
    }
    
    public static double getFiveMinLoadAve() {
        return readLoadAve().get(5);
    }
    
    public static double getFifteenMinLoadAve() {
        return readLoadAve().get(15);
    }
    
    public static Map<Integer, Double> readLoadAve() {
        
        Map<Integer, Double> LoadInfo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LOAD_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] tokens = line.split("\\s+");
                LoadInfo.put(1,Double.parseDouble(tokens[0]));
                LoadInfo.put(5, Double.parseDouble(tokens[1]));
                LoadInfo.put(15, Double.parseDouble(tokens[2]));
                
            }
            
            return Collections.unmodifiableMap(LoadInfo);
            
        }
        catch (IOException e) {
            return Collections.emptyMap();
        }
        
    }
    
    public static long getSwapTotal() {
        return readMemoryStats().get("SwapTotal");
    } 
    
    public static long getSwapFree() {
        return readMemoryStats().get("SwapFree");
    }
    
    public static long getSwapUsed() {
        Map<String, Long> memStats = readMemoryStats();
        return memStats.get("SwapTotal") - memStats.get("SwapFree");
    }
    
    public static Map<String, Object> getAllStats() {
        Map<String, Object> allStats = new HashMap<>();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            
            method.setAccessible(true);
            String methodName = method.getName();
            if (methodName.startsWith("get")
              && Modifier.isPublic(method.getModifiers())) {
                  
                try {
                    Object value = method.invoke(operatingSystemMXBean);
                    allStats.put(methodName, value);
                } catch (Exception e) {
                    // Do nothing.
                } // try
                
            } // if
            
        } // for
        
        return allStats;
        
    }
    
    public static void run() {
        LOGGER.debug(">>>STATS<<< Starting CPU tracker.");
        executor.scheduleAtFixedRate(new SystemUtilitiesCPU(), 5, 5, TimeUnit.MINUTES);
    }
    
    public static void shutdown() {
        LOGGER.debug(">>>STATS<<< Shutting down CPU tracker.");
        executor.shutdownNow();
    }
    
    private static class SystemUtilitiesCPU implements Runnable {
        
        private Map<String, Long> prev = new HashMap<>();
        
        private Map<String, Long> curr = new HashMap<>();
        
        public SystemUtilitiesCPU() {
            prev = readCPUStats();
            curr = readCPUStats();
        }
        
        @Override
        public void run() {
            LOGGER.debug(">>>STATS<<< Updating CPU statistics...");
            prev = curr;
            curr = readCPUStats();
            
            Long deltaTotal = curr.get("total") - prev.get("total");
            
            for (Entry<String, Long> entry : curr.entrySet()) {
                String key = entry.getKey();
                Long prevValue = prev.get(key);
                Long currValue = entry.getValue();
                Long delta = currValue - prevValue;
                Double util = delta.doubleValue() / deltaTotal.doubleValue() * 100;
                CPU_STATS.put(key, util);
            }
            
            CPU_STATS.put("timestamp", Double.longBitsToDouble(System.currentTimeMillis()));
            
        }
        
    }
    
}
