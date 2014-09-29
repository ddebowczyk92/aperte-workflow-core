package pl.net.bluesoft.org.aperteworkflow.casemanagement.ui;

import org.aperteworkflow.webapi.main.ui.AbstractViewBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.Case;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.CaseStateRole;
import pl.net.bluesoft.rnd.processtool.model.IAttributesProvider;
import pl.net.bluesoft.rnd.processtool.model.config.IStateWidget;
import pl.net.bluesoft.rnd.processtool.web.domain.IHtmlTemplateProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by pkuciapski on 2014-04-28.
 */
public class CaseViewBuilder extends AbstractViewBuilder<CaseViewBuilder> {
    private final Case caseInstance;

    public CaseViewBuilder(final Case caseInstance) {
        this.caseInstance = caseInstance;
    }

	@Override
	protected void buildWidgets(Document document, Element widgetsNode) {
		widgetsNode.append(
				new StringBuilder(100)
				.append("<h3 style=\"padding-bottom:0px;margin-top:0px !important\">")
				.append(MessageFormat.format(i18Source.getMessage("case.view.header"), caseInstance.getNumber()))
				.append("</h3>")
				.toString());
		super.buildWidgets(document, widgetsNode);
	}

	@Override
    protected CaseViewBuilder getThis() {
        return this;
    }

    @Override
    protected IAttributesProvider getViewedObject() {
        return this.caseInstance;
    }

    @Override
    protected void addSpecificHtmlWidgetData(final Map<String, Object> viewData, final IAttributesProvider viewedObject) {
        viewData.put(IHtmlTemplateProvider.CASE_PARAMETER, caseInstance);
    }

    @Override
    protected void buildAdditionalData(final Document document) {
        // no additional data to show for a case
    }

    @Override
    protected String getViewedObjectId() {
        return String.valueOf(caseInstance.getId());
    }

    @Override
    protected boolean isViewedObjectClosed() {
        return false;
    }

    @Override
    protected String getSaveButtonDescriptionKey() {
        return "case.management.button.save.desc";
    }

    @Override
    protected String getSaveButtonMessageKey() {
        return "case.management.button.save";
    }

    @Override
    protected String getCancelButtonMessageKey() {
        return "case.management.button.close";
    }

    @Override
    protected boolean isSubstitutingUser() {
        return ctx.getUserSubstitutionDAO().isSubstitutedBy(caseInstance.getCurrentStage().getSimpleAttributeValue("assignedPerson"), user.getLogin());
    }

    @Override
    protected String getActionsListHtmlId() {
        return "case-actions-list";
    }

    @Override
    protected String getSaveButtonHtmlId() {
        return "case-action-button-save";
    }

    @Override
    protected String getActionsGenericListHtmlId() {
        return "case-actions-generic-list";
    }

    @Override
    protected String getVaadinWidgetsHtmlId() {
        return "case-vaadin-widgets";
    }

    @Override
    protected String getCancelButtonClickFunction() {
        return "caseManagement.onCloseButton";
    }

    @Override
    protected String getCancelButtonHtmlId() {
        return "case-action-button-cancel";
    }

    @Override
    protected boolean isUserAssignedToViewedObject() {
        return true;
    }

    @Override
    protected boolean isUserCanPerformActions() {
		if (!hasCurrentStageEditPrivilege()) {
			return false;
		}

		List<String> privileges = new ArrayList<String>();
		privileges.add(CaseStateRole.PRIVILEGE_EDIT);

		CasePrivilegeHandlers.INSTANCE.handle(caseInstance, user, privileges);

		return privileges.contains(CaseStateRole.PRIVILEGE_EDIT);
    }

	private boolean hasCurrentStageEditPrivilege() {
		if (this.caseInstance.getCurrentStage() != null) {
			for (CaseStateRole role : this.caseInstance.getCurrentStage().getCaseStateDefinition().getRoles()) {
				if (CaseStateRole.PRIVILEGE_EDIT.equals(role.getPrivilegeName()) && (role.getRoleName().contains("*") || user.hasRole(role.getRoleName()))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected Collection<String> getPrivileges(IStateWidget widget) {
        Collection<String> privileges = super.getPrivileges(widget);
        // if user has am EDIT privilege, it forces all widgets privileges to EDIT
        if (isUserCanPerformActions()) {
            privileges.add(CaseStateRole.PRIVILEGE_EDIT);
        }
		CasePrivilegeHandlers.INSTANCE.handle(caseInstance, user, privileges);
        return privileges;
    }

    @Override
    protected void buildSpecificActionButtons(Element specificActionButtons) {
        // no specific actions to build
    }

    @Override
    protected String getActionsSpecificListHtmlId() {
        return "case-actions-specific-list";
    }

    @Override
    protected String getSaveButtonClickFunction() {
        return "caseManagement.onSaveButton";
    }
}
