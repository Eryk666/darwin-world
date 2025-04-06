package agh.ics.oop.util;

import agh.ics.oop.exception.WrongInputException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//because I can
public class InputParser {
    public static int parse(String input) throws WrongInputException {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher((input));

        if(matcher.find()){
            return Integer.parseInt(matcher.group());
        }else {
            throw new WrongInputException(input + " is not a natural number!");
        }
    }
}
