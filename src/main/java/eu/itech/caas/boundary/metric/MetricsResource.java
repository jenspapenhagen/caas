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
    private ServerWatch watch;

    private List<String> metricParts;

    private String value;

    /**
     * This is a basic metic
     *
     * @return Metric
     */
    @GET
    public String metric() {
        metricParts = new ArrayList<>();

        List<Metric> metricList = new ArrayList<>();
        metricList.add(usedmemory());
        metricList.add(availablememory());
        metricList.add(bootTime());
        metricList.add(systemLoadAverage());
        metricList.add(availableProcessors());
        metricList.add(osName());
        metricList.add(osArchitecture());
        metricList.add(osVersion());
        metricList.add(treadCount());
        metricList.add(preakTreadCount());
        metricList.add(classLoadedCount());
        //add new metics here
        
        
        

        List<String> output = new ArrayList<>();
        metricList.stream().forEach(m -> {
            fillMetric(m);
            output.add(toMetric());
        });

        //output without brackets and commas
        StringBuilder stringBuilder = new StringBuilder();
        output.forEach(stringBuilder::append);

        return stringBuilder.toString();
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
        Metric m = new Metric("caasservice", "systemLoad", null, "systemLoad");
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
     * this methode give back the Thread count of live threads including both
     * daemon and non-daemon threads.
     *
     * @return Metric
     */
    private Metric treadCount() {
        Metric m = new Metric("caasservice", "tread", "count", "total");
        m.setValue(String.valueOf(this.watch.treadCount()));
        return m;
    }

    /**
     * this methode give back the Thread peak count since the Java virtual
     * machine started or peak was reset.
     *
     * @return Metric
     */
    private Metric preakTreadCount() {
        Metric m = new Metric("caasservice", "tread", "count", "peak");
        m.setValue(String.valueOf(this.watch.peakTradCount()));
        return m;
    }

    /**
     * this methode give back the Class number of classes that are currently
     * loaded in the Java virtual machine.
     *
     * @return Metric
     */
    private Metric classLoadedCount() {
        Metric m = new Metric("caasservice", "classLoaded", "count", "total");
        m.setValue(String.valueOf(this.watch.classLoadedCount()));
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
                filter(s -> !s.isEmpty()).
                collect(Collectors.joining("_"));
        //reset value to null if not forund
        if (value == null) {
            value = "";
        }

        return outputMetric + " " + value + "\n";
    }

}
