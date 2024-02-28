package edu.java.bot.utils;

import java.net.MalformedURLException;
import java.net.URI;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Validation {

    public static boolean isLink(String text) {
        try {
            //noinspection ResultOfMethodCallIgnored
            URI.create(text).toURL();
            return true;
        } catch (IllegalArgumentException | MalformedURLException e) {
            return false;
        }
    }
}
