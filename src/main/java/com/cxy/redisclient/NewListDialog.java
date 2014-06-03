package com.cxy.redisclient;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.cxy.redisclient.dto.ListInfo;

public class NewListDialog extends Dialog {

	protected ListInfo result = null;
	protected Shell shell;
	private Text text;
	private Table table;
	private String server;
	private int db;
	private String key;
	private Button btnDelete;
	private Button btnUp;
	private Button btnDown;
	private boolean headTail = true;
	private boolean exist = true;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NewListDialog(Shell parent, int style, String server, int db,
			String key) {
		super(parent, style);
		setText("SWT Dialog");
		this.server = server;
		this.db = db;
		this.key = key;
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
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 433);
		shell.setText("New List");

		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width
				/ 2, screenSize.y + screenSize.height / 2 - shellSize.height
				/ 2);

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 424, 359);

		TabItem tbtmList = new TabItem(tabFolder, SWT.NONE);
		tbtmList.setText("List");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmList.setControl(composite);

		Group grpOrderToAdd = new Group(composite, SWT.NONE);
		grpOrderToAdd.setText("Order to add into list");
		grpOrderToAdd.setBounds(10, 205, 396, 52);

		Button button_1 = new Button(grpOrderToAdd, SWT.RADIO);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				headTail = true;
			}
		});
		button_1.setSelection(true);
		button_1.setText("From head to tail");
		button_1.setBounds(10, 26, 158, 16);

		Button btnFromTailTo = new Button(grpOrderToAdd, SWT.RADIO);
		btnFromTailTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				headTail = false;
			}
		});
		btnFromTailTo.setText("From tail to head");
		btnFromTailTo.setBounds(228, 26, 158, 16);

		Group grpWhenListNot = new Group(composite, SWT.NONE);
		grpWhenListNot.setText("If list does not exist");
		grpWhenListNot.setBounds(10, 271, 396, 52);

		Button btnCreateList = new Button(grpWhenListNot, SWT.RADIO);
		btnCreateList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exist = true;
			}
		});
		btnCreateList.setSelection(true);
		btnCreateList.setText("Create a list");
		btnCreateList.setBounds(10, 26, 158, 16);

		Button btnNothingToDo = new Button(grpWhenListNot, SWT.RADIO);
		btnNothingToDo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exist = false;
			}
		});
		btnNothingToDo.setText("Do nothing");
		btnNothingToDo.setBounds(228, 26, 158, 16);

		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setBounds(10, 32, 49, 13);
		lblKey.setText("Key");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(61, 29, 345, 19);
		text.setText(key == null ? "" : key);
		text.selectAll();
		text.setFocus();

		Group grpValues = new Group(composite, SWT.NONE);
		grpValues.setText("Values");
		grpValues.setBounds(10, 54, 396, 145);

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setBounds(10, 20, 262, 115);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(200);
		tblclmnNewColumn.setText("New Column");

		Button btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog(shell,
						"Input values",
						"Values(multiple values are separated by ;)", "", null);
				if (inputDialog.open() == InputDialog.OK) {
					String values = inputDialog.getValue();
					String[] listValues = values.split(";");
					for (String value : listValues) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(value);
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

		btnUp = new Button(grpValues, SWT.NONE);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				TableItem[] selectedItems = table.getSelection();
				String selectedText = selectedItems[0].getText();

				int selected = table.getSelectionIndex();
				String upText = items[selected - 1].getText();

				items[selected].setText(upText);
				items[selected - 1].setText(selectedText);

				table.setSelection(selected - 1);
				if (selected == 1)
					tableItemSelected();
			}
		});
		btnUp.setEnabled(false);
		btnUp.setBounds(318, 78, 68, 23);
		btnUp.setText("UP");

		btnDown = new Button(grpValues, SWT.NONE);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				TableItem[] selectedItems = table.getSelection();
				String selectedText = selectedItems[0].getText();

				int selected = table.getSelectionIndex();
				String downText = items[selected + 1].getText();

				items[selected].setText(downText);
				items[selected + 1].setText(selectedText);

				table.setSelection(selected + 1);
				if (selected == table.getItemCount() - 2)
					tableItemSelected();
			}
		});
		btnDown.setEnabled(false);
		btnDown.setBounds(318, 107, 68, 23);
		btnDown.setText("Down");

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

		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				String key = text.getText();
				List<String> values = new ArrayList<String>();
				
				if(key.length() == 0 || items.length == 0)
					MessageDialog.openError(shell, "error","please input list information!");
				else {
					for (TableItem item : items) {
						values.add(item.getText());
					}
					result = new ListInfo(key, values, headTail, exist);
					shell.dispose();
				}
					
				
			}
		});
		btnOk.setBounds(104, 375, 68, 23);
		btnOk.setText("OK");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setBounds(276, 375, 68, 23);
		btnCancel.setText("Cancel");
	}

	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		if (items.length == 1) {
			btnDelete.setEnabled(true);
			if (table.getSelectionIndex() != 0)
				btnUp.setEnabled(true);
			else
				btnUp.setEnabled(false);
			if (table.getSelectionIndex() != table.getItemCount() - 1)
				btnDown.setEnabled(true);
			else
				btnDown.setEnabled(false);
		} else if (items.length > 1) {
			btnDelete.setEnabled(true);
			btnUp.setEnabled(false);
			btnDown.setEnabled(false);
		} else {
			btnDelete.setEnabled(false);
			btnUp.setEnabled(false);
			btnDown.setEnabled(false);
		}
	}
}
