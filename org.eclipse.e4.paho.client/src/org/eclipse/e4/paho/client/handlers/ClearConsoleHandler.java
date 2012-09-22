 
package org.eclipse.e4.paho.client.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.swt.widgets.Text;

public class ClearConsoleHandler {
	@Execute
	public void execute(@Optional @Named(INamingConstants.CONSLE_CONTROL) Text control, IEclipseContext context) {
		if (control!=null)
		control.setText("");
	}
	
}