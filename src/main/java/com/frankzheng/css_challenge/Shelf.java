package com.frankzheng.css_challenge;


import java.util.LinkedList;
import java.util.List;

public class Shelf {


    public Shelf(String name, OrderTemperature allowableTemp, int capacity) {
        this.name = name;
        this.allowableTemp = allowableTemp;
        this.capacity = capacity;
    }

    private String name;
    private OrderTemperature allowableTemp;
    private int capacity;
    protected List<Order> orders = new LinkedList<>();

    public String getName() {
        return name;
    }

    public OrderTemperature getAllowableTemp() {
        return allowableTemp;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean hasRoomForOrder(Order order) {
        return order.getTemperature() == allowableTemp && orders.size() < capacity;

    }

    public boolean isFull() {
        return orders.size() == capacity;
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public void placeOrder(Order order) {
        if(order.getTemperature() != allowableTemp) {
            throw new IllegalArgumentException("Order's temperature is disallowed");
        }

        if(isFull()) {
            throw new IllegalArgumentException(name + " is full, can't place order");
        }

        System.out.printf("Place order[%s] to shelf[%s]%n", order.getId(), name);
        orders.add(order);
    }

    public boolean pickUpOrder(Order orderToPickUp) {
        for(int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if(order.getId().equals(orderToPickUp.getId())) {
                orders.remove(i);
                return true;
            }
        }
        return false;
    }

}
