/*
 * Copyright 2018 jay.
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

/**
 * Prometheus Metric https://prometheus.io/docs/concepts/metric_types/
 *
 * @author jay
 */
public class Metric {

    private String application;

    private String component;

    private String units;

    private String suffix;

    private String value;

    /**
     * this is the constructor you have to add value later
     *
     * @param application
     * @param component
     * @param units
     * @param suffix
     */
    public Metric(String application, String component, String units, String suffix) {
        this.application = application;
        this.component = component;
        this.units = units;
        this.suffix = suffix;
    }

    public String getApplication() {
        return application;
    }

    public String getComponent() {
        return component;
    }

    public String getUnits() {
        return units;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
