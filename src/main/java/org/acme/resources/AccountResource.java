package org.acme.resources;

import org.acme.dtos.*;
import org.acme.entities.Account;
import org.acme.services.AccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import jakarta.ws.rs.core.Response;
import io.smallrye.mutiny.Uni;


@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    AccountService service;
   
    @GET
    public List<Account> getAll() {
        return service.getAll();
    }

    @POST
    public Account create(AccountInputDto dto) {
        
        if (dto.getFirstName() == null) { throw new BadRequestException("First name must be provided"); }
        if (dto.getLastName() == null) { throw new BadRequestException("Last name must be provided"); }

        Account account = new Account(dto.getFirstName(), dto.getLastName());

        return service.create(account);
    }

    @POST
    @Path("/deposit/{id}")
    public Account depositMoney(@PathParam("id") Long id, DepositInputDto dto) {

        if (dto.getAmount() == null) { throw new BadRequestException("Amount of money must be provided"); }

        return service.depositMoney(id, dto.getAmount());
    }

    @POST
    @Path("/transaction/{fromId}")
    public Response transferMoney(@PathParam("fromId") Long fromId, TransactionInputDto dto) {

        if (dto.getToAccountId() == null) { throw new BadRequestException("Receiving account must be provided"); }
        if (dto.getAmount() == null) { throw new BadRequestException("Amount of money must be provided"); }

        service.transferMoney(fromId, dto.getToAccountId(), dto.getAmount());

        return Response.noContent().build();
    }

    @GET
    @Path("/balance/{id}")
    public float getBalance(@PathParam("id") Long id) {
        return service.getBalance(id);
    }

    @GET
    @Path("/USD")
    public USDRateOutputDto getExchangeRate() {
        return service.getUSDRate();
    }

    /*
    @GET
    @Path("/history")
    public Uni<HistoryOutputDto> getHistoricalData() {
        return service.getHistoricalData();
    }
    */

        

}
