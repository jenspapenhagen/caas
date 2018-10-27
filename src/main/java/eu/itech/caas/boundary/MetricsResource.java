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
package eu.itech.caas.boundary;

import eu.itech.caas.control.ServerWatch;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Prometheus Endpoint with 2 simple metrics
 *
 * this is a testrun for not use io.prometheus dependency For easyer and
 * complexer metics better use it. but for this very basic and small metrics its
 * fine
 *
 * @author Jens Papenhagen
 */
@Path("metrics")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class MetricsResource {

    @Inject
    ServerWatch watch;

    /**
     * This is a basic metic for memory usage of the heap that is used for
     * object allocation.
     *
     * @return a JsonObject in Prometheus format
     */
    @GET
    @Path("usedmemory")
    public JsonObject metric() {
        return Json.createObjectBuilder()
                .add("application", "caas-service")
                .add("component", "jvmusedmemory")
                .add("units", "bytes")
                .add("suffix", "size")
                .add("value", String.valueOf(this.watch.usedMemoryInMb()))
                .build();
    }

    /**
     * This is a basic metic for memory available of the heap
     *
     * @return a JsonObject in Prometheus format
     */
    @GET
    @Path("availablememory")
    public JsonObject additional() {
        return Json.createObjectBuilder()
                .add("application", "caas-service")
                .add("component", "jvmavailablememory")
                .add("units", "bytes")
                .add("suffix", "size")
                .add("value", String.valueOf(this.watch.availableMemoryInMB()))
                .build();
    }

    /**
     * This methode give backt he starting time of this service
     *
     * @return a JsonObject in flat JSON format
     */
    @GET
    @Path("/start-time")
    @Produces(MediaType.TEXT_PLAIN)
    public JsonObject bootTime() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("boottime", watch.getDateTime().toString());

        return builder.build();
    }

    /**
     * WARNING 
     * do _NOT_ expose in production
     * 
     * this methode is showing all Infos form the OS
     * 
     * @return  JsonObject in flat JSON format
     */
    @GET
    @Path("/os-info")
    public JsonObject osInfo() {
        return this.watch.osInfo();
    }

}
