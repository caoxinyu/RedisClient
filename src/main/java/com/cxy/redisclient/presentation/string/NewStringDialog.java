package com.cxy.redisclient.presentation.string;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import com.cxy.redisclient.dto.StringInfo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class NewStringDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text_1;
	private Text text_2;
	private String server;
	private int db;
	private String key;
	private Button btnOk;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewStringDialog(Shell parent, int style, String server, int db, String key) {
		super(parent, style);
		setText("SWT Dialog");
		this.server = server;
		this.db = db;
		this.key = key == null?"":key;
	}

	/**
	 * Open the dialog.
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
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(482, 283);
		shell.setText("Add String");
		
		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tabFolder.setSize(290, 124);
		
		TabItem tbtmString = new TabItem(tabFolder, SWT.NONE);
		tbtmString.setText("String");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmString.setControl(composite);
		composite.setLayout(new GridLayout(4, true));
		
		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText("Server");
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(server);
		
		Label lblDatabase = new Label(composite, SWT.NONE);
		lblDatabase.setText("Database");
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(String.valueOf(db));
		
		Label lblValue = new Label(composite, SWT.NONE);
		lblValue.setText("key");
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		text_1.setText(key);
		text_1.selectAll();
		text_1.setFocus();
		text_1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String newKey = text_1.getText() == null?"":text_1.getText();
				if(newKey.equals(key)) 
					btnOk.setEnabled(false);
				else
					btnOk.setEnabled(true);
			}
		});
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Value");
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));
		
		btnOk = new Button(composite_1, SWT.NONE);
		btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnOk.setEnabled(false);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String key = text_1.getText();
				String value = text_2.getText();
				
				if( key.length() == 0 || value.length() == 0){
					MessageDialog.openError(shell, "error","please input string information!");
				} else {
					result = new StringInfo(key, value);
					shell.dispose();
				}
			}
		});
		btnOk.setText("OK");
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		button_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});
		button_1.setText("Cancel");

	}
}
