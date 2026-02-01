## bankdata-code-challenge

This is my repository for the bankdata code challenge.
The project is written in Java and uses Quarkus.

## Running the application in dev mode

You can run the application in dev mode by the following command:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.


## Hitting the endpoints

# Create a new account

- Substitude 'FIRST-NAME' with the first name
- Substitude 'LAST-NAME' with the last name

```shell script
curl -X POST http://localhost:8080/accounts \
     -H "Content-Type: application/json" \
     -d '{"firstName": "FIRST-NAME", "lastName": "LAST-NAME"}'
```

# Deposit money to an account

- Substitude 'ID' with the ID of the account
- Substitude 'AMOUNT' with the amount of DKK

```shell script
curl -X POST http://localhost:8080/accounts/deposit/ID \
     -H "Content-Type: application/json" \
     -d '{"amount": AMOUNT}'
```

# Transfer money between two accounts

- Substitude 'FROM-ID' with the ID of the account you want to withdraw money from
- Substitude 'TO-ID' with the ID of the account you want to deposit money to
- Substitude 'AMOUNT' with the amount of DKK to transfer

```shell script
curl -X POST http://localhost:8080/accounts/transaction/FROM-ID \
     -H "Content-Type: application/json" \
     -d '{"toAccountId": TO-ID, "amount": AMOUNT}'
```

# Get the balance of an account

- Substitude 'ID' with the ID of the account

```shell script
curl -X GET http://localhost:8080/accounts/balance/ID
```

# Get all accounts

```shell script
curl -X GET http://localhost:8080/accounts
```

# Get the exchange rate from DKK to USD

```shell script
curl -X GET http://localhost:8080/accounts/USD
```

# Get the exchange rates from DKK to USD at 1st of January 2005-2015, excluding 2012 and including today's rate

The implementation for the endpoint GET http://localhost:8080/accounts/history is provided in the project. 
However due to payment requirements, the implementation has not been tested and is therfore commented out.

