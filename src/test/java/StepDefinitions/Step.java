package StepDefinitions;

import com.google.gson.JsonObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.RestRequests;
import net.serenitybdd.rest.SerenityRest;
import org.json.simple.JSONObject;
import org.junit.Test;

public class Step {
    @Test
    @Given("GET demo request 1")
    public void GET_request_1() {
        Response response = RestRequests
                .given()
                .baseUri("https://reqres.in").basePath("/api/user?page=2")
                .when()
                    .get()
                .then()
                    .extract().response();

        Serenity.setSessionVariable("response").to(response);
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        jsonPath.prettyPrint();

    }

    @When("POST demo request 1")
    public void POST_request_1() {
//        RestAssured.baseURI = "https://reqres.in/api/users";

//        JsonObject requestBody = new JsonObject();
//        requestBody.addProperty("name","Nittin");
//        System.out.println(requestBody);
//        requestBody.addProperty("job","Test enginner");
//        System.out.println(requestBody);

        JSONObject request = new JSONObject();
        request.put("name","Nittin");
        request.put("job","Test enginner");

        Response response = RestRequests
                .given()
                    .baseUri("https://reqres.in").basePath("/api/users")
                    .contentType(ContentType.JSON)
                    .request()
                .when()
                    .body(request)
                    .post()
                .then()
                    .statusCode(201)
                    .extract().response();

        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        jsonPath.prettyPrint();
    }
    @Then("POST demo request 2")
    public void POST_request_2() {
//        JsonObject requestBody = new JsonObject();
//        requestBody.addProperty("email","Nitin@gmail.com");

        JSONObject request = new JSONObject();
        request.put("email","Nitin@gmail.com");

        Response response = RestRequests
                .given()
                    .baseUri("https://reqres.in").basePath("/api/login")
                    .contentType(ContentType.JSON)
                .when()
                    .body(request)
                    .post()
                .then()
                    .statusCode(400)
                    .extract().response()
                ;

        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        jsonPath.prettyPrint();
    }

}
