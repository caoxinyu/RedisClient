package com.cxy.redisclient.presentation.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.dto.ListInfo;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class NewListDialog extends RedisClientDialog {

	protected final class ModifyKey implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			String newKey = text.getText() == null ? "" : text.getText();
			if (newKey.equals(key))
				btnOk.setEnabled(false);
			else
				btnOk.setEnabled(true);
		}
	}

	protected Text text;
	protected Table table;
	protected int id;
	protected String server;
	protected int db;
	protected String key;
	private Button btnDelete;
	private Button btnUp;
	private Button btnDown;
	private boolean headTail = true;
	private boolean exist = true;
	protected Button btnOk;
	protected Group grpWhenListNot;
	protected Group grpValues;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NewListDialog(Shell parent, Image image, int id, String server, int db,
			String key) {
		super(parent,  image);
		this.id = id;
		this.server = server;
		this.db = db;
		this.key = key == null ? "" : key;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setSize(805, 638);
		shell.setText("New List");

		shell.setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tabFolder.setSize(414, 360);

		TabItem tbtmList = new TabItem(tabFolder, SWT.NONE);
		tbtmList.setText("List");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmList.setControl(composite);
		composite.setLayout(new GridLayout(4, true));

		Label label = new Label(composite, SWT.NONE);
		label.setText("Server");

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(server);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("Database");

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(String.valueOf(db));

		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText("Key");

		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		text.setText(key);
		text.selectAll();
		text.setFocus();
		text.addModifyListener(new ModifyKey());

		grpValues = new Group(composite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText("Values");
		grpValues.setLayout(new GridLayout(4, false));

		table = getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 4));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new EditListener(table));

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(200);
		tblclmnNewColumn.setText("New Column");

		Button btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
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
		btnAdd.setText("Add");

		btnDelete = new Button(grpValues, SWT.NONE);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
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
		btnDelete.setText("Delete");

		btnUp = new Button(grpValues, SWT.NONE);
		btnUp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
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
		btnUp.setText("UP");

		btnDown = new Button(grpValues, SWT.NONE);
		btnDown.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1,
				1));
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
		btnDown.setText("Down");

		Group grpOrderToAdd = new Group(composite, SWT.NONE);
		grpOrderToAdd.setLayout(new GridLayout(2, false));
		grpOrderToAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 4, 1));
		grpOrderToAdd.setText("Order to add into list");

		Button button_1 = new Button(grpOrderToAdd, SWT.RADIO);
		button_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				headTail = true;
			}
		});
		button_1.setSelection(true);
		button_1.setText("From head to tail");

		Button btnFromTailTo = new Button(grpOrderToAdd, SWT.RADIO);
		btnFromTailTo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnFromTailTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				headTail = false;
			}
		});
		btnFromTailTo.setText("From tail to head");

		grpWhenListNot = new Group(composite, SWT.NONE);
		grpWhenListNot.setLayout(new GridLayout(2, true));
		grpWhenListNot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 4, 1));
		grpWhenListNot.setText("If list does not exist");

		Button btnCreateList = new Button(grpWhenListNot, SWT.RADIO);
		btnCreateList.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnCreateList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exist = true;
			}
		});
		btnCreateList.setSelection(true);
		btnCreateList.setText("Create a list");

		Button btnNothingToDo = new Button(grpWhenListNot, SWT.RADIO);
		btnNothingToDo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnNothingToDo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exist = false;
			}
		});
		btnNothingToDo.setText("Do nothing");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));

		btnOk = new Button(composite_1, SWT.NONE);
		btnOk.setEnabled(false);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				String key = text.getText();
				List<String> values = new ArrayList<String>();

				if (key.length() == 0 || items.length == 0)
					MessageDialog.openError(shell, "error",
							"please input list information!");
				else {
					for (TableItem item : items) {
						if(item.getText().length() > 0)
							values.add(item.getText());
					}
					result = new ListInfo(key, values, headTail, exist);
					shell.dispose();
				}

			}
		});
		btnOk.setText("OK");

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setText("Cancel");
		
		super.createContents();
	}

	protected Table getTable() {
		return new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
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
