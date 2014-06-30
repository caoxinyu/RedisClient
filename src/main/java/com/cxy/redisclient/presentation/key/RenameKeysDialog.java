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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class RenameKeysDialog extends Dialog {

	protected Object result = null;
	protected Shell shlRenameKey;
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
		shlRenameKey.open();
		shlRenameKey.layout();
		Display display = getParent().getDisplay();
		while (!shlRenameKey.isDisposed()) {
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
		shlRenameKey = new Shell(getParent(), getStyle());
		shlRenameKey.setSize(430, 258);
		shlRenameKey.setText("Rename Key");
		
		Rectangle screenSize = shlRenameKey.getParent().getBounds();
		Rectangle shellSize = shlRenameKey.getBounds();
		shlRenameKey.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		shlRenameKey.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shlRenameKey, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tabFolder.setSize(290, 122);
		
		TabItem tbtmString = new TabItem(tabFolder, SWT.NONE);
		tbtmString.setText("Key");
		
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
		
		Label lblNewKey = new Label(composite, SWT.NONE);
		lblNewKey.setText("New key");
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
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
		
		final Button btnCheckButton = new Button(composite, SWT.CHECK);
		btnCheckButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		btnCheckButton.setSelection(true);
		btnCheckButton.setText("Overwritten if exists");
		
		Composite composite_1 = new Composite(shlRenameKey, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));
		
		button = new Button(composite_1, SWT.NONE);
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		button.setEnabled(false);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String newContainer = text_2.getText();
				boolean overwritten = btnCheckButton.getSelection(); 
				
				if( newContainer.length() == 0){
					MessageDialog.openError(shlRenameKey, "error","please input new key name!");
				} else {
					result = new RenameInfo(newContainer, overwritten);
					shlRenameKey.dispose();
				}
			}
		});
		button.setText("OK");
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		button_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shlRenameKey.dispose();
			}
		});
		button_1.setText("Cancel");

	}
}
