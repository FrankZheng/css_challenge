package com.frankzheng.css_challenge;

import org.junit.Test;
import static org.junit.Assert.*;


public class OrderTest {
    @Test
    public void testOrderCreation() {
        String orderId = "MyOrderId";
        String orderName = "MyOrder";
        OrderTemperature orderTemperature = OrderTemperature.cold;
        int shelfLife = 100;
        double decayRate = 0.45;
        long time = System.currentTimeMillis();
        Order order = new Order(orderId, orderName, orderTemperature, shelfLife, decayRate, time);
        assertEquals(orderId, order.getId());
        assertEquals(orderName, order.getName());
        assertEquals(orderTemperature, order.getTemperature());
        assertEquals(shelfLife, order.getShelfLife());
        assertEquals(decayRate, order.getDecayRate(), 0);
        assertEquals(time, order.getOrderCreationTime());
        assertEquals(ShelfDecayModifier.notPlacedToShelf, order.getShelfDecayModifier());
    }

    @Test
    public void testValue() {
        String orderId = "MyOrderId";
        String orderName = "MyOrder";
        OrderTemperature orderTemperature = OrderTemperature.cold;
        int shelfLife = 100;
        double decayRate = 0.45;
        long time = System.currentTimeMillis();
        Order order = new Order(orderId, orderName, orderTemperature, shelfLife, decayRate, time);
        order.setShelfDecayModifier(ShelfDecayModifier.singleTempShelf);
        //long orderAge = (currTimeMills - orderCreationTime) / 1000;
        //return (shelfLife - orderAge - decayRate * orderAge * shelfDecayModifier.getValue()) / (shelfLife * 1.0);
        long currTimeMills = time + 1000 * 5;
        long orderAge = ( currTimeMills - time) / 1000;
        double value = (shelfLife - orderAge - decayRate * orderAge * order.getShelfDecayModifier().getValue()) / (shelfLife * 1.0);
        double actualValue = order.getValue(currTimeMills);
        //System.out.println(actualValue);
        assertEquals(value, actualValue, 0);;
        assertFalse(order.isWasted(currTimeMills));
        currTimeMills = time + shelfLife*1000 + 5000;
        assertTrue(order.isWasted(currTimeMills));
    }
}
