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

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.StringTokenizer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthenticationService
 *
 * @author jens.papenhagen
 */
@Named
@ApplicationScoped
public class AuthenticationService implements Serializable{

    private static final Logger L = LoggerFactory.getLogger(AuthenticationService.class);

    @Inject
    private LDAPService ldpaService;

    /**
     * authenticate with a base64 string of user:password for base-auth
     *
     * @param authCredentials a base64 string of user:password for base-auth
     * @return true if success
     */
    public boolean authenticate(String authCredentials) {
        if (null == authCredentials) {
            return false;
        }
        if (authCredentials.isEmpty()) {
            return false;
        }
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException ex) {
            L.error("IOException on decoding: {}", ex.getMessage());
        }

        if (usernameAndPassword == null) {
            return false;
        }
        //split the user:password into user and password
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        //using a LDAP Server
//        if(!username.equalsIgnoreCase("prometheus")){
//            return false;
//        }
//        ldpaService.connect(username,password,"cn=users");
//        return ldpaService.authentication();
        //TODO WARNING HARDCODED PASSWORD
        return "prometheus".equals(username) && "prometheus".equals(password);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        //using a LDAP Server
//        ldpaService.connect(username,password,"cn=users");
//        return ldpaService.authentication();
        //TODO WARNING HARDCODED PASSWORD
        return "admin".equals(username) && "admin".equals(password);
    }
}
