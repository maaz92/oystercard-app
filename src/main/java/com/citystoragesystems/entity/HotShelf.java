package com.citystoragesystems.entity;

import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * The hot shelf which can store hot orders
 * It has a capacity to store 10 orders
 * The shelf decay modifier for hot shelf is 1
 *
 * @author  Mohammad Maaz Khan
 */
@Component
public class HotShelf extends  Shelf {

    /**
     * Constructs an instance of HotShelf that can be used to store hot orders
     *
     *
     * @author  Mohammad Maaz Khan
     */
    public HotShelf() {
        super("Hot Shelf", Arrays.asList(Temperature.hot), 10, 1.0);
    }
}
