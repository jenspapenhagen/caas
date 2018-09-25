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
package eu.itech.caas.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * the Catalog pojo more infos:
 * https://en.wikipedia.org/wiki/Plain_old_Java_object
 *
 * @author Jens Papenhagen
 */
@Entity
public class Catalog implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String catalogName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void fetchEager() {
        productList.size();
    }

    public Catalog(String catalogName) {
        this.catalogName = catalogName;
    }

    @Override
    public String toString() {
        return "Catalog{" + "id=" + id + ", catalogName=" + catalogName + ", productList=" + productList + '}';
    }
    
    

}
