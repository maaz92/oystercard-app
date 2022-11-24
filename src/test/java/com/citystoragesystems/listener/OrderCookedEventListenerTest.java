package com.citystoragesystems.listener;

import com.citystoragesystems.entity.Order;
import com.citystoragesystems.entity.Temperature;
import com.citystoragesystems.event.OrderCookedEvent;
import com.citystoragesystems.service.CourierService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class OrderCookedEventListenerTest {

    @Test
    public void testOnApplicationEvent() {
        CourierService courierService = Mockito.mock(CourierService.class);
        OrderCookedEventListener orderCookedEventListener = new OrderCookedEventListener(courierService);
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        OrderCookedEvent orderCookedEvent = new OrderCookedEvent(order);
        orderCookedEventListener.onApplicationEvent(orderCookedEvent);
        Mockito.verify(courierService, Mockito.times(1)).schedulePickup(orderCookedEvent.getOrder());
    }
}
