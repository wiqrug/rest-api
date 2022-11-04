package com.wiqrug.springboot.restapi;


import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

    //http://localhost:8080/surveys/survey1/questions/Question1

    //This is text block
    String str = """
                    {
                    id: "Question1",
                    description: "Most Popular Cloud Platform Today",
                    options: [
                    "AWS",
                    "Azure",
                    "Google Cloud",
                    "Oracle Cloud"
                    ],
                    correctAnswer: "AWS"
                    }
            """;

    //http://localhost:RANDOMPORT/surveys/survey1/questions/Question1

    //Magic happens again!
    //Spring recognizes the random port and utilizes it.


    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";


    @Autowired
    TestRestTemplate template;


    @Test
    void retrieveSpecificSurveyQuestion_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL,String.class);
        String expectedResponse =
                """
                        {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                        """;
        JSONAssert.assertEquals(expectedResponse,responseEntity.getBody(),false);

  //      System.out.println(responseEntity.getBody())
 //       System.out.println(responseEntity.getHeaders());

    }

}
