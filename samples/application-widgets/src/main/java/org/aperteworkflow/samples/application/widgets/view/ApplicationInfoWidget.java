package org.aperteworkflow.samples.application.widgets.view;

import pl.net.bluesoft.rnd.processtool.plugins.IBundleResourceProvider;
import pl.net.bluesoft.rnd.processtool.ui.widgets.ProcessHtmlWidget;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.AliasName;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.WidgetGroup;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.WidgetType;
import pl.net.bluesoft.rnd.processtool.ui.widgets.impl.SimpleWidgetDataHandler;
import pl.net.bluesoft.rnd.processtool.web.widgets.impl.FileWidgetContentProvider;

/**
 * Created by Dominik Dębowczyk on 2015-08-05.
 */
@AliasName(name = "CaseApplicationInfo", type = WidgetType.Html)
@WidgetGroup("application-process")
public class ApplicationInfoWidget extends ProcessHtmlWidget {

    public ApplicationInfoWidget(IBundleResourceProvider bundleResourceProvider) {
        setContentProvider(new FileWidgetContentProvider("case-application-info.html", bundleResourceProvider));
        SimpleWidgetDataHandler simpleWidgetDataHandler = new SimpleWidgetDataHandler();
        addDataHandler(simpleWidgetDataHandler);
    }
}


