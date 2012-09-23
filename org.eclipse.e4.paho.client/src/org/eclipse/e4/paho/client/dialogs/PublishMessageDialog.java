package org.eclipse.e4.paho.client.dialogs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

public class PublishMessageDialog extends Dialog {

	private String message;
	private Text messageText;
	private String topic;
	private int qos = -1;
	private Text qosText;
	private boolean retained = false;
	private Button retainedCheckbox;

	@Inject
	@Named(IServiceConstants.ACTIVE_SHELL)
	Shell shell;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public PublishMessageDialog(Shell parentShell, String topic) {
		super(parentShell);
		this.topic = topic;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;

		Label lblMessageToPublish = new Label(container, SWT.NONE);
		lblMessageToPublish.setText("Message to publish on topic: " + topic);

		Label lblQos = new Label(container, SWT.NONE);
		lblQos.setText("Qos");

		Label lblRetained = new Label(container, SWT.NONE);
		lblRetained.setText("Retained");

		messageText = new Text(container, SWT.BORDER);
		GridData gd_messageText = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_messageText.widthHint = 209;
		messageText.setLayoutData(gd_messageText);
		messageText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				message = new String(((Text) e.getSource()).getText());
			}
		});
		qosText = new Text(container, SWT.BORDER);
		qosText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		retainedCheckbox = new Button(container, SWT.CHECK);
		retainedCheckbox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				retained = retainedCheckbox.getSelection();
			}

		});
		qosText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					qos = Integer.valueOf(new String(((Text) e.getSource())
							.getText()));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					qos = -1;
				}
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(381, 176);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	@Override
	protected void okPressed() {
		boolean valid = true;
		if ((qosText.getText().isEmpty()) || (messageText.getText().isEmpty()))
			valid = false;
		try {
			int i = Integer.valueOf(qosText.getText());
			if ((i < 0) || (i > 2))
				valid = false;
		} catch (NumberFormatException nfe) {
			valid = false;
		}
		if (!valid) {
			MessageDialog.openWarning(shell, "Warning",
					"Invalid values");
			return;
		}
		super.okPressed();
	}
}
