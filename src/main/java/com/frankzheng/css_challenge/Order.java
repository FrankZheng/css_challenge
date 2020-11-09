package com.frankzheng.css_challenge;

public class Order {
    public Order(String id, String name, OrderTemperature temperature, int shelfLife, double decayRate, long orderCreationTime) {
        this.id = id;
        this.name = name;
        this.temperature = temperature;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
        this.orderCreationTime = orderCreationTime;
        shelfDecayModifier = ShelfDecayModifier.notPlacedToShelf;
    }

    public Order(String id, String name, OrderTemperature temperature, int shelfLife, double decayRate) {
        this(id, name, temperature, shelfLife, decayRate, System.currentTimeMillis());
    }

    private String id;
    private String name;
    private OrderTemperature temperature;
    private int shelfLife;
    private double decayRate;
    private long orderCreationTime;
    private ShelfDecayModifier shelfDecayModifier;

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

    public double getDecayRate() {
        return decayRate;
    }

    public void setShelfDecayModifier(ShelfDecayModifier shelfDecayModifier) {
        this.shelfDecayModifier = shelfDecayModifier;
    }

    public ShelfDecayModifier getShelfDecayModifier() {
        return shelfDecayModifier;
    }

    public long getOrderCreationTime() {
        return orderCreationTime;
    }

    //for testing convenience
    public double getValue(long currTimeMills) {
        long orderAge = (currTimeMills - orderCreationTime) / 1000;
        return (shelfLife - orderAge - decayRate * orderAge * shelfDecayModifier.getValue()) / (shelfLife * 1.0);
    }

    public double getValue() {
        return getValue(System.currentTimeMillis());
    }

    //for testing convenience
    public boolean isWasted(long currTimeMills) {
        return getValue(currTimeMills) <= 0;
    }

    public boolean isWasted() {
        return isWasted(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return String.format("[%s,%s,%s,%d,%f,%f]",
                id, name, temperature.toString(), shelfLife, decayRate, getValue());
    }
}
