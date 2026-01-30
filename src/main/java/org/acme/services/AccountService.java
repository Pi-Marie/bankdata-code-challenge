package org.acme.services;

import org.acme.entities.Account;
import org.acme.dtos.internal.*;
import org.acme.dtos.external.*;
import org.acme.clients.ExchangeRateClient;
import jakarta.ws.rs.NotFoundException;
import org.acme.repositories.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository repository;

    @Inject
    @RestClient
    ExchangeRateClient exhangeClient;

    @ConfigProperty(name = "exchange.api.key")
    String exchangeApiKey;

    public List<Account> getAll() {
        return repository.listAll();
    }

    @Transactional
    public Account create(Account account) {
        repository.persist(account);
        return account;
    }

    @Transactional
    public Account depositMoney(Long id, Float amount) {
        Account account = repository.findById(id);
        if (account == null) {
            throw new NotFoundException();
        }

        account.setBalance(account.getBalance() + amount);
        return account;
    }

    @Transactional
    public void transferMoney(Long fromId, Long toId, Float amount) {
        Account fromAccount = repository.findById(fromId);
        Account toAccount = repository.findById(toId);

        if (fromAccount == null || toAccount == null) {
            throw new NotFoundException();
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount); // må en balance være negativ??
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    public float getBalance(Long id) {
        Account account = repository.findById(id);

        if (account == null) { throw new NotFoundException(); }

        return account.getBalance();
    }

    public USDRateResponse getUSDRate() {
        float amount = 100f;
        ExchangeRateApiResponse apiResponse = exhangeClient.getRate(exchangeApiKey, "DKK", "USD", amount);
        USDRateResponse response = new USDRateResponse();
        response.DKK = amount;
        response.USD = apiResponse.conversion_result;
        return response;
    }
}