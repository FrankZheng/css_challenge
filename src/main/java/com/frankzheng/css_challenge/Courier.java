package com.frankzheng.css_challenge;

import java.util.List;

public class Courier {
    private Order order;
    private long arrivalTimeMills = 0;
    public Courier() {

    }

    public void assignOrder(Order order, long currentMills) {
        this.order = order;
        int sec = RandomUtils.randInt(2, 6);
        arrivalTimeMills = currentMills + sec * 1000;
    }

    public boolean isArrival(long currentMills) {
        return currentMills >= arrivalTimeMills;
    }

    public boolean pickUpOrderFromShelves(List<Shelf> shelves) {
        //TODO: check no order case
        for(Shelf shelf : shelves) {
            if(shelf.pickUpOrder(order)) {
                System.out.printf("Order[%s] has been picked up from Shelf[%s]%n", order.getId(), shelf.getName());
               return true;
            }
        }
        return false;
    }




}
