package com.frankzheng.css_challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Courier {
    static Logger logger = LoggerFactory.getLogger(Courier.class);

    private final Order order;
    private final long arrivalTimeMills;


    public Courier(Order order, long currentMills, int arrivalTimeMin, int arrivalTimeMax) {
        this.order = order;
        int sec = RandomUtils.randInt(arrivalTimeMin, arrivalTimeMax);
        logger.debug("Order[{}] has been assigned to a courier, will pick up order {} seconds later",
                order.getId(), sec);
        arrivalTimeMills = currentMills + sec * 1000;
    }

    public Courier(Order order, long currentMills) {
        this(order, currentMills, 2, 6);
    }

    public boolean isArrival(long currentMills) {
        return currentMills >= arrivalTimeMills;
    }

    public Order getOrder() {
        return order;
    }

    public long getArrivalTimeMills() {
        return arrivalTimeMills;
    }

    public boolean pickUpOrderFromShelves(List<Shelf> shelves) {

        for(Shelf shelf : shelves) {
            if(shelf.pickUpOrder(order)) {
                logger.debug("Order[{}] has been picked up from Shelf[{}]", order.getId(), shelf.getName());
               return true;
            }
        }
        return false;
    }

}
