package test.logger;

import tvtrader.model.Listener;
import tvtrader.model.ListenerField;

public class TestListener implements Listener {
	boolean notified = false;
	
	@Override
	public void update(ListenerField changedField, Object subject) {
		notified=true;			
	}
	
	public boolean isNotified() {
		return notified;
	}
	
	public void reset() {
		notified = false;
	}
	
}
