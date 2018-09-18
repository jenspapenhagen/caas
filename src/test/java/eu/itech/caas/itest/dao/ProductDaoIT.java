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
package eu.itech.caas.itest.dao;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.dao.ProductDao;
import eu.itech.caas.entity.Product;
import eu.itech.caas.itest.ArquillianProjectArchive;
import eu.itech.caas.itest.Utils;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.*;
import org.junit.After;

/**
 *
 * @author jay
 */
@RunWith(Arquillian.class)
public class ProductDaoIT extends ArquillianProjectArchive {

    @Inject
    @Caas
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @Inject
    private ProductDao dao;

    @After
    public void teardown() throws Exception {
        utx.begin();
        em.joinTransaction();
        Utils.clearH2Db(em);
        utx.commit();
    }

    @Test
    public void testFindByName() throws Exception {
        String testProductName = "test Product3";

        utx.begin();
        em.joinTransaction();

        Product p = new Product(testProductName, 3.50, 19);
        em.persist(p);
        utx.commit();

        Product productFormDb = dao.findByName(testProductName);

        assertThat(productFormDb.getProductName()).as("Testing the Product Name is not Blank").isNotBlank();
        assertThat(productFormDb.getProductName()).as("Testing the Product Name").isEqualToNormalizingWhitespace(testProductName);
    }

}
