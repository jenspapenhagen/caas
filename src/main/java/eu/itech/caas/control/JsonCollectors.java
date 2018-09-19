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

import java.util.Map;
import java.util.stream.Collector;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author airhacks.com
 */
public interface JsonCollectors {

    public static <T> Collector<Map.Entry<T, T>, ?, JsonObjectBuilder> toJsonBuilder() {
        return Collector.of(Json::createObjectBuilder, (t, u) -> {
            t.add(String.valueOf(String.valueOf(u.getKey())), String.valueOf(u.getValue()));
        }, JsonCollectors::merge);
    }

    static JsonObjectBuilder merge(JsonObjectBuilder left, JsonObjectBuilder right) {
        JsonObjectBuilder retVal = Json.createObjectBuilder();
        JsonObject leftObject = left.build();
        JsonObject rightObject = right.build();
        leftObject.keySet().stream().forEach((key) -> retVal.add(key, leftObject.get(key)));
        rightObject.keySet().stream().forEach((key) -> retVal.add(key, rightObject.get(key)));
        return retVal;
    }
}
