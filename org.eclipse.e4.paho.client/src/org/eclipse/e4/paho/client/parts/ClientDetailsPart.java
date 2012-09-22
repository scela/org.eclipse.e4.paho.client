package org.eclipse.e4.paho.client.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.paho.client.dialogs.ConnectionSettingsDialog;
import org.eclipse.e4.paho.client.dialogs.PublishMessageDialog;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ClientDetailsPart{
	private DataBindingContext m_bindingContext;

	private Composite parent;
	private Text id;
	private Text URL;
	private Button btnConnectToServer;
	private Label lblStatus;
	@Inject
	@Named(IServiceConstants.ACTIVE_SHELL)
	private Shell shell;

	private MqttClient client;
	private Text topic;
	private Text clientLog;
	private Button btnUnsubscribe;

	private UISynchronize sync;

	@PostConstruct
	public void postConstruct(
			Composite parent,
			final UISynchronize sync,
			@Optional @Named(INamingConstants.JUST_CREATED) final MqttClient client, final IEventBroker eventBroker) {
		this.parent = parent;
		parent.setLayout(null);

		this.client = client;
		this.sync = sync;
		try {
			this.client.setCallback(new MqttCallback() {@Override
				public void connectionLost(Throwable cause) {
				eventBroker.post(INamingConstants.CONSOLE_EVENT, "Lost connectivity");
//				clientLog.append("Connection lost. \n");
				cause.printStackTrace();
				sync.asyncExec(new Runnable() {
					
					@Override
					public void run() {

						lblStatus.setText("Disconnected");
						btnConnectToServer.setText("Connect");
						
						
					}
				});
				
			}

			@Override
			public void messageArrived(final MqttTopic topic, final MqttMessage message)
					throws Exception {
				sync.asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							clientLog.append("Got message "+new String(message.getPayload())+" from topic "+topic.getName()+"\n");
						} catch (MqttException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
			}

			@Override
			public void deliveryComplete(final MqttDeliveryToken token) {
//				if (token.isComplete()){
//							
//								try {
//								// TODO Auto-generated method stub
//								clientLog.append("Publishing of message "+token.getMessage().getPayload()+" is complete \n");
//							} catch (MqttException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							}
//				
			}});
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Label lblId = new Label(parent, SWT.NONE);
		lblId.setBounds(10, 33, 14, 15);
		lblId.setText("ID:");

		id = new Text(parent, SWT.BORDER);
		id.setEnabled(false);
		id.setBounds(40, 30, 171, 21);

		Label lblUrl = new Label(parent, SWT.NONE);
		lblUrl.setBounds(10, 61, 24, 15);
		lblUrl.setText("URL:");

		URL = new Text(parent, SWT.BORDER);
		URL.setEnabled(false);
		URL.setBounds(40, 61, 171, 21);

		btnConnectToServer = new Button(parent, SWT.NONE);
		btnConnectToServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
						if (client.isConnected()) {
							try {
								client.disconnect();
								System.err.println(client.getClientId()
										+ " disconnected");
								btnConnectToServer.setText("Connect");
								lblStatus.setText("Disconnected");
								eventBroker.post(INamingConstants.CONSOLE_EVENT, "Disconnected successfully");
							} catch (MqttException e1) {
								lblStatus.setText("Error trying to disconnect");
								eventBroker.post(INamingConstants.CONSOLE_EVENT, "Error trying to disconnect");
								e1.printStackTrace();
							} finally {
								return;
							}
						}
						try {
							ConnectionSettingsDialog d = new ConnectionSettingsDialog(
									shell);
							if (d.open() == Dialog.OK) {
								client.connect(d.getOptions());
								System.err.println(client.getClientId()
										+ " connected");
								lblStatus.setText("Connected");
								btnConnectToServer.setText("Disconnect");
								eventBroker.post(INamingConstants.CONSOLE_EVENT, "Connected");
								
							}
						}catch (MqttException e1) {
							lblStatus.setText("Error !");
							MessageDialog.openError(shell, "Error", "Error trying to connect. See activity log for more details");
							eventBroker.post(INamingConstants.CONSOLE_EVENT, "Error trying to connect: "+e1.getCause());
						}

					}
				});
		btnConnectToServer.setBounds(100, 1, 111, 25);
		btnConnectToServer.setText("Connect to server");

		lblStatus = new Label(parent, SWT.NONE);
		lblStatus.setBounds(10, 6, 72, 15);
		lblStatus.setText("Disconnected");
		
		topic = new Text(parent, SWT.BORDER);
		topic.setBounds(10, 118, 201, 21);
		
		Label lblTopic = new Label(parent, SWT.NONE);
		lblTopic.setBounds(10, 97, 30, 15);
		lblTopic.setText("Topic");
		
		
		btnUnsubscribe = new Button(parent, SWT.NONE);
		btnUnsubscribe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (topic.getText().isEmpty() || (!client.isConnected())) return;
				try {
					client.unsubscribe(topic.getText());
					clientLog.append("Unsubscribed from topic: "+topic.getText()+"\n");
					eventBroker.post(INamingConstants.CONSOLE_EVENT, "Unsubscribtion");
				} catch (MqttException e1) {
					clientLog.append("Error unsubscribing from topic: "+topic.getText()+"\n");
				}
				
			}
		});
		btnUnsubscribe.setBounds(10, 147, 75, 25);
		btnUnsubscribe.setText("Unsubscribe");
		
		Button btnSubscribe = new Button(parent, SWT.NONE);
		btnSubscribe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (topic.getText().isEmpty() || (!client.isConnected())) return;
				try {
					client.subscribe(topic.getText());
					clientLog.append("Subscribed to topic: "+topic.getText()+"\n");
					System.err.println("Subscribed to topic: "
							+ topic.getText() + "\n");
					eventBroker.post(INamingConstants.CONSOLE_EVENT, "Subscribtion");
				} catch (MqttSecurityException e1) {
					clientLog.append("Security error subscribing to topic: "+topic.getText()+"\n");
					e1.printStackTrace();
				} catch (MqttException e1) {
					clientLog.append("Error subscribing to topic: "+topic.getText()+"\n");
					e1.printStackTrace();
				}
				catch (Exception e1){
					e1.printStackTrace();
				}
			}
				
			});
		btnSubscribe.setBounds(91, 147, 63, 25);
		btnSubscribe.setText("Subscribe");
		
		Button btnPublish = new Button(parent, SWT.NONE);
		btnPublish.addSelectionListener(new SelectionAdapter() {
			private MqttDeliveryToken token;

			@Override
			public void widgetSelected(SelectionEvent e) {
				final String s = topic.getText();
				if (s.isEmpty() || (!client.isConnected())) return;
				PublishMessageDialog pmd = new PublishMessageDialog(shell, s);
				if (pmd.open()==Dialog.OK){
					final MqttTopic topic2 = client.getTopic(s);
					final String m = pmd.getMessage();
					boolean r = pmd.isRetained();
					final int qos = pmd.getQos();
					if ((qos>2)||(qos<0)||(m.isEmpty())) {
						System.err.println("validation error");
						return;
					}
					final MqttMessage mqttMessage = new MqttMessage(m.getBytes());
					mqttMessage.setQos(qos);
					mqttMessage.setRetained(r);
						sync.asyncExec(new Runnable() {
							
							@Override
							public void run() {
								try {
								token = topic2.publish(mqttMessage);
								clientLog.append("Published message \""+m+"\" with qos "+qos+"\n");
								System.err.println("Published message \"" + m
										+ "\" with qos " + qos + "\n");
								eventBroker.post(INamingConstants.CONSOLE_EVENT, "Publishing");
								} catch (MqttPersistenceException e1) {
									clientLog.append("Failed to publish message \""+m+"\" with qos "+qos+"\n");
									e1.printStackTrace();
								} catch (MqttException e1) {
									clientLog.append("Failed to publish message \""+m+"\" with qos "+qos+"\n");
									e1.printStackTrace();
								}
							}
						});
						
						
					
				}
			}
		});
		btnPublish.setBounds(160, 147, 51, 25);
		btnPublish.setText("Publish");
		
		clientLog = new Text(parent, SWT.BORDER | SWT.MULTI);
		clientLog.setEditable(false);
		clientLog.setBounds(243, 33, 354, 134);
		
		Label lblClientActivity = new Label(parent, SWT.NONE);
		lblClientActivity.setBounds(243, 11, 72, 15);
		lblClientActivity.setText("Client activity");
		System.err.println(client);
		m_bindingContext = initDataBindings();
	}

	@PreDestroy
	public void preDestroy() throws MqttException {

		if (client.isConnected()) {
			client.unsubscribe("#");
			client.disconnect();
		}

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
