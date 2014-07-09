package com.cxy.redisclient.presentation.server;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class AddServerDialog extends RedisClientDialog {

	protected Text text_3;
	protected Text text_4;
	protected Text text_5;
	protected Text text_6;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AddServerDialog(Shell parent, Image image) {
		super(parent, image);
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setSize(362, 284);
		shell.setText(getTitle());

		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tabFolder.setSize(290, 118);
		
		TabItem tbtmServerInformation = new TabItem(tabFolder, SWT.NONE);
		tbtmServerInformation.setText("Server");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmServerInformation.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setText("Name");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_3.setFocus();
		
		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setText("Host");
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(composite, SWT.NONE);
		lblPort.setText("Port");
		
		text_5 = new Text(composite, SWT.BORDER);
		text_5.setText("6379");
		text_5.selectAll();
		
		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setText("Password");
		
		text_6 = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_6.selectAll();
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button btnOk = new Button(composite_1, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				String name = text_3.getText();
				String host = text_4.getText();
				String port = text_5.getText();
				String password= text_6.getText();
				if(name.length() == 0 || host.length() == 0 || port.length() == 0)
					MessageDialog.openError(shell, "error","please input server information!");
				else {
					result = new Server(0, name, host, port, password);
					shell.dispose();
				}
					
					
			}
		});
		btnOk.setText("OK");
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				shell.dispose();
			}
		});
		btnCancel.setText("Cancel");
		super.createContents();
	}

	protected String getTitle() {
		return "Add Server";
	}
}
