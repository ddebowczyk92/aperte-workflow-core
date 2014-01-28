package org.aperteworkflow.webapi.main.processes.processor;

import com.google.common.eventbus.EventBus;
import org.aperteworkflow.webapi.main.processes.action.domain.SaveResultBean;
import org.aperteworkflow.webapi.main.processes.action.domain.ValidateResultBean;
import org.aperteworkflow.webapi.main.processes.domain.HtmlWidget;
import org.codehaus.jackson.map.ObjectMapper;
import pl.net.bluesoft.rnd.processtool.event.SaveTaskEvent;
import pl.net.bluesoft.rnd.processtool.event.ValidateTaskEvent;
import pl.net.bluesoft.rnd.processtool.event.beans.ErrorBean;
import pl.net.bluesoft.rnd.processtool.model.BpmTask;
import pl.net.bluesoft.rnd.processtool.model.ProcessInstance;
import pl.net.bluesoft.rnd.processtool.model.ProcessInstanceLog;
import pl.net.bluesoft.rnd.processtool.ui.widgets.*;
import pl.net.bluesoft.rnd.util.i18n.I18NSource;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.net.bluesoft.rnd.processtool.plugins.ProcessToolRegistry.Util.getRegistry;

/**
 * Task save processor class 
 * 
 * @author mpawlak@bluesoft.net.pl
 *
 */
public class TaskProcessor 
{
	private EventBus eventBus;
	private BpmTask task;
	private Collection<HtmlWidget> widgets;
	private I18NSource messageSource;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(TaskProcessor.class.getName());
	
	public TaskProcessor(BpmTask task, EventBus eventBus, I18NSource messageSource, Collection<HtmlWidget> widgets)
	{
		this.eventBus = eventBus;
		this.task = task;
		this.widgets = widgets;
		this.messageSource = messageSource;
	}
	
	/** Validate vaadin and html widgets. Validation is performed to all widgets and
	 *  one widgets error does not stop validation processes */
	public ValidateResultBean validateWidgets()
	{
		ValidateResultBean validateResult = new ValidateResultBean();
		validateHtmlWidgets(validateResult);
		validateVaadinWidgets(validateResult);
		
		return validateResult;
	}
	
	/** Save vaadin and html widgets */
	public SaveResultBean saveWidgets()
	{
		SaveResultBean saveResult = new SaveResultBean();
		saveHtmlWidgets(saveResult);
		saveVaadinWidgets(saveResult);
		
		return saveResult;
	}
	
	private void validateHtmlWidgets(ValidateResultBean validateResult)
	{
		for(HtmlWidget widgetToValidate: widgets)
		{
			/** Get widget definition to retrive validator class */
			ProcessHtmlWidget processWidget = getRegistry().getGuiRegistry().getHtmlWidget(widgetToValidate.getWidgetName());
			if(processWidget == null)
				throw new RuntimeException(messageSource.getMessage("process.widget.name.unknown", widgetToValidate.getWidgetName()));
			
			IWidgetValidator widgetValidator = processWidget.getValidator();

            WidgetData widgetData = new WidgetData();
            widgetData.addWidgetData(widgetToValidate.getData());
			
			Collection<String> errors = widgetValidator.validate(task, widgetData);
			for(String error: errors)
				validateResult.addError(widgetToValidate.getWidgetId().toString(), error);
		}
	}
	
	private void saveHtmlWidgets(SaveResultBean saveResult)
	{
        Collection<HandlingResult> results = new LinkedList<HandlingResult>();
		for(HtmlWidget widgetToSave: widgets)
		{
			/** Get widget definition to retrive data handler class */
			ProcessHtmlWidget processWidget = getRegistry().getGuiRegistry().getHtmlWidget(widgetToSave.getWidgetName());
			if(processWidget == null)
				throw new RuntimeException(messageSource.getMessage("process.widget.name.unknown", widgetToSave.getWidgetName()));
			
			IWidgetDataHandler widgetDataHandler = processWidget.getDataHandler();

            WidgetData widgetData = new WidgetData();
            widgetData.addWidgetData(widgetToSave.getData());


            results.addAll(widgetDataHandler.handleWidgetData(task, widgetData));
		}
        ProcessInstance process = task.getProcessInstance();

        if(!results.isEmpty()) {
            String json = null;
            try {
                json = mapper.writeValueAsString(results);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            ProcessInstanceLog log = new ProcessInstanceLog();
            log.setState(null);
            log.setEntryDate(new Date());
            log.setEventI18NKey("process.log.process-change");
            log.setUserLogin(task.getAssignee());
            log.setLogType(ProcessInstanceLog.LOG_TYPE_PROCESS_CHANGE);
            log.setOwnProcessInstance(process);
            log.setLogValue(json);
            process.getRootProcessInstance().addProcessLog(log);
        }
    }
	
	/** Send event to all vaadin widgets to perform validation task. Widgets are 
	 * registered for this event and filtration is done by taskId
	 *
	 * @return
	 */
	private void validateVaadinWidgets(ValidateResultBean validateResult)
	{
		ValidateTaskEvent validateEvent = new ValidateTaskEvent(task);
		
		eventBus.post(validateEvent);
		
		/* Copy all errors from event */
		for(ErrorBean errorBean: validateEvent.getErrors())
			validateResult.addError(errorBean);
	}
	
	/** Send event to all vaadin widgets to perform save task. Widgets are 
	 * registered for this event and filtration is done by taskId
	 *
	 * @return
	 */
	private void saveVaadinWidgets(SaveResultBean saveResult)
	{
		SaveTaskEvent saveEvent = new SaveTaskEvent(task);
		
		eventBus.post(saveEvent);
		
		/* Copy all errors from event */
		for(ErrorBean errorBean: saveEvent.getErrors())
			saveResult.addError(errorBean);
	}
}
