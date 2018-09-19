/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.itech.caas.itest;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.entity.Product;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 *
 * @author jens.papenhagen
 */
public class Gernator {

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
    public void genarateProduct(int amount) throws Exception {
        utx.begin();
        em.joinTransaction();
        for (int i = 0; i < amount; i++) {
            Product p = new Product("testProduct " + amount, (3.50 * amount), 19);
            em.persist(p);
        }

        utx.commit();
    }
}
