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

public class ZRangeCmd extends DataCommand {

	private boolean withScore;
	private TableColumn tblclmnNewColumn;
	private TableColumn tblclmnMember;

	public ZRangeCmd(Console console, String cmd) {
		super(console, cmd);
	}

	@Override
	protected void initData(Composite composite) {
		String[] strs = cmd.trim().split(" ");
		withScore = strs[strs.length-1].equalsIgnoreCase("WITHSCORES");

		Table table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 5));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		EditListener listener = new EditListener(table, false);
		table.addListener(SWT.MouseDown, listener);
		
		if(withScore){
			tblclmnNewColumn = new TableColumn(table, SWT.NONE);
			tblclmnNewColumn.setWidth(88);
			tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.SCORE));
		}
		
		tblclmnMember = new TableColumn(table, SWT.NONE);
		tblclmnMember.setWidth(164);
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.MEMBER));
		
		String[] data = result.getResult().split("\n");
		
		if(withScore){
			for(int i = 0 ; i < data.length/2; i ++){
				TableItem item = new TableItem(table, SWT.NONE);
				String[] values = new String[]{data[i*2+1], data[i*2]};
				item.setText(values);
			}
		}else{
			for(int i = 0 ; i < data.length; i ++){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(data[i]);
			}
		}
	}

	@Override
	protected void refreshLangUI() {
		if(withScore)
			tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.SCORE));
		tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.MEMBER));
		super.refreshLangUI();
	}

}
