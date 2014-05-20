package com.cxy.redisclient;

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
	protected Shell shlNewServer;
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
		shlNewServer.open();
		shlNewServer.layout();
		Display display = getParent().getDisplay();
		while (!shlNewServer.isDisposed()) {
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
		shlNewServer = new Shell(getParent(), getStyle());
		shlNewServer.setSize(WIDTH, HEIGHT);
		shlNewServer.setText(getTitle());

		Rectangle screenSize = shlNewServer.getParent().getBounds();
		shlNewServer.setLocation(screenSize.x + screenSize.width / 2 - WIDTH / 2,
				screenSize.y + screenSize.height / 2 - HEIGHT / 2);
		
		TabFolder tabFolder = new TabFolder(shlNewServer, SWT.NONE);
		tabFolder.setBounds(10, 10, 300, 121);
		
		TabItem tbtmServerInformation = new TabItem(tabFolder, SWT.NONE);
		tbtmServerInformation.setText("Server");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmServerInformation.setControl(composite);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setBounds(10, 15, 29, 13);
		lblName.setText("Name");
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(45, 12, 237, 19);
		
		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setBounds(10, 43, 29, 13);
		lblHost.setText("Host");
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setBounds(45, 40, 237, 19);
		
		text_5 = new Text(composite, SWT.BORDER);
		text_5.setText("6379");
		text_5.setBounds(45, 68, 237, 19);
		
		Label lblPort = new Label(composite, SWT.NONE);
		lblPort.setText("Port");
		lblPort.setBounds(10, 71, 30, 13);
		
		Button btnOk = new Button(shlNewServer, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				String name = text_3.getText();
				String host = text_4.getText();
				String port = text_5.getText();
				if(name.length() == 0 || host.length() == 0 || port.length() == 0)
					MessageDialog.openError(shlNewServer, "error","please input server information!");
				else {
					result = new Server(0, name, host, port);
					shlNewServer.dispose();
				}
					
					
			}
		});
		btnOk.setBounds(61, 144, 68, 23);
		btnOk.setText("OK");
		
		Button btnCancel = new Button(shlNewServer, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				shlNewServer.dispose();
			}
		});
		btnCancel.setBounds(188, 144, 68, 23);
		btnCancel.setText("Cancel");
		
	}

	protected String getTitle() {
		return "Add Server";
	}
}
