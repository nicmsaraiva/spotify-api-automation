package com.nicmsaraiva;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserProfileTest {

    private String accessToken;

    @BeforeTest
    public void setUp() {

        String CLIENT_ID = "25a3c782622346fa9126c1fe17b93eb9";
        String CLIENT_SECRET = "b412a586b0e341e680766225659d980e";
        String TOKEN_URL = "https://accounts.spotify.com/api/token";

        accessToken =
                given()
                        .formParam("grant_type", "client_credentials")
                        .formParam("client_id", CLIENT_ID)
                        .formParam("client_secret", CLIENT_SECRET)
                .when()
                        .post(TOKEN_URL)
                .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getString("access_token");

        System.out.println("Access token: " + accessToken);
    }

    @Test
    public void getUserId() {
        RestAssured.baseURI = "https://api.spotify.com/v1";
        String response = given()
                .header("Authorization", ("Bearer " + accessToken))
        .when()
                .get("/me")
        .then()
                .extract().asPrettyString();

        System.out.println(response);
    }

    @Test
    public void createPlaylist() {
        String userId = "31ashhkhlei5mmwixxuinxkg22la";
        String requestBody = "{ \"name\": \"Test Playlist 0078\", \"description\": \"New playlist description\", \"public\": true }";
        RestAssured.baseURI = ("https://api.spotify.com/v1/users/" + userId);
        String response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestBody)
                .when()
                .post("/playlists")
                .then().extract().asPrettyString();
        System.out.println("Request: " + response);
    }
}
