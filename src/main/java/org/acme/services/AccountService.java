package org.acme.services;

import org.acme.entities.Account;
import org.acme.dtos.*;
import org.acme.clients.exchangeapi.*;
import jakarta.ws.rs.*;
import org.acme.repositories.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * AccountService class defines the business logic behind the api endpoints.
 */
@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository repository;

    @Inject
    @RestClient
    ExchangeRateClient exchangeClient;

    @ConfigProperty(name = "exchange.api.key")
    String exchangeApiKey;

    /**
    * Get all accounts.
    *
    * @return  a list of all accounts
    */
    public List<Account> getAll() {
        return repository.listAll();
    }

    /**
    * Create a new account.
    *
    * @param account   the account to be created
    * @return          the created account
    */
    @Transactional
    public Account create(Account account) {
        if (account.getFirstName().isEmpty() || account.getLastName().isEmpty()) {
            throw new BadRequestException("Name must be provided");
        }
        
        repository.persist(account); // tjek hvad dette gør
        return account;
    }

    /**
    * Deposit a non-negative amount of DKK on the given account.
    *
    * @param id   the id of the account
    * @return     the account
    */
    @Transactional
    public Account depositMoney(Long id, Float amount) {
        Account account = repository.findById(id);

        if (account == null) { throw new NotFoundException(); }
        if (amount < 0) { throw new BadRequestException("Amount of money must be non-negative"); }

        account.setBalance(account.getBalance() + amount);
        return account;
    }

    /**
    * Transfer a non-negative amount of DKK from one account to another.
    *
    * @param fromId   the id of the account where money is withdrawn
    * @param toId     the id of the account where money is deposited
    * @param amount   the amount of money to be transfered
    */
    @Transactional
    public void transferMoney(Long fromId, Long toId, Float amount) {
        Account fromAccount = repository.findById(fromId);
        Account toAccount = repository.findById(toId);

        if (fromAccount == null || toAccount == null) {
            throw new NotFoundException();
        }

        if (amount < 0) { throw new BadRequestException("Amount of money must be non-negative"); }
        if (fromAccount.getBalance() - amount < 0) { throw new BadRequestException(String.format("%d does not have enough money", fromId)); }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    /**
    * Get balance of a given account.
    *
    * @param id  the id of the account
    * @return    the balance
    */
    public float getBalance(Long id) {
        Account account = repository.findById(id);

        if (account == null) { throw new NotFoundException(); }

        return account.getBalance();
    }

    /**
    * Get the exhange rate from DKK to USD.
    *
    * @return    the exchange rate
    */
    public USDRateOutputDto getUSDRate() {
        float amount = 100f;

        ExchangeRateApiDto apiResponse = exchangeClient.getRate(exchangeApiKey, "DKK", "USD", amount);
        USDRateOutputDto response = new USDRateOutputDto();

        return new USDRateOutputDto(amount, apiResponse.conversion_result);
    }

    
    /**
    * Get the exhange rates from DKK to USD at 1st of January 2005-2015, excluding 2012,
    * and including today's rate.
    *
    * @return    A Uni of the exchange rates
    *//*
    public Uni<HistoryOutputDto> getHistoricalData() {

        float amount = 100f;

        // List of years 2005 - 2015, excluding 2012
        List<Integer> years =
            IntStream.rangeClosed(2005, 2015)
                    .filter(y -> y != 2012)
                    .boxed()
                    .toList();

        // List of Unis, one for each year's USD rate called in parallel
        List<Uni<YearRateDto>> historyUnis =
            years.stream()
                .map(year ->
                    exchangeClient
                        .getHistoricalRateAsUni(exchangeApiKey, "DKK", year, 1, 1, amount)
                        .map(dto -> new YearRateDto(
                            year,
                            dto.conversion_results.get("USD") 
                        ))
                )
                .toList();

        // Combine all historical Unis to one
        Uni<List<YearRateDto>> historyUni =
            Uni.combine().all().unis(historyUnis)
                .with(list -> (List<YearRateDto>)(List<?>) list);

        // Uni for tody's rate
        Uni<Float> todayUni =
            exchangeClient
                .getRateAsUni(exchangeApiKey, "DKK", "USD", amount)
                .map(dto -> dto.conversion_result);

        // Combine and return historical rates and today's rate
        return Uni.combine().all().unis(todayUni, historyUni)
            .asTuple()
            .map(tuple ->
                new HistoryOutputDto(
                    tuple.getItem1(),
                    tuple.getItem2()
                )
            );
    }
    */

}