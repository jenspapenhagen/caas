/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * the Catalog
 *
 * @author jay
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
}
