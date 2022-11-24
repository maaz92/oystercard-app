package com.citystoragesystems.service;

import com.citystoragesystems.entity.ColdShelf;
import com.citystoragesystems.entity.FrozenShelf;
import com.citystoragesystems.entity.HotShelf;
import com.citystoragesystems.entity.Order;
import com.citystoragesystems.entity.OverflowShelf;
import com.citystoragesystems.entity.Shelf;
import com.citystoragesystems.entity.Temperature;
import com.citystoragesystems.event.OrderCookedEvent;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;

public class KitchenServiceTest {

    @Test
    public void testCookAndDispatch_HappyCase() {
        Shelf coldShelf = (Shelf) new ColdShelf();
        Shelf frozenShelf = (Shelf) new FrozenShelf();
        Shelf hotShelf = (Shelf) new HotShelf();
        Shelf overflowShelf = (Shelf) new OverflowShelf();
        List<Shelf> shelves = Arrays.asList(coldShelf,frozenShelf,hotShelf,overflowShelf);
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        KitchenService kitchenService = new KitchenService(applicationEventPublisher, shelves);
        Map<Order, Shelf> orderLookup = new HashMap<>();
        kitchenService.setOrderLookup(orderLookup);
        kitchenService.cook(order);
        Assert.assertEquals(1, coldShelf.getOrders().size());
        Assert.assertEquals(order, coldShelf.getOrders().get(0));
        Assert.assertEquals(coldShelf, orderLookup.get(order));
        Mockito.verify(applicationEventPublisher, Mockito.times(1)).publishEvent(Mockito.any(OrderCookedEvent.class));
        kitchenService.dispatch(order);
        Assert.assertEquals(0, coldShelf.getOrders().size());
        Assert.assertNull(orderLookup.get(order));
    }

    @Test
    public void testCookAndDispatch_ShelfFull() {
        Shelf coldShelf = (Shelf) new ColdShelf();
        Shelf frozenShelf = (Shelf) new FrozenShelf();
        Shelf hotShelf = (Shelf) new HotShelf();
        Shelf overflowShelf = (Shelf) new OverflowShelf();
        List<Shelf> shelves = Arrays.asList(coldShelf,frozenShelf,hotShelf,overflowShelf);
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        KitchenService kitchenService = new KitchenService(applicationEventPublisher, shelves);
        Map<Order, Shelf> orderLookup = new HashMap<>();
        kitchenService.setOrderLookup(orderLookup);
        Order order=null;
        for(int i = 0; i < 11; i++ ) {
            order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
            kitchenService.cook(order);
        }
        Assert.assertNotNull(order);
        Assert.assertEquals(10, coldShelf.getOrders().size());
        Assert.assertEquals(1, overflowShelf.getOrders().size());
        Assert.assertEquals(order, overflowShelf.getOrders().get(0));
        Assert.assertEquals(overflowShelf, orderLookup.get(order));
        Mockito.verify(applicationEventPublisher, Mockito.times(11)).publishEvent(Mockito.any(OrderCookedEvent.class));
        kitchenService.dispatch(order);
        Assert.assertEquals(0, overflowShelf.getOrders().size());
        Assert.assertNull(orderLookup.get(order));
    }

    @Test
    public void testCookAndMove_ShelfFull_OverflowShelfFull_BestCandidate() {
        Shelf coldShelf = (Shelf) new ColdShelf();
        Shelf frozenShelf = (Shelf) new FrozenShelf();
        Shelf hotShelf = (Shelf) new HotShelf();
        Shelf overflowShelf = (Shelf) new OverflowShelf();
        List<Shelf> shelves = Arrays.asList(coldShelf,frozenShelf,hotShelf,overflowShelf);
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        KitchenService kitchenService = new KitchenService(applicationEventPublisher, shelves);
        Map<Order, Shelf> orderLookup = new HashMap<>();
        kitchenService.setOrderLookup(orderLookup);
        for(int i = 0; i < 10; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
            kitchenService.cook(order);
        }
        List<Order> overflowShelfOrders = new ArrayList<>();
        for(int i = 0; i < 15; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.hot, 250L, 0.25);
            order.setTimeCookedAt(System.currentTimeMillis());
            overflowShelf.getOrders().add(order);
            orderLookup.put(order,overflowShelf);
            overflowShelfOrders.add(order);
        }

        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        kitchenService.cook(order);

        Assert.assertEquals(1, hotShelf.getOrders().size());
        Assert.assertEquals(overflowShelfOrders.get(0), hotShelf.getOrders().get(0));
        Assert.assertEquals(hotShelf,orderLookup.get(overflowShelfOrders.get(0)));
        Assert.assertEquals(15, overflowShelf.getOrders().size());
        Assert.assertEquals(order, overflowShelf.getOrders().get(14));
        Assert.assertEquals(overflowShelf, orderLookup.get(order));
    }

    @Test
    public void testCookAndDiscardFromOverflowShelf_ShelfFull_OverflowShelfFull_FourthBestCandidate() {
        Shelf coldShelf = (Shelf) new ColdShelf();
        Shelf frozenShelf = (Shelf) new FrozenShelf();
        Shelf hotShelf = (Shelf) new HotShelf();
        Shelf overflowShelf = (Shelf) new OverflowShelf();
        List<Shelf> shelves = Arrays.asList(coldShelf,frozenShelf,hotShelf,overflowShelf);
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        KitchenService kitchenService = new KitchenService(applicationEventPublisher, shelves);
        Map<Order, Shelf> orderLookup = new HashMap<>();
        kitchenService.setOrderLookup(orderLookup);
        for(int i = 0; i < 10; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
            kitchenService.cook(order);
        }
        List<Order> overflowShelfOrders = new ArrayList<>();
        for(int i = 0; i < 15; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
            order.setTimeCookedAt(System.currentTimeMillis());
            overflowShelf.getOrders().add(order);
            orderLookup.put(order,overflowShelf);
            overflowShelfOrders.add(order);
        }
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        kitchenService.cook(order);
        Assert.assertEquals(15, overflowShelf.getOrders().size());
        Assert.assertEquals(order, overflowShelf.getOrders().get(14));
        Assert.assertEquals(overflowShelf, orderLookup.get(order));
    }

    @Test
    public void testCookAndDiscardFromOverflowShelf_ShelfFull_OverflowShelfFull_SecondBestCandidate() {
        Shelf coldShelf = (Shelf) new ColdShelf();
        Shelf frozenShelf = (Shelf) new FrozenShelf();
        Shelf hotShelf = (Shelf) new HotShelf();
        Shelf overflowShelf = (Shelf) new OverflowShelf();
        List<Shelf> shelves = Arrays.asList(coldShelf,frozenShelf,hotShelf,overflowShelf);
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        KitchenService kitchenService = new KitchenService(applicationEventPublisher, shelves);
        Map<Order, Shelf> orderLookup = new HashMap<>();
        kitchenService.setOrderLookup(orderLookup);
        for(int i = 0; i < 10; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
            kitchenService.cook(order);
        }
        List<Order> overflowShelfOrders = new ArrayList<>();
        for(int i = 0; i < 15; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.hot, 8L, 0.25);
            order.setTimeCookedAt(System.currentTimeMillis());
            try {
                Thread.sleep(100);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            overflowShelf.getOrders().add(order);
            orderLookup.put(order,overflowShelf);
            overflowShelfOrders.add(order);
        }
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        kitchenService.cook(order);
        Assert.assertEquals(1, hotShelf.getOrders().size());
        Assert.assertEquals(overflowShelfOrders.get(0), hotShelf.getOrders().get(0));
        Assert.assertEquals(15, overflowShelf.getOrders().size());
        Assert.assertEquals(order, overflowShelf.getOrders().get(14));
        Assert.assertEquals(overflowShelf, orderLookup.get(order));
    }

    @Test
    public void testCookAndDiscardFromOverflowShelf_ShelfFull_OverflowShelfFull_ThirdBestCandidate() {
        Shelf coldShelf = (Shelf) new ColdShelf();
        Shelf frozenShelf = (Shelf) new FrozenShelf();
        Shelf hotShelf = (Shelf) new HotShelf();
        Shelf overflowShelf = (Shelf) new OverflowShelf();
        List<Shelf> shelves = Arrays.asList(coldShelf,frozenShelf,hotShelf,overflowShelf);
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        KitchenService kitchenService = new KitchenService(applicationEventPublisher, shelves);
        Map<Order, Shelf> orderLookup = new HashMap<>();
        kitchenService.setOrderLookup(orderLookup);
        for(int i = 0; i < 10; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
            kitchenService.cook(order);
        }
        List<Order> overflowShelfOrders = new ArrayList<>();
        for(int i = 0; i < 15; i++ ) {
            Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.hot, 7L, 0.25);
            order.setTimeCookedAt(System.currentTimeMillis());
            try {
                Thread.sleep(100);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            overflowShelf.getOrders().add(order);
            orderLookup.put(order,overflowShelf);
            overflowShelfOrders.add(order);
        }
        Order order = new Order(UUID.randomUUID(), "Test Order", Temperature.cold, 250L, 0.25);
        kitchenService.cook(order);
        Assert.assertEquals(1, hotShelf.getOrders().size());
        Assert.assertEquals(overflowShelfOrders.get(14), hotShelf.getOrders().get(0));
        Assert.assertEquals(15, overflowShelf.getOrders().size());
        Assert.assertEquals(order, overflowShelf.getOrders().get(14));
        Assert.assertEquals(overflowShelf, orderLookup.get(order));
    }
}