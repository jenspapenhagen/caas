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
package eu.itech.caas.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * a basic class for handling the user attems on login
 *
 * @author Jens Papenhagen
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "UserAttempts.findByIpAddress", query = "SELECT u FROM UserAttempts u WHERE u.ipAddress = ?1")
})
public class UserAttempts implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String ipAddress;

    private int attempts;

    private LocalDateTime lastModified;

    /**
     * set the attempts minus one and set the lastModified LocalDateTime to
     * now()
     */
    public void minusOne() {
        attempts--;
        lastModified = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getAttempts() {
        return attempts;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public UserAttempts() {
    }

    public UserAttempts(String username, int attempts, LocalDateTime lastModified) {
        this.ipAddress = username;
        this.attempts = attempts;
        this.lastModified = lastModified;
    }

}
