package com.citystoragesystems.event;

import com.citystoragesystems.entity.Order;
import org.springframework.context.ApplicationEvent;

/**
 * Event to notify that an order has been cooked
 *
 *
 * @see     Order
 * @author  Mohammad Maaz Khan
 */
public class OrderCookedEvent extends ApplicationEvent {

    private Order order;

    /**
     * Constructs an instance of OrderCookedEvent that can be fired after an order is cooked
     *
     *
     * @param   order  order which is cooked
     * @see            Order
     * @author         Mohammad Maaz Khan
     */
    public OrderCookedEvent(Order order) {
        super(order);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
