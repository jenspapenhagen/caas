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
package eu.itech.caas.dao;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.entity.Product;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Class that provides find and aggregation methods for {@link Product}.
 *
 * @author Jens Papenhagen
 */
@Stateless
public class ProductDao extends AbstractDao<Product> {

    @Inject
    @Caas
    private EntityManager em;

    public ProductDao() {
        super(Product.class);
    }

    public ProductDao(EntityManager entityManager) {
        this();
        this.em = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Product findByName(String productName) {
        return em.createNamedQuery("Product.findByName", Product.class).setParameter(1, productName).getSingleResult();
    }

}
