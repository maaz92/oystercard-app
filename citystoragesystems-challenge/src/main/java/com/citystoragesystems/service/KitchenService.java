package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import com.citystoragesystems.entity.Shelf;
import com.citystoragesystems.entity.Temperature;
import com.citystoragesystems.event.OrderCookedEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cooks, stores, dispatches and discards orders.
 *
 *
 * @see     Shelf
 * @see     Order
 * @author  Mohammad Maaz Khan
 */
@Service
public class KitchenService {

    @Autowired
    private List<Shelf> shelves;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private Map<Order, Shelf> orderLookup;

    private Set<Order> cookedOrders;

    private Shelf overflowShelf;

    private static final Long MAX_PICKUP_TIME = 6000L;

    private static final Long MIN_PICKUP_TIME = 2000L;

    private final ReadWriteLock shelvesLock = new ReentrantReadWriteLock();

    private static Logger LOG = LogManager.getLogger(KitchenService.class);

    /**
     * Constructs an instance of KitchenService
     *
     *
     * @param   applicationEventPublisher  The applicationEventPublisher
     * @param   shelves                    list of shelves
     * @see                                ApplicationEventPublisher
     * @see                                Shelf
     * @author                             Mohammad Maaz Khan
     */
    public KitchenService(ApplicationEventPublisher applicationEventPublisher, List<Shelf> shelves) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.shelves = shelves;
        this.overflowShelf = shelves.stream().
                filter(shelf -> {
                    return shelf.getAllowableTemperatures().stream()
                            .filter(temperature -> Temperature.any.equals(temperature))
                            .count() > 0;
                })
                .findFirst().get();
        setOrderLookup(new HashMap<>());
        this.cookedOrders = new HashSet<>();
    }

    protected void setOrderLookup(Map<Order, Shelf> orderLookup) {
        this.orderLookup = orderLookup;
    }

    /**
     * Cooks and places the order in the appropriate shelf
     * and shuffles them if needed
     *
     *
     * @param   order  the order to be cooked
     * @see            Order
     * @author         Mohammad Maaz Khan
     */
    public void cook(Order order) {
        if(this.cookedOrders.contains(order)) {
            LOG.info("{} already cooked", order);
            return;
        }
        this.cookedOrders.add(order);
        shelvesLock.writeLock().lock();
        boolean isPlaced = false;
        Shelf orderShelf = null;
        if(isShelfOfOwnTemperatureVacant(order)) {
            isPlaced = true;
            orderShelf = getShelfOfOwnTemperature(order);
        }
        if(!isPlaced && isOverflowShelfVacant()) {
            isPlaced = true;
            orderShelf = overflowShelf;
        }
        if(!isPlaced) {
            replaceOrRemoveOrderFromOverflowShelf();
            isPlaced = true;
            orderShelf = overflowShelf;
        }
        Long time = System.currentTimeMillis();
        order.setTimeCookedAt(time);
        if(!overflowShelf.equals(orderShelf)) {
            order.setTimePlacedAtShelfOfOwnTemperature(time);
        }
        orderShelf.getOrders().add(order);
        orderLookup.put(order, orderShelf);
        OrderCookedEvent orderCookedEvent = new OrderCookedEvent(order);
        applicationEventPublisher.publishEvent(orderCookedEvent);
        LOG.info("{} with value={} cooked and placed in {}\n{}", order, getOrderValue(order), orderShelf, getShelvesContents());
        shelvesLock.writeLock().unlock();
    }

    /**
     * Dispatches the order from the shelf
     * or discards the order if order value is less than or equal to 0
     *
     * @param   order  the order to be cooked
     * @see            Order
     * @author         Mohammad Maaz Khan
     */
    public void dispatch(Order order) {
        shelvesLock.writeLock().lock();
        Shelf orderShelf = orderLookup.get(order);
        if(orderShelf != null) {
            Double orderValue = getOrderValue(order);
            removeOrder(order);
            if(orderValue > 0.0) {
                LOG.info("{} with value={} dispatched from {}\n{}", order, orderValue, orderShelf, getShelvesContents());
            } else {
                LOG.info("{} with value={} discarded from {}\n{}", order, orderValue, orderShelf, getShelvesContents());
            }
        } else {
            LOG.info("{} already discarded\n{}", order, getShelvesContents());
        }
        shelvesLock.writeLock().unlock();
    }

    private void removeOrder(Order order) {
        Shelf orderShelf = orderLookup.get(order);
        if(orderShelf != null) {
            orderShelf.getOrders().remove(order);
            orderLookup.remove(order);
        }
    }

    private void moveOrderToShelfOfOwnTemperature(Order order) {
        Shelf orderShelf = orderLookup.get(order);
        if(orderShelf != null) {
            orderShelf.getOrders().remove(order);
            Shelf ownTemperatureShelf = getShelfOfOwnTemperature(order);
            ownTemperatureShelf.getOrders().add(order);
            order.setTimePlacedAtShelfOfOwnTemperature(System.currentTimeMillis());
            orderLookup.put(order, ownTemperatureShelf);
        }
    }

    private Shelf getShelfOfOwnTemperature(Order order) {
        return this.shelves.stream().filter(shelf -> {
            return shelf.getAllowableTemperatures().stream()
                    .filter(temperature -> temperature.equals(order.getTemp()))
                    .count() > 0;
        }).findFirst().get();
    }

    private boolean isShelfOfOwnTemperatureVacant(Order order) {
        Shelf shelfOfOwnTemperature = getShelfOfOwnTemperature(order);
        return shelfOfOwnTemperature.getCapacity() > shelfOfOwnTemperature.getOrders().size();
    }

    private boolean isOverflowShelfVacant() {
        return overflowShelf.getCapacity() > overflowShelf.getOrders().size();
    }

    private int getRandomIndexFromOverflowShelf() {
        int min = 0;
        int max = overflowShelf.getCapacity() - 1;
        return (int) ((Math.random() * (max - min)) + min);
    }

    private Double getOrderValueAfterMaxPickupTimeInOverflowShelf(Order order) {
        return 1 - decayInOrderValue(order.getShelfLife(), MAX_PICKUP_TIME, order.getDecayRate(),overflowShelf.getShelfDecayModifier());
    }

    private Double getOrderValueAfterGivenMilliSecondsIfMovedToOwnShelf(Order order, Long duration) {
        Long clockTimeAfterGivenDuration = order.getTimeCookedAt() + duration;
        Long timeInShelfOfOwnTemperature = clockTimeAfterGivenDuration - System.currentTimeMillis();
        Shelf shelfOfOwnTemperature = getShelfOfOwnTemperature(order);
        return getOrderValue(order) - decayInOrderValue(order.getShelfLife(), timeInShelfOfOwnTemperature, order.getDecayRate(),shelfOfOwnTemperature.getShelfDecayModifier());
    }

    private Double getOrderValueAfterMaxPickupTimeIfMovedToOwnShelf(Order order) {
        return getOrderValueAfterGivenMilliSecondsIfMovedToOwnShelf(order, MAX_PICKUP_TIME);
    }

    private Double getOrderValueAfterMinPickupTimeIfMovedToOwnShelf(Order order) {
        return getOrderValueAfterGivenMilliSecondsIfMovedToOwnShelf(order, MIN_PICKUP_TIME);
    }

    private void replaceOrRemoveOrderFromOverflowShelf() {
        Optional<Order> bestCandidate = overflowShelf.getOrders().stream()
                .filter(order -> isShelfOfOwnTemperatureVacant(order))
                .filter(order -> getOrderValueAfterMaxPickupTimeInOverflowShelf(order) > 0.0)
                .sorted(Comparator.comparing(order -> getShelfOfOwnTemperature(order).getOrders().size()))
                .findFirst();
        if(bestCandidate.isPresent()) {
            Order order = bestCandidate.get();
            Double oldOrderValue = getOrderValue(order);
            moveOrderToShelfOfOwnTemperature(order);
            LOG.info("{} with value={} moved from {} to {}\n{}",order, oldOrderValue, overflowShelf, getShelfOfOwnTemperature(order), getShelvesContents());
            return;
        }
        Optional<Order> secondBestCandidate = overflowShelf.getOrders().stream()
                .filter(order -> isShelfOfOwnTemperatureVacant(order))
                .filter(order -> getOrderValueAfterMaxPickupTimeIfMovedToOwnShelf(order) > 0.0)
                .sorted(Comparator.comparing(order -> getOrderValueAfterMaxPickupTimeIfMovedToOwnShelf(order)))
                .findFirst();
        if(secondBestCandidate.isPresent()) {
            Order order = secondBestCandidate.get();
            Double oldOrderValue = getOrderValue(order);
            moveOrderToShelfOfOwnTemperature(order);
            LOG.info("{} with value={} moved from {} to {}\n{}",order, oldOrderValue, overflowShelf, getShelfOfOwnTemperature(order), getShelvesContents());
            return;
        }
        Optional<Order> thirdBestCandidate = overflowShelf.getOrders().stream()
                .filter(order -> isShelfOfOwnTemperatureVacant(order))
                .filter(order -> getOrderValueAfterMinPickupTimeIfMovedToOwnShelf(order) > 0.0)
                .sorted((order1, order2) -> getOrderValueAfterMaxPickupTimeIfMovedToOwnShelf(order2).compareTo(getOrderValueAfterMaxPickupTimeIfMovedToOwnShelf(order1)))
                .findFirst();
        if(thirdBestCandidate.isPresent()) {
            Order order = thirdBestCandidate.get();
            Double oldOrderValue = getOrderValue(order);
            moveOrderToShelfOfOwnTemperature(order);
            LOG.info("{} with value={} moved from {} to {}\n{}",order, oldOrderValue, overflowShelf, getShelfOfOwnTemperature(order), getShelvesContents());
            return;
        }
        Order randomOrderFromOverflowShelf = overflowShelf.getOrders().get(getRandomIndexFromOverflowShelf());
        LOG.info("{} with value={} discarded from {}\n{}",randomOrderFromOverflowShelf, getOrderValue(randomOrderFromOverflowShelf), overflowShelf, getShelvesContents());
        removeOrder(randomOrderFromOverflowShelf);
    }

    private boolean isMovedOrder(Order order) {
        return !order.getTimeCookedAt().equals(order.getTimePlacedAtShelfOfOwnTemperature());
    }

    private Double decayInOrderValue(Long shelfLife, Long orderAge, Double decayRate, Double shelfDecayModifier) {
        return (orderAge + decayRate*orderAge*shelfDecayModifier)/ TimeUnit.SECONDS.toMillis(shelfLife);
    }

    private Double getOrderValue(Order order) {
        if(order.getShelfLife().equals(0L)) {
            return 0.0;
        }
        Shelf shelf = orderLookup.get(order);
        Double orderValue = 1.0;
        if(shelf.equals(overflowShelf)){
            Long timeInOverflowShelf = System.currentTimeMillis() - order.getTimeCookedAt();
            orderValue -= decayInOrderValue(order.getShelfLife(), timeInOverflowShelf, order.getDecayRate(), overflowShelf.getShelfDecayModifier());
            return orderValue;
        }
        if(isMovedOrder(order)) {
            Long timeInOverflowShelf = order.getTimePlacedAtShelfOfOwnTemperature() - order.getTimeCookedAt();
            orderValue -= decayInOrderValue(order.getShelfLife(), timeInOverflowShelf, order.getDecayRate(), overflowShelf.getShelfDecayModifier());
        }
        Long timeInShelfOfOwnTemperature = System.currentTimeMillis() - order.getTimePlacedAtShelfOfOwnTemperature();
        orderValue -= decayInOrderValue(order.getShelfLife(), timeInShelfOfOwnTemperature, order.getDecayRate(), shelf.getShelfDecayModifier());
        return orderValue;
    }

    private String getShelvesContents() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(shelves);
    }
}
