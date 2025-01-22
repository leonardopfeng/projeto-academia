package br.com.lelis.integrationtests.controller.json;

import br.com.lelis.config.TestConfigs;
import br.com.lelis.data.vo.security.TokenVO;
import br.com.lelis.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.lelis.integrationtests.vo.AccountCredentialsVO;
import br.com.lelis.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest{

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    public static void setup(){
        objectMapper = new ObjectMapper();
        // disable fails caused by HATEOS
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException, JsonMappingException{
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                        .as(TokenVO.class)
                .getAccessToken();

        System.out.println("Access Token: " + accessToken);


        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v2")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given()
                .spec(specification)
                .when()
                .get()
                .then()
                    .statusCode(200);
    }


    @Test
    @Order(1)
    public void testDisabledPersonById() throws JsonMappingException, JsonProcessingException{
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        Assertions.assertNotNull(persistedPerson);

        Assertions.assertNotNull(persistedPerson.getId());
        Assertions.assertNotNull(persistedPerson.getFirstName());
        Assertions.assertNotNull(persistedPerson.getLastName());
        Assertions.assertNotNull(persistedPerson.getAddress());
        Assertions.assertNotNull(persistedPerson.getGender());
        Assertions.assertTrue(persistedPerson.isEnabled());

        Assertions.assertEquals(persistedPerson.getId(), person.getId());
    }

    @Test
    @Order(2)
    public void testCreate() throws JsonProcessingException {
        mockPerson();

        var content =given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LELIS)
                    .body(person)
                        .when()
                            .post()
                        .then()
                            .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;

        Assertions.assertEquals("Richard",createdPerson.getFirstName());
        Assertions.assertEquals("Stallman",createdPerson.getLastName());
        Assertions.assertEquals("New York - NY", createdPerson.getAddress());
        Assertions.assertEquals("male",createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getId() > 0);
        Assertions.assertTrue(createdPerson.isEnabled());

    }

    @Test
    @Order(3)
    public void testCreateWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        var content =given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SILEL)
                    .body(person)
                        .when()
                            .post()
                        .then()
                            .statusCode(403)
                    .extract()
                        .body()
                            .asString();


        Assertions.assertNotNull(content);
        Assertions.assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York - NY");
        person.setGender("male");
        person.setEnabled(true);
    }
}
