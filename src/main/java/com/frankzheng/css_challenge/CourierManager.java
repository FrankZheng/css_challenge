package com.frankzheng.css_challenge;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CourierManager implements Runnable, OrderListener {

    private final List<Courier> courierList = new LinkedList<>();
    private final AtomicBoolean working = new AtomicBoolean(false);
    private final List<CourierListener> courierListeners = new LinkedList<>();


    synchronized public void addCourierListener(CourierListener listener) {
        courierListeners.add(listener);
    }

    synchronized public void removeCourierListener(CourierListener listener) {
        courierListeners.remove(listener);
    }

    @Override
    public void onNewOrder(Order order) {
        Courier courier = new Courier();
        courier.assignOrder(order, System.currentTimeMillis());

        synchronized (courierList) {
            courierList.add(courier);
        }
    }

    @Override
    public void onOrdersFinished() {
        working.set(false);
    }

    @Override
    public void run() {

        working.set(true);

        while(working.get() || !courierList.isEmpty()) {
            List<Courier> couriersToRemove = new LinkedList<>();

            synchronized (courierList) {
                for(Courier courier : courierList) {
                    if(courier.isArrival(System.currentTimeMillis())) {
                        couriersToRemove.add(courier);
                        for(CourierListener courierListener : courierListeners) {
                            courierListener.onCourierArrival(courier);
                        }
                    }
                }
            }

            if(!couriersToRemove.isEmpty()) {
                synchronized (courierList) {
                    courierList.removeAll(couriersToRemove);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
