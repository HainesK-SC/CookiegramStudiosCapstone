package com.cookiegramstudios.cookiegram.common;

/**
 * Utility class for parsing and normalizing name strings.
 * <p>
 * Provides common name parsing functionality used across the application
 * for splitting full names into first and last name components.
 * </p>
 * <p>
 * This utility assumes Western name conventions (First Last) and handles
 * edge cases like single names, multiple middle names, and compound last names.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
public class NameParser {
	
    private NameParser() {
        throw new UnsupportedOperationException("Utility class - do not instantiate");
    }

    public static String[] parseFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", ""};
        }
 
        String trimmedName = fullName.trim();
        String[] parts = trimmedName.split(" ", 2);
 
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";
 
        return new String[]{firstName, lastName};
    }
 
  
    public static String getFirstName(String fullName) {
        return parseFullName(fullName)[0];
    }
 
  
    public static String getLastName(String fullName) {
        return parseFullName(fullName)[1];
    }
 
    
    public static String normalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        // Replace multiple spaces with single space and trim
        return name.trim().replaceAll("\\s+", " ");
    }

    
    public static String capitalizeWords(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
 
        String normalized = normalizeName(name);
        String[] words = normalized.split(" ");
        StringBuilder capitalized = new StringBuilder();
 
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    capitalized.append(word.substring(1).toLowerCase());
                }
            }
            if (i < words.length - 1) {
                capitalized.append(" ");
            }
        }
 
        return capitalized.toString();
    }

}
