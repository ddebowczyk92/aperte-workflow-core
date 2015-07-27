package pl.net.bluesoft.lot.casemanagement.ui;

import pl.net.bluesoft.lot.casemanagement.model.Case;
import pl.net.bluesoft.rnd.processtool.model.UserData;

import java.util.Collection;

/**
 * User: POlszewski
 * Date: 2014-06-24
 */
public interface CasePrivilegeHandler {
	void handleEdit(Case caseInstance, UserData user, Collection<String> privileges);

    void handView(Case caseInstance, UserData user, Collection<String> privileges);
}
