package fr.stevecohen.eventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EventBus {

	private static EventBus instance = null;
	private Map<String, List<Callback>> watchers = new HashMap<>();

	/**
	 * Call the EventBus singleton instance
	 * 
	 * @return EventBus
	 * @author Steve Cohen
	 */
	public static EventBus getEventBus() {
		if (instance == null) {
			instance = new EventBus();
		}
		return instance;
	}

	/**
	 * @param events		The name of events you want to listen separed by ";"
	 * @param callback	The function witch will be called when event is fired
	 * @return CallbackSettings Array associated to all the events. You can use this CallbackSettings to interact with the callback
	 * 
	 * @see EventBus.Callback
	 * @see EventBus.CallbackSettings for more informations about Callback interactions
	 */
	public CallbackSettings[] on(String events, final Callback callback) {
		final String[] eventsArray = events.split(";");
		int nbrEvents = eventsArray.length;
		synchronized (watchers) {
			for (String eventName : eventsArray) {
				if (!watchers.containsKey(eventName)) {
					watchers.put(eventName, new ArrayList<EventBus.Callback>());
				}
				watchers.get(eventName).add(callback);
			}
		}

		CallbackSettings[] multipleCallbackSettings = new CallbackSettings[nbrEvents];
		for (int i = 0; i < nbrEvents; i++) {
			final int index = i;
			multipleCallbackSettings[i] = new CallbackSettings() {

				@Override
				public void start() {
					synchronized (watchers) {
						watchers.get(eventsArray[index]).add(callback);
					}
				}

				@Override
				public void remove() {
					synchronized (watchers) {
						watchers.get(eventsArray[index]).remove(callback);
					}
				}

				@Override
				public void pause() {
					synchronized (watchers) {
						watchers.get(eventsArray[index]).remove(callback);
					}
				}
			};
		}
		return multipleCallbackSettings;
	}

	/**
	 * 
	 * @param event		The event you want to dispatch
	 * @param arg		An optional argument you can send to the listener callback
	 * @return			True if the event have one or more listener
	 */
	public boolean dispatch(String event, String arg) {
		synchronized (watchers.get(event)) {
			if (watchers.containsKey(event)) {
				Iterator<Callback> it = watchers.get(event).iterator();
				while (it.hasNext()) {
					try {
						it.next().call(arg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * Callback class used for EventBus
	 * @author Steve Cohen
	 *
	 */
	public interface Callback {
		public void call(String argument);
	}

	/**
	 * CallbackSettings give you some "commands" to interact with your callback. You are somes functions you can call simply.
	 * @author Steve
	 *
	 */
	public interface CallbackSettings {
		/**
		 * To remove your listener
		 */
		public void remove();

		/**
		 * To stop to listen your event temporaly
		 */
		public void pause();

		/**
		 * To start listen again (when pause the listener)
		 */
		public void start();
	}
}
