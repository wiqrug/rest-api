package com.wiqrug.springboot.restapi.survey;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)       //Disabling filters, because spring security runs on filters
public class SurveyResourceTest {


    //We need to Mock -> surveyService.retrieveSpecificQuestion(surveyId, questionId)
    //We also want to fire a request to the url  /surveys/Survey1/question/Question1
    //Then check if the response matches as expected
    //We will use Mock MVC framework


    /*
    About MockBean annotation.

    If we run the test without @Mockbean SurveyService surveyService, we will see that it fails.The reason is
    that SurveyResource has a dependency on SurveyService. Using MockBean we mock out the SurveyService.
    This way we can run the test effectively.

    */

    @MockBean
    private SurveyService surveyService;
    //Fire a request using mock MVC
    @Autowired
    private MockMvc mockMvc;

    private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/";
    @Test
    void retrieveSpecificSurveyQuestion_404basicScenario() throws Exception {

        //.accept(MediaType.APPLICATION_JSON is used because we need to get JSON response back
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);


        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());

        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println(mvcResult.getResponse().getStatus());

    }


    @Test
    void retrieveSpecificSurveyQuestion_basicScenario() throws Exception {

        //.accept(MediaType.APPLICATION_JSON is used because we need to get JSON response back
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);

        String expectedResponse = """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;
        Question question = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        when(surveyService.retrieveSpecificSurveyQuestion("Survey1","Question1")).thenReturn(question);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString(),false);


    }
    //for a post request
    //addNewSurveyQuestion
    //OK since this is a post request we need to test the responseStatus. We need it to be 201
    //we also want to check the location header  (properly populated)



    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception {
        String requestBody = """
                {
                    "description": "Most Popular programming language",
                    "options": [
                        "Python",
                        "Cobol",
                        "Go",
                        "Matlab"
                    ],
                    "correctAnswer": "Python"      
                }            
            """;

        //Im sending post request
        //I want my response format to be JSON
        //my content is requestBody and its in JSON format as well.

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(GENERIC_QUESTION_URL)
                        .accept(MediaType.APPLICATION_JSON).content(requestBody).contentType(MediaType.APPLICATION_JSON);
        //see comments line

        when(surveyService.addNewSurveyQuestion(anyString(),any())).thenReturn("SOME_ID");

        //To fire that request
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(201,mvcResult.getResponse().getStatus());

        //To check the headers
        //To get the location header back
        //System.out.println(mvcResult.getResponse().getHeader("Location"));
        //extract the location header to a variable

        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/SOME_ID"));


      //  JSONAssert.assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString(),false);

        //now i need to mock the right method on the SurveyResource
        //implementation happens over MVC RESULT

    }
}
