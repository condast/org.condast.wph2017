package org.condast.wph;

import org.condast.wph.service.Dispatcher;
import org.condast.wph.ui.swt.WPHFrontend;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class BasicEntryPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;

	@Override
    protected void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        WPHFrontend frontend = new WPHFrontend(parent, SWT.CHECK);
        frontend.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
        Dispatcher.getInstance().startApplication(frontend);
    }
}