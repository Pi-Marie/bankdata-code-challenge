package org.acme.clients;

import org.acme.dtos.external.ExchangeRateApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.quarkus.rest.client.reactive.Url;

@Path("/v6/{apiKey}")
@RegisterRestClient(configKey = "exchange.api.key")
public interface ExchangeRateClient {

    @GET
    @Path("/pair/{base}/{target}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    ExchangeRateApiResponse getRate(
            @PathParam("apiKey") String apiKey,
            @PathParam("base") String baseCurrency,
            @PathParam("target") String targetCurrency,
            @PathParam("amount") float amount
    );
}
