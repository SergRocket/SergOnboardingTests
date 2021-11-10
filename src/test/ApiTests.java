import RestApiSetup.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests {
    private static Autorization api;

    @BeforeClass
    public static void prepeareClient(){
        api = Autorization.loginAs("eve.holt@reques.in", "citysLicka");
    }

    //for eliminating code repeats
    private static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri("https://reqres.in/api")
            .setBasePath("/users")
            .setContentType(ContentType.JSON)
            .build();

    @Test
    public void getUsers(){
        given()
                .spec(REQUEST_SPECIFICATION)
                .when().get()
                .then().statusCode(200);
    }
    @Test
    public void getOneUsersName(){
        given()
                .spec(REQUEST_SPECIFICATION)
                .when().get()
                .then()
                .statusCode(200)
                .body("data[0].email", equalTo("george.bluth@reqres.in"));

    }

    @Test
    public void printUserName(){
        String user = given().spec(REQUEST_SPECIFICATION)
                .when().get()
                .then()
                .statusCode(200)
                .extract().asString();
        System.out.println(user);
    }

    @Test
    public void checkUserNameWithLamda (){
        given().spec(REQUEST_SPECIFICATION)
                .when().get()
                .then()
                .statusCode(200)
                .body("data.find{it.email=='george.bluth@reqres.in'}.first_name", equalTo("George"));
    }
    @Test
    public void getTheListOfEmails(){
        List<String> emails = given()
                .spec(REQUEST_SPECIFICATION)
                .when().get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("data.email");
        System.out.println(emails.get(2));
        System.out.println(emails.get(3));
        System.out.println(emails.get(4));
    }

    @Test
    public void deserializationOfTheListtoOBJ(){
        List<Deserialization> users = given()
                .spec(REQUEST_SPECIFICATION)
                .when().get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("data", Deserialization.class);
        assertThat(users).extracting(Deserialization::getEmail).contains("george.bluth@reqres.in");
    }
    @Test
    public void deserializationOfTheListtoOBJFULL() {
        assertThat(api.getUsers()).extracting(Deserialization::getEmail).contains("george.bluth@reqres.in");
    }

    @Test
    public void createUser(){
        ParamForUserCreation createUsersTest = UserGenerator.getSimpleUser();
        UserResponse createUserResponseTest = api.createUser(createUsersTest);
        AssertionsForClassTypes.assertThat(createUserResponseTest)
                .isNotNull()
                .extracting(UserResponse::getName)
                .isEqualTo(createUsersTest.getName());

        AssertionsForClassTypes.assertThat(createUsersTest)
                .isNotNull()
                .extracting(ParamForUserCreation::getName)
                .isEqualTo(createUsersTest.getName());
    }
}
