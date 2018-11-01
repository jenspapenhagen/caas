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
package eu.itech.caas.service;

import eu.itech.caas.dao.UserAttemptsDao;
import eu.itech.caas.entity.UserAttempts;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * LoginAttempt Managet the Login Attempts
 *
 * @author Jens Papenhagen
 */
@Named
@ApplicationScoped
public class LoginAttempt implements Serializable {

    //Map<IPAddress,UserAttempts>
    private Map<String, UserAttempts> attempts;

    @Inject
    private UserAttemptsDao userAtemptsDao;

    /**
     * Adding this IP-Address to the List of and
     *
     * @param ipAddress
     * @param ua UserAttempts
     */
    public void add(String ipAddress, UserAttempts ua) {
        if (attempts.containsKey(ipAddress)) {
            //found a attempt with this IP Address and countdown
            attempts.get(ipAddress).minusOne();
        } else {
            attempts.put(ipAddress, ua);
        }
    }

    /**
     * return the Attempts per IP-Address
     *
     * @param ipAddress
     * @return the count of attempts or -1 for not having this IP Address
     */
    public int getAttempts(String ipAddress) {
        UserAttempts ua = userAtemptsDao.findByIp(ipAddress);
        //find a UserAttemptsin the database by this IP
        if (ua == null) {
            return -1;
        }
        if (ua.getAttempts() == 0) {
            //last Modified time plus 20 min have to be before now() 
            //than reset the attempt countdown back to 3
            LocalDateTime plus20MinutesAgo = ua.getLastModified().plusMinutes(20);
            if (plus20MinutesAgo.isBefore(LocalDateTime.now())) {
                ua.setAttempts(3);
                return 3; //return 3 for resetting the attempt countdown
            }
        } else {
            return ua.getAttempts();
        }

        return -1;
    }

}
