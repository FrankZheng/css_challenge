package com.frankzheng.css_challenge;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KitchenTest {
    @Test
    public void testKitchenCreation() {
        Shelf shelf1 = mock(Shelf.class);
        Shelf shelf2 = mock(Shelf.class);
        Shelf shelf3 = mock(Shelf.class);
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        List<Shelf> shelves = new LinkedList<>();
        shelves.add(shelf1);
        shelves.add(shelf2);
        shelves.add(shelf3);
        List<Shelf> allShelves = new LinkedList<>(shelves);
        allShelves.add(overflowShelf);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        assertEquals(shelves, kitchen.getShelves());
        assertEquals(overflowShelf, kitchen.getOverflowShelf());
        assertEquals(allShelves, kitchen.getAllShelves());
    }

    @Test
    public void testDropWastedOrderFromAllShelves() {
        Shelf shelf1 = mock(Shelf.class);
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        List<Shelf> shelves = Collections.singletonList(shelf1);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        kitchen.dropWastedOrderFromAllShelves();
        verify(shelf1).dropWastedOrder();
        verify(overflowShelf).dropWastedOrder();
    }

    @Test
    public void testOnCourierArrival() {
        Shelf shelf1 = mock(Shelf.class);
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        List<Shelf> shelves = Collections.singletonList(shelf1);
        List<Shelf> allShelves = new LinkedList<>(shelves);
        allShelves.add(overflowShelf);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        Kitchen spyKitchen = spy(kitchen);
        Courier courier = mock(Courier.class);
        when(courier.getOrder()).thenReturn(mock(Order.class));
        spyKitchen.onCourierArrival(courier);
        verify(spyKitchen).dropWastedOrderFromAllShelves();
        verify(courier).pickUpOrderFromShelves(eq(allShelves));
    }

    @Test
    public void testOnNewOrder_orderInTempMatchedShelf() {
        Shelf shelf1 = mock(Shelf.class);
        when(shelf1.getOrders()).thenReturn(Collections.<Order>emptyList());
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        when(overflowShelf.getOrders()).thenReturn(Collections.<Order>emptyList());

        List<Shelf> shelves = Collections.singletonList(shelf1);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        Kitchen spyKitchen = spy(kitchen);
        Order order = mock(Order.class);

        when(shelf1.hasRoomForOrder(order)).thenReturn(true);
        spyKitchen.onNewOrder(order);

        verify(spyKitchen).dropWastedOrderFromAllShelves();
        verify(shelf1).hasRoomForOrder(order);
        verify(shelf1).placeOrder(order);

        verify(overflowShelf, never()).hasRoomForOrder(order);
        verify(overflowShelf, never()).placeOrder(order);

        verify(overflowShelf, never()).moveOrderToOtherShelves(shelves);
    }

    @Test
    public void testOnNewOrder_orderInOverflowShelfFreeRoom() {
        Shelf shelf1 = mock(Shelf.class);
        when(shelf1.getOrders()).thenReturn(Collections.<Order>emptyList());
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        when(overflowShelf.getOrders()).thenReturn(Collections.<Order>emptyList());

        List<Shelf> shelves = Collections.singletonList(shelf1);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        Kitchen spyKitchen = spy(kitchen);
        Order order = mock(Order.class);

        when(overflowShelf.hasRoomForOrder(order)).thenReturn(true);
        spyKitchen.onNewOrder(order);

        verify(spyKitchen).dropWastedOrderFromAllShelves();

        verify(shelf1).hasRoomForOrder(order);
        verify(shelf1, never()).placeOrder(order);

        verify(overflowShelf).hasRoomForOrder(order);
        verify(overflowShelf).placeOrder(order);

        verify(overflowShelf, never()).moveOrderToOtherShelves(shelves);
    }

    @Test
    public void testOnNewOrder_moveOrderFromOverflowShelfToOthers() {
        Shelf shelf1 = mock(Shelf.class);
        when(shelf1.getOrders()).thenReturn(Collections.<Order>emptyList());
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        when(overflowShelf.getOrders()).thenReturn(Collections.<Order>emptyList());

        List<Shelf> shelves = Collections.singletonList(shelf1);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        Kitchen spyKitchen = spy(kitchen);
        Order order = mock(Order.class);

        when(overflowShelf.moveOrderToOtherShelves(eq(shelves))).thenReturn(true);
        spyKitchen.onNewOrder(order);

        verify(spyKitchen).dropWastedOrderFromAllShelves();


        verify(shelf1).hasRoomForOrder(order);
        verify(shelf1, never()).placeOrder(order);

        verify(overflowShelf).hasRoomForOrder(order);
        verify(overflowShelf).moveOrderToOtherShelves(shelves);
        verify(overflowShelf).placeOrder(order);
    }

    @Test
    public void testOnNewOrder_discardOrderFromOverflowShelfRandomly() {
        Shelf shelf1 = mock(Shelf.class);
        when(shelf1.getOrders()).thenReturn(Collections.<Order>emptyList());
        OverflowShelf overflowShelf = mock(OverflowShelf.class);
        when(overflowShelf.getOrders()).thenReturn(Collections.<Order>emptyList());

        List<Shelf> shelves = Collections.singletonList(shelf1);
        Kitchen kitchen = new Kitchen(shelves, overflowShelf);
        Kitchen spyKitchen = spy(kitchen);
        Order order = mock(Order.class);

        when(overflowShelf.discardOrderRandomly()).thenReturn(mock(Order.class));
        spyKitchen.onNewOrder(order);

        verify(spyKitchen).dropWastedOrderFromAllShelves();


        verify(shelf1).hasRoomForOrder(order);
        verify(shelf1, never()).placeOrder(order);

        verify(overflowShelf).hasRoomForOrder(order);
        verify(overflowShelf).moveOrderToOtherShelves(shelves);
        verify(overflowShelf).discardOrderRandomly();

        verify(overflowShelf).placeOrder(order);
    }




}
