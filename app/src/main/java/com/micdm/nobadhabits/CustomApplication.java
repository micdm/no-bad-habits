package com.micdm.nobadhabits;

import android.app.Application;

import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.intents.IntentBasedEventManager;

public class CustomApplication extends Application {

    private EventManager _eventManager;

    public EventManager getEventManager() {
        if (_eventManager == null) {
            _eventManager = new IntentBasedEventManager(this);
        }
        return _eventManager;
    }
}
