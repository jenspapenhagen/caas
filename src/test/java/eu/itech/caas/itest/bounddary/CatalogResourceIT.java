/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.itech.caas.itest.bounddary;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.boundary.CatalogResource;
import eu.itech.caas.itest.ArquillianProjectArchive;
import eu.itech.caas.itest.Gernator;
import eu.itech.caas.itest.Utils;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author jens.papenhagen
 */
@RunWith(Arquillian.class)
public class CatalogResourceIT extends ArquillianProjectArchive {

    @Inject
    @Caas
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @Inject
    private CatalogResource cr;

    @Inject
    private Gernator gen;

    @After
    public void teardown() throws Exception {
        utx.begin();
        em.joinTransaction();
        Utils.clearH2Db(em);
        utx.commit();
    }

    @Test
    @Ignore
    public void testGetPreTaxPrice() throws Exception {
        int amount = 5;
        gen.genarateProduct(amount);
        JsonObject preTaxPrice = cr.getPreTaxPrice(1);

        assertThat(preTaxPrice).as("pre Tax Products are not null").isNotNull();
        assertThat(preTaxPrice).as("pre Tax Products are not empty").isNotEmpty();
        assertThat(preTaxPrice.getJsonString("ProductId")).as("the JSONObject with the fieldname ProductId is none").isEqualTo(1l);
    }
}
