package com.olajideosho.komodohub.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AccessCodeGenerator {
    public static String generateAccessCode(String firstName, String dateOfBirthString, String role, String classroomName) {
        try {
            String combinedString = (firstName.toLowerCase() +
                    dateOfBirthString.replaceAll("/", "") +
                    role +
                    classroomName).trim();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combinedString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b: hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);

            }

            return hexString.toString().substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "INVALID CODE. TRY AGAIN.";
        }
    }

    public static boolean verifyAccessCode(String accessCode, String firstName, String dateOfBirthString, String role, String classroomName) {
        String generatedCode = generateAccessCode(firstName, dateOfBirthString, role, classroomName);
        return accessCode.equals(generatedCode);
    }
}
