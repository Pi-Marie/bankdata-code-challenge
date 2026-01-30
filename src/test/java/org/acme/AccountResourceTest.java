package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.entities.Account;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AccountResourceTest {

    /*
    @Test    
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)    
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    public void testGreetingEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
          .pathParam("name", uuid)
          .when().get("/hello/greeting/{name}")
          .then()
            .statusCode(200)
            .body(is("hello " + uuid));
    }*/

    @Test
    void testCreateAccount() {
        String json = """
            {
                "firstName": "Pi",
                "lastName": "Bohlbro"
            }
            """;

        Account created = given()
            .contentType("application/json")
            .body(json)
            .when().post("/accounts")
            .then()
                .statusCode(200)
                .body("firstName", is("Pi"))
                .body("lastName", is("Bohlbro"))
                .body("balance", is(0f))
                    .extract().as(Account.class); // skal jeg gøre noget ved dette??

        assertEquals(false, created.getId() == null);
    }

    /*
    @Test
    void testGetAllAccounts() {
        given()
            .when().get("/accounts")
            .then()
                .statusCode(200)
                .body("size()", is(1)); // adjust depending on DB state
    }

    */

    @Test
    void testDeposit100DKK() {
        // Create account
        Account acc = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Pi\",\"lastName\":\"Bohlbro\"}")
            .when().post("/accounts")
            .then().statusCode(200)
                .extract().as(Account.class);

        // Deposit 100DKK
        Account updatedAcc = given()
            .contentType("application/json")
            .body("{\"amount\":100}")
            .when().post("/accounts/" + acc.getId() + "/deposit")
            .then().statusCode(200)
                .extract().as(Account.class);

        assertEquals(100f, updatedAcc.getBalance());
    }

    /*
    @Test
    void testGetBalance() {
        // Create account
        Account acc = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Gina\",\"lastName\":\"Lopez\"}")
        .when().post("/accounts")
        .then().statusCode(200)
        .extract().as(Account.class);

        String balance = given()
        .when().get("/accounts/" + acc.getId() + "/balance")
        .then().statusCode(200)
        .extract().asString();

        assertEquals("0.0", balance);
    } */

    @Test
    void testTransfer50DKK() {
        // Create two accounts
        Account acc1 = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Tobias\",\"lastName\":\"Rasmussen\"}")
            .when().post("/accounts")
            .then().statusCode(200)
                .extract().as(Account.class);

        Account acc2 = given()
            .contentType("application/json")
            .body("{\"firstName\":\"Michael\",\"lastName\":\"Munk Lerskov\"}")
            .when().post("/accounts")
            .then().statusCode(200)
                .extract().as(Account.class);

        // Deposit 100DKK into acc1
        given().contentType("application/json")
            .body("{\"amount\":200}")
            .when().post("/accounts/" + acc1.getId() + "/deposit")
            .then().statusCode(200);

        // Transfer 50 from acc1 to acc2
        String transaction = String.format("""
            {
                "toAccountId": %d,
                "amount": 50
            }
            """, acc2.getId());

        given().contentType("application/json")
            .body(transaction)
            .when().post("/accounts/" + acc1.getId() + "/transaction")
            .then().statusCode(204); 

        // Check balances
        String balance1 = given()
            .when().get("/accounts/" + acc1.getId() + "/balance")
            .then().statusCode(200)
                .extract().asString();
        String balance2 = given()
            .when().get("/accounts/" + acc2.getId() + "/balance")
            .then().statusCode(200)
                .extract().asString();

        assertEquals("150.0", balance1);
        assertEquals("50.0", balance2);
    }
        
}
