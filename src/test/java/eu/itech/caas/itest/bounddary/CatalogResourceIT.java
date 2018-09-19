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
package eu.itech.caas.itest.bounddary;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.boundary.CatalogResource;
import eu.itech.caas.itest.ArquillianProjectArchive;
import eu.itech.caas.itest.Generator;
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
 * an intetraiontest for the CatalogResource
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
    private Generator gen;

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
        gen.generateProduct(amount);
        JsonObject preTaxPrice = cr.getPreTaxPrice(1);

        assertThat(preTaxPrice).as("pre Tax Products are not null").isNotNull();
        assertThat(preTaxPrice).as("pre Tax Products are not empty").isNotEmpty();
        assertThat(preTaxPrice.getJsonString("ProductId")).as("the JSONObject with the fieldname ProductId is none").isEqualTo(1l);
    }
}
