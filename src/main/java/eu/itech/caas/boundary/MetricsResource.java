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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
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

    private List<String> metricParts;

    private String value;

    public static final String APPLICATION = "application";

    public static final String COMPONENT = "component";

    public static final String UNITS = "units";

    public static final String SUFFIX = "suffix";

    /**
     * This is a basic metic
     *
     * @return a JsonObject in Prometheus format
     */
    @GET
    public String metric() {
        metricParts = new ArrayList<>();

        List<String> output = new ArrayList<>();
        fillMetric(usedmemory());
        output.add(toMetric());

        fillMetric(availablememory());
        output.add(toMetric());

        fillMetric(bootTime());
        output.add(toMetric());

        fillMetric(systemLoadAverage());
        output.add(toMetric());

        fillMetric(availableProcessors());
        output.add(toMetric());

        fillMetric(osName());
        output.add(toMetric());

        fillMetric(osArchitecture());
        output.add(toMetric());

        fillMetric(osVersion());
        output.add(toMetric());

        //output without brackets and commas
        StringBuilder builder = new StringBuilder();
        output.forEach(builder::append);

        return builder.toString();
    }

    private void fillMetric(JsonObject input) {

        String application = input.getString(APPLICATION, null);
        String component = input.getString(COMPONENT, null);
        String units = input.getString(UNITS, null);
        String suffix = input.getString(SUFFIX, null);

        this.value = input.getString("value", null);

        this.metricParts = Arrays.asList(application, component, units, suffix);
    }

    /**
     * This is a basic metic for memory usage of the heap that is used for
     * object allocation.
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject usedmemory() {
        return Json.createObjectBuilder()
                .add("application", "caasservice")
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
    private JsonObject availablememory() {
        return Json.createObjectBuilder()
                .add("application", "caasservice")
                .add("component", "jvmavailablememory")
                .add("units", "bytes")
                .add("suffix", "size")
                .add("value", String.valueOf(this.watch.availableMemoryInMB()))
                .build();
    }

    /**
     * This methode give backt the starting time of this service
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject bootTime() {
        return Json.createObjectBuilder().add("application", "caasservice")
                .add("component", "bootup")
                .add("units", "seconds")
                .add("suffix", "time")
                .add("value", String.valueOf(watch.getDateTime().toEpochSecond()))
                .build();
    }

    /**
     * this methode give back the System Load Average
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject systemLoadAverage() {
        return Json.createObjectBuilder().add("application", "caasservice")
                .add("component", "systemLoad")
                .add("suffix", "average")
                .add("value", this.watch.systemLoadAverage())
                .build();
    }

    /**
     * this methode give back the available Processors/cores
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject availableProcessors() {
        return Json.createObjectBuilder().add("application", "caasservice")
                .add("component", "availableProcessors")
                .add("units", "count")
                .add("suffix", "total")
                .add("value", String.valueOf(this.watch.availableProcessors()))
                .build();
    }

    /**
     * this methode give back the Name of the underlaying Operation System
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject osName() {
        return Json.createObjectBuilder().add("application", "caasservice")
                .add("component", "osName")
                .add("value", this.watch.osName())
                .build();
    }

    /**
     * this methode give back the Architecture of the underlaying Operation
     * System
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject osArchitecture() {
        return Json.createObjectBuilder().add("application", "caasservice")
                .add("component", "osArchitecture")
                .add("value", this.watch.osArchitecture())
                .build();
    }

    /**
     * this methode give back the Verison of the underlaying Operation System
     *
     * @return a JsonObject in Prometheus format
     */
    private JsonObject osVersion() {
        return Json.createObjectBuilder().add("application", "caasservice")
                .add("component", "osVersion")
                .add("value", this.watch.osVersion())
                .build();
    }

    /**
     * this methode convert a strict JsonObject to a "Prometheus format" String
     *
     * @return a "Prometheus format" String
     */
    private String toMetric() {
        String metric = this.metricParts.stream().
                filter(s -> s != null).
                collect(Collectors.joining("_"));
        //reset value to null if not forund
        if (value == null) {
            value = "";
        }

        return metric + " " + value + "\n";
    }

}
