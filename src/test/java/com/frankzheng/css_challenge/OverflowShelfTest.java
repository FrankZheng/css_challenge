package com.frankzheng.css_challenge;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OverflowShelfTest {
    @Test
    public void testCreation() {
        OverflowShelf overflowShelf = new OverflowShelf(10);
        assertEquals(OrderTemperature.any, overflowShelf.getAllowableTemp());
        assertEquals(10, overflowShelf.getCapacity());
        assertEquals(ShelfDecayModifier.overflowShelf, overflowShelf.getShelfDecayModifier());
    }

    @Test
    public void testHasRoomForOrder() {
        OverflowShelf overflowShelf = new OverflowShelf(2);
        Order order = new Order("Order Id", "Some food", OrderTemperature.cold, 100, 100.0f);
        assertTrue(overflowShelf.hasRoomForOrder(order));
        overflowShelf.placeOrder(order);
        Order hotOrder = new Order("Order Id", "Some food", OrderTemperature.hot, 100, 100.0f);
        assertTrue(overflowShelf.hasRoomForOrder(hotOrder));
        overflowShelf.placeOrder(hotOrder);
        Order frozenOrder = new Order("Order Id", "Some food", OrderTemperature.frozen, 100, 100.0f);
        assertFalse(overflowShelf.hasRoomForOrder(frozenOrder));
    }

    @Test
    public void testDiscardOrderRandomly() {
        OverflowShelf overflowShelf = new OverflowShelf(10);
        Order order = new Order("Order Id", "Some food", OrderTemperature.cold, 100, 100.0f);
        overflowShelf.placeOrder(order);
        Order order1 = overflowShelf.discardOrderRandomly();
        assertEquals(order, order1);
        assertTrue(overflowShelf.isEmpty());
    }

    @Test
    public void testMoveOrderToOtherShelves() {
        OverflowShelf overflowShelf = new OverflowShelf(10);
        Order order = new Order("Order Id", "Some food", OrderTemperature.cold, 100, 100.0f);
        overflowShelf.placeOrder(order);
        Shelf shelf1 = mock(Shelf.class);
        Shelf shelf2 = mock(Shelf.class);
        List<Shelf> shelfList = new ArrayList<>();
        shelfList.add(shelf1);
        shelfList.add(shelf2);

        assertFalse(overflowShelf.moveOrderToOtherShelves(shelfList));

        when(shelf2.hasRoomForOrder(order)).thenReturn(true);
        assertTrue(overflowShelf.moveOrderToOtherShelves(shelfList));
        verify(shelf2).placeOrder(order);
    }


}
