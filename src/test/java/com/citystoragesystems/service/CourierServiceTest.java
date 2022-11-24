package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import com.citystoragesystems.entity.Temperature;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class CourierServiceTest {

    @Test
    public void testDeliver() {
        KitchenService kitchenService = Mockito.mock(KitchenService.class);
        CourierService courierService = new CourierService(kitchenService);
        courierService = Mockito.spy(courierService);
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        order.setTimeCookedAt(System.currentTimeMillis());
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = Mockito.mock(ScheduledThreadPoolExecutor.class);
        courierService.setScheduledThreadPoolExecutor(scheduledThreadPoolExecutor);
        courierService.schedulePickup(order);
        Mockito.verify(scheduledThreadPoolExecutor, Mockito.times(1)).schedule(Mockito.any(Runnable.class), Mockito.anyLong(), Mockito.any());
    }
}
