package org.acme.clients.exchangeapi;

import org.acme.clients.exchangeapi.ExchangeRateApiDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.quarkus.rest.client.reactive.Url;
import io.smallrye.mutiny.Uni;

@Path("/v6/{apiKey}")
@RegisterRestClient(configKey = "exchange.api.key")
public interface ExchangeRateClient {

    @GET
    @Path("/pair/{base}/{target}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    ExchangeRateApiDto getRate(
            @PathParam("apiKey") String apiKey,
            @PathParam("base") String baseCurrency,
            @PathParam("target") String targetCurrency,
            @PathParam("amount") float amount
    );

    /*
    @GET
    @Path("/pair/{base}/{target}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<ExchangeRateApiDto> getRateAsUni(
            @PathParam("apiKey") String apiKey,
            @PathParam("base") String baseCurrency,
            @PathParam("target") String targetCurrency,
            @PathParam("amount") float amount
    );

    @GET
    @Path("/history/{base}/{year}/{month}/{day}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<ExchangeHistoricalRateApiDto> getHistoricalRateAsUni(
            @PathParam("apiKey") String apiKey,
            @PathParam("base") String baseCurrency,
            @PathParam("year") int year,
            @PathParam("month") int month,
            @PathParam("day") int day,
            @PathParam("amount") float amount
    );
    */
}
