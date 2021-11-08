package RestApiSetup;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFullSpec {
    private int id;
    private String avatar;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
}
