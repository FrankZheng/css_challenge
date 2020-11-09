package com.frankzheng.css_challenge;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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

    @Test(expected = IllegalArgumentException.class)
    public void testPlaceOrderWrongTemperature() {
        Shelf shelf = new Shelf("My Shelf", OrderTemperature.cold, 10);
        Order order = mock(Order.class);
        when(order.getTemperature()).thenReturn(OrderTemperature.hot);
        shelf.placeOrder(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlaceOrderFull() {
        Shelf shelf = new Shelf("My Shelf", OrderTemperature.cold, 1);
        Order order = mock(Order.class);
        when(order.getId()).thenReturn("mock order id");
        when(order.getTemperature()).thenReturn(OrderTemperature.cold);
        shelf.placeOrder(order);
        Order order1 = mock(Order.class);
        when(order1.getTemperature()).thenReturn(OrderTemperature.cold);
        shelf.placeOrder(order1);
    }

    @Test
    public void testPlaceOrder() {
        Shelf shelf = new Shelf("My Shelf", OrderTemperature.cold, 1);
        Order order = mock(Order.class);
        when(order.getId()).thenReturn("mock order id");
        when(order.getTemperature()).thenReturn(OrderTemperature.cold);
        shelf.orders = mock(ArrayList.class);
        shelf.placeOrder(order);
        verify(order).setShelfDecayModifier(ShelfDecayModifier.singleTempShelf);
        verify(shelf.orders).add(order);
    }

    @Test
    public void testPickUpOrder() {
        Shelf shelf = new Shelf("My Shelf", OrderTemperature.cold, 1);
        Order order = mock(Order.class);
        String orderId = "mock order id";
        when(order.getId()).thenReturn(orderId);
        when(order.getTemperature()).thenReturn(OrderTemperature.cold);
        shelf.placeOrder(order);
        assertTrue(shelf.pickUpOrder(order));

        when(order.getId()).thenReturn("another order id");
        assertFalse(shelf.pickUpOrder(order));
    }

    @Test
    public void testDropWastedOrder() {
        Shelf shelf = new Shelf("My Shelf", OrderTemperature.cold, 10);
        Order order1 = mock(Order.class);
        String orderId = "mock order id";
        when(order1.getId()).thenReturn(orderId);
        when(order1.getTemperature()).thenReturn(OrderTemperature.cold);
        when(order1.isWasted()).thenReturn(false);

        Order order2 = mock(Order.class);
        when(order2.getId()).thenReturn("wasted order 1");
        when(order2.getTemperature()).thenReturn(OrderTemperature.cold);
        when(order2.isWasted()).thenReturn(true);

        Order order3 = mock(Order.class);
        when(order3.getId()).thenReturn("wasted order 2");
        when(order3.getTemperature()).thenReturn(OrderTemperature.cold);
        when(order3.isWasted()).thenReturn(true);

        shelf.placeOrder(order1);
        shelf.placeOrder(order2);
        shelf.placeOrder(order3);

        shelf.dropWastedOrder();
        assertEquals(1, shelf.orders.size());
    }






}

