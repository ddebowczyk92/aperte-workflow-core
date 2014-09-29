package pl.net.bluesoft.org.aperteworkflow.casemanagement.ui;

import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.Case;
import pl.net.bluesoft.rnd.processtool.model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: POlszewski
 * Date: 2014-06-24
 */
public class CasePrivilegeHandlers {
	public static final CasePrivilegeHandlers INSTANCE = new CasePrivilegeHandlers();

	private List<CasePrivilegeHandler> handlers = new ArrayList<CasePrivilegeHandler>();

	public void add(CasePrivilegeHandler handler) {
		handlers.add(handler);
	}

	public void remove(CasePrivilegeHandler handler) {
		handlers.remove(handler);
	}

	public void handle(Case caseInstance, UserData user, Collection<String> privileges) {
		for (CasePrivilegeHandler handler : handlers) {
			handler.handle(caseInstance, user, privileges);
		}
	}
}
