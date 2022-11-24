package com.citystoragesystems.entity;

import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * The frozen shelf which can store frozen orders
 * It has a capacity to store 10 orders
 * The shelf decay modifier for frozen shelf is 1
 *
 * @author  Mohammad Maaz Khan
 */
@Component
public class FrozenShelf extends Shelf {

    /**
     * Constructs an instance of FrozenShelf that can be used to store frozen orders
     *
     *
     * @author  Mohammad Maaz Khan
     */
    public FrozenShelf() {
        super("Frozen Shelf", Arrays.asList(Temperature.frozen), 10, 1.0);
    }
}
