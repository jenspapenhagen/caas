/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.itech.caas.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * a Product
 *
 * @author jay
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
