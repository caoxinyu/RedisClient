package com.cxy.redisclient.presentation.list;

import org.eclipse.jface.dialogs.InputDialog;
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
import com.cxy.redisclient.service.ListService;

public class ListDataContent extends DataContent {
	private Table table;
	private Button btnAppendTail;
	private Button btnDeleteHead;
	private Button btnDeleteTail;
	private ListService service = new ListService();
	private Status status;
	private Button btnInsertHead;
	private Button btnApply;
	private Button btnCancel;
	private Button btnRefresh;
	private Button btnWatch;
	private CurrentData currentData = new CurrentData();
	private EditListener listener;
	private Text editor;
	private PagingListener pageListener;

	private Group grpValues;

	private TableColumn tblclmnNewColumn;
	
	public ListDataContent(CTabItem tabItem, Image image, int id, String server, int db, String key,
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
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table =  new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 5));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		listener = new EditListener(table, true);
		table.addListener(SWT.MouseDown, listener);
		pageListener = new PagingListener(table, new ListPage(id, db, key));
		table.addListener(SWT.SetData, pageListener);

		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		tblclmnNewColumn.setWidth(200);

		btnInsertHead = new Button(grpValues, SWT.NONE);
		btnInsertHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnInsertHead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.INSERTHEAD), RedisClient.i18nFile.getText(I18nFile.INPUTVALUES), "", null);
				if(dialog.open() == InputDialog.OK){
				    String value = dialog.getValue();
				    service.addHead(id, db, key, value);
				    refresh();
				}
			}
		});
		btnInsertHead.setText(RedisClient.i18nFile.getText(I18nFile.INSERTHEAD));

		btnAppendTail = new Button(grpValues, SWT.NONE);
		btnAppendTail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
		btnAppendTail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(shell.getParent().getShell(), RedisClient.i18nFile.getText(I18nFile.APPENDTAIL), RedisClient.i18nFile.getText(I18nFile.INPUTVALUES), "", null);
				if(dialog.open() == InputDialog.OK){
				    String value = dialog.getValue();
				    service.addTail(id, db, key, value);
				    pageListener.setCount();
				    table.clear(table.getItemCount()-1);
				    table.setSelection(table.getItemCount()-1);
				    table.setSelection(-1);
				    currentData.setItem(null);
				    status = Status.Normal;
					statusChanged();
				}
			}
		});
		btnAppendTail.setText(RedisClient.i18nFile.getText(I18nFile.APPENDTAIL));

		btnDeleteHead = new Button(grpValues, SWT.NONE);
		btnDeleteHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDeleteHead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.removeFirst(id, db, key);
				refresh();
			}
		});
		btnDeleteHead.setText(RedisClient.i18nFile.getText(I18nFile.DELETEHEAD));

		btnDeleteTail = new Button(grpValues, SWT.NONE);
		btnDeleteTail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1,
				1));
		btnDeleteTail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.removeLast(id, db, key);
				pageListener.setCount();
				table.getItem(table.getItemCount()-1);
				table.setSelection(table.getItemCount()-1);
			    table.setSelection(-1);
			    currentData.setItem(null);
			    status = Status.Normal;
				statusChanged();
			}
		});
		btnDeleteTail.setText(RedisClient.i18nFile.getText(I18nFile.DELETETAIL));
		
		btnApply = new Button(grpValues, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				service.setValue(id, db, key, table.getSelectionIndex(), items[0].getText());
				table.setSelection(-1);
				currentData.setItem(null);
				status = Status.Normal;
				statusChanged();
			}
		});
		btnApply.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		setApply(false);
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		
		btnCancel = new Button(grpValues, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(status){
				case Normal:
					break;
				case Update:
					table.setSelection(-1);
					status = Status.Normal;
					currentData.setItem(null);
					statusChanged();
					break;
				case Updating:
					currentData.reset();
					status = Status.Update;
					listener.clickRow(currentData.getItem(), 0);
					addModifyTextListener();
					statusChanged();
					break;
				}
				
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnCancel.setEnabled(false);
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		btnRefresh = new Button(grpValues, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRefresh.setEnabled(true);
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));

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
		
		
		
	}
	@Override
	public void refreshLangUI() {
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		btnInsertHead.setText(RedisClient.i18nFile.getText(I18nFile.INSERTHEAD));
		btnAppendTail.setText(RedisClient.i18nFile.getText(I18nFile.APPENDTAIL));
		btnDeleteHead.setText(RedisClient.i18nFile.getText(I18nFile.DELETEHEAD));
		btnDeleteTail.setText(RedisClient.i18nFile.getText(I18nFile.DELETETAIL));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		super.refreshLangUI();
	}

	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		addModifyTextListener();
		
		if(currentData.isItemChanged(items[0])){
			switch(status){
			case Normal:
				status = Status.Update;
				statusChanged();
				
				break;
			
			case Update:
				break;
				
			case Updating:
				service.setValue(id, db, key, table.indexOf(currentData.getItem()), currentData.getItem().getText());
				status = Status.Update;
				statusChanged();
				break;
			}
			currentData.setItem(items[0]);
		}
	}
	
	public void statusChanged(){
		switch(status){
		case Normal:
			btnInsertHead.setEnabled(true);
			btnAppendTail.setEnabled(true);
			
			btnDeleteHead.setEnabled(true);
			btnDeleteTail.setEnabled(true);
			setApply(false);
			btnCancel.setEnabled(false);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(false);
			break;
			
		case Update:
			btnInsertHead.setEnabled(true);
			btnAppendTail.setEnabled(true);
			
			btnDeleteHead.setEnabled(true);
			btnDeleteTail.setEnabled(true);
			setApply(false);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(true);
			break;
			
		case Updating:
			btnInsertHead.setEnabled(false);
			btnAppendTail.setEnabled(false);
			
			btnDeleteHead.setEnabled(false);
			btnDeleteTail.setEnabled(false);
			setApply(true);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			btnWatch.setEnabled(false);
			break;
		
		}
		
	}
	private void addModifyTextListener() {
		editor = listener.getText();
		if(!editor.isDisposed()){
			editor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					switch(status){
					case Normal:
						break;
					
					case Update:
						status = Status.Updating;
						statusChanged();
						
						break;
					case Updating:
						break;
					}
				}
			});
		}
	}

	private void refresh() {
		pageListener.setCount();
		table.clearAll();
		table.setSelection(0);
		table.setSelection(-1);
		currentData.setItem(null);
		status = Status.Normal;
		statusChanged();
	}
	@Override
	public Button getApplyButtion() {
		return btnApply;
	}
}
