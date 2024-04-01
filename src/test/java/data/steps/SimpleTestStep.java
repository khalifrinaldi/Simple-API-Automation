package data.steps;

import data.utilities.Helper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.InputStream;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SimpleTestStep {

    private Response response;
    private String PROPERTYFILE = "src/test/resources/constants.properties";
    private String URL = Helper.getPropertyValue(PROPERTYFILE, "url");

    @Given("User send GET to the API")
    public void userSendGETToTheAPI() {
        response = given().get(URL);
    }

    @Then("the status code is {int}")
    public void theStatusCodeIs(int code) {
        response.then().assertThat().statusCode(code);
    }

    @Then("response content type is JSON")
    public void responseContentTypeIsJSON() {
        response.then().assertThat().contentType(ContentType.JSON);
    }

    @Given("User send POST to the API")
    public void userSendPOSTToTheAPI(String body){
        response = given().header("Content-Type", "application/json").body(body).when().post(URL);
        response.asString();
        response.prettyPrint();
    }

    @Then("validate schema json response {string}")
    public void validateSchemaJsonResponse(String json){
        InputStream placeHolderJsonSchema = getClass().getClassLoader().getResourceAsStream("jsonschema/" + json + ".json");
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(placeHolderJsonSchema));
    }

    @Then("the body response content should be matched string")
    public void theBodyResponseContentShouldBeMatchedString(DataTable table){
        List<List<String>> data = table.asLists();
        for (int i = 1; i < data.size(); i++) {
            response.then().assertThat().body(data.get(i).get(0), equalTo((data.get(i).get(1))));
        }
    }

    @Then("the body response content should be matched integer")
    public void theBodyResponseContentShouldBeMatchedInteger(DataTable table){
        List<List<String>> data = table.asLists();
        for (int i = 1; i < data.size(); i++) {
            response.then().assertThat().body(data.get(i).get(0), equalTo(Integer.parseInt(data.get(i).get(1))));
        }
    }
}
