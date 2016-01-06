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

public class HGetallCmd extends DataCommand {

	private TableColumn tblclmnNewColumn;
	private TableColumn tblclmnMember;

	public HGetallCmd(Console console, String cmd) {
		super(console, cmd);
	}

	protected void initData(Composite composite) {
		Table table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 5));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		EditListener listener = new EditListener(table, false);
		table.addListener(SWT.MouseDown, listener);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(132);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.FIELD));

		tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(236);
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		
		String[] data = result.getResult().split("\n");
		
		for(int i = 0 ; i < data.length/2; i ++){
			TableItem item = new TableItem(table, SWT.NONE);
			String[] values = new String[]{data[i*2], data[i*2+1]};
			item.setText(values);
		}
	}

	@Override
	protected void refreshLangUI() {
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.FIELD));
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		super.refreshLangUI();
	}

}
