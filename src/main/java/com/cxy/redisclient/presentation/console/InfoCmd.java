package com.cxy.redisclient.presentation.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;

public class InfoCmd extends DataCommand {

	private TableColumn clmnKey;
	private TableColumn clmnValue;

	public InfoCmd(Console console, String cmd) {
		super(console, cmd);
	}

	@Override
	protected void initData(Composite composite) {
		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		String[] tabs = result.getResult().split("#");
		
		for(String tab:tabs){
			if(tab.length() > 0){
				TabItem tbtmServerInformation = new TabItem(tabFolder, SWT.NONE);
				String[] strs = tab.split("\r\n");
				tbtmServerInformation.setText(strs[0]);
				Table table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
				tbtmServerInformation.setControl(table);
				table.setHeaderVisible(true);
				table.setLinesVisible(true);
				EditListener listener = new EditListener(table, false);
				table.addListener(SWT.MouseDown, listener);
				
				clmnKey = new TableColumn(table, SWT.LEFT);
				clmnKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
				clmnKey.setWidth(250);
				
				clmnValue = new TableColumn(table, SWT.LEFT);
				clmnValue.setWidth(442);
				clmnValue.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
				
				for(int i = 1; i < strs.length; i ++){
					String[] serverkeys = strs[i].split(":");
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(serverkeys);
				}
			}
		}
	}

	@Override
	protected void refreshLangUI() {
		clmnKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
		clmnValue.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		super.refreshLangUI();
	}

}
