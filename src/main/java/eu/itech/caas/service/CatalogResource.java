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
package eu.itech.caas.service;

import eu.itech.caas.dao.ProductDao;
import eu.itech.caas.entity.Product;
import java.text.NumberFormat;
import java.util.Optional;
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
 * Catalog as a Service Mircoservice for an ITECH Project a small microservice
 * (https://en.wikipedia.org/wiki/Microservices) as an REST API to calc. a price
 * of a selected Product very fast.
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
         Optional<Product> product = getProduct(param);

        //not found give back a empty json
        if (!product.isPresent()) {
            builder.add("ProductId", "none");
            return builder.build();
        }

        Product p = product.get();
        builder.add("ProductId", p.getId())
                .add("ProductName", p.getProductName())
                .add("Tax", p.getTaxRate())
                .add("Price without tax", currenyFormatEU.format(p.getPrice()));

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
        Optional<Product> product = getProduct(param);

        //not found give back a empty json
        if (!product.isPresent()) {
            builder.add("ProductId", "none");
            return builder.build();
        }

        Product p = product.get();
        
        double tax = (100 + p.getTaxRate());

        builder.add("ProductId", p.getId())
                .add("ProductName", p.getProductName())
                .add("Tax", p.getTaxRate())
                .add("Price", currenyFormatEU.format((p.getPrice() * tax) / 100))
                .add("Price without tax", currenyFormatEU.format(p.getPrice()));

        return builder.build();
    }

    /**
     * This Methode use an Optional of Nullable
     * for cleaner connection handling with the DB 
     * 
     * some infos of Handling Optionals
     * https://www.baeldung.com/java-optional
     *
     * @param param as the given ProductId
     * @return an Optional of Product
     */
    private Optional<Product> getProduct(long param) {
        Product product = dao.findById(param);
        Optional<Product> opt = Optional.ofNullable(product);

        return opt;
    }

}
