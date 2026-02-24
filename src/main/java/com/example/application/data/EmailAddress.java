package com.example.application.data;

public record EmailAddress(String value) {

    public static final int MAX_LENGTH = 320; // local name 64 bytes, @ 1 byte, domain name 255 bytes

    public EmailAddress {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    /**
     * Checks if the given string is a valid e-mail address.
     *
     * @param value the e-mail address to validate
     * @return {@code true} if the e-mail address is valid, {@code false} otherwise
     */
    public static boolean isValid(String value) {
        // Check length
        if (value.isEmpty() || value.length() > MAX_LENGTH) {
            return false;
        }
        var parts = value.split("@");
        // Check number of parts
        if (parts.length != 2) {
            return false;
        }
        // Validate parts
        return isValidLocalPart(parts[0]) && isValidDomainName(parts[1]);
    }

    /**
     * Note! Comments and quoted local parts are not supported (yet).
     */
    private static boolean isValidLocalPart(String localPart) {
        // Check length
        if (localPart.isEmpty() || localPart.length() > 64) {
            return false;
        }
        // Check for invalid characters
        if (!localPart.matches("[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+")) {
            return false;
        }
        // Check for double dots
        if (localPart.contains("..")) {
            return false;
        }
        // Check for leading or trailing dots
        return !localPart.startsWith(".") && !localPart.endsWith(".");
    }

    private static boolean isValidDomainName(String domainName) {
        // Check length
        if (domainName.isEmpty() || domainName.length() > 255) {
            return false;
        }
        // Is it an IP address?
        if (domainName.startsWith("[")) {
            if (!domainName.endsWith("]")) {
                return false;
            }
            if (domainName.startsWith("[IPv6:")) {
                return IpAddress.Ipv6.isValidIpv6(domainName.substring(6, domainName.length() - 1));
            } else {
                return IpAddress.Ipv4.isValidIpv4(domainName.substring(1, domainName.length() - 1));
            }
        } else {
            return DomainName.isValid(domainName);
        }
    }
}