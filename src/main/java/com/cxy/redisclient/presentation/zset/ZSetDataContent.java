package com.cxy.redisclient.presentation.zset;

import java.util.HashMap;
import java.util.Map;

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
import com.cxy.redisclient.presentation.component.PagingListener;
import com.cxy.redisclient.service.ZSetService;

public class ZSetDataContent extends DataContent {
	private Table table;
	private Button btnDelete;
	private Button btnAdd;
	private Group grpValues;
	private Button btnRefresh;
	private Button btnWatch;
	private TableColumn tblclmnNewColumn;
	private TableColumn tblclmnMember;
	private PagingListener pageListener;
	private ZSetService service = new ZSetService();
	private EditListener editListener;
	private Button btnApply;
	private Button btnCancel;
	private Status status;
	private TableItem currentItem;
	private Text editor;
	
	public ZSetDataContent(CTabItem tabItem, Image image, int id, String server, int db, String key,
			String dataTitle) {
		super(tabItem, image, id, server, db, key, dataTitle);
		status = Status.Normal;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initData(Composite dataComposite) {
		grpValues = new Group(dataComposite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI | SWT.VIRTUAL);;
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 6));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new EditListener(table, false));
		editListener = new EditListener(table, true);

		pageListener = new PagingListener(table, new ZSetPage(id, db, key));
		table.addListener(SWT.SetData, pageListener);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(88);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.SCORE));

		tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(164);
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.MEMBER));

		btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.addListener(SWT.MouseDown, editListener);
				
				table.removeListener(SWT.SetData, pageListener);
				pageListener = new PagingListener(table, new ZSetPage(id, db, key), true);
				table.addListener(SWT.SetData, pageListener);
				table.clearAll();
				
				TableItem item = table.getItem(0);
				
				table.setSelection(item);
				item.setText(new String[]{"", ""});
				editListener.clickRow(item, 0);
				addModifyTextListener();
				currentItem = item;
				status = Status.Add;
				statusChanged();
			}
		});
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));

		btnDelete = new Button(grpValues, SWT.NONE);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionCount() > 0){
					String[] members = new String[table.getSelectionCount()];
					int i = 0;
					
					TableItem[] items = table.getSelection();
					for (TableItem item : items) {
						members[i++] = item.getText(1);
					}
					service.removeMembers(id, db, key, members);
					
					for (TableItem item : items) {
						item.dispose();
					}
					table.setSelection(-1);
					currentItem = null;
					btnDelete.setEnabled(false);
				}
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		
		btnApply = new Button(grpValues, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				add();
			}
		});
		setApply(false);
		btnApply.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		
		btnCancel = new Button(grpValues, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(status){
				case Normal:
					break;
				case Add:
					refresh();
					status = Status.Normal;
					currentItem = null;
					statusChanged();
					break;
				case Adding:
					refresh();
					status = Status.Normal;
					currentItem = null;
					statusChanged();
					break;
				}
			}
		});
		btnCancel.setEnabled(false);
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		btnRefresh = new Button(grpValues, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		
		btnWatch = new Button(grpValues, SWT.NONE);
		btnWatch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		btnWatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				WatchDialog dialog = new WatchDialog(shell.getParent()
						.getShell(), image, items[0].getText(1));
				dialog.open();
			}
		});
	}
	@Override
	public void refreshLangUI() {
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.SCORE));
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.MEMBER));
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		super.refreshLangUI();
	}

	

	private void refresh() {
		table.removeListener(SWT.MouseDown, editListener);
		table.addListener(SWT.MouseDown, new EditListener(table, false));
		
		table.removeListener(SWT.SetData, pageListener);
		pageListener = new PagingListener(table, new ZSetPage(id, db, key));
		table.addListener(SWT.SetData, pageListener);
		
		table.clearAll();
		table.setSelection(0);
		statusChanged();
		
	}

	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		
		if(status != Status.Normal)
			addModifyTextListener();
		
		if(items.length > 0 && currentItem != items[0]){
			switch(status){
			case Normal:
				break;
			
			case Add:
				refresh();
				editListener.getText().dispose();
				break;
				
			case Adding:
				add();
				editListener.getText().dispose();
				break;
			}
			currentItem = items[0];
			status = Status.Normal;
		}
		statusChanged();
	}
	
	public void statusChanged(){
		switch(status){
		case Normal:
			btnAdd.setEnabled(true);
			if(table.getSelectionCount() > 0){
				btnDelete.setEnabled(true);
				btnWatch.setEnabled(true);
			}else{
				btnDelete.setEnabled(false);
				btnWatch.setEnabled(false);
			}
			setApply(false);
			btnCancel.setEnabled(false);
			btnRefresh.setEnabled(true);
			break;
			
		case Add:
			btnAdd.setEnabled(false);
			btnDelete.setEnabled(false);
			btnWatch.setEnabled(false);
			setApply(false);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			break;
			
		case Adding:
			btnAdd.setEnabled(false);
			btnDelete.setEnabled(false);
			btnWatch.setEnabled(false);
			setApply(true);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			break;
		
		}
	}
	private void addModifyTextListener() {
		editor = editListener.getText();
		if(!editor.isDisposed()){
			editor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					switch(status){
					case Normal:
						break;
					
					case Add:
						status = Status.Adding;
						statusChanged();
						
						break;
					case Adding:
						break;
					}
				}
			});
		}
	}

	private void add() {
		Map<String, Double> mapValues = new HashMap<String, Double>();
		
		TableItem item = table.getItem(0);
		if(item.getText(0).length() == 0 && item.getText(1).length() > 0)
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SCOREERROR) + item.getText(1));
		String member;
		
		try{
			member = item.getText(1);
			mapValues.put(member,	Double.valueOf(item.getText(0)));
		}catch(NumberFormatException e1){
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SCOREERROR) + e1.getLocalizedMessage());
		}
		service.addValues(id, db, key, mapValues);
		refresh();
		gotoMember(member);
		status = Status.Normal;
		statusChanged();
	}
	private void gotoMember(String member){
		TableItem[] items = table.getItems();
		
		for(TableItem item : items){
			if(item.getText(1).equals(member)){
				table.setSelection(item);
				return;
			}
		}

	}
	@Override
	public Button getApplyButtion() {
		return btnApply;
	}
}
