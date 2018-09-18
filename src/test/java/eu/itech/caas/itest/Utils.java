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
package eu.itech.caas.itest;

import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jay
 */
public class Utils {

    /**
     * Clears the content of one or more H2 database, keeping the structure and
     * resetting all sequences. This method uses H2 native SQL commands. Tran
     *
     * @param ems the entitymanager of the database.
     */
    public static void clearH2Db(EntityManager... ems) {
        final Logger L = LoggerFactory.getLogger(Utils.class);
        if (ems == null) {
            L.info("No entitymanagers supplierd, ignoring clear");
            return;
        }

        for (EntityManager em : ems) {
            L.info("Clearing EntityManager {}", em);
            L.debug("Disabing foraign key constraints");
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            List<String> tables = em.createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='PUBLIC'").getResultList();
            tables.stream().map((table) -> {
                L.debug("Truncating Table {}", table);
                return table;
            }).forEachOrdered((table) -> {
                em.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            });

            List<String> sequences = em.createNativeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'").getResultList();
            sequences.stream().map((sequence) -> {
                L.debug("Resetting Sequence {}", sequence);
                return sequence;
            }).forEachOrdered((sequence) -> {
                em.createNativeQuery("ALTER SEQUENCE " + sequence + " RESTART WITH 1").executeUpdate();
            });
            L.debug("Enabling foraign key constraints");
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        }
    }
}
