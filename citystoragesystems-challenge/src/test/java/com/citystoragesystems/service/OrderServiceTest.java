package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import com.citystoragesystems.entity.Temperature;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OrderServiceTest {

    @Test
    public void testParseValidFormatFileJSON() throws IOException {
        KitchenService kitchenService = Mockito.mock(KitchenService.class);
        OrderService orderService = new OrderService(kitchenService);
        Assert.assertNotNull(orderService);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = Mockito.mock(ScheduledThreadPoolExecutor.class);
        orderService.setScheduledThreadPoolExecutor(scheduledThreadPoolExecutor);
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        orderService.processOrders(Arrays.asList(order), 2.0);
        Mockito.verify(scheduledThreadPoolExecutor, Mockito.times(1)).schedule(Mockito.any(Runnable.class), Mockito.eq(0L), Mockito.eq(TimeUnit.MILLISECONDS));
    }
}
