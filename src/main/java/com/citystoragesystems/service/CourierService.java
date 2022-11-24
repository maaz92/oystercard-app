package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Schedules the delivery persons to pickup and deliver the order
 *
 *
 * @see     KitchenService
 * @author  Mohammad Maaz Khan
 */
@Service
public class CourierService {

    private static final double MINIMUM_DELAY_MILLISECONDS=2000;

    private static final double MAXIMUM_DELAY_MILLISECONDS=6000;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    protected KitchenService kitchenService;

    /**
     * Constructs an instance of CourierService that will
     * schedule the delivery persons to pickup and deliver the order
     *
     *
     * @param   kitchenService  the kitchen service
     * @see                        KitchenService
     * @author                     Mohammad Maaz Khan
     */
    public CourierService(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
        this.setScheduledThreadPoolExecutor((ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(0));
    }

    public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
    }

    private Long pickupDelay() {
        Double delay = Math.random()*(MAXIMUM_DELAY_MILLISECONDS-MINIMUM_DELAY_MILLISECONDS)+MINIMUM_DELAY_MILLISECONDS;
        return delay.longValue();
    }

    /**
     * Schedules a delivery person to pickup and deliver the order
     *
     *
     * @param   order  the order
     * @see            Order
     * @author         Mohammad Maaz Khan
     */
    public void schedulePickup(Order order) {
        this.scheduledThreadPoolExecutor.schedule(new CourierTask(order), pickupDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Task to send the delivery person
     *
     *
     * @see     KitchenService
     * @author  Mohammad Maaz Khan
     */
    private class CourierTask implements Runnable {

        private Order order;

        /**
         * Constructs the task to send the delivery person
         *
         *
         * @param   order  the order to be picked up and delivered
         * @see            Order
         * @author         Mohammad Maaz Khan
         */
        public CourierTask(Order order) {
            this.order = order;
        }

        /**
         * Sends the delivery person to pickup and deliver the order
         *
         *
         * @see            KitchenService
         * @author         Mohammad Maaz Khan
         */
        @Override
        public void run() {
            kitchenService.dispatch(order);
        }
    }

}
