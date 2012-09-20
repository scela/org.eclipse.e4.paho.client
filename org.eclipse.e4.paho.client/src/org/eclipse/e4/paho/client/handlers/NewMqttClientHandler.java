package org.eclipse.e4.paho.client.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class NewMqttClientHandler {
	
	@CanExecute
	public boolean canExecute(){
		return true;
	}

	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
		
		MPart clientPart = partService.createPart(INamingConstants.CLIENT_CREATION_DESCRIPTOR);
		MUIElement result = modelService.find(INamingConstants.STACK_CLIENT_ID, application);
		if (result!=null){
			MPartStack stack = (MPartStack) result;
			if (stack.getChildren().size()>0)
			{
				MessageDialog.openInformation(shell, "One at a time", "You can only add one client at a time");
				return;
			}
			stack.getChildren().add(clientPart);
			partService.activate(clientPart);
			application.getContext().set(INamingConstants.NEWCLIENT_PART_REF, clientPart);
		}
		
	}
}
