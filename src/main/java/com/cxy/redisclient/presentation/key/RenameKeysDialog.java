package com.cxy.redisclient.presentation.key;

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

import com.cxy.redisclient.dto.RenameInfo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class RenameKeysDialog extends Dialog {

	protected Object result = null;
	protected Shell shell;
	private Text text_2;
	private String server;
	private int db;
	private String oldContainer;
	private Button button;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RenameKeysDialog(Shell parent, int style,String server, int db, String oldContainer) {
		super(parent, style);
		setText("SWT Dialog");
		this.server = server;
		this.db = db;
		this.oldContainer = oldContainer == null?"":oldContainer;
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
		shell.setSize(326, 201);
		shell.setText("Rename Keys");
		
		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 300, 121);
		
		TabItem tbtmString = new TabItem(tabFolder, SWT.NONE);
		tbtmString.setText("String");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmString.setControl(composite);
		
		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText("Server");
		lblKey.setBounds(10, 13, 45, 19);
		
		Label lblNewKey = new Label(composite, SWT.NONE);
		lblNewKey.setText("New key");
		lblNewKey.setBounds(10, 44, 56, 16);
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setBounds(72, 41, 210, 19);
		text_2.setText(oldContainer);
		text_2.selectAll();
		text_2.setFocus();
		text_2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String newContainer = text_2.getText() == null?"":text_2.getText();
				if(newContainer.equals(oldContainer)) 
					button.setEnabled(false);
				else
					button.setEnabled(true);
			}
		});
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(72, 13, 78, 19);
		label_1.setText(server);
		
		Label lblDatabase = new Label(composite, SWT.NONE);
		lblDatabase.setText("Database");
		lblDatabase.setBounds(156, 13, 61, 19);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(223, 13, 45, 19);
		label_3.setText(String.valueOf(db));
		
		final Button btnCheckButton = new Button(composite, SWT.CHECK);
		btnCheckButton.setSelection(true);
		btnCheckButton.setBounds(10, 69, 272, 16);
		btnCheckButton.setText("Overwritten if exists");
		
		button = new Button(shell, SWT.NONE);
		button.setEnabled(false);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String newContainer = text_2.getText();
				boolean overwritten = btnCheckButton.getSelection(); 
				
				if( newContainer.length() == 0){
					MessageDialog.openError(shell, "error","please input new key name!");
				} else {
					result = new RenameInfo(newContainer, overwritten);
					shell.dispose();
				}
			}
		});
		button.setText("OK");
		button.setBounds(61, 144, 68, 23);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});
		button_1.setText("Cancel");
		button_1.setBounds(188, 144, 68, 23);

	}
}
