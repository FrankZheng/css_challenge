package com.frankzheng.css_challenge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OrderIngestionWorker implements Runnable {
    static Logger logger = LoggerFactory.getLogger(OrderIngestionWorker.class);

    final private List<OrderListener> orderListeners = new LinkedList<>();
    final int ingestRate;

    public OrderIngestionWorker(int ingestRate) {
        if(ingestRate <= 0) {
            throw new IllegalArgumentException("invalid feedRate parameter");
        }
        this.ingestRate = ingestRate;

    }

    public void addOrderListener(OrderListener listener) {
        orderListeners.add(listener);
    }

    public void removeOrderListener(OrderListener listener) {
        orderListeners.remove(listener);
    }

    List<Order> parseOrdersFromJsonString(String jsonStr) {
        JSONArray jsonArray = new JSONArray(jsonStr);
        List<Order> orderList = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            try {
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String temp = jsonObject.getString("temp");
                int shelfLife = jsonObject.getInt("shelfLife");
                double decayRate = jsonObject.getFloat("decayRate");
                OrderTemperature temperature = parseOrderTemperature(temp);
                if(temperature == null) {
                    logger.warn("Invalid temperature for order, {}, {}", id, temp);
                    continue;
                }
                Order order = new Order(id, name, temperature, shelfLife, decayRate);
                orderList.add(order);
            } catch (JSONException e) {
                logger.error("Failed to parse orders from json file", e);
            }
        }
        return orderList;
    }

    OrderTemperature parseOrderTemperature(String temp) {
        if("hot".equals(temp)) {
            return OrderTemperature.hot;
        } else if("cold".equals(temp)) {
            return OrderTemperature.cold;
        } else if("frozen".equals(temp)) {
            return OrderTemperature.frozen;
        }
        return null;
    }

    @Override
    public void run() {
        logger.debug("start to run...");
        //read file and parse json, feed order with a fixed rate
        String jsonStr = null;
        try {
            String orderJsonFileName = "/orders.json";
            InputStream inputStream = this.getClass().getResourceAsStream(orderJsonFileName);
            if (inputStream == null) {
                logger.error("orders json file not found.");
            } else {
                StringBuilder buffer = new StringBuilder();
                FileUtils.readToBuffer(inputStream, buffer);
                jsonStr = buffer.toString();
            }

        } catch (IOException ex) {
            logger.error("Failed to read data from orders file",  ex);
            return;
        }

        if(jsonStr != null && !jsonStr.isEmpty()) {
            List<Order> orders = parseOrdersFromJsonString(jsonStr);
            for(Order order : orders) {
                for(OrderListener listener : orderListeners) {
                    listener.onNewOrder(order);
                }

                try {
                    Thread.sleep(1000 / ingestRate);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        for(OrderListener listener : orderListeners) {
            listener.onOrdersFinished();
        }

        logger.debug("done");
    }
}
