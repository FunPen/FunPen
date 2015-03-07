package fr.stevecohen.eventBus;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Steve Cohen
 */
public class EventBus {

    private boolean	isDispatching = false;
    private static EventBus instance = null;
    private Map<String, List<EventCallback>> watchers = new HashMap<>();

    private Map<String, List<EventCallback>> watchersToAddSafely = new HashMap<>();

    /**
     * Call the EventBus singleton instance
     *
     * @return EventBus
     */
    public static EventBus getEventBus() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * @param events		The name of events you want to listen separated by ";"
     * @param eventCallback	The function witch will be called when event is fired
     *
     * @see EventBus.EventCallback
     */
    public void on(String events, final EventCallback eventCallback) {
        final String[] eventsArray = events.split(";");
        if (isDispatching == true) {
            for (String eventName : eventsArray) {
                if (!watchersToAddSafely.containsKey(eventName)) {
                    watchersToAddSafely.put(eventName, new ArrayList<EventCallback>());
                }
                watchersToAddSafely.get(eventName).add(eventCallback);
            }
        }else {
            for (String eventName : eventsArray) {
                if (!watchers.containsKey(eventName)) {
                    watchers.put(eventName, new ArrayList<EventCallback>());
                }
                watchers.get(eventName).add(eventCallback);
            }
        }
    }

    /**
     *
     * @param event		The event you want to dispatch
     * @param arg		An optional argument you can send to the listener callback
     * @return			True if the event have one or more listener
     */
    public boolean dispatch(String event, Object arg) {
        isDispatching = true;
        if (watchers.containsKey(event)) {
            Iterator<EventCallback> it = watchers.get(event).iterator();
            List<EventCallback> toDelete = new ArrayList<>();
            while (it.hasNext()) {
                try {
                    EventCallback ec = it.next();
                    if (ec.delete)
                        toDelete.add(ec);
                    else {
                        ec.call(arg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (EventCallback callbackToDelete : toDelete)
                watchers.get(event).remove(callbackToDelete);
            isDispatching = false;
            updateTmpWatchers();
            return true;
        }
        isDispatching = false;
        updateTmpWatchers();
        return false;
    }

    private void updateTmpWatchers() {
        for (Entry<String, List<EventCallback>> entry : watchersToAddSafely.entrySet()) {
            if (!watchers.containsKey(entry.getKey()))
                watchers.put(entry.getKey(), entry.getValue());
            else
                watchers.get(entry.getKey()).addAll(entry.getValue());
        }
        watchersToAddSafely.clear();
    }

    /**
     * Callback class used for EventBus
     * @author Steve Cohen
     *
     */
    public static abstract class EventCallback {
        private boolean delete = false;
        public abstract void call(Object argument);

        public void destroyCallback() {
            this.delete = true;
        }
    }
}
