/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.itech.caas.boundary;

import eu.itech.caas.dao.ProductDao;
import eu.itech.caas.entity.Product;
import java.text.NumberFormat;
import java.util.Currency;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Catalog as a Service Mircoservice for an ITECH Project
 *
 * @author Jens Papenhagen
 */
@Path("caas")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

    @Inject
    private ProductDao dao;

    private NumberFormat currenyFormatEU = NumberFormat.getCurrencyInstance(java.util.Locale.GERMANY);

    /**
     * This Methode giveback the Post Tax Price (Nettopreis) of a given
     * ProductId value-added tax = "Mehrwert Steuer"
     *
     * @param param as the given ProductId
     * @return a Valid JSON (RFC 4627) form the Product with the price
     */
    @GET
    @Path("/product/{product}")
    @Produces(MediaType.TEXT_PLAIN)
    public JsonObject getPreTaxPrice(@PathParam("product") long param) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        Product product = dao.findById(param);
        //if not found give back a empty json
        if (product == null) {
            builder.add("Product", "none");
            return builder.build();
        }

        builder.add("ProductId", product.getId())
                .add("ProductName", product.getProductName())
                .add("Tax", product.getTaxRate())
                .add("Price", currenyFormatEU.format(product.getPrice()));

        return builder.build();
    }

    /**
     * This Methode giveback the Post Tax Price (Nettopreis) of a given
     * ProductId value-added tax = "Mehrwert Steuer"
     *
     * @param param as the given ProductId
     * @return a Valid JSON (RFC 4627) form the Product with the require post
     * tax added the price
     */
    @GET
    @Path("/product/{product}/posttax/")
    @Produces(MediaType.TEXT_PLAIN)
    public JsonObject getPostTaxPrice(@PathParam("product") long param) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Product product = dao.findById(param);
        //if not found give back a empty json
        if (product == null) {
            builder.add("Product", "none");
            return builder.build();
        }

        double tax = (100 + product.getTaxRate());

        builder.add("ProductId", product.getId())
                .add("ProductName", product.getProductName())
                .add("Tax", product.getTaxRate())
                .add("Price", currenyFormatEU.format((product.getPrice() * tax) / 100))
                .add("Price without tax", currenyFormatEU.format(product.getPrice()));

        return builder.build();
    }

}
