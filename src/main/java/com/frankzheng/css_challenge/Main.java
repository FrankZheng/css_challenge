package com.frankzheng.css_challenge;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //create shelves
        List<Shelf> shelves = new LinkedList<>();
        shelves.add(new Shelf("Hot shelf", OrderTemperature.hot, 10));
        shelves.add(new Shelf("Cold shelf", OrderTemperature.cold, 10));
        shelves.add(new Shelf("Frozen shelf", OrderTemperature.frozen, 10));
        OverflowShelf overflowShelf = new OverflowShelf(15);

        Kitchen kitchen = new Kitchen(shelves, overflowShelf);

        CourierManager courierManager = new CourierManager();
        courierManager.addCourierListener(kitchen);

        OrderIngestionWorker worker = new OrderIngestionWorker(2);
        worker.addOrderListener(kitchen);
        worker.addOrderListener(courierManager);

        List<Thread> threads = new LinkedList<>();
        threads.add(new Thread(worker));
        threads.add(new Thread(courierManager));

        //start all sub threads
        for(Thread thread : threads) {
            thread.start();
        }

        //wait for all sub threads finished
        try {
            for(Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
