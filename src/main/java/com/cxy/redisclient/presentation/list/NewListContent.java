package com.cxy.redisclient.presentation.list;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.NewDataContent;

public class NewListContent extends NewDataContent {
	private Table table;
	private Button btnDelete;
	private Button btnUp;
	private Button btnDown;
	private boolean headTail = true;
	private boolean exist = true;
	private Group grpWhenListNot;
	private Group grpValues;
	private TableColumn tblclmnNewColumn;
	
	public NewListContent(int id, String server, int db, String key,
			String dataTitle) {
		super(id, server, db, key, dataTitle);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initData(Composite dataComposite) {
		grpValues = new Group(dataComposite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table =  new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setHeaderVisible(true);;
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new EditListener(table, true));

		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		tblclmnNewColumn.setWidth(200);

		Button btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog((Shell) shell,
						RedisClient.i18nFile.getText(I18nFile.INPUTVALUES),
						RedisClient.i18nFile.getText(I18nFile.LISTINPUTFORMAT), "", null);
				if (inputDialog.open() == InputDialog.OK) {
					String values = inputDialog.getValue();
					String[] listValues = values.split(";");
					TableItem item = null;
					
					for (String value : listValues) {
						item = new TableItem(table, SWT.NONE);
						item.setText(value);
					}
					
					if(item != null)
						table.setSelection(item);
				}
			}
		});
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));

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
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));

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
		btnUp.setText(RedisClient.i18nFile.getText(I18nFile.UP));

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
		btnDown.setText(RedisClient.i18nFile.getText(I18nFile.DOWN));

		Group grpOrderToAdd = new Group(dataComposite, SWT.NONE);
		grpOrderToAdd.setLayout(new GridLayout(2, false));
		grpOrderToAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 4, 1));
		grpOrderToAdd.setText(RedisClient.i18nFile.getText(I18nFile.LISTORDER));

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
		button_1.setText(RedisClient.i18nFile.getText(I18nFile.HEADTAIL));

		Button btnFromTailTo = new Button(grpOrderToAdd, SWT.RADIO);
		btnFromTailTo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnFromTailTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				headTail = false;
			}
		});
		btnFromTailTo.setText(RedisClient.i18nFile.getText(I18nFile.TAILHEAD));

		grpWhenListNot = new Group(dataComposite, SWT.NONE);
		grpWhenListNot.setLayout(new GridLayout(2, true));
		grpWhenListNot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 4, 1));
		grpWhenListNot.setText(RedisClient.i18nFile.getText(I18nFile.LISTNOTEXIST));

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
		btnCreateList.setText(RedisClient.i18nFile.getText(I18nFile.CREATELIST));

		Button btnNothingToDo = new Button(grpWhenListNot, SWT.RADIO);
		btnNothingToDo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnNothingToDo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exist = false;
			}
		});
		btnNothingToDo.setText(RedisClient.i18nFile.getText(I18nFile.DONOTHING));
		
	}
	public boolean isHeadTail() {
		return headTail;
	}

	public boolean isExist() {
		return exist;
	}

	protected Table getTable() {
		return table;
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
