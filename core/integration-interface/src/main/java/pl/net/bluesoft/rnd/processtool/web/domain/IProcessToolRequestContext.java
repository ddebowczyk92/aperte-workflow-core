package pl.net.bluesoft.rnd.processtool.web.domain;

import pl.net.bluesoft.rnd.processtool.bpm.ProcessToolBpmSession;
import pl.net.bluesoft.rnd.processtool.model.UserData;
import pl.net.bluesoft.rnd.processtool.plugins.ProcessToolRegistry;
import pl.net.bluesoft.rnd.util.i18n.I18NSource;

import java.util.Collection;

/**
 * Process Tool request context 
 * 
 * @author mpawlak@bluesoft.net.pl
 *
 */
public interface IProcessToolRequestContext 
{
	/** Is user logged */
	boolean isUserAuthorized();
	
	/** Get user from request */
	UserData getUser();
	
	/** Get process tool bpm session */
	ProcessToolBpmSession getBpmSession();
	
	/** Get message source */
	I18NSource getMessageSource();

    /** Get user queues */
    Collection<String> getUserQueues();

}
