package tvtrader.controllers;

import tvtrader.model.ListenerField;

public interface Listener {

	public void update(ListenerField changedField, Object subject);

}
