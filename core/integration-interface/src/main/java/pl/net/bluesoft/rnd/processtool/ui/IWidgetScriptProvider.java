package pl.net.bluesoft.rnd.processtool.ui;

import java.io.InputStream;

/**
 * JavaScript provider interface for widgets 
 * 
 * @author mpawlak@bluesoft.net.pl
 *
 */
public interface IWidgetScriptProvider 
{
	/** Get script content */
	InputStream getJavaScriptContent();
	
	/** Get file name */
	String getFileName();

}
