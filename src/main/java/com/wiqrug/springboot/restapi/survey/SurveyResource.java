package com.wiqrug.springboot.restapi.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
public class SurveyResource {

    @Autowired
    private SurveyService surveyService;

    @RequestMapping("/surveys")
    public List<Survey> retrieveAllSurveys()
    {
        return surveyService.retrieveAllSurveys();
    }


    @RequestMapping("/surveys/{surveyId}")
    public Survey retrieveSurveyById(@PathVariable String surveyId)
    {
                Survey survey = surveyService.retrieveSurveyById(surveyId);

        //in case surveyId does not exist, we need to make sure that it returns 404
        //The easiest way to do this is by throwing exception HttpStatus.NOT_FOUND
        if (survey==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return survey;
    }

    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveAllSurveyQuestions(@PathVariable String surveyId)
    {
       List<Question> questions = surveyService.retrieveAllSurveyQuestions(surveyId);
                if (questions == null)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND );
                return questions;

    }

    @RequestMapping("surveys/{surveyId}/questions/{questionId}")

    public Question retrieveSpecificSurveyQuestion(@PathVariable String surveyId , @PathVariable String questionId)
    {
        Question question = surveyService.retrieveSpecificSurveyQuestion(surveyId,questionId);

        if (question==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return question;

    }


    //Adding a survey question.
    //RequestBody is the data sent by the client to my API.
    //So when we post we don't use PathParam.
    //@PathParam is a parameter annotation which allows you to map variable URI path fragments into your method call.


    //POST is used to ADD a new element, not to update!
    @RequestMapping(value = "/surveys/{surveyId}/questions" , method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId , @RequestBody Question question)
    {
        String questionId  = surveyService.addNewSurveyQuestion(surveyId, question);
        //surveys/{surveyId}/questions/{questionId}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{questionId}").buildAndExpand(questionId).toUri();
        return ResponseEntity.created(location).build();

    }

    //Btw the important is what is the right ResponseStatus, and what is the content of the ResponseBody
    

    @RequestMapping(value = "surveys/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)

    public ResponseEntity<Object> deleteSurveyQuestionById(@PathVariable String surveyId , @PathVariable String questionId)
    {
       surveyService.deleteSurveyQuestionById(surveyId,questionId);

        return ResponseEntity.noContent().build();
    }


    //If you want to update something the correct RequestMethod is PUT
    @RequestMapping(value = "surveys/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)

    public ResponseEntity<Object> updateSurveyQuestion(@PathVariable String surveyId , @PathVariable String questionId, @RequestBody Question question)
    {
        surveyService.updateSurveyQuestion(surveyId,questionId,question);

        return ResponseEntity.noContent().build();
    }



}
