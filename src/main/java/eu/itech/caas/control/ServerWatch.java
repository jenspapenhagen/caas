/*
 * Copyright 2018 airhacks.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.itech.caas.control;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.time.ZonedDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author airhacks.com
 */
@Startup
@Singleton
public class ServerWatch {

    private ZonedDateTime startTime;
    private MemoryMXBean memoryMxBean;

    @PostConstruct
    public void initialize() {
        this.initializeStartTime();
        this.memoryMxBean = ManagementFactory.getMemoryMXBean();
    }

    private void initializeStartTime() {
        this.startTime = ZonedDateTime.now();
    }

    public ZonedDateTime getDateTime() {
        return this.startTime;
    }

    public double availableMemoryInMB() {
        MemoryUsage current = this.memoryMxBean.getHeapMemoryUsage();
        long available = (current.getCommitted() - current.getUsed());
        return asMb(available);
    }

    public double usedMemoryInMb() {
        MemoryUsage current = this.memoryMxBean.getHeapMemoryUsage();
        return asMb(current.getUsed());
    }

    public JsonObject osInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return Json.createObjectBuilder().
                add("System Load Average", osBean.getSystemLoadAverage()).
                add("Available CPUs", osBean.getAvailableProcessors()).
                add("Architecture", osBean.getArch()).
                add("OS Name", osBean.getName()).
                add("Version", osBean.getVersion()).build();
    }

    /**
     * This methode give back MB with out divide two times 1024 with double.
     * This getting unexcact
     *
     * @param bytes
     * @return a double in Megabyte
     */
    private double asMb(long bytes) {
        // 1024 * 1024 = 1048576
        return bytes / 1048576;
    }

}
