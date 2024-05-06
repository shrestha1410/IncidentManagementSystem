package com.incident.utils;

import java.util.Random;
import java.time.Year;

public class IncidentIdGenerator {

    private static final String PREFIX = "RMG";
    private static final int RANDOM_NUMBER_LENGTH = 5;

    public static String generateUniqueId() {
        String randomNumber = generateRandomNumber();
        int year = Year.now().getValue();
        return PREFIX + randomNumber + year;
    }

    private static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < RANDOM_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
