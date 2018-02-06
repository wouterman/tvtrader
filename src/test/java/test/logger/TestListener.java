package test.logger;

import tvtrader.controllers.Listener;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;

public class TestListener implements Listener {
	boolean notified = false;
	
	@Override
	public void update(ConfigurationField changedField, Configuration configuration) {
		notified=true;			
	}
	
	public boolean isNotified() {
		return notified;
	}
	
	public void reset() {
		notified = false;
	}
	
}
