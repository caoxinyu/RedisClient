package com.cxy.redisclient.presentation.zset;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.NewDataContent;

public class NewZSetContent extends NewDataContent {
	private Table table;
	private Button btnDelete;
	private Group grpValues;
	
	public NewZSetContent(int id, String server, int db, String key,
			String dataTitle) {
		super(id, server, db, key, dataTitle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void initData(Composite dataComposite) {
		grpValues = new Group(dataComposite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 3));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new EditListener(table, true));

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(88);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.SCORE));

		TableColumn tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(164);
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.MEMBER));

		Button btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog(
						(Shell) shell,
						RedisClient.i18nFile.getText(I18nFile.INPUTVALUES),
						RedisClient.i18nFile.getText(I18nFile.INPUTZSETFORMAT),
						"", null);
				if (inputDialog.open() == InputDialog.OK) {
					String values = inputDialog.getValue();
					String[] zsetValues = values.split(";");
					TableItem item = null;
					for (String value : zsetValues) {
						item = new TableItem(table, SWT.NONE);
						String[] zset = value.split(",");
						item.setText(zset);
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
		new Label(grpValues, SWT.NONE);
		
	}
	protected Table getTable() {
		return table;
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
