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
package eu.itech.caas.boundary.metric;

import eu.itech.caas.control.ServerWatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

//    public static final String APPLICATION = "application";
//
//    public static final String COMPONENT = "component";
//
//    public static final String UNITS = "units";
//
//    public static final String SUFFIX = "suffix";

    /**
     * This is a basic metic
     *
     * @return Metric
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

    private void fillMetric(Metric input) {
        String application = input.getApplication();
        String component = input.getComponent();
        String units = input.getUnits();
        String suffix = input.getSuffix();

        this.value = input.getValue();

        this.metricParts = Arrays.asList(application, component, units, suffix);
    }

    /**
     * This is a basic metic for memory usage of the heap that is used for
     * object allocation.
     *
     * @return Metric
     */
    private Metric usedmemory() {
        Metric m = new Metric("caasservice", "jvmusedmemory", "bytes", "size");
        m.setValue(String.valueOf(this.watch.usedMemoryInMb()));
        return m;
    }

    /**
     * This is a basic metic for memory available of the heap
     *
     * @return Metric
     */
    private Metric availablememory() {
        Metric m = new Metric("caasservice", "jvmavailablememory", "bytes", "size");
        m.setValue(String.valueOf(this.watch.availableMemoryInMB()));
        return m;
    }

    /**
     * This methode give backt the starting time of this service
     *
     * @return Metric
     */
    private Metric bootTime() {
        Metric m = new Metric("caasservice", "bootup", "seconds", "time");
        m.setValue(String.valueOf(watch.getDateTime().toEpochSecond()));
        return m;
    }

    /**
     * this methode give back the System Load Average
     *
     * @return Metric
     */
    private Metric systemLoadAverage() {
        Metric m = new Metric( "caasservice", "systemLoad", null, "systemLoad");
        m.setValue(this.watch.systemLoadAverage());
        return m;
    }

    /**
     * this methode give back the available Processors/cores
     *
     * @return Metric
     */
    private Metric availableProcessors() {
        Metric m = new Metric("caasservice", "availableProcessors", "count", "total");
        m.setValue(String.valueOf(this.watch.availableProcessors()));
        return m;
    }

    /**
     * this methode give back the Name of the underlaying Operation System
     *
     * @return Metric
     */
    private Metric osName() {
        Metric m = new Metric("caasservice", "osName", null, null);
        m.setValue(this.watch.osName());
        return m;
    }

    /**
     * this methode give back the Architecture of the underlaying Operation
     * System
     *
     * @return Metric
     */
    private Metric osArchitecture() {
        Metric m = new Metric("caasservice", "osArchitecture", null, null);
        m.setValue(this.watch.osArchitecture());
        return m;
    }

    /**
     * this methode give back the Verison of the underlaying Operation System
     *
     * @return Metric
     */
    private Metric osVersion() {
        Metric m = new Metric("caasservice", "osVersion", null, null);
        m.setValue(this.watch.osVersion());
        return m;
    }

    /**
     * this methode convert a strict Metric to a "Prometheus format" String
     *
     * @return a "Prometheus format" String
     */
    private String toMetric() {
        String outputMetric = this.metricParts.stream().
                filter(s -> s != null).
                filter(s-> !s.isEmpty()).
                collect(Collectors.joining("_"));
        //reset value to null if not forund
        if (value == null) {
            value = "";
        }

        return outputMetric + " " + value + "\n";
    }

}
