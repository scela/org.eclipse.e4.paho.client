package org.eclipse.e4.paho.client.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ClientDetailsPart {
	private DataBindingContext m_bindingContext;
	
	private Composite parent;
	private Text id;
	private Text URL;
	@Inject
	@Optional
	@Named(INamingConstants.JUST_CREATED)
	private MqttClient client;
	private Button btnConnectToServer;
	private Label lblDisconnected;
	private MqttConnectOptions cop = new MqttConnectOptions();
	
	@PostConstruct
	public void postConstruct(Composite parent, final UISynchronize sync){
		this.parent = parent;
		parent.setLayout(null);
		
		Label lblId = new Label(parent, SWT.NONE);
		lblId.setBounds(10, 33, 14, 15);
		lblId.setText("ID:");
		
		id = new Text(parent, SWT.BORDER);
		id.setBounds(40, 30, 165, 21);
		
		Label lblUrl = new Label(parent, SWT.NONE);
		lblUrl.setBounds(10, 61, 24, 15);
		lblUrl.setText("URL:");
		
		URL = new Text(parent, SWT.BORDER);
		URL.setBounds(40, 61, 165, 21);
		
		btnConnectToServer = new Button(parent, SWT.NONE);
		btnConnectToServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sync.asyncExec(new Runnable() {
					
					@Override
					public void run() {
						try {
							client.connect();
						} catch (MqttSecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MqttException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
			}
		});
		btnConnectToServer.setBounds(100, 1, 105, 25);
		btnConnectToServer.setText("Connect to server");
		
		lblDisconnected = new Label(parent, SWT.NONE);
		lblDisconnected.setBounds(10, 6, 72, 15);
		lblDisconnected.setText("Disconnected");
		System.err.println(client);
		m_bindingContext = initDataBindings();
	}
	
	@PreDestroy
	public void preDestroy(){
		
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextIdObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(id);
		IObservableValue clientIdClientObserveValue = PojoProperties.value("clientId").observe(client);
		bindingContext.bindValue(observeTextIdObserveWidget, clientIdClientObserveValue, null, null);
		//
		IObservableValue observeTextURLObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(URL);
		IObservableValue serverURIClientObserveValue = PojoProperties.value("serverURI").observe(client);
		bindingContext.bindValue(observeTextURLObserveWidget, serverURIClientObserveValue, null, null);
		//
		return bindingContext;
	}
}
