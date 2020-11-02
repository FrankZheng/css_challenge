package com.frankzheng.css_challenge;

public class RandomUtils {
    public static int randInt(int min, int max) {
        return min + (int)(Math.random() * (max-min+1));
    }
}
