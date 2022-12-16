package com.b.ignited.pidemo;

import com.b.ignited.pidemo.entity.ApiResponse;
import com.b.ignited.pidemo.entity.MainResponseObject;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Scanner;

public class PiDemo {
    private static final Scanner console = new Scanner(System.in);
    private static int consecutiveNumberCount;
    public static void runDemo(){

        String userSelection = "0";

        System.out.println("Welcome to the Pi Demo Application, what would you like to do?");
        printOptions();

        while(!userSelection.equals("3")){

            userSelection = console.next();

            makeChoice(userSelection);

        }

    }

    private static void handleFirstSelection(){
        System.out.println("What number do you want to check? (Single digits only) ");

        String digitToCheck = console.next();
        boolean isInputValid = validateInputSingleDigit(digitToCheck);

        String checkResult = isInputValid ? handleConsecutiveNumberOfIndividualDigitCount(digitToCheck) : "bad input, please try again";

        System.out.println(checkResult);
        resetCount();
    }

    private static void handleSecondSelection(){
        System.out.println("What number do you want to check?");

        String digitsToCheck = console.next();
        boolean isInputValid = validateInputMultipleDigits(digitsToCheck);

        String checkResult = isInputValid ? handleCheckIfSeriesOfNumberExists(digitsToCheck) : "bad input, please try again";

        System.out.println(checkResult);
    }

    private static String handleConsecutiveNumberOfIndividualDigitCount(String number){

        String singleDigitNumber = String.valueOf(number.charAt(0));

        ApiResponse apiResponse = handleApiCall(number);
        MainResponseObject mainResponseObject = apiResponse.getR().get(0);

        if(mainResponseObject.getStatus().equals("found")) {
            consecutiveNumberCount++;
            handleConsecutiveNumberOfIndividualDigitCount(number + singleDigitNumber);
        }

        return String.format("The answer to your check is: %s", consecutiveNumberCount);

    }

    private static String handleCheckIfSeriesOfNumberExists(String number){

        ApiResponse apiResponse = handleApiCall(number);
        MainResponseObject mainResponseObject = apiResponse.getR().get(0);

        if(mainResponseObject.getStatus().equals("found")){

            return String.format("The number %s has been found in position %s of pi", number, mainResponseObject.getP());

        } else {

            return String.format("The number %s has not been found", number);

        }

    }

    private static ApiResponse handleApiCall(String number) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("q", number);

        WebClient client = WebClient.builder()
                .baseUrl("https://www.angio.net/newpi/piquery")
                .build();

        return client.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .block()
                .bodyToMono(ApiResponse.class)
                .block();
    }

    private static void printOptions(){
        System.out.println();
        System.out.println("1 - Check for the longest consecutive occurrence of a single digit number");
        System.out.println("2 - Check if a series of numbers is present in pi");
        System.out.println("3 - Exit Application");
        System.out.print("Choice:");
    }

    private static void makeChoice(String choice){

        switch(choice){
            case "1"-> {
                handleFirstSelection();
                printNextSelectionText();
                printOptions();
            }
            case "2" -> {
                handleSecondSelection();
                printNextSelectionText();
                printOptions();
            }
            case "3" -> {
                System.out.println("Thanks for trying out the demo!");
            }
            default -> {
                System.out.println("Please pick one of the options above");
                printOptions();
            }

        }

    }

    private static void resetCount(){

        consecutiveNumberCount = 0;

    }

    private static Boolean validateInputSingleDigit(String stringToValidate){

        return stringToValidate.matches("[0-9]+") && stringToValidate.length() == 1;

    }

    private static Boolean validateInputMultipleDigits(String stringToValidate){

        return stringToValidate.matches("[0-9]+") && stringToValidate.length() > 0;

    }

    private static void printNextSelectionText(){
        System.out.println("");
        System.out.println("What else would you like to do?");
    }

}
