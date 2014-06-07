package com.cxy.redisclient.presentation.server;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.domain.Server;

public class AddServerDialog extends Dialog {

	private static final int HEIGHT = 201;
	private static final int WIDTH = 326;
	protected Object result = null;
	protected Shell shell;
	protected Text text_3;
	protected Text text_4;
	protected Text text_5;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AddServerDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(WIDTH, HEIGHT);
		shell.setText(getTitle());

		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 300, 121);
		
		TabItem tbtmServerInformation = new TabItem(tabFolder, SWT.NONE);
		tbtmServerInformation.setText("Server");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmServerInformation.setControl(composite);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setBounds(10, 10, 35, 16);
		lblName.setText("Name");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(56, 10, 226, 19);
		text_3.setFocus();
		
		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setBounds(10, 36, 29, 16);
		lblHost.setText("Host");
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setBounds(56, 36, 226, 19);
		
		text_5 = new Text(composite, SWT.BORDER);
		text_5.setText("6379");
		text_5.selectAll();
		text_5.setBounds(56, 62, 226, 19);
		
		Label lblPort = new Label(composite, SWT.NONE);
		lblPort.setText("Port");
		lblPort.setBounds(10, 62, 30, 16);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				String name = text_3.getText();
				String host = text_4.getText();
				String port = text_5.getText();
				if(name.length() == 0 || host.length() == 0 || port.length() == 0)
					MessageDialog.openError(shell, "error","please input server information!");
				else {
					result = new Server(0, name, host, port);
					shell.dispose();
				}
					
					
			}
		});
		btnOk.setBounds(61, 144, 68, 23);
		btnOk.setText("OK");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				shell.dispose();
			}
		});
		btnCancel.setBounds(188, 144, 68, 23);
		btnCancel.setText("Cancel");
		
	}

	protected String getTitle() {
		return "Add Server";
	}
}
