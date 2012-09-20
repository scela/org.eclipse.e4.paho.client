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

	private Composite parent;
	private Text server;
	private Text port;
	private Combo combo;

	private String bPort="1883";
	private String bProtocol="tcp";
	private String bServer="m2m.eclipse.org";

//	private String bPort="";
//	private String bProtocol;
//	private String bServer;
	private MqttClient client;

	@PostConstruct
	public void postConstruct(Composite parent,@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, final IEventBroker eventBroker) {
		this.parent = parent;
		parent.setLayout(null);

		Label lblProtocol = new Label(parent, SWT.NONE);
		lblProtocol.setBounds(10, 13, 45, 15);
		lblProtocol.setText("Protocol");

		combo = new Combo(parent, SWT.NONE);
		combo.setBounds(61, 10, 97, 23);
		combo.add("TCP");
		combo.add("LOCAL");
		combo.add("SSL");
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setBounds(10, 40, 32, 15);
		lblNewLabel.setText("Server");

		server = new Text(parent, SWT.BORDER);
		server.setBounds(61, 37, 97, 21);

		Label lblPort = new Label(parent, SWT.NONE);
		lblPort.setBounds(10, 68, 22, 15);
		lblPort.setText("Port");

		port = new Text(parent, SWT.BORDER);
		port.setBounds(61, 64, 45, 21);

		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ((bServer.equals(""))||(bPort.equals(""))||(bProtocol.equals(""))){
					MessageDialog.openInformation(shell, "Error", "Please fill the field with valid values");
					return;
				}
				
				try {
					MqttClient mqttClient = new MqttClient(bProtocol+"://"+bServer+":"+bPort, MqttClient.generateClientId());
					eventBroker.post(INamingConstants.CLIENT_CREATED, mqttClient);//this parts job ends here

				} catch (MqttException e1) {
					MessageDialog.openError(shell, "Wrong", "Something went wrong: "+e1.getMessage());
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(112, 64, 46, 25);
		btnNewButton.setText("Create");
//		m_bindingContext = initDataBindings();
	}
//	protected DataBindingContext initDataBindings() {
//		DataBindingContext bindingContext = new DataBindingContext();
//		//
//		IObservableValue observeTextServerObserveWidget = WidgetProperties.text(SWT.Modify).observe(server);
//		IObservableValue bytesBServerObserveValue = PojoProperties.value("bServer").observe(this);
//		bindingContext.bindValue(observeTextServerObserveWidget, bytesBServerObserveValue, null, null);
//		
		
		//
//		return bindingContext;
//	}
}
