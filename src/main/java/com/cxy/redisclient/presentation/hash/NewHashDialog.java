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

import com.cxy.redisclient.dto.HashInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class NewHashDialog extends RedisClientDialog {

	protected final class ModifyKey implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			String newKey = text.getText() == null ? "" : text.getText();
			if (newKey.equals(key))
				btnOk.setEnabled(false);
			else
				btnOk.setEnabled(true);
		}
	}

	private String server;
	private int db;
	private String key;
	protected Text text;
	protected Table table;
	private Button btnDelete;
	protected Button btnOk;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NewHashDialog(Shell parent, Image image,  String server, int db,
			String key) {
		super(parent, image);
		this.server = server;
		this.db = db;
		this.key = key == null ? "" : key;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setSize(654, 524);
		shell.setText(RedisClient.i18nFile.getText(I18nFile.NEWHASH));

		shell.setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tabFolder.setSize(414, 249);

		TabItem tbtmList = new TabItem(tabFolder, SWT.NONE);
		tbtmList.setText(RedisClient.i18nFile.getText(I18nFile.HASH));

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmList.setControl(composite);
		composite.setLayout(new GridLayout(4, true));

		Label label = new Label(composite, SWT.NONE);
		label.setText(RedisClient.i18nFile.getText(I18nFile.SERVER));

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(server);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText(RedisClient.i18nFile.getText(I18nFile.DATABASE));

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(String.valueOf(db));

		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));

		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		text.setText(key);
		text.selectAll();
		text.setFocus();
		text.addModifyListener(new ModifyKey());

		Group grpValues = new Group(composite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 3));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new EditListener(table));

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(132);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.FIELD));

		TableColumn tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(236);
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));

		Button btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog(
						shell,
						RedisClient.i18nFile.getText(I18nFile.INPUTVALUES),
						RedisClient.i18nFile.getText(I18nFile.HASHINPUTFORMAT),
						"", null);
				if (inputDialog.open() == InputDialog.OK) {
					String values = inputDialog.getValue();
					String[] hashValues = values.split(";");
					TableItem item = null;
					for (String value : hashValues) {
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
				Map<String, String> values = new HashMap<String, String>();

				if (key.length() == 0 || items.length == 0)
					MessageDialog.openError(shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.INPUTHASH));
				else if(key.endsWith(":")){
					MessageDialog.openError(shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.KEYENDERROR)+ ":");
					
				}else {
					for (TableItem item : items) {
						if((item.getText(0).length() == 0 && item.getText(1).length() > 0) || (item.getText(0).length() > 0 && item.getText(1).length() == 0))
							throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.INPUTWHOLE) + item.getText(0) + " " + item.getText(1));
						if((item.getText(0).length() > 0 && item.getText(1).length() > 0))
							values.put(item.getText(0), item.getText(1));
					}
					result = new HashInfo(key, values);
					shell.dispose();
				}

			}
		});
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.OK));

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));

		super.createContents();
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
