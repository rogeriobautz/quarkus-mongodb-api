package com.bautz.util;

public class StringUtil {

    public static String capitalizeEveryWord(String input) {
    if (input == null || input.isBlank()) {
        return input;
    }

    char[] chars = input.trim().toLowerCase().toCharArray();
    boolean capitalizeNext = true;

    for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        
        // Character.isLetter lida corretamente com Unicode (ã, é, ç, etc.)
        if (Character.isLetter(c)) {
            if (capitalizeNext) {
                chars[i] = Character.toUpperCase(c);
                capitalizeNext = false;
            }
        } else {
            // Se não é letra (espaço, hífen, número, ponto), 
            // a próxima letra deve ser capitalizada.
            capitalizeNext = true;
        }
    }
    return new String(chars);
}


}
