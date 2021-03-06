package org.eclipse.e4.paho.client.dialogs;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.directory.InvalidSearchControlsException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.e4.paho.client.databinding.StringToCharArrayConverter;
import org.eclipse.e4.paho.client.databinding.StringToMqttMessageConverter;
import org.eclipse.e4.paho.client.databinding.StringToNumberConverter;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ConnectionSettingsDialog extends Dialog {
	private DataBindingContext m_bindingContext;
	private Text keepAlive;
	private Text username;
	private Text password;
	private Text connTimeout;

	@Inject
	@Named(IServiceConstants.ACTIVE_SHELL)
	private Shell shell;
	
	private MqttConnectOptions options = new MqttConnectOptions();
	private Button cleanSession ;
	protected boolean def=false;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConnectionSettingsDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("KeepAlive interval");
		
		keepAlive = new Text(container, SWT.BORDER);
		keepAlive.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		keepAlive.setText("60");
		Label lblUserName = new Label(container, SWT.NONE);
		lblUserName.setText("User name");
		
		username = new Text(container, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText("Password");
		
		password = new Text(container, SWT.BORDER);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCleanSession = new Label(container, SWT.NONE);
		lblCleanSession.setText("Clean session");
		
		cleanSession = new Button(container, SWT.CHECK);
		cleanSession.setSelection(true);
		Label lblConnectionTimeout = new Label(container, SWT.NONE);
		lblConnectionTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionTimeout.setText("Connection timeout");
		
		connTimeout = new Text(container, SWT.BORDER);
		connTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		connTimeout.setText("30");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(247, 259);
	}

	public MqttConnectOptions getOptions() {
		return options;
	}

	public void setOptions(MqttConnectOptions options) {
		this.options = options;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
//		//
//		IObservableValue observeTextKeepAliveObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(keepAlive);
//		IObservableValue keepAliveIntervalOptionsObserveValue = PojoProperties.value("keepAliveInterval").observe(options);
//		UpdateValueStrategy strategy = new UpdateValueStrategy();
//		strategy.setConverter(new StringToNumberConverter());
//		bindingContext.bindValue(observeTextKeepAliveObserveWidget, keepAliveIntervalOptionsObserveValue, strategy, null);
//		//
//		IObservableValue observeTextWillMsgObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(willMsg);
//		IObservableValue willMessageOptionsObserveValue = PojoProperties.value("willMessage").observe(options);
//		UpdateValueStrategy strategy_1 = new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER);
//		strategy_1.setConverter(new StringToMqttMessageConverter());
//		bindingContext.bindValue(observeTextWillMsgObserveWidget, willMessageOptionsObserveValue, null, strategy_1);
//		//
//		IObservableValue observeTextUsernameObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(username);
//		IObservableValue userNameOptionsObserveValue = PojoProperties.value("userName").observe(options);
//		bindingContext.bindValue(observeTextUsernameObserveWidget, userNameOptionsObserveValue, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
//		//
//		IObservableValue observeTextPasswordObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(password);
//		IObservableValue passwordOptionsObserveValue = PojoProperties.value("password").observe(options);
//		UpdateValueStrategy strategy_2 = new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER);
//		strategy_2.setConverter(new StringToCharArrayConverter());
//		bindingContext.bindValue(observeTextPasswordObserveWidget, passwordOptionsObserveValue, null, strategy_2);
//		//
//		IObservableValue observeSelectionCleanSessionObserveWidget = WidgetProperties.selection().observe(cleanSession);
//		IObservableValue cleanSessionOptionsObserveValue = PojoProperties.value("cleanSession").observe(options);
//		bindingContext.bindValue(observeSelectionCleanSessionObserveWidget, cleanSessionOptionsObserveValue, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
//		//
		return bindingContext;
	}
	
	@Override
	protected void okPressed() {
		if (!def){
			if (!validateOptions()){
				MessageDialog.openError(shell, "Error", "Error validating connection settings.");
				return;
			}
			options = new MqttConnectOptions();
			options.setCleanSession(cleanSession.getSelection());
			options.setConnectionTimeout(Integer.valueOf(connTimeout.getText()));
			options.setKeepAliveInterval(Integer.valueOf(keepAlive.getText()));
			if (!username.getText().isEmpty())
				options.setUserName(username.getText());
			if (!password.getText().isEmpty())
			options.setPassword(password.getTextChars());
			
		}
		super.okPressed();
	}

	private boolean validateOptions() {
		if ((connTimeout.getText().isEmpty())||(keepAlive.getText().isEmpty())) return false
				;
		try {
		Integer.valueOf(connTimeout.getText());
		Integer.valueOf(keepAlive.getText());
		}
		catch (NumberFormatException nfe){
			return false;
		}
		return true;
	}
}
