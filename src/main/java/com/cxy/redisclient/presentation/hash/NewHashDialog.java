package com.cxy.redisclient.presentation.hash;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.dto.HashInfo;

public class NewHashDialog extends Dialog {

	protected Object result;
	protected Shell shlNewHash;
	private String server;
	private int db;
	private String key;
	private Text text;
	private Table table;
	private Button btnDelete;
	private Button btnOk;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewHashDialog(Shell parent, int style, String server, int db,
			String key) {
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
		shlNewHash.open();
		shlNewHash.layout();
		Display display = getParent().getDisplay();
		while (!shlNewHash.isDisposed()) {
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
		shlNewHash = new Shell(getParent(), getStyle());
		shlNewHash.setSize(450, 319);
		shlNewHash.setText("New Hash");

		Rectangle screenSize = shlNewHash.getParent().getBounds();
		Rectangle shellSize = shlNewHash.getBounds();
		shlNewHash.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width
				/ 2, screenSize.y + screenSize.height / 2 - shellSize.height
				/ 2);

		TabFolder tabFolder = new TabFolder(shlNewHash, SWT.NONE);
		tabFolder.setBounds(10, 10, 424, 234);

		TabItem tbtmList = new TabItem(tabFolder, SWT.NONE);
		tbtmList.setText("Hash");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmList.setControl(composite);

		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setBounds(10, 32, 49, 13);
		lblKey.setText("Key");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(61, 29, 345, 19);
		text.setText(key);
		text.selectAll();
		text.setFocus();
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String newKey = text.getText() == null?"":text.getText();
				if(newKey.equals(key)) 
					btnOk.setEnabled(false);
				else
					btnOk.setEnabled(true);
			}
		});
		

		Group grpValues = new Group(composite, SWT.NONE);
		grpValues.setText("Values");
		grpValues.setBounds(10, 54, 396, 145);

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setBounds(10, 20, 262, 115);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(56);
		tblclmnNewColumn.setText("Field");
		
		TableColumn tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(164);
		tblclmnMember.setText("Value");

		Button btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog(shlNewHash,
						"Input values",
						"Values(multiple values are separated by ; field and value are separated by ,)", "", null);
				if (inputDialog.open() == InputDialog.OK) {
					String values = inputDialog.getValue();
					String[] zsetValues = values.split(";");
					for (String value : zsetValues) {
						TableItem item = new TableItem(table, SWT.NONE);
						String[] zset = value.split(",");
						item.setText(zset);
					}
				}
			}
		});
		btnAdd.setBounds(318, 20, 68, 23);
		btnAdd.setText("Add");

		btnDelete = new Button(grpValues, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				for (TableItem item : items) {
					item.dispose();
				}
				tableItemSelected();
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setBounds(318, 49, 68, 23);
		btnDelete.setText("Delete");

		Label label = new Label(composite, SWT.NONE);
		label.setText("Server");
		label.setBounds(10, 10, 45, 13);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(server);
		label_1.setBounds(61, 10, 89, 13);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("Database");
		label_2.setBounds(248, 10, 45, 13);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(String.valueOf(db));
		label_3.setBounds(331, 10, 45, 13);

		btnOk = new Button(shlNewHash, SWT.NONE);
		btnOk.setEnabled(false);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				String key = text.getText();
				Map<String, String> values = new HashMap<String, String>();
				
				if(key.length() == 0 || items.length == 0)
					MessageDialog.openError(shlNewHash, "error","please input hash information!");
				else {
					for (TableItem item : items) {
						values.put(item.getText(0), item.getText(1));
					}
					result = new HashInfo(key, values);
					shlNewHash.dispose();
				}
					
				
			}
		});
		btnOk.setBounds(104, 260, 68, 23);
		btnOk.setText("OK");

		Button btnCancel = new Button(shlNewHash, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlNewHash.dispose();
			}
		});
		btnCancel.setBounds(276, 260, 68, 23);
		btnCancel.setText("Cancel");

	}
	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		if (items.length == 1) {
			btnDelete.setEnabled(true);
		} else if (items.length > 1) {
			btnDelete.setEnabled(true);
		} else {
			btnDelete.setEnabled(false);
		}
	}

}
