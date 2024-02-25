package StepDefinitions;

import Function.ExcelDataReader;
import com.google.gson.JsonObject;
import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.RestRequests;
import org.json.JSONObject;
import org.junit.Assert;

import static Variable.Variables.excelFilePath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class Step {
    @Test
    @Given("^GET demo request 1$")
    public void GET_request_1() {
        Response response = RestRequests
                .given()
                    .baseUri("https://reqres.in").basePath("/api/user?page=2")
                .when()
                    .get()
                .then()
                    .extract().response();
        Serenity.throwExceptionsImmediately();
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        jsonPath.prettyPrint();

    }

    @Test
    @When("POST demo request 1")
    public void POST_request_1() {
//        RestAssured.baseURI = "https://reqres.in/api/users";

//        JsonObject requestBody = new JsonObject();
//        requestBody.addProperty("name","Nittin");
//        System.out.println(requestBody);
//        requestBody.addProperty("job","Test enginner");
//        System.out.println(requestBody);

        Map<String, String> request = new HashMap<>();
        request.put("name","Nittin");
        request.put("job","Test enginner");
//        JSONObject request = new JSONObject(map);

        System.out.println(request);

        JSONObject request1 = new JSONObject();
        request1.put("name","Nittin");
        request1.put("job","Test enginner");

        System.out.println(request1);

        Response response = RestRequests
                .given()
                    .baseUri("https://reqres.in").basePath("/api/users")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .request()
                .when()
                    .body(request1.toString())
//                    .body(request)
                    .post()
                .then()
                    .statusCode(201)
                    .extract().response();
        Serenity.throwExceptionsImmediately();
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        jsonPath.prettyPrint();
        try {
            Assert.assertEquals(jsonPath.get("name"),"Nittin");
            assertThat(jsonPath.get("job"),is("Test enginner"));

            Serenity.done();
            System.out.println("__________________PASS__________________");
        }

        catch (Exception | AssertionError e){
            Serenity.throwExceptionsImmediately();
            System.out.println("__________________FALSE__________________\n\n"+e);
        }
    }
    @Test
    @Then("POST demo request 2")
    public void POST_request_2() {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email","Nitin@gmail.com");
        System.out.println(requestBody);

        JSONObject request = new JSONObject();
        request.put("email","Nitin@gmail.com");
        System.out.println(request);


        Response response = RestRequests
                .given()
                    .baseUri("https://reqres.in").basePath("/api/login")
                    .contentType(ContentType.JSON)
                .when()
//                    .body(request.toString())
                    .body(requestBody.toString())
                    .post()
                .then()
                    .statusCode(400)
                    .extract().response()
                ;
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        jsonPath.prettyPrint();
        try {
            Assert.assertEquals(jsonPath.get("error"),"Missing password");
            assertThat(jsonPath.get("error"),is("Mising password"));
            System.out.println("__________________Pass__________________");
        }

        catch (Exception | AssertionError e){
            System.out.println("__________________False__________________\n\n"+e);
        }
    }

    @Test
    @When("Test SOAP API GET")
    public void GET_RESTAssuredSOAPAPI() throws Exception {
        Response response = RestRequests
                .given()
                    .baseUri("http://restapi.adequateshop.com")
                    .basePath("/api/Traveler")
                    .contentType("application/xml")

                .when()
                    .get()
                .then()
                    .body("id",notNullValue())
                    .extract().response();

        String responseBody = response.getBody().asString();
        XmlPath jsXMLpath = new XmlPath(responseBody);
        jsXMLpath.prettyPrint();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(jsXMLpath.prettify())));
        Element rootElement = document.getDocumentElement();
        System.out.println("\nName is: " + rootElement.getElementsByTagName("name").item(1).getTextContent());
        System.out.println("\nID is: " + rootElement.getElementsByTagName("id").item(1).getTextContent());
        System.out.println("\nEmail is: " + rootElement.getElementsByTagName("email").item(1).getTextContent());
        System.out.println("\nAdderes is: " + rootElement.getElementsByTagName("adderes").item(1).getTextContent());

        try {
            Assert.assertEquals(rootElement.getElementsByTagName("name").item(1).getTextContent(),"AS");
            assertThat(rootElement.getElementsByTagName("email").item(1).getTextContent(),is("qweqw@mail.ru"));
            System.out.println("__________________PASS__________________");
        }

        catch (Exception | AssertionError e){
            System.out.println("__________________FALSE__________________\n\n"+e);
        }
    }

}


