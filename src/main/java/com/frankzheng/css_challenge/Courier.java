package com.frankzheng.css_challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Courier {
    static Logger logger = LoggerFactory.getLogger(Courier.class);

    private final Order order;
    private long arrivalTimeMills = 0;

    public Courier(Order order, long currentMills) {
        this.order = order;
        int sec = RandomUtils.randInt(2, 6);
        logger.debug("Order[{}] has been assigned to a courier, will pick up order {} seconds later",
                order.getId(), sec);
        arrivalTimeMills = currentMills + sec * 1000;
    }

    public boolean isArrival(long currentMills) {
        return currentMills >= arrivalTimeMills;
    }

    public Order getOrder() {
        return order;
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

    public void delivery() {
        logger.debug("Order[{}] has been delivered", order.getId());
    }




}
