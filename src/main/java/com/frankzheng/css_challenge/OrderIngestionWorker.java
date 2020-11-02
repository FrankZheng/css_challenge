package com.frankzheng.css_challenge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OrderIngestionWorker implements Runnable {
    final private List<OrderListener> orderListeners = new LinkedList<>();

    public OrderIngestionWorker() {

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
                float decayRate = jsonObject.getFloat("decayRate");
                OrderTemperature temperature = parseOrderTemperature(temp);
                if(temperature == null) {
                    System.out.println("Invalid temperature for order, " + id);
                    continue;
                }
                Order order = new Order(id, name, temperature, shelfLife, decayRate);
                orderList.add(order);
            } catch (JSONException e) {
                //TODO: add logging later
                System.out.println("Failed to parse order, " + e.getLocalizedMessage());
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
        //read file and parse json, feed order with a fixed rate
        String jsonStr = null;
        try {
            String orderJsonFileName = "/orders.json";
            InputStream inputStream = this.getClass().getResourceAsStream(orderJsonFileName);
            if (inputStream == null) {
                System.out.println("orders json file not found, ");
            }
            StringBuilder buffer = new StringBuilder();
            FileUtils.readToBuffer(inputStream, buffer);
            jsonStr = buffer.toString();

        } catch (IOException ex) {
            System.out.println("Failed to read orders file, " + ex.getLocalizedMessage());
            return;
        }

        if(!jsonStr.isEmpty()) {
            List<Order> orders = parseOrdersFromJsonString(jsonStr);
            for(int i = 0 ; i < orders.size(); i++) {
                Order order = orders.get(i);
                for(OrderListener listener : orderListeners) {
                    listener.onNewOrder(order);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }

        for(OrderListener listener : orderListeners) {
            listener.onOrdersFinished();
        }
    }
}
