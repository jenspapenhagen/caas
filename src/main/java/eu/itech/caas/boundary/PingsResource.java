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

import eu.itech.caas.control.JsonCollectors;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author adam-bien.com
 */
@Path("pings")
@Produces(MediaType.APPLICATION_JSON)
public class PingsResource {

    @GET
    @Path("/echo/{echo}")
    public JsonObject echo(@PathParam("echo") String param) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("output", param);
        
        return builder.build();
    }

    
    @GET
    @Path("/system-properties")
    public JsonObject systemProperties() {
        Properties properties = System.getProperties();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        return entries.stream().collect(JsonCollectors.toJsonBuilder()).build();
    }

    @GET
    @Path("/environment-variables")
    public JsonObject environmentVariables() {
        Map<String, String> environment = System.getenv();
        return environment.entrySet().stream().collect(JsonCollectors.toJsonBuilder()).build();
    }

    @GET
    @Path("/jndi/{namespace}")
    public JsonObject jndi(@PathParam("namespace") String namespace) throws NamingException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        InitialContext c = new InitialContext();
        NamingEnumeration<NameClassPair> list = c.list(namespace);
        while (list.hasMoreElements()) {
            NameClassPair nameClassPair = list.nextElement();
            String name = nameClassPair.getName();
            String type = nameClassPair.getClassName();
            builder.add(name, type);
        }
        return builder.build();
    }

}
