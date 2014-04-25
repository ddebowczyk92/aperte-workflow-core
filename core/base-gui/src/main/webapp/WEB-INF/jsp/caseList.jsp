﻿<%@ page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="utils/globals.jsp" %>
<%@include file="utils/apertedatatable.jsp" %>

<div class="process-tasks-view" id="task-view-processes">
    <table id="caseTable" class="process-table table table-striped" border="1">
        <thead>
                <th style="width:20%;">Numer sprawy</th>
                <th style="width:20%;">Rodzaj</th>
                <th style="width:20%;">Nazwa</th>
                <th style="width:20%;">Bieżący etap</th>
                <th style="width:20%;">Data utworzenia</th>
                <th style="width:20%;">Data modyfikacji</th>
        </thead>
        <tbody></tbody>
    </table>
</div>


<script type="text/javascript">
//<![CDATA[

  	$(document).ready(function() {
        var dataTable = new AperteDataTable("caseTable",
            [
                 { "sName":"number", "bSortable": true ,"mData": "number"},
                 { "sName":"definitionName", "bSortable": false ,"mData": "definitionName"},
                 { "sName":"name", "bSortable": true , "mData": "name"},
                 { "sName":"currentStageName", "bSortable": false ,"mData": "currentStageName"},
                 { "sName":"createDate", "bSortable": true ,"mData": "createDate"},
                 { "sName":"modificationDate", "bSortable": true ,"mData": "modificationDate"}
             ],
             [[ 0, "asc" ]]
            );

        dataTable.addParameter("controller", "casemanagementcontroller");
        dataTable.addParameter("action", "getAllCasesPaged");
        // if (window.console) console.log(dispatcherPortlet);
        dataTable.reloadTable(dispatcherPortlet);

    });
	

//]]>
</script>