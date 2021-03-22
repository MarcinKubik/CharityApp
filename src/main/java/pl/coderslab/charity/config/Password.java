package pl.coderslab.charity.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    private static final String UPPER_CASE_REGEX = "[A-Z]+";
    private static final String LOWER_CASE_REGEX = "[a-z]+";
    private static final String DIGIT_REGEX = "[0-9]+";
    private static final String SPECIAL_CHARACTER_REGEX = "\\W+";

    public String testPassword(String password, String password2){

        if (password.length() < 8){
            return "Hasło musi zawierać co najmniej 8 znaków";
        }
        Pattern upperCasePattern = Pattern.compile(UPPER_CASE_REGEX);
        Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        if(!upperCaseMatcher.find()){
            return "Hasło musi zawierać co najmniej jedną wielką literę";
        }

        Pattern lowerCasePattern = Pattern.compile(LOWER_CASE_REGEX);
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);
        if(!lowerCaseMatcher.find()){
            return "Hasło musi zawierać co najmniej jedną małą literę";
        }
        Pattern digitPattern = Pattern.compile(DIGIT_REGEX);
        Matcher digitMatcher = digitPattern.matcher(password);
        if(!digitMatcher.find()){
            return "Hasło musi zawierać co najmniej jedną cyfrę";
        }
        Pattern specialCharacterPattern = Pattern.compile(SPECIAL_CHARACTER_REGEX);
        Matcher specialCharacterMatcher = specialCharacterPattern.matcher(password);
        if(!specialCharacterMatcher.find()){
            return "Hasło musi zawierać co najmniej jeden znak specjalny";
        }
        if (password2 != null && !password2.equals(password)) {
            return "Hasła nie są takie same";
        }

        return "Hasło OK";
    }
}
