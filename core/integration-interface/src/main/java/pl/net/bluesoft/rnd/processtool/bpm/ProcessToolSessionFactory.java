package pl.net.bluesoft.rnd.processtool.bpm;

import pl.net.bluesoft.rnd.processtool.model.UserData;

import java.util.Collection;

/**
 * @author tlipski@bluesoft.net.pl
 */
public interface ProcessToolSessionFactory {
	String getBpmDefinitionLanguage();

	ProcessToolBpmSession createSession(UserData user);

	ProcessToolBpmSession createSession(String userLogin);
	ProcessToolBpmSession createSession(String userLogin, Collection<String> roles);

    ProcessToolBpmSession createAutoSession();
	ProcessToolBpmSession createAutoSession(Collection<String> roles);
}
