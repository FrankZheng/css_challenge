package com.frankzheng.css_challenge;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShelfTest {

    @Test
    public void testShelfCreation() {
        String name = "My Shelf";
        OrderTemperature temp = OrderTemperature.hot;
        int capacity = 10;

        Shelf shelf = new Shelf(name, temp, capacity);
        assertEquals(name, shelf.getName());
        assertEquals(temp, shelf.getAllowableTemp());
        assertEquals(capacity, shelf.getCapacity());
        assertTrue(shelf.getOrders().isEmpty());
    }

    @Test
    public void testHasRoomForOrder() {
        Shelf shelf = new Shelf("My Shelf", OrderTemperature.cold, 10);
        Order order = new Order("Order Id", "Some food", OrderTemperature.cold, 100, 100.0f);
        assertTrue(shelf.hasRoomForOrder(order));
        Order hotOrder = new Order("Order Id", "Some food", OrderTemperature.hot, 100, 100.0f);
        assertFalse(shelf.hasRoomForOrder(hotOrder));

        Shelf shelf1 = new Shelf("My Shelf", OrderTemperature.cold, 0);
        assertFalse(shelf1.hasRoomForOrder(order));
    }

}

