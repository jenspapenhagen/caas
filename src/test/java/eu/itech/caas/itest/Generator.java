/*
 * Copyright 2018 jens papenhagen.
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

import eu.itech.caas.assist.Caas;
import eu.itech.caas.entity.Catalog;
import eu.itech.caas.entity.Product;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 * this is a Generator for needed entities
 *
 * @author jens.papenhagen
 */
public class Generator {

    @Inject
    @Caas
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    /**
     * this Gerarate amounts Product and persist them in the Database
     *
     * @param amount of req. Products
     * @throws Exception
     */
    public void generateProduct(int amount) throws Exception {
        utx.begin();
        em.joinTransaction();
        for (int i = 0; i < amount; i++) {
            Product p = new Product("testProduct " + amount, (3.50 * amount), 19);
            em.persist(p);
        }

        utx.commit();
    }

    /**
     * this Gerarate amounts Catalog and persist them in the Database WARNING
     * this Catalog has an empty List of Products. have be to added later
     *
     * @param amount of req. Catalog
     * @throws Exception
     */
    public void generateCatalog(int amount) throws Exception {
        utx.begin();
        em.joinTransaction();
        for (int i = 0; i < amount; i++) {
            Catalog c = new Catalog("testProduct " + amount);
            em.persist(c);
        }

        utx.commit();
    }
}
