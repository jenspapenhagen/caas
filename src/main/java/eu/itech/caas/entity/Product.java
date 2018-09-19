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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * the Product pojo more infos:
 * https://en.wikipedia.org/wiki/Plain_old_Java_object
 *
 * @author Jens Papenhagen
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.productName = ?1")
})
public class Product implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String productName;

    private double price;

    private int taxRate;

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public Product(String productName, double price, int taxRate) {
        this.productName = productName;
        this.price = price;
        this.taxRate = taxRate;
    }

    public Product() {
    }

    
}
