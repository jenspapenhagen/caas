/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.itech.caas.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Catalog as a Service
 * Mircoservice for an ITECH Project
 * @author Jens Papenhagen
 */
@Path("caas")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

    /**
     * This Methode giveback the Post Tax Price (Nettopreis) of a given
     * ProductId value-added tax = "Mehrwert Steuer"
     *
     * @param param as the given ProductId
     * @return a JSON form the Product with the price
     */
    @GET
    @Path("/product/{product}")
    @Produces(MediaType.TEXT_PLAIN)
    public JsonObject getPreTaxPrice(@PathParam("product") int param) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        //TODO connect to DB and giveback the product with the given id
        return builder.build();
    }

    /**
     * This Methode giveback the Post Tax Price (Nettopreis) of a given
     * ProductId value-added tax = "Mehrwert Steuer"
     *
     * @param param as the given ProductId
     * @return a JSON form the Product with the require post tax added the price
     */
    @GET
    @Path("/product/{product}/posttax/")
    @Produces(MediaType.TEXT_PLAIN)
    public JsonObject getPostTaxPrice(@PathParam("product") int param) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        /**
         * TODO connect to DB and giveback the product with the given id and
         * adding 19% or 7% to it form the db, too
         */
        return builder.build();
    }

}
