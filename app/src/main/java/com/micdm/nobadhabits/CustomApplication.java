package com.micdm.nobadhabits;

import android.app.Application;

import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.intents.IntentBasedEventManager;

public class CustomApplication extends Application {

    private EventManager eventManager;

    public EventManager getEventManager() {
        if (eventManager == null) {
            eventManager = new IntentBasedEventManager(this);
        }
        return eventManager;
    }
}
