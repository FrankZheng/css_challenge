package com.frankzheng.css_challenge;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CourierDispatcher implements Runnable, OrderListener {
    static Logger logger = LoggerFactory.getLogger(CourierDispatcher.class);

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
        Courier courier = new Courier(order, System.currentTimeMillis());

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
        logger.debug("start to run...");

        working.set(true);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                synchronized (courierList) {
                    //if no order incoming and all dispatched couriers arrived
                    if(!working.get() && courierList.isEmpty()) {
                        timer.cancel();
                        logger.info("stopped");
                    } else {
                        Iterator<Courier> iterator = courierList.iterator();
                        while (iterator.hasNext()) {
                            Courier courier = iterator.next();
                            if(courier.isArrival(System.currentTimeMillis())) {
                                //notify listeners
                                for(CourierListener courierListener : courierListeners) {
                                    courierListener.onCourierArrival(courier);
                                }
                                //remove courier from courier list
                                iterator.remove();
                            }
                        }
                    }
                }

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 200);
    }
}
