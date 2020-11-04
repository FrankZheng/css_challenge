package com.frankzheng.css_challenge;

import java.util.List;

public class OverflowShelf extends Shelf {

    public OverflowShelf(int capacity) {
        super("Overflow shelf", OrderTemperature.any, capacity, ShelfDecayModifier.overflowShelf);
    }

    @Override
    public boolean hasRoomForOrder(Order order) {
        return orders.size() < getCapacity();
    }

    public boolean moveOrderToOtherShelves(List<Shelf> shelfList) {
        for(Order order : orders) {
            for(Shelf shelf: shelfList) {
                if(shelf.hasRoomForOrder(order)) {
                    shelf.placeOrder(order);
                    //TODO: use remove(index) instead
                    orders.remove(order);
                    return true;
                }
            }
        }
        return false;
    }

    public Order discardOrderRandomly() {
        if(!orders.isEmpty()) {
            int idx = RandomUtils.randInt(0, orders.size()-1);
            Order order = orders.get(idx);
            orders.remove(idx);
            return order;
        }
        return null;
    }


}
