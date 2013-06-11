package org.aperteworkflow.webapi.main.processes.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aperteworkflow.webapi.context.IProcessToolRequestContext;
import org.aperteworkflow.webapi.main.AbstractProcessToolServletController;
import org.aperteworkflow.webapi.main.ui.TaskViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.bluesoft.rnd.processtool.ProcessToolContext;
import pl.net.bluesoft.rnd.processtool.ProcessToolContextCallback;
import pl.net.bluesoft.rnd.processtool.model.BpmTask;
import pl.net.bluesoft.rnd.processtool.model.config.ProcessStateAction;
import pl.net.bluesoft.rnd.processtool.model.config.ProcessStateConfiguration;
import pl.net.bluesoft.rnd.processtool.model.config.ProcessStateWidget;
import pl.net.bluesoft.rnd.util.i18n.I18NSource;
import pl.net.bluesoft.rnd.util.i18n.I18NSourceFactory;

@Controller
public class TaskViewController extends AbstractProcessToolServletController
{
	private static Logger logger = Logger.getLogger(TaskViewController.class.getName());
    
	@RequestMapping(method = RequestMethod.POST, value = "/task/loadTask")
	@ResponseBody
	public void loadOsgiView(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException
	{
		final I18NSource messageSource = I18NSourceFactory.createI18NSource(request.getLocale());
		
		/* Get process state configuration db id */
		final String processStateConfigurationId = request.getParameter("processStateConfigurationId");
		final String taskId = request.getParameter("taskId");
		
		if(taskId == null || taskId.isEmpty())
		{
			response.getWriter().print(messageSource.getMessage("request.performaction.error.notaskid"));
			return;
		}
		else if(processStateConfigurationId == null || processStateConfigurationId.isEmpty())
		{
			response.getWriter().print(messageSource.getMessage("request.performaction.error.nocofnigurationid"));
			return;
		}
		
		
		/* Initilize request context */
		final IProcessToolRequestContext context = this.initilizeContext(request);
		
		if(!context.isUserAuthorized())
		{
			response.getWriter().print(messageSource.getMessage("request.handle.error.nouser"));
			return;
		}
		
		context.getRegistry().withProcessToolContext(new ProcessToolContextCallback() 
		{

			@Override
			public void withContext(ProcessToolContext ctx) 
			{
				BpmTask task = context.getBpmSession().getTaskData(taskId, ctx);

				ProcessStateConfiguration config = ctx.getProcessDefinitionDAO().getProcessStateConfiguration(Long.parseLong(processStateConfigurationId));

				/* Load view widgets */
				List<ProcessStateWidget> widgets = new ArrayList<ProcessStateWidget>(config.getWidgets());
				Collections.sort(widgets, new Comparator<ProcessStateWidget>() {

					@Override
					public int compare(ProcessStateWidget widget1, ProcessStateWidget widget2) {
						// TODO Auto-generated method stub
						return widget1.getPriority().compareTo(widget2.getPriority());
					}
				});
				
				/* Load view actions */
				List<ProcessStateAction> actions = new ArrayList<ProcessStateAction>(config.getActions());
				Collections.sort(actions, new Comparator<ProcessStateAction>() {

					@Override
					public int compare(ProcessStateAction action1, ProcessStateAction action2) {
						// TODO Auto-generated method stub
						return action1.getPriority().compareTo(action2.getPriority());
					}
				});
				
				TaskViewBuilder taskViewBuilder = new TaskViewBuilder()
					.setWidgets(widgets)
					.setActions(actions)
					.setI18Source(messageSource)
					.setUser(context.getUser())
					.setTask(task);

				try
				{
					taskViewBuilder.processView(response.getOutputStream());
					response.getOutputStream().print("test");
				}
				catch(IOException ex)
				{
					logger.log(Level.SEVERE, "Problem during task view generation. TaskId="+taskId, ex);
				}
				
//			    try
//			    {
//			    	ServletOutputStream op = response.getOutputStream();
//			    	
//			    	/* Get content provider for view */
//			    	IWidgetContentProvider contentProvider = processToolRegistry.getHtmlView(widgetName);
//			    	
//			    	widgetContent = contentProvider.getHtmlContent();
//			    	
//			        byte[] buffer = new byte[1024];
//			        int bytesRead = 0;
//			        
//			        while ((bytesRead = widgetContent.read(buffer)) != -1) 
//			        	op.write(buffer, 0, bytesRead);
//
//			        op.flush();
//			    }
//			    finally
//			    {
//			        if(widgetContent != null)
//			        	widgetContent.close();
//			    }
				
			}
		});
		
		



	}
}
