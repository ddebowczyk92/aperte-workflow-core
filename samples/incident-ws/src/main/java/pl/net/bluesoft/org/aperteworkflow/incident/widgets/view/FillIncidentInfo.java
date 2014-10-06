package pl.net.bluesoft.org.aperteworkflow.incident.widgets.view;

import pl.net.bluesoft.rnd.processtool.plugins.IBundleResourceProvider;
import pl.net.bluesoft.rnd.processtool.ui.widgets.IKeysToIgnoreProvider;
import pl.net.bluesoft.rnd.processtool.ui.widgets.ProcessHtmlWidget;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.AliasName;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.WidgetGroup;
import pl.net.bluesoft.rnd.processtool.ui.widgets.annotations.WidgetType;
import pl.net.bluesoft.rnd.processtool.ui.widgets.impl.SimpleWidgetDataHandler;
import pl.net.bluesoft.rnd.processtool.web.widgets.impl.FileWidgetContentProvider;

import java.util.Collection;

/**
 * Created by Dominik Dębowczyk on 2014-09-30.
 */
@AliasName(name = "FillIncidentInfo", type = WidgetType.Html)
@WidgetGroup("incident-process")
public class FillIncidentInfo extends ProcessHtmlWidget{
    public FillIncidentInfo(IBundleResourceProvider bundleResourceProvider) {
        setContentProvider(new FileWidgetContentProvider("fill-incident-info.html", bundleResourceProvider));

        SimpleWidgetDataHandler simpleWidgetDataHandler = new SimpleWidgetDataHandler();


        addDataHandler(simpleWidgetDataHandler);
    }
}
