package com.pun.cool.chatcool.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Cool on 18/3/2560.
 */

public final class BusProvider {

    private static  Bus BUS;

    public static Bus getInstance() {
        if (BUS == null) BUS = new Bus(ThreadEnforcer.ANY);
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
