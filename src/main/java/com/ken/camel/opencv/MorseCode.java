package com.ken.camel.opencv;

import java.util.HashMap;
import java.util.Map;

/**
 * MorseCode a class to convert letters to morse code and vice versa.
 */
public class MorseCode {
    private static Map<String, String> lookup = new HashMap<String, String>();
    private static Map<String, String> reverse = new HashMap<String, String>();
    // Morse code data
    private static String[][] code = new String[][] {
        {"A", ".-"},
        {"B", "-..."},
        {"C", "-.-."},
        {"D", "-.."},
        {"E", "."},
        {"F", "..-."},
        {"G", "--."},
        {"H", "...."},
        {"I", ".."},
        {"J", ".---"},
        {"K", "-.-"},
        {"L", ".-.."},
        {"M", "--"},
        {"N", "-."},
        {"O", "---"},
        {"P", ".--."},
        {"Q", "--.-"},
        {"R", ".-."},
        {"S", "..."},
        {"T", "-"},
        {"U", "..-"},
        {"V", "...-"},
        {"W", ".--"},
        {"X", "-..-"},
        {"Y", "-.--"},
        {"Z", "--.."},
        {"1", ".----"},
        {"2", "..---"},
        {"3", "...--"},
        {"4", "....-"},
        {"5", "....."},
        {"6", "-...."},
        {"7", "--..."},
        {"8", "---.."},
        {"9", "----."},
        {"0", "-----"},
        {".", ".-.-.-"},
        {",", "--..--"},
        {"?", "..--.."},
        {"'", ".----."},
        {"!", "-.-.--"},
        {"/", "-..-."},
        {"(", "-.--."},
        {")", "-.--.-"},
        {":", "---..."},
        {";", "-.-.-."},
        {"=", "-...-"},
        {"+", ".-.-."},
        {"-", "-....-"},
        {"_", "..--.-"},
        {"\"", ".-..-."},
        {"$", "...-..-"},
        {"@", ".--.-."},
        {"&", ".-..."},
        {"%", "----- .-..-. -----"},
        {" ", "_"}
    };

    // Initialize the lookup maps
    static {
        for (String[] strings : code) {
            lookup.put(strings[0], strings[1]);
            reverse.put(strings[1], strings[0]);
        }
    }

    public static void main(String[] args) {
        System.out.println(string2MorseCode("Hello World"));
    }
    
    public static String string2MorseCode(String morse) {
        StringBuilder ret = new StringBuilder();
        for (Byte b : morse.toUpperCase().getBytes()) {
                ret.append(lookup.get(new String(new byte[] {b}))).append(" ");
        }
        return ret.toString();
    }
    
    public static String letter2MorseCode(String morse) {
        return lookup.get(morse.toUpperCase());
    }
    
    public static String lookUpMorseCode(String morse) {
        return reverse.get(morse);
    }
}
