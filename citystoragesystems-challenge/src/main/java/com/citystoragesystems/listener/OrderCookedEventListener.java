package com.citystoragesystems.listener;

import com.citystoragesystems.event.OrderCookedEvent;
import com.citystoragesystems.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Listener for the OrderCookedEvent
 *
 *
 * @see     OrderCookedEvent
 * @author  Mohammad Maaz Khan
 */
@Component
public class OrderCookedEventListener implements ApplicationListener<OrderCookedEvent> {

    @Autowired
    private CourierService courierService;

    /**
     * Constructs an instance of OrderCookedEventListener that will listen to OrderCookedEvent
     *
     *
     * @param   courierService     Order
     * @see                        CourierService
     * @see                        OrderCookedEvent
     * @author                     Mohammad Maaz Khan
     */
    public OrderCookedEventListener(CourierService courierService) {
        this.courierService = courierService;
    }

    /**
     * Reacts to the event OrderCookedEvent
     *
     *
     * @param   orderCookedEvent   event which triggers this method call
     * @see                        OrderCookedEvent
     * @author                     Mohammad Maaz Khan
     */
    @Override
    public void onApplicationEvent(OrderCookedEvent orderCookedEvent) {
        this.courierService.schedulePickup(orderCookedEvent.getOrder());
    }
}
