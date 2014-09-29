<#import "/spring.ftl" as spring />

<#assign portlet=JspTaglibs["http://java.sun.com/portlet_2_0"] />


<script type="text/javascript">
	var dispatcherPortlet = '<@portlet.resourceURL id="dispatcher"/>';
	var portletNamespace = '&<@portlet.namespace/>';
	var dataTableLanguage =
    {
        "sInfo": "Wyniki od _START_ do _END_ z _TOTAL_",
        "sEmptyTable": "<@spring.message code='datatable.empty' />",
        "sInfoEmpty": "<@spring.message code='datatable.empty' />",
        "sProcessing": "<@spring.message code='datatable.processing' />",
        "sLengthMenu": "<@spring.message code='datatable.records' />",
        "sInfoFiltered": "",
        "oPaginate": {
            "sFirst": "<@spring.message code='datatable.paginate.firstpage' />",
            "sNext": "<@spring.message code='datatable.paginate.next' />",
            "sPrevious": "<@spring.message code='datatable.paginate.previous' />"
        }

    };
</script>

<div class="apw main-view">
    <div class="process-tasks-view" id="case-list-view" hidden>
        <table id="caseManagementTable" class="process-table table table-striped" border="1">
            <thead>
            <th style="width:15%;">
                <@spring.message "admin.case.management.results.table.number"/>
            </th>
            <th style="width:15%;">
                <@spring.message "admin.case.management.results.table.definitionName"/>
            </th>
            <th style="width:20%;">
                <@spring.message "admin.case.management.results.table.name"/>
            </th>
            <th style="width:20%;">
                <@spring.message "admin.case.management.results.table.currentStageName"/>
            </th>
            <th style="width:15%;">
                <@spring.message "admin.case.management.results.table.createDate"/>
            </th>
            <th style="width:15%;">
                <@spring.message "admin.case.management.results.table.modificationDate"/>
            </th>
            </thead>
            <tbody></tbody>
        </table>
    </div>

    <div id="case-data-view" class="process-data-view" hidden="false">
        <div id="case-vaadin-widgets" class="vaadin-widgets-view">
        </div>

        <div id="case-actions-list" class="actions-view">
        </div>
    </div>

    <div class="modal fade aperte-modal" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="panel panel-warning">
                <div class="panel-heading"><h4><@spring.message code="processes.alerts.modal.title" /></h4></div>
                <ul id="case-alerts-list">

                </ul>
                <button type="button" class="btn btn-warning" data-dismiss="modal" style="margin: 20px;">
                    <@spring.message code="processes.alerts.modal.close" />
                </button>
            </div>
        </div>
        <!-- /.modal-dialog -->
    </div>

</div>

<script type="text/javascript">
//<![CDATA[
    var caseManagement = {}
    caseManagement['tableInitialized'] = false;
    caseManagement['alertsShown'] = false;
    caseManagement['alertsInit'] = false;

  	$(document).ready(function()
  	{
        <#if caseId?has_content>
            caseManagement.loadCaseView('${caseId?string}');
        <#else>
            caseManagement.showCaseList();
        </#if>
    });

    caseManagement.initDataTable = function()
    {
        if(caseManagement.tableInitialized == true)
            return;

        caseManagement.caseListDT = new AperteDataTable("caseManagementTable",
            [
                 { "sName":"number", "bSortable": true ,"mData": function(object) { return caseManagement.generateNameColumn(object) }
                 },
                 { "sName":"definitionName", "bSortable": false ,"mData": "definitionName"},
                 { "sName":"name", "bSortable": true , "mData": "name"},
                 { "sName":"currentStageName", "bSortable": false ,"mData": "currentStageName"},
                 { "sName":"createDate", "bSortable": true ,"mData": "createDate"},
                 { "sName":"modificationDate", "bSortable": true ,"mData": "modificationDate"}
            ],
            [[ 5, "desc" ]]
        );

        caseManagement.caseListDT.addParameter("controller", "casemanagementcontroller");
        caseManagement.caseListDT.addParameter("action", "getAllCasesPaged");
        // if (window.console) console.log(dispatcherPortlet);
        caseManagement.caseListDT.reloadTable(dispatcherPortlet);
        caseManagement.tableInitialized = true;
    }

	caseManagement.generateNameColumn = function(caseInstance) {
        // if (window.console) console.log(caseInstance);
        var showOnClickCode = 'onclick="caseManagement.loadCaseView(' + caseInstance.id + ')"';
        // if (window.console) console.log(showOnClickCode);
        return '<a class="process-view-link"  '+ showOnClickCode + ' >' + caseInstance.number + '</a>';
    }

    caseManagement.loadCaseView = function(caseId)
    {
        caseManagement.changeUrl('?caseId=' + caseId);
        //windowManager.showLoadingScreen();

        var widgetJson = $.post(dispatcherPortlet, {
                "controller": "casemanagementcontroller",
                "action": "loadCase",
                "caseId" : caseId
            })
            .done(function(data) {
                // if (window.console) console.log(data);
                caseManagement.clearAlerts();
                // windowManager.showProcessData();
                caseManagement.hideCaseList();
                caseManagement.showCaseData();
                $('#case-data-view').empty();
                $("#case-data-view").append(data.data);
                caseManagement.enableButtons();
                // checkIfViewIsLoaded();
            })
            .fail(function(data, textStatus, errorThrown) {
            }
        );
    }

    caseManagement.showCaseData = function() {
        $('#case-data-view').fadeIn(500);
    }

    caseManagement.hideCaseData = function() {
        $('#case-data-view').hide();
    }

    caseManagement.hideCaseList = function() {
        $('#case-list-view').hide();
    }

    caseManagement.showCaseList = function()
    {
        caseManagement.initDataTable();
        $('#case-list-view').fadeIn(500);
    }

    caseManagement.onCloseButton = function() {
        caseManagement.hideCaseData();
        caseManagement.clearCaseView();
        caseManagement.showCaseList();
    }

    caseManagement.clearCaseView = function() {
        widgets = []
    }

    caseManagement.enableButtons = function() {
   		$('#case-actions-list').find('button').prop('disabled', false);
   	}

   	caseManagement.disableButtons = function() {
		$('#case-actions-list').find('button').prop('disabled', true);
	}

    caseManagement.changeUrl = function(newUrl) {
        if(window.history && typeof(window.history.pushState) === 'function')
        {
            if(newUrl == '')
            {
                var currentUrl = location.href.replace(/&?taskId=([^&]$|[^&]*)/i, "");
                window.history.pushState('', '', currentUrl);
            }
            else
            {
                window.history.pushState('', '', newUrl);
            }
        }
    }

    caseManagement.onSaveButton = function(caseId) {
        caseManagement.disableButtons();
        caseManagement.saveAction(caseId);
    }

    caseManagement.saveAction = function(caseId) {
		caseManagement.clearAlerts();

		var errors = [];
		<!-- Validate html widgets -->
		$.each(widgets, function() {
			var errorMessages = this.validate();
			if(!errorMessages) {
			} else {
				$.each(errorMessages, function() {
				    errors.push(this);
					caseManagement.addAlert(this);
				});
			}
	    });

		if(errors.length > 0) {
			caseManagement.enableButtons();
			return;
		}

		var widgetData = [];

		$.each(widgets, function() {
			var widgetDataBean = new WidgetDataBean(this.widgetId, this.name, this.getData());
			widgetData.push(widgetDataBean);
	    });

		var JsonWidgetData = JSON.stringify(widgetData, null, 2);

		var state = 'OK';
		var newBpmTask = $.post(dispatcherPortlet,
		{
		    "controller": "casemanagementcontroller",
		    "action": "saveAction",
			"caseId": caseId,
			"widgetData": JsonWidgetData
		})
		.done(function(data) {
			if(data.errors != null) {
				caseManagement.addAlerts(data.errors);
			}
		})
		.always(function() {
			caseManagement.enableButtons();
		})
		.fail(function(data) {
			caseManagement.addAlerts(data.errors);
		});

		return state;
	}

	caseManagement.addAlert = function(alertMessage) {
		if(caseManagement.alertsShown == false) {
			if(caseManagement.alertsInit == false) {
				caseManagement.alertsInit = true;
				$('#alertModal').appendTo("body").modal({
				    keyboard: false
				});
				$('#alertModal').on('hidden.bs.modal', function (e) {
					caseManagement.clearAlerts();
					caseManagement.alertsShown = false;
				});

			} else {
				$('#alertModal').appendTo("body").modal('show');
			}
			caseManagement.alertsShown = true;
		}
		$('#case-alerts-list').append('<li><h5>'+alertMessage+'</h5></li>');
	}

	caseManagement.clearAlerts = function() {
		$('#case-alerts-list').empty();
	}

	caseManagement.addAlerts = function(alertsMessages) {
		caseManagement.clearAlerts();
		$.each(alertsMessages, function() {
			caseManagement.addAlert(this.message);
		});
	}

//]]>






</script>

