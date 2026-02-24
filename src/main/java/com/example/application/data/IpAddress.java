package com.example.application.data;

public sealed interface IpAddress {

    record Ipv4(String value) implements IpAddress {

        public static final int MIN_LENGTH = 7; // 0.0.0.0
        public static final int MAX_LENGTH = 15; // 255.255.255.255

        public Ipv4 {
            if (!isValidIpv4(value)) {
                throw new IllegalArgumentException("Invalid ipv4 address");
            }
        }

        /**
         * Checks if the given string is a valid IPv4 address.
         *
         * @param value the string to check
         * @return {@code true} if the string is a valid IPv4 address, {@code false} otherwise
         */
        public static boolean isValidIpv4(String value) {
            // Check length
            if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
                return false;
            }
            // Check number of octets
            var octets = value.split("\\.");
            if (octets.length != 4) {
                return false;
            }
            // Check that each octet is a number between 0 and 255
            for (var octet : octets) {
                try {
                    var number = Integer.parseInt(octet);
                    if (number < 0 || number > 255) {
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Value object representing an IPv6 address.
     */
    record Ipv6(String value) implements IpAddress {

        public static final int MIN_LENGTH = 2; // ::
        public static final int MAX_LENGTH = 39; // 2001:0db8:85a3:0000:0000:8a2e:0370:7334

        public Ipv6 {
            if (!isValidIpv6(value)) {
                throw new IllegalArgumentException("Invalid ipv6 address");
            }
        }

        /**
         * Checks if the given string is a valid IPv6 address.
         *
         * @param value the string to check
         * @return {@code true} if the string is a valid IPv6 address, {@code false} otherwise
         */
        public static boolean isValidIpv6(String value) {
            // Check length
            if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
                return false;
            }
            // Check number of quibbles
            var quibbles = value.split(":", -1);
            if (quibbles.length < 3 || quibbles.length > 8) {
                return false;
            }
            // Check that each quibble is either empty or a valid hexadecimal number
            for (var quibble : quibbles) {
                if (quibble.isEmpty()) {
                    continue;
                }
                if (quibble.length() > 4) {
                    return false;
                }
                for (var c : quibble.toCharArray()) {
                    if (!Character.isDigit(c) && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                        return false;
                    }
                }
            }
            var doubleColon = value.indexOf("::");
            // Check that if there is no double colon, there are exactly 8 quibbles
            if (doubleColon == -1 && quibbles.length != 8) {
                return false;
            }
            // Check that if there is a double colon, it occurs at most once
            if (doubleColon > -1 && value.indexOf("::", doubleColon + 1) != -1) {
                return false;
            }
            // Check that if there is a double colon, and it is not at the beginning of the string, the first quibble must not be empty
            if (doubleColon > 0 && quibbles[0].isEmpty()) {
                return false;
            }
            // Check that if there is a double colon, and it is not at the end of the string, the last quibble must not be empty
            if (doubleColon < value.length() - 2 && quibbles[quibbles.length - 1].isEmpty()) {
                return false;
            }

            return true;
        }
    }
}