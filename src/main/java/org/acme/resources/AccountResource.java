package org.acme.resources;

import org.acme.dtos.internal.*;
import org.acme.entities.Account;
import org.acme.services.AccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import jakarta.ws.rs.core.Response;


@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    AccountService service;

    /*
    @POST
    public Account create(AccountDto dto) {
        Account account = new Account(dto.getFirstName(), dto.getLastName());
        account = service.create(account);
        return new AccountOutputDto(account.getBalance(), account.getFirstName(), account.getLastName());
    }
    */
   
    @GET
    public List<Account> getAll() {
        return service.getAll();
    }

    @POST
    public Account create(Account account) {
        return service.create(account);
    }

    @POST
    @Path("/{id}/deposit")
    public Account depositMoney(@PathParam("id") Long id, DepositMoneyDto dto) {

        if (dto.getAmount() == null) { throw new BadRequestException("Amount of money must be provided"); }

        return service.depositMoney(id, dto.getAmount());
    }

    @POST
    @Path("/{fromId}/transaction")
    public Response transferMoney(@PathParam("fromId") Long fromId, TransferMoneyDto dto) {

        if (dto.getToAccountId() == null) { throw new BadRequestException("Receiving account must be provided"); }
        if (dto.getAmount() == null || dto.getAmount() < 0) { throw new BadRequestException("Amount of money provided and positive"); }

        service.transferMoney(fromId, dto.getToAccountId(), dto.getAmount());

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/balance")
    public float getBalance(@PathParam("id") Long id) {
        return service.getBalance(id);
    }

    @GET
    @Path("/USD")
    public USDRateResponse getExchangeRate() {
        return service.getUSDRate();
    }

    @GET
    @Path("/historical")
    public HistoricalDataResponse getHistoricalData() {
        return service.getHistoricalData();
    }

        

}
