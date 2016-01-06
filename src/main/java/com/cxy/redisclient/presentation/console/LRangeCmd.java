package com.cxy.redisclient.presentation.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;

public class LRangeCmd extends DataCommand {

	private TableColumn tblclmnNewColumn;

	public LRangeCmd(Console console, String cmd) {
		super(console, cmd);
	}

	@Override
	protected void initData(Composite composite) {
		Table table =  new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 5));
		table.setLinesVisible(true);
		EditListener listener = new EditListener(table, false);
		table.addListener(SWT.MouseDown, listener);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		tblclmnNewColumn.setWidth(200);

		String[] data = result.getResult().split("\n");
		
		for(int i = 0 ; i < data.length; i ++){
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(data[i]);
		}
	}

	@Override
	protected void refreshLangUI() {
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		super.refreshLangUI();
	}

}
