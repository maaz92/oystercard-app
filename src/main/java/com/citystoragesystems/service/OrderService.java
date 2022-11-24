package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Schedules the orders to be cooked
 * by KitchenService
 *
 * @see     Order
 * @author  Mohammad Maaz Khan
 */
@Service
public class OrderService {

    @Autowired
    private KitchenService kitchenService;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private static Logger LOG = LogManager.getLogger(OrderService.class);

    /**
     * Constructs an instance of OrderService
     *
     *
     * @param   kitchenService  The kitchen service
     * @see                     KitchenService
     * @author                  Mohammad Maaz Khan
     */
    public OrderService(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
        this.setScheduledThreadPoolExecutor((ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(0));
    }

    public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
    }

    /**
     * Task to fulfill the order
     *
     *
     * @see     KitchenService
     * @author  Mohammad Maaz Khan
     */
    private class OrderTask implements Runnable {

        private Order order;

        /**
         * Constructs the task to fulfill the order
         *
         *
         * @param   order  the order to be fulfilled
         * @see            Order
         * @author         Mohammad Maaz Khan
         */
        public OrderTask(Order order) {
            this.order = order;
        }

        /**
         * Sends the order to the kitchen
         *
         *
         * @see            KitchenService
         * @author         Mohammad Maaz Khan
         */
        @Override
        public void run() {
            kitchenService.cook(order);
        }
    }

    private void placeOrder(Order order, Long delayMilliSeconds) {
        this.scheduledThreadPoolExecutor.schedule(new OrderTask(order), delayMilliSeconds, TimeUnit.MILLISECONDS);
    }

    public void processOrders(List<Order> orders, Double ingestionRate) {
        if(orders == null) {
            return;
        }
        Double ordersPerSecond = 1/ingestionRate;
        LOG.info(ordersPerSecond);
        Double delayMilliSeconds = ordersPerSecond*TimeUnit.SECONDS.toMillis(1L);
        LOG.info(delayMilliSeconds);
        for(int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            this.placeOrder(order, i*delayMilliSeconds.longValue());
        }
    }
}
