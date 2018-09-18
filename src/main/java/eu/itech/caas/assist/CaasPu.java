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
package eu.itech.caas.assist;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jens Papenhagen
 */
public class CaasPu {

    public final static String PERSISTENCE_UNIT = "caas-pu";

    public final static String DATASOURCE_MANAGED = "caas";

    static {
        Map<String, String> o = new HashMap<>();

        o.put(PERSISTENCE_UNIT + ".hibernate.show_sql", "false");
        o.put(PERSISTENCE_UNIT + ".hibernate.hbm2ddl.auto", "create-drop");
        o.put(PERSISTENCE_UNIT + ".hibernate.jdbc.batch_size", "0");
        // Overwrite the property for Glassfish
        o.put(PERSISTENCE_UNIT + ".hibernate.transaction.jta.platform", "org.apache.openejb.hibernate.OpenEJBJtaPlatform");

        o.put(DATASOURCE_MANAGED, "new://Resource?type=DataSource");
        o.put(DATASOURCE_MANAGED + ".JdbcDriver", "org.hsqldb.jdbcDriver");
        o.put(DATASOURCE_MANAGED + ".JdbcUrl", "jdbc:hsqldb:mem:" + DATASOURCE_MANAGED);
        CMT_IN_MEMORY = o;

        o = new HashMap<>();

        o.put("hibernate.show_sql", "false");
        o.put("hibernate.jdbc.batch_size", "0");
        o.put("hibernate.hbm2ddl.auto", "create-drop");
        o.put("hibernate.jdbc.batch_size", "0");
        o.put("javax.persistence.jtaDataSource", "");
        o.put("javax.persistence.nonJtaDataSource", "");
        o.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
        o.put("javax.persistence.jdbc.user", "sa");
        o.put("javax.persistence.jdbc.password", "");
        o.put("javax.persistence.jdbc.url", "jdbc:hsqldb:mem:" + DATASOURCE_MANAGED);

        JPA_IN_MEMORY = o;

    }

    public final static Map<String, String> CMT_IN_MEMORY;

    public final static Map<String, String> JPA_IN_MEMORY;

    @Produces
    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    @Caas
    private EntityManager entityManager;
}
