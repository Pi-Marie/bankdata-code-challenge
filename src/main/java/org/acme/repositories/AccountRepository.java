package org.acme.repositories;

import org.acme.entities.Account;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {}
