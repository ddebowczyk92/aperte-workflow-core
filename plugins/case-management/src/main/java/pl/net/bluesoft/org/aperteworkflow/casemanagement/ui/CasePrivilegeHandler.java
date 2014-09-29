package pl.net.bluesoft.org.aperteworkflow.casemanagement.ui;

import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.Case;
import pl.net.bluesoft.rnd.processtool.model.UserData;

import java.util.Collection;

/**
 * User: POlszewski
 * Date: 2014-06-24
 */
public interface CasePrivilegeHandler {
	void handle(Case caseInstance, UserData user, Collection<String> privileges);
}
