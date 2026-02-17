package com.bautz.util;

import java.util.regex.Pattern;

public class StringUtil {

    public static String normalizeName(String input) {
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

    /* 

    // UNICODE_CHARACTER_CLASS: Faz o \w, \b e \p{L} entenderem acentos corretamente
    // CASE_INSENSITIVE: Permite trabalhar com o input original ou lowercase
    private static final Pattern WORD_START = Pattern.compile("(?U)\\b\\p{L}");

    public static String capitalizeEveryWord(String input) {
        if (input == null || input.isBlank())
            return input;

        // Passamos para lowercase primeiro para garantir que o resto da palavra fique
        // minuscula
        return WORD_START.matcher(input.toLowerCase())
                .replaceAll(m -> m.group().toUpperCase());
    }
                
    */

}
