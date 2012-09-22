package org.eclipse.e4.paho.client.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.paho.client.naming.INamingConstants;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ConsolePart {
	
	private Composite parent;
	private Text text;
	
	@PostConstruct
	public void postConstruct(Composite parent,IEclipseContext context){
		this.parent = parent;
//		parent.setLayout(Gr);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		scrolledComposite.setBounds(0, 0, 294, 86);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
//		scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		text = new Text(scrolledComposite, SWT.BORDER | SWT.MULTI);
		context.set(INamingConstants.CONSLE_CONTROL, text);
		scrolledComposite.setContent(text);
		scrolledComposite.setMinSize(text.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@Inject
	public void consoleUpdateEvent(@UIEventTopic(INamingConstants.CONSOLE_EVENT) @Optional String message){
		if (message==null) return;
		text.append(message+" \n");
	}
}
