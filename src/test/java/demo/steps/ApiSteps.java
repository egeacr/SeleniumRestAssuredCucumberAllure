package demo.steps;

import com.github.javafaker.Faker;
import demo.helper.ConfigurationReader;
import demo.helper.factory.LoggerFactory;
import demo.helper.utils.GlobalData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    protected RequestSpecification requestSpecification;
    protected Logger logger = LoggerFactory.getLogger(ApiSteps.class);
    protected String baseURI = ConfigurationReader.get("apiBaseURI");
    private Faker faker  = new Faker();
    private GlobalData globalData = new GlobalData();



    public Response createBrand(String endpoint){
        JSONObject requestBody = new JSONObject();
        String name = faker.name().firstName();
        int number = faker.number().numberBetween(10,100);
        requestBody.put("name", name);
        requestBody.put("slug", name+number);

        Response response = RestAssured
                .given()
                .accept("application/json")
                .contentType("application/json")
                .and()
                .body(requestBody)
        .post(baseURI+endpoint);
        globalData.set("createdBrandID", response.jsonPath().get("id"));
        return response;
    }

    public Response getBrand( String brandID){
        Response response = RestAssured
                .given()
                .accept("application/json")
                .when()
                .get(baseURI+ "/" + brandID);
        return response;
    }

    public Response updateBrand(String brandID){
        JSONObject requestBody = new JSONObject();
        String name = faker.name().firstName();
        int number = faker.number().numberBetween(10,100);
        requestBody.put("name", name);
        requestBody.put("slug", name+number);

        Response response = RestAssured
                .given()
                .accept("application/json")
                .contentType("application/json")
                .and()
                .body(requestBody)
                .put(baseURI+ "/" + brandID);
        return response;
    }

    public Response delete(String endpoint){
        Response response = this.requestSpecification.when().get(baseURI+endpoint);
        return response;
    }


    @Given("User Create a new brand")
    public void userCrateANewBrand(){
        Response response = createBrand("");
        String id = response.jsonPath().get("id");
        logger.info("Created Brand ID:" + id);
        globalData.set("RequestStatusCode", response.statusCode());
        globalData.set("responseMessage", response.body());
    }


    @And("User search created brand with id")
    public void userSearchCreatedBrandWithId() {
        userCrateANewBrand();
        String createdBrandID = globalData.getString("createdBrandID");
        logger.info("Search brand with this id = " + createdBrandID );
        Response response = getBrand(createdBrandID);
        globalData.set("RequestStatusCode", response.statusCode());
        globalData.set("responseMessage", response.body());
    }

    @And("user update created brand")
    public void userUpdateCreatedBrand() {
        userCrateANewBrand();
        String createdBrandID = globalData.getString("createdBrandID");
        logger.info("Update brand with this id = " + createdBrandID);
        Response response = updateBrand(createdBrandID);
        globalData.set("RequestStatusCode", response.statusCode());
        globalData.set("responseMessage", response.body());

    }

    @Then("user verifies that request is completed successfuly with {int}")
    public void userVerifiesThatRequestIsCompletedSuccessfulyWith(int expectedStatusCode) {
        int statusCode = (globalData.getInt("RequestStatusCode"));
        try {
            Assert.assertEquals(statusCode, expectedStatusCode,
                    "Request is failed. Status Code: " + statusCode + " and message is:" + globalData.getString("responseMessage"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
