package com.frankzheng.css_challenge;

public class Order {
    public Order(String id, String name, OrderTemperature temperature, int shelfLife, float decayRate) {
        this.id = id;
        this.name = name;
        this.temperature = temperature;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
    }

    private String id;
    private String name;
    private OrderTemperature temperature;
    private int shelfLife;
    private float decayRate;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OrderTemperature getTemperature() {
        return temperature;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public float getDecayRate() {
        return decayRate;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s,%s,%d,%f]", id, name, temperature.toString(), shelfLife, decayRate);
    }
}
