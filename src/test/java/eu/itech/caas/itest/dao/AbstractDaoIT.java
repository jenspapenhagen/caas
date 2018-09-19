/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.itech.caas.itest.dao;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.dao.AbstractDao;
import eu.itech.caas.dao.ProductDao;
import eu.itech.caas.entity.Product;
import eu.itech.caas.itest.ArquillianProjectArchive;
import eu.itech.caas.itest.Utils;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
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
        genarateProduct(amount);

        List<Product> products = dao.findAll();

        assertThat(products).as("List of Products are not null").isNotNull();
        assertThat(products).as("List of Products are not empty").isNotEmpty();
        assertThat(products.size()).as("List of Products has 1 item").isEqualTo(amount);
    }

    @Test
    public void testFindAllWithStartAndAmount() throws Exception {
        int amount = 5;
        genarateProduct(amount);

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
        genarateProduct(amount);

        Product product = dao.findById(1l);
        assertThat(product).as("Product is not null").isNotNull();
        assertThat(product.getId()).as("Product has the same id").isEqualTo(1);

    }

    @Test
    public void testCount() throws Exception {
        int amount = 3;
        genarateProduct(amount);

        int count = dao.count();
        assertThat(count).as("Products count is not null").isNotNull();
        assertThat(count).as("Product has the same id").isEqualTo(amount);

    }

    private void genarateProduct(int amount) throws Exception {
        utx.begin();
        em.joinTransaction();
        for (int i = 0; i < amount; i++) {
            Product p = new Product("testProduct " + amount, (3.50 * amount), 19);
            em.persist(p);
        }

        utx.commit();
    }

}
