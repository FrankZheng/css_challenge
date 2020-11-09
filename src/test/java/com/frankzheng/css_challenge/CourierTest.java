package com.frankzheng.css_challenge;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;


public class CourierTest {
    @Test
    public void testCourierCreation() {
        Order order = mock(Order.class);
        long currentTimeMills = 0;
        int min = 2;
        int max = 6;
        Courier courier = new Courier(order, currentTimeMills, 2, 6);
        assertEquals(order, courier.getOrder());
        assertTrue("arrival time should be >= min", courier.getArrivalTimeMills() >= currentTimeMills + min * 1000);
        assertTrue("arrival time should be <= max", courier.getArrivalTimeMills() <= currentTimeMills + max * 1000);
    }

    @Test
    public void testIsArrival() {
        Order order = mock(Order.class);
        long currentTimeMills = 0;
        int min = 2;
        int max = 6;
        Courier courier = new Courier(order, currentTimeMills, 2, 6);
        assertFalse(courier.isArrival(currentTimeMills + min * 1000 - 1));
        assertTrue(courier.isArrival(currentTimeMills + max * 1000 + 1));
    }

    @Test
    public void testPickUpOrderFromShelves() {
        Order order = mock(Order.class);
        Courier courier = new Courier(order, System.currentTimeMillis());
        Shelf shelf = mock(Shelf.class);
        List<Shelf> shelfList = new LinkedList<>();
        shelfList.add(shelf);
        when(shelf.pickUpOrder(order)).thenReturn(true);
        assertTrue(courier.pickUpOrderFromShelves(shelfList));

        when(shelf.pickUpOrder(order)).thenReturn(false);
        assertFalse(courier.pickUpOrderFromShelves(shelfList));
    }



}
