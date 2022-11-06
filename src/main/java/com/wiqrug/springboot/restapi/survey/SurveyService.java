package com.wiqrug.springboot.restapi.survey;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Predicate;

@Service
public class SurveyService {

    private static List<Survey> surveys = new ArrayList<>();

    static {
        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey",
                "Description of the Survey", questions);

        surveys.add(survey);
    }


    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    public Survey retrieveSurveyById(String surveyId) {

        //First is loop through predicates and select the one with the matching id
        //Second one is the one i implement, using functional programming



        Predicate<? super Survey> predicate=
                //This is a lamda function, calls getter and checks if its the same with the PathVariable
                //interesting piece of code.
                        survey-> survey.getId().equalsIgnoreCase(surveyId);

        //stream.filter... if we have 10 surveys then we make a stream of all of them, and then
        //find the first that matches
        Optional<Survey> optionalSurvey = surveys.stream().filter(predicate).findFirst();

        if (optionalSurvey.isEmpty()) return null;

        return optionalSurvey.get();

    }

    public List<Question> retrieveAllSurveyQuestions(String surveyId) {


    Survey survey = retrieveSurveyById(surveyId);

    if (survey==null) return null;

    return survey.getQuestions();

    }


    public Question retrieveSpecificSurveyQuestion(String surveyId, String questionId) {
            List<Question> surveyQuestions = retrieveAllSurveyQuestions(surveyId);

            if (surveyQuestions==null)
                return null;
        Optional<Question> optionalQuestion = surveyQuestions.stream().filter(q -> q.getId().equalsIgnoreCase(questionId)).findFirst();
            if (optionalQuestion.isEmpty())
                return null;
        return optionalQuestion.get();

    }

    public String addNewSurveyQuestion(String surveyId, Question question) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);

        question.setId(generateRandomId());

        questions.add(question);

        return question.getId();
    }


    //Constructs a secure random number generator (RNG)
    //I think i could also use UUID
    private static String generateRandomId() {
        SecureRandom secureRandom = new SecureRandom();
        String randomId = new BigInteger(32, secureRandom).toString();
        return randomId;
    }

    public String deleteSurveyQuestionById(String surveyId, String questionId) {
        List<Question> surveyQuestions = retrieveAllSurveyQuestions(surveyId);

        if (surveyQuestions==null)
            return null;

        Predicate<? super Question> predicate  = q -> q.getId().equalsIgnoreCase(questionId);
        boolean removed = surveyQuestions.removeIf(predicate);

        if (!removed) return null;

        return questionId;

    }

    public void updateSurveyQuestion(String surveyId, String questionId, Question question) {
            List<Question> questions = retrieveAllSurveyQuestions(surveyId);
            questions.removeIf(q-> q.getId().equalsIgnoreCase(questionId));
            questions.add(question);


    }
}
