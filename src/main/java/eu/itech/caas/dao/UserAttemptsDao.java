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
package eu.itech.caas.dao;

import eu.itech.caas.assist.Caas;
import eu.itech.caas.entity.UserAttempts;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Class that provides find and aggregation methods for {@link UserAttempts}.
 *
 * @author Jens Papenhagen
 */
@Stateless
public class UserAttemptsDao extends AbstractDao<UserAttempts> {

    @Inject
    @Caas
    private EntityManager em;

    public UserAttemptsDao() {
        super(UserAttempts.class);
    }

    public UserAttemptsDao(EntityManager entityManager) {
        this();
        this.em = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public UserAttempts findByIp(String ipAddress) {
        return em.createNamedQuery("UserAttempts.findByIpAddress", UserAttempts.class).setParameter(1, ipAddress).getSingleResult();
    }
    
}
