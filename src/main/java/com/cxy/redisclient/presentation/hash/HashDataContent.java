package com.cxy.redisclient.presentation.hash;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.WatchDialog;
import com.cxy.redisclient.presentation.component.DataContent;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.service.HashService;

public class HashDataContent extends DataContent {
	private Table table;
	private Button btnDelete;
	private HashService service = new HashService();
	private Status currentStatus;
	private Button btnAdd;
	private Button btnCancel;
	private Button btnApply;
	private Button btnRefresh;
	private Button btnWatch;
	private EditListener listener;
	private CurrentData currentData = new CurrentData();

	private Group grpValues;

	private TableColumn tblclmnNewColumn;

	private TableColumn tblclmnMember;
	
	public HashDataContent(CTabItem tabItem, Image image, int id, String server, int db, String key,
			String dataTitle) {
		super(tabItem, image, id, server, db, key, dataTitle);
		currentStatus = Status.Normal;
	}

	@Override
	public void refreshLangUI() {
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.FIELD));
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		super.refreshLangUI();
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

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 6));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(132);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.FIELD));

		tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(236);
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));

		btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));

		btnDelete = new Button(grpValues, SWT.NONE);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		
		btnApply = new Button(grpValues, SWT.NONE);
		btnApply.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		
		btnCancel = new Button(grpValues, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		listener = new EditListener(table, true);
		table.addListener(SWT.MouseDown, listener);

		btnRefresh = new Button(grpValues, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		
		btnWatch = new Button(grpValues, SWT.NONE);
		btnWatch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		btnWatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WatchDialog dialog = new WatchDialog(shell.getParent()
						.getShell(), image, currentData.getValue());
				dialog.open();
			}
		});
		
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(currentStatus){
				case Normal:
					break;
				case Add:
					break;
				case Adding:
					addData();
					
					break;
				case Edit:
					break;
				case Editing:
					addData();
					
					break;
				}
			}
		});
		
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(currentStatus){
				case Normal:
					break;
				case Add:
					TableItem[] addItems = table.getSelection();
					addItems[0].dispose();
					
					currentStatus = Status.Normal;
					table.setSelection(-1);
					currentData.setItem(null);
					statusChanged();
					break;
				case Adding:
					TableItem[] addingItems = table.getSelection();
					addingItems[0].dispose();
					
					currentStatus = Status.Normal;
					table.setSelection(-1);
					currentData.setItem(null);
					statusChanged();
					break;
				case Edit:
					table.setSelection(-1);
					currentStatus = Status.Normal;
					currentData.setItem(null);
					statusChanged();
					break;
				case Editing:
					currentData.reset();
					currentStatus = Status.Edit;
					listener.clickRow(currentData.getItem(), 0);
					addModifyTextListener();
					statusChanged();
					break;
				}
			}
		});
		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(currentStatus){
				case Normal:
					add();
					break;
				case Add:
					break;
				case Adding:
					break;
				case Edit:
					add();
					break;
				case Editing:
					break;
				}
				
			}
		});
		
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionCount() > 0){
					switch(currentStatus){
					case Normal:
						break;
					case Add:
						delete();
						break;
					case Adding:
						boolean ok = MessageDialog.openConfirm(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.DELETE), RedisClient.i18nFile.getText(I18nFile.DELETEHASH));
						if(ok){
							delete();
						}
						break;
					case Edit:
						ok = MessageDialog.openConfirm(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.DELETE), RedisClient.i18nFile.getText(I18nFile.DELETEHASH));
						if(ok){
							deleteData();
							refresh();
						}
						break;
					case Editing:
						currentData.reset();
						
						ok = MessageDialog.openConfirm(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.DELETE), RedisClient.i18nFile.getText(I18nFile.DELETEHASH));
						if(ok){
							deleteData();
							refresh();
						}
						break;
					}
				}else
					statusChanged();
				
				
			}
		});
		
		initHash();
		statusChanged();
	}

	private void initHash() {
		Map<String, String> value = service.read(id, db, key);
		
		Set<Entry<String, String>> set = value.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) i.next();
			TableItem item = new TableItem(table, SWT.NONE);
			String[] values = new String[]{entry.getKey(), entry.getValue()};
			
			item.setText(values);
		}
	}
	private void refresh(){
		table.removeAll();
		initHash();
		currentStatus = Status.Normal;
		statusChanged();
	}
	
	protected void tableItemSelected() {
		addModifyTextListener();
		
		TableItem[] items = table.getSelection();
		if(items.length > 0 && currentData.isItemChanged(items[0])){
			
			switch(currentStatus){
			case Normal:
				currentStatus = Status.Edit;
				statusChanged();
				
				break;
			case Add:
				currentData.getItem().dispose();
				currentStatus = Status.Edit;
				statusChanged();
				
				break;
			case Adding:
				currentData.getItem().dispose();
				currentStatus = Status.Edit;
				statusChanged();
				break;
				
			case Edit:
				break;
				
			case Editing:
				currentData.reset();
				currentStatus = Status.Edit;
				statusChanged();
				break;
			}
			currentData.setItem(items[0]);
			
		}
		
	}
		
	public void statusChanged(){
		switch(currentStatus){
		case Normal:
			btnAdd.setEnabled(true);
			btnDelete.setEnabled(false);
			setApply(false);
			btnCancel.setEnabled(false);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(false);
			break;
		case Add:
			btnAdd.setEnabled(false);
			btnDelete.setEnabled(true);
			setApply(false);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(false);
			break;
		case Adding:
			btnAdd.setEnabled(false);
			btnDelete.setEnabled(true);
			setApply(true);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			btnWatch.setEnabled(false);
			break;
		case Edit:
			btnAdd.setEnabled(true);
			btnDelete.setEnabled(true);
			setApply(false);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(true);
			break;
		case Editing:
			btnAdd.setEnabled(false);
			btnDelete.setEnabled(true);
			setApply(true);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			btnWatch.setEnabled(false);
			break;
			
		}
	}

	private void add() {
		TableItem item = new TableItem(table, SWT.NONE, 0);
		table.setSelection(item);
		listener.clickRow(item, 0);
		currentStatus = Status.Add;
		currentData.setItem(item);
		addModifyTextListener();
		statusChanged();
	}

	private void addModifyTextListener() {
		Text editor = listener.getText();
		if(!editor.isDisposed()){
			editor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					switch(currentStatus){
					case Normal:
						break;
					case Add:
						currentStatus = Status.Adding;
						statusChanged();
						
						break;
					case Adding:
						break;
					case Edit:
						currentStatus = Status.Editing;
						statusChanged();
						
						break;
					case Editing:
						break;
					}
				}
			});
		}
	}

	private void delete() {
		TableItem[] items = table.getSelection();
		String[] fields  = new String[items.length];
		for(int i = 0; i < items.length; i ++){
			fields[i] = items[i].getText(0);
			items[i].dispose();
		}
		currentStatus = Status.Normal;
		statusChanged();
	}
	
	private void deleteData(){
		TableItem[] items = table.getSelection();
		String[] fields  = new String[items.length];
		for(int i = 0; i < items.length; i ++){
			fields[i] = items[i].getText(0);
			items[i].dispose();
		}
		service.delField(id, db, key, fields);
		currentStatus = Status.Normal;
		statusChanged();
	}

	private void addData() {
		TableItem[] items = table.getSelection();
		String field = items[0].getText(0);
		String value = items[0].getText(1);
		
		if(currentStatus == Status.Adding){
			if(service.isFieldExist(id, db, key, field)){
				boolean ok = MessageDialog.openConfirm(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.UPDATE), RedisClient.i18nFile.getText(I18nFile.UPDATEFIELD));
				if(ok){
					service.setField(id, db, key, field, value);
					refresh();
					currentStatus = Status.Normal;
					statusChanged();
					gotoField(field);
				}
			}else{
				service.setField(id, db, key, field, value);
				refresh();
				currentStatus = Status.Normal;
				statusChanged();
				gotoField(field);
			}
		}
		if(currentStatus == Status.Editing){
			if(currentData.isFieldChanged(field) && service.isFieldExist(id, db, key, field)){
				boolean ok = MessageDialog.openConfirm(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.UPDATE), RedisClient.i18nFile.getText(I18nFile.UPDATEFIELD));
				if(ok){
					service.setField(id, db, key, field, value);
					refresh();
					currentStatus = Status.Normal;
					statusChanged();
					gotoField(field);
				}
			}else{
				service.setField(id, db, key, field, value);
				refresh();
				currentStatus = Status.Normal;
				statusChanged();
				gotoField(field);
			}
		}
	}
	
	private void gotoField(String field){
		TableItem[] items = table.getItems();
		
		for(TableItem item: items){
			if(item.getText(0).equals(field)){
				table.setSelection(item);
				table.setSelection(-1);
			}
		}
	}

	@Override
	public Button getApplyButtion() {
		return btnApply;
	}
}
