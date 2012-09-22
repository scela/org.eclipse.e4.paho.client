package org.eclipse.e4.paho.client.parts;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.StringToNumberConverter;
import org.eclipse.core.internal.databinding.validation.StringToIntegerValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MqttClientPart {

//	private DataBindingContext m_bindingContext;

	private final class CreationListener extends SelectionAdapter {
		private final Shell shell;
		private final IEventBroker eventBroker;
		private boolean def;

		private CreationListener(Shell shell, IEventBroker eventBroker, boolean def) {
			this.shell = shell;
			this.eventBroker = eventBroker;
			this.def=def;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!def){
			bServer=server.getText();
			bPort=port.getText();
			if (combo.getSelectionIndex()>-1)//avoid IOoB
			bProtocol=prots[combo.getSelectionIndex()];
			if ((bServer.equals(""))||(bPort.equals(""))||(bProtocol.equals(""))){
				MessageDialog.openInformation(shell, "Error", "Please fill the field with valid values");
				return;
			}
			}
			
			try {
				String uri;
				if (!def)
				uri = bProtocol+"://"+bServer+":"+bPort;
				else
				uri = "tcp://m2m.eclipse.org:1883";
				MqttClient mqttClient = new MqttClient(uri, MqttClient.generateClientId());
				eventBroker.post(INamingConstants.CLIENT_CREATED, mqttClient);//this parts job ends here
				eventBroker.post(INamingConstants.CONSOLE_EVENT, "Client created");

			} catch (MqttException e1) {
				MessageDialog.openError(shell, "Wrong", "Something went wrong. Could not create client");
				eventBroker.post(INamingConstants.CONSOLE_EVENT, "Error trying to create a client");
				e1.printStackTrace();
			}
			
		}
	}

	private Composite parent;
	private Text server;
	private Text port;
	private Combo combo;

//	private String bPort="1883";
//	private String bProtocol="tcp";
//	private String bServer="m2m.eclipse.org";

	private String bPort;
	private String bProtocol;
	private String bServer;
	private MqttClient client;
	private String[] prots = new String[]{"tcp","local","ssl"};

	@PostConstruct
	public void postConstruct(Composite parent,@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, final IEventBroker eventBroker) {
		this.parent = parent;
		parent.setLayout(null);

		Label lblProtocol = new Label(parent, SWT.NONE);
		lblProtocol.setBounds(10, 13, 45, 15);
		lblProtocol.setText("Protocol");

		combo = new Combo(parent, SWT.READ_ONLY);
		combo.setBounds(61, 10, 67, 23);
		for (String s : prots) {
			combo.add(s);
		}
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setBounds(10, 40, 32, 15);
		lblNewLabel.setText("Server");

		server = new Text(parent, SWT.BORDER);
		server.setBounds(61, 37, 158, 21);

		Label lblPort = new Label(parent, SWT.NONE);
		lblPort.setBounds(134, 13, 22, 15);
		lblPort.setText("Port");

		port = new Text(parent, SWT.BORDER);
		port.setBounds(162, 10, 57, 21);

		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new CreationListener(shell, eventBroker,false));
		btnNewButton.setBounds(10, 61, 104, 24);
		btnNewButton.setText("Create from form");
		
		Button btnSandbox = new Button(parent, SWT.NONE);
		btnSandbox.setBounds(120, 61, 99, 24);
		btnSandbox.setText("Default sandbox");
		btnSandbox.addSelectionListener(new CreationListener(shell, eventBroker, true));
//		m_bindingContext = initDataBindings();
	}
//	protected DataBindingContext initDataBindings() {
//		DataBindingContext bindingContext = new DataBindingContext();
//		//
//		IObservableValue observeTextServerObserveWidget = WidgetProperties.text(SWT.Modify).observe(server);
//		IObservableValue bytesBServerObserveValue = PojoProperties.value("bServer").observe(this);
//		bindingContext.bindValue(observeTextServerObserveWidget, bytesBServerObserveValue, null, null);
//		

	public MqttClient getClient() {
		return client;
	}

	public void setClient(MqttClient client) {
		this.client = client;
	}
}
