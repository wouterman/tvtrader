package tvtrader.controllers;

import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;

public interface Listener {

	public void update(ConfigurationField changedField, Configuration configuration);

}
