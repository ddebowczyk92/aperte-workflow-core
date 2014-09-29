package pl.net.bluesoft.org.aperteworkflow.casemanagement.dao;

import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.CaseDefinition;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.CaseStateDefinition;

/**
 * Created by pkuciapski on 2014-04-22.
 */
public interface CaseStateDefinitionDAO {
    CaseStateDefinition getStateDefinitionById(long caseStateDefinitionId);

    CaseStateDefinition getStateDefinitionByName(String caseStateDefinitionName, CaseDefinition caseDefinition);

    CaseStateDefinition createStateDefinition(String name, long caseDefinitionId);
}
