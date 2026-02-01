package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.entities.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AccountResourceTest {

    private Long accountId1;
    private Long accountId2;

    /*
    Set up two accounts account1 and account2 and deposit 200DKK to account1's account.
    */
    @BeforeEach
    void setup() {
        // Create two accounts
        accountId1 = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Andrea\",\"lastName\":\"Andersen\"}")
            .when().post("/accounts")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class)
                .getId();

        accountId2 = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Line\",\"lastName\":\"Lauersen\"}")
            .when().post("/accounts")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class)
                .getId();

        // Deposit 200DKK into account1
        given().contentType("application/json")
            .body("{\"amount\":200}")
            .when().post("/accounts/deposit/" + accountId1)
            .then().statusCode(200);
    }

    /*
    Test that a created account is created and has the correct values.
    */
    @Test
    void testCreateAccount() {
        String json = """
            {
                "firstName": "Jens",
                "lastName": "Jensen"
            }
            """;

        given()
            .contentType("application/json")
            .body(json)
            .when().post("/accounts")
            .then()
                .statusCode(200)
                .body("firstName", is("Jens"))
                .body("lastName", is("Jensen"))
                .body("balance", is(0f));
    }

    /*
    Test that creating an account with empty first name returns a 400 bad request response.
    */
    @Test
    void testCreateAccountWithEmptyName() {
        String json = """
            {
                "firstName": "",
                "lastName": "Jensen"
            }
            """;

        given()
            .contentType("application/json")
            .body(json)
            .when().post("/accounts")
            .then()
                .statusCode(400); 
    }

    /*
    Test that 100 DKK can be deposited to a created account.
    */
    @Test
    void testDeposit100DKK() {
        // Get balance before deposit
        Float balanceBefore = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        given()
            .contentType("application/json")
            .body("{\"amount\":100}")
            .when().post("/accounts/deposit/" + accountId2)
            .then()
                .statusCode(200)
                .body("balance", is(balanceBefore + 100));
    }

    /*
    Test that -100 DKK can not be deposited.
    */
    @Test
    void testDepositNegDKK() {
        given()
            .contentType("application/json")
            .body("{\"amount\":-100}")
            .when().post("/accounts/deposit/" + accountId1)
            .then().statusCode(400);
    }

    /*
    Test that 0 is retrieved when checking the balnce of a newly created account
    */
    @Test
    void testGetBalance() {
        // Create account
        Account acc = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Line\",\"lastName\":\"Lauersen\"}")
        .when().post("/accounts")
        .then().statusCode(200)
        .extract().as(Account.class);

        String balance = given()
        .when().get("/accounts/balance/" + acc.getId())
        .then().statusCode(200)
        .extract().asString();

        assertEquals("0.0", balance);
    }

   /*
    Test that 75 DKK can be transfered from an account with 200 DKK to an account with 0 DKK.
   */
    @Test
    void testTransfer75DKK() {
        // Get balances before transaction
        Float balance1Before = given()
            .when().get("/accounts/balance/" + accountId1)
            .then().statusCode(200)
                .extract().as(Float.class);
        Float balance2Before = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        // Transfer 75 from account1 to account2
        String transaction = String.format("""
            {
                "toAccountId": %d,
                "amount": 75
            }
            """, accountId2);

        given().contentType("application/json")
            .body(transaction)
            .when().post("/accounts/transaction/" + accountId1)
            .then().statusCode(204); 

        // Get balances after transaction
        Float balance1After = given()
            .when().get("/accounts/balance/" + accountId1)
            .then().statusCode(200)
                .extract().as(Float.class);
        Float balance2After = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        assertEquals(balance1Before - 75, balance1After);
        assertEquals(balance2Before + 75, balance2After);
    }

    /*
    Test that -75 DKK can not be transfered from an account with 200 DKK to an account with 0 DKK.
   */
    @Test
    void testTransferNeg75DKK() {
        // Get balances before
        Float balance1Before = given()
            .when().get("/accounts/balance/" + accountId1)
            .then().statusCode(200)
                .extract().as(Float.class);
        Float balance2Before = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        // Transfer -75 from account1 to account2
        String transaction = String.format("""
            {
                "toAccountId": %d,
                "amount": -75
            }
            """, accountId2);

        given().contentType("application/json")
            .body(transaction)
            .when().post("/accounts/transaction/" + accountId1)
            .then().statusCode(400); 

        // Get balances after
        Float balance1After = given()
            .when().get("/accounts/balance/" + accountId1)
            .then().statusCode(200)
                .extract().as(Float.class);
        Float balance2After = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        assertEquals(balance1Before, balance1After);
        assertEquals(balance2Before, balance2After);
    }

    /*
    Test that 300 DKK can not be transfered from an account with 200 DKK to an account with 0 DKK.
   */
    @Test
    void testTransfer300DKKFromAccountWith200DKK() {
        // Get balances before
        Float balance1Before = given()
            .when().get("/accounts/balance/" + accountId1)
            .then().statusCode(200)
                .extract().as(Float.class);
        Float balance2Before = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        // Transfer 300 from acc1 to acc2
        String transaction = String.format("""
            {
                "toAccountId": %d,
                "amount": 300
            }
            """, accountId2);

        given().contentType("application/json")
            .body(transaction)
            .when().post("/accounts/transaction/" + accountId1)
            .then().statusCode(400); 

        // Get balances after
        Float balance1After = given()
            .when().get("/accounts/balance/" + accountId1)
            .then().statusCode(200)
                .extract().as(Float.class);
        Float balance2After = given()
            .when().get("/accounts/balance/" + accountId2)
            .then().statusCode(200)
                .extract().as(Float.class);

        assertEquals(balance1Before, balance1After);
        assertEquals(balance2Before, balance2After);
    }

    /*
    Test that retrieving the USD exchange rate returns an 200 OK response.
    */
    @Test
    void testUSDExchangeRate() {
    given().contentType("application/json")
        .when().get("/accounts/USD")
        .then()
            .statusCode(200)
            .body("DKK", is(100f)); 
    }
    
}
