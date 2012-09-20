package org.eclipse.e4.paho.client.addons;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.swt.widgets.Shell;

public class InterpartListener {

	@PostConstruct
	public void postConstruct() {
		// register listeners

	}

	@Inject
	@Optional
	public void clientCreated(@UIEventTopic(INamingConstants.CLIENT_CREATED) MqttClient client,
			IEclipseContext context,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named(INamingConstants.NEWCLIENT_PART_REF) MPart part,
			EModelService modelService,
			EPartService partService, MApplication application) {
		if (client != null) {
			MessageDialog.openInformation(shell, "Client created successfully",
					"Client (ID: " + client.getClientId() + ") was created");
			partService.hidePart(part, true);
			context.set(INamingConstants.JUST_CREATED, client);
			MPart detailPart = partService.createPart(INamingConstants.CLIENT_DETAIL_DESCRIPTOR);
			detailPart.setLabel("Client: "+client.getClientId());
			MPartStack detailStack = (MPartStack) modelService.find(INamingConstants.DETAIL_STACK_ID, application);
			if (detailStack!=null){
				detailStack.getChildren().add(detailPart)
				;
				partService.activate(detailPart,true);
			}
			
			
			
		}
	}

	@PreDestroy
	public void preDestroy() {
		// unregister listeners
	}

}
