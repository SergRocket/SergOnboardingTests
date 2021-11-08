package RestApiSetup;

import static io.restassured.RestAssured.given;

public class UserSteps {
    private UserResponse user;
    public UserResponse createUser(ParamForUserCreation paramForUserCreation){
        user = given().body(paramForUserCreation).post().as(UserResponse.class);
        return user;
    }
}
