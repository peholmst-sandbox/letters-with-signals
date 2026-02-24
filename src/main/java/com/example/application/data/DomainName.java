package com.example.application.data;

public record DomainName(String value) {

    public static final int MAX_LENGTH = 253;

    public DomainName {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid domain name");
        }
    }

    /**
     * Checks if the given string is a valid domain name (without actually checking that the domain name exists).
     *
     * @param value the string to check
     * @return {@code true} if the string is a valid domain name, {@code false} otherwise
     */
    public static boolean isValid(String value) {
        // Check length
        if (value.isEmpty() || value.length() > MAX_LENGTH) {
            return false;
        }
        var labels = value.split("\\.", -1);
        for (var label : labels) {
            // Check label length
            if (label.isEmpty() || label.length() > 63) {
                return false;
            }
            // Check label characters (only ASCII letters, digits, and - are allowed)
            for (var c : label.toCharArray()) {
                if (!Character.isDigit(c) && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '-') {
                    return false;
                }
            }
            // Check that label does not start or end with a -
            if (label.charAt(0) == '-' || label.charAt(label.length() - 1) == '-') {
                return false;
            }
        }
        return true;
    }
}