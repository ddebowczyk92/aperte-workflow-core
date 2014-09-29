package pl.net.bluesoft.org.aperteworkflow.casemanagement.step;

import org.springframework.beans.factory.annotation.Autowired;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.ICaseManagementFacade;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.exception.CaseManagementException;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.Case;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.CaseAttributes;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.processor.CaseProcessor;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.step.util.CaseStepUtil;
import pl.net.bluesoft.rnd.processtool.auditlog.AuditLogContext;
import pl.net.bluesoft.rnd.processtool.model.BpmStep;
import pl.net.bluesoft.rnd.processtool.model.processdata.AbstractProcessInstanceAttribute;
import pl.net.bluesoft.rnd.processtool.steps.ProcessToolProcessStep;
import pl.net.bluesoft.rnd.processtool.ui.widgets.HandlingResult;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.AliasName;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.AutoWiredProperty;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static pl.net.bluesoft.util.lang.Formats.nvl;

/**
 * Created by pkuciapski on 2014-04-23.
 */
@AliasName(name = "CreateCase")
public class CreateCaseStep implements ProcessToolProcessStep {
    private final Logger logger = Logger.getLogger(CreateCaseStep.class.getName());

    @AutoWiredProperty(required = true, substitute = true)
    private String caseDefinitionName;
    @AutoWiredProperty(required = true, substitute = true)
    private String caseName;
    @AutoWiredProperty(required = true, substitute = true)
    private String initialState;
    @AutoWiredProperty(required = true, substitute = true)
    private String caseNumber;
    @AutoWiredProperty
    private String caseAttributesQuery;
	@AutoWiredProperty
	private String newStageAttributesQuery = "";
	@AutoWiredProperty
	private String newStageLargeAttributesQuery = "";
    @AutoWiredProperty
    private boolean addAllVariables = false;
	@AutoWiredProperty
	private boolean auditLog = false;
	@AutoWiredProperty(substitute = true)
	private String changeAuthor;
	@AutoWiredProperty(substitute = true)
	private String ignoredAttributes;

    @Autowired
    private ICaseManagementFacade caseManagement;

    @Override
    public String invoke(final BpmStep step, final Map<String, String> params) throws Exception {
        if (caseNumber == null) {
			throw new CaseManagementException("Case number cannot be null");
		}

        /* Create new case */
        final Case newCase = caseManagement.createCase(caseDefinitionName, caseName, caseNumber, initialState);

        step.getProcessInstance().setSimpleAttribute(CaseAttributes.CASE_ID.value(), String.valueOf(newCase.getId()));

        /* Copy attributes to new case */
        final List<AbstractProcessInstanceAttribute> attributes = CaseStepUtil.getProcessAttributes(
				caseAttributesQuery, ignoredAttributes, step.getProcessInstance(), addAllVariables);

		CaseStepUtil.copyAttributesToStep(newCase, newStageAttributesQuery, step.getProcessInstance());
		CaseStepUtil.copyLargeAttributesToStep(newCase, newStageLargeAttributesQuery, step.getProcessInstance());

		// copy process instance attributes and other variables
        final CaseProcessor processor = new CaseProcessor(newCase, step.getProcessInstance(), null, null, null);

		step.getProcessInstance().setSimpleAttribute("changeAuthor", changeAuthor);

		List<HandlingResult> results = AuditLogContext.withContext(newCase, new AuditLogContext.Callback() {
			@Override
			public void invoke() throws Exception {
				if (auditLog) {
					processor.copyAllAttributes(attributes);
				}
			}
		});

		if (!auditLog) {
			processor.copyAllAttributes(attributes);
		}

		CaseStepUtil.auditLog(newCase, nvl(changeAuthor, "auto"), results);

		caseManagement.updateCase(newCase);

        return STATUS_OK;
    }
}
