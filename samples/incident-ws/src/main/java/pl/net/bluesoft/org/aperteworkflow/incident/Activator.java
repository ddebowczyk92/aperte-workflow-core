package pl.net.bluesoft.org.aperteworkflow.incident;

import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.example.incident.IncidentService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import pl.net.bluesoft.org.aperteworkflow.incident.ws.beans.IncidentServiceImpl;
import pl.net.bluesoft.rnd.processtool.ISettingsProvider;
import org.apache.cxf.endpoint.Server;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Dominik Dębowczyk on 2014-09-30.
 */
public class Activator implements BundleActivator {
    @Autowired
    private IncidentServiceImpl incidentService;

    @Autowired
    private ISettingsProvider settingsProvider;

    private final static String INCIDENT_WS = "incident";


    private final static String SETTING_PREFIX = "aperteworkflow.ws.";
    private final static String SETTING_PATH = ".path";
    private final static String SETTING_USER = ".user";
    private final static String SETTING_PASSWORD = ".password";
    private final static String SETTING_PASSWORD_TYPE = ".password.type";
    private final static String SETTING_ACTION = ".action";

    private Set<Server> servers = new HashSet<Server>();

    private final Logger logger = Logger.getLogger(Activator.class.getName());

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        buildEndpoint(INCIDENT_WS, IncidentService.class, incidentService);

    }

    private <T> void buildEndpoint(final String serviceName, final Class<T> port, final T impl) {
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(port);
        svrFactory.setAddress("/" + getSetting(serviceName, SETTING_PATH));
        svrFactory.setBus(BusFactory.getDefaultBus());
        svrFactory.getProperties(true).put(Message.SCHEMA_VALIDATION_ENABLED, true);
        svrFactory.setServiceBean(impl);
        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        svrFactory.getInInterceptors().add(new WSS4JInInterceptor(createConfig(serviceName)));
        svrFactory.getInInterceptors().add(new SAAJInInterceptor());
        servers.add(svrFactory.create());
    }

    private Map<String, Object> createConfig(final String serviceName) {
        Map<String,Object> props = new HashMap<String, Object>();
        props.put(WSHandlerConstants.PASSWORD_TYPE, getSetting(serviceName, SETTING_PASSWORD_TYPE));
        props.put(WSHandlerConstants.USER, getSetting(serviceName, SETTING_USER));
        props.put(WSHandlerConstants.ACTION, getSetting(serviceName, SETTING_ACTION));
        props.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler(){
            @Override
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                WSPasswordCallback pc = (WSPasswordCallback)callbacks[0];
                pc.setPassword(getSetting(serviceName, SETTING_PASSWORD));
            }
        });
        return props;
    }



    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        for (Server server : servers) {
            try {
                server.stop();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(),e);
            }
        }
        servers.clear();

        logger.info("Deactivating the incident-ws plugin");

    }

    private String getSetting(String name, String setting){
        return settingsProvider.getSetting(SETTING_PREFIX + name + setting);
    }
}
