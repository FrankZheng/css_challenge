package com.frankzheng.css_challenge;

public enum OrderTemperature {
    hot("hot"),
    cold("cold"),
    frozen("frozen"),
    any("any");

    private final String name;

    private OrderTemperature(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
