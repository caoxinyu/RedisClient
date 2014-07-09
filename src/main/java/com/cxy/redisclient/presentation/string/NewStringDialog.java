package com.cxy.redisclient.presentation.string;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import com.cxy.redisclient.dto.StringInfo;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class NewStringDialog extends RedisClientDialog {

	protected final class ModifyKey implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			String newKey = text_key.getText() == null?"":text_key.getText();
			if(newKey.equals(key)) 
				btnOk.setEnabled(false);
			else
				btnOk.setEnabled(true);
		}
	}

	protected Text text_key;
	protected Text text_value;
	private String server;
	private int db;
	protected String key;
	protected Button btnOk;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewStringDialog(Shell parent, Image image, String server, int db, String key) {
		super(parent, image);
		this.server = server;
		this.db = db;
		this.key = key == null?"":key;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setSize(622, 284);
		shell.setText("New String");
		
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
		
		text_key = new Text(composite, SWT.BORDER);
		text_key.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		text_key.setText(key);
		text_key.selectAll();
		text_key.setFocus();
		text_key.addModifyListener(new ModifyKey());
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Value");
		
		text_value = new Text(composite, SWT.BORDER);
		text_value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		btnOk = new Button(composite_1, SWT.NONE);
		btnOk.setEnabled(false);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String key = text_key.getText();
				String value = text_value.getText();
				
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
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});
		button_1.setText("Cancel");

		super.createContents();
	}
}
