package com.frankzheng.css_challenge;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //create shelves
        List<Shelf> shelves = new LinkedList<>();
        shelves.add(new Shelf("Hot shelf", OrderTemperature.hot, 5));
        shelves.add(new Shelf("Cold shelf", OrderTemperature.cold, 5));
        shelves.add(new Shelf("Frozen shelf", OrderTemperature.frozen, 5));
        OverflowShelf overflowShelf = new OverflowShelf(15);

        Kitchen kitchen = new Kitchen(shelves, overflowShelf);

        CourierManager courierManager = new CourierManager();
        courierManager.addCourierListener(kitchen);

        OrderIngestionWorker worker = new OrderIngestionWorker();
        worker.addOrderListener(kitchen);
        worker.addOrderListener(courierManager);

        Thread thread1 = new Thread(worker);
        Thread thread2 = new Thread(courierManager);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
