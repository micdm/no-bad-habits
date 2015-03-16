package com.micdm.nobadhabits;

import android.app.Application;

import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.PlainEventManager;

public class CustomApplication extends Application {

    private EventManager eventManager;

    public EventManager getEventManager() {
        if (eventManager == null) {
            eventManager = new PlainEventManager();
        }
        return eventManager;
    }
}
