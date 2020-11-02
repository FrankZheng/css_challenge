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


    synchronized public void serveOrder(Order order) {
        //cook order


        //put order into shelf
        boolean orderPlaced = false;
        for(Shelf shelf : allShelves) {
            if(shelf.hasRoomForOrder(order)) {
                shelf.placeOrder(order);
                orderPlaced = true;
                break;
            }
        }

        if(!orderPlaced) {
            if(!overflowShelf.moveOrderToOtherShelves(shelves)) {
                Order discardedOrder = overflowShelf.discardOrderRandomly();
                logger.info("Order[{}] has been discarded from overflow shelf", discardedOrder.getId());
                overflowShelf.placeOrder(order);
            }
        }
    }


    @Override
    public void onNewOrder(Order order) {
        serveOrder(order);
    }

    @Override
    public void onOrdersFinished() {

    }

    @Override
    synchronized public void onCourierArrival(Courier courier) {

        boolean picked = courier.pickUpOrderFromShelves(allShelves);
        if(!picked) {
            //courier not picked order
            logger.info("Courier could not pick up any order");
        }
    }
}
