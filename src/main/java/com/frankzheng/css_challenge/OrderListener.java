package com.frankzheng.css_challenge;


public interface OrderListener {
    //new order incoming
    void onNewOrder(Order order);

    //no orders any more
    void onOrdersFinished();
}
