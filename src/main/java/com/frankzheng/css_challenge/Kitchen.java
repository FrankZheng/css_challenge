package com.frankzheng.css_challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class Kitchen implements OrderListener, CourierListener {
    static Logger logger = LoggerFactory.getLogger(Kitchen.class);

    public Kitchen(List<Shelf> shelves, OverflowShelf overflowShelf) {
        this.overflowShelf = overflowShelf;
        this.shelves = shelves;
        allShelves.addAll(shelves);
        allShelves.add(overflowShelf);
    }

    final private List<Shelf> shelves;
    final private OverflowShelf overflowShelf;
    final private List<Shelf> allShelves = new LinkedList<>();


    @Override
    synchronized public void onNewOrder(Order order) {
        logger.info("Order received: {}", order.info());

        dropWastedOrderFromAllShelves();

        //put order into shelves
        boolean orderPlaced = false;
        for(Shelf shelf : allShelves) {
            if(shelf.hasRoomForOrder(order)) {
                shelf.placeOrder(order);
                orderPlaced = true;
                break;
            }
        }

        //if no shelf room for order
        //check if can get room on overflow shelf
        if(!orderPlaced) {
            if(!overflowShelf.moveOrderToOtherShelves(shelves)) {
                Order discardedOrder = overflowShelf.discardOrderRandomly();
                logger.info("Order:{} has been discarded from overflow shelf", discardedOrder.info());
                overflowShelf.placeOrder(order);
            }
        }

        printShelvesContent();
    }

    @Override
    public void onOrdersFinished() {

    }

    @Override
    synchronized public void onCourierArrival(Courier courier) {
        logger.info("Courier arrived, for order:{}", courier.getOrder().info());

        dropWastedOrderFromAllShelves();

        boolean picked = courier.pickUpOrderFromShelves(allShelves);
        if(!picked) {
            //courier not picked order
            logger.info("Courier did not pick up any order");
        } else {
            logger.info("Order picked up");
            printShelvesContent();
        }
    }

    void dropWastedOrderFromAllShelves() {
        for(Shelf shelf : allShelves) {
            shelf.dropWastedOrder();
        }
    }

    void printShelvesContent() {
        for(Shelf shelf : allShelves) {
            logger.info("{} has {} orders", shelf.getName(), shelf.orders.size());
            for(Order order : shelf.orders) {
                logger.info(order.info());
            }
        }
    }

}
