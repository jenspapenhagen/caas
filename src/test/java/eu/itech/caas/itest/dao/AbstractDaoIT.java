/*
 * Copyright 2018 Jens Papenhagen.
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
import eu.itech.caas.dao.AbstractDao;
import eu.itech.caas.entity.Product;
import eu.itech.caas.itest.ArquillianProjectArchive;

import eu.itech.caas.itest.Generator;
import eu.itech.caas.itest.Utils;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * an intetraiontest for the Abstract Dao in this testcase for Product.
 *
 * @author jens.papenhagen
 */
@RunWith(Arquillian.class)
public class AbstractDaoIT extends ArquillianProjectArchive {

    @Inject
    @Caas
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @Inject
    private AbstractDao<Product> dao;

    @Inject
    private Generator gen;

    @After
    public void teardown() throws Exception {
        utx.begin();
        em.joinTransaction();
        Utils.clearH2Db(em);
        utx.commit();
    }

    @Test
    public void testFindAll() throws Exception {
        int amount = 1;
        gen.generateProduct(amount);

        List<Product> products = dao.findAll();

        assertThat(products).as("List of Products are not null").isNotNull();
        assertThat(products).as("List of Products are not empty").isNotEmpty();
        assertThat(products.size()).as("List of Products has 1 item").isEqualTo(amount);
    }

    @Test
    public void testFindAllWithStartAndAmount() throws Exception {
        int amount = 5;
        gen.generateProduct(amount);

        int start = 2;
        int returnAmout = amount - start;
        List<Product> products = dao.findAll(start, returnAmout);

        assertThat(products).as("List of Products are not null").isNotNull();
        assertThat(products).as("List of Products are not empty").isNotEmpty();
        assertThat(products.size()).as("List of Products has exact the return amount item").isEqualTo(returnAmout);
    }

    @Test
    public void testFindById() throws Exception {
        int amount = 1;
        gen.generateProduct(amount);

        Product product = dao.findById(1l);
        assertThat(product).as("Product is not null").isNotNull();
        assertThat(product.getId()).as("Product has the same id").isEqualTo(1);

    }

    @Test
    public void testCount() throws Exception {
        int amount = 3;
        gen.generateProduct(amount);

        int count = dao.count();
        assertThat(count).as("Products count is not null").isNotNull();
        assertThat(count).as("Product has the same id").isEqualTo(amount);

    }

}
