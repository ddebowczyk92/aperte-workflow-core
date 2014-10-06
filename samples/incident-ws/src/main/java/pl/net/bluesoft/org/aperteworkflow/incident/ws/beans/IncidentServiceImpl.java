package pl.net.bluesoft.org.aperteworkflow.incident.ws.beans;

import org.example.incident.IncidentService;
import org.example.incident.RegisterIncidentRequestType;
import org.example.incident.RegisterIncidentResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import pl.net.bluesoft.rnd.processtool.ProcessToolContext;
import pl.net.bluesoft.rnd.processtool.ProcessToolContextCallback;
import pl.net.bluesoft.rnd.processtool.bpm.ProcessToolBpmSession;
import pl.net.bluesoft.rnd.processtool.bpm.ProcessToolSessionFactory;
import pl.net.bluesoft.rnd.processtool.bpm.StartProcessResult;
import pl.net.bluesoft.rnd.processtool.model.ProcessInstance;
import pl.net.bluesoft.rnd.processtool.plugins.ProcessToolRegistry;

import javax.jws.WebParam;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Dominik Dębowczyk on 2014-10-02.
 */
public class IncidentServiceImpl implements IncidentService {

    private final Logger logger = Logger.getLogger(IncidentServiceImpl.class.getName());
    @Autowired
    private ProcessToolRegistry registry;

    @Autowired
    private ProcessToolSessionFactory jbpmSessionFactory;

    @Override
    public RegisterIncidentResponseType registerIncident(@WebParam(partName = "registerIncidentParametersPart", name = "registerResponseRQ", targetNamespace = "") final RegisterIncidentRequestType req) {
        final RegisterIncidentResponseType res = new RegisterIncidentResponseType();
        try{

            registry.withExistingOrNewContext(new ProcessToolContextCallback() {
                @Override
                public void withContext(ProcessToolContext context) {
                    registerIncident(req, res, context);
                }
            });
            return  res;
        } catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            res.setStatus("ERROR");
        }


        return res;
    }

    private void registerIncident(RegisterIncidentRequestType req, RegisterIncidentResponseType res, ProcessToolContext context) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("name", req.getName());
        attributes.put("surname", req.getSurname());
        attributes.put("incident-description", req.getIncidentDescription());
        attributes.put("isWS", "true");

        ProcessToolBpmSession jbpmSession = jbpmSessionFactory.createAutoSession();
        StartProcessResult startProcessResult = jbpmSession.startProcess("Incident_Process", null, IncidentServiceImpl.class.getName(), attributes);
        ProcessInstance processInstance = startProcessResult.getProcessInstance();
        res.setProcessId(processInstance.getId().toString());
        res.setStatus("OK");

    }
}
