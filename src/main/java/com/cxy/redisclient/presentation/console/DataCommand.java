package com.cxy.redisclient.presentation.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.protocol.ResultType;
import com.cxy.redisclient.presentation.RedisClient;

public abstract class DataCommand extends Command {

	private Group grpValues;
	private CTabItem tbtmNewItem;
	@Override
	public boolean printResult() {
		return result.getType() == ResultType.Error;
	}
	public DataCommand(Console console, String cmd) {
		super(console, cmd);
	}
	@Override
	public void execute() {
		super.execute();
		
		if(result.getType() != ResultType.Error){
			tbtmNewItem = new CTabItem(console.getTabFolder_2(), SWT.NONE);
			tbtmNewItem.setText(RedisClient.i18nFile.getText(I18nFile.DATA)+(console.getTabFolder_2().indexOf(tbtmNewItem)));
			Composite composite = new Composite(console.getTabFolder_2(), SWT.NONE);
			tbtmNewItem.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			
			grpValues = new Group(composite, SWT.NONE);
			grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
			grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
			grpValues.setLayout(new GridLayout(4, false));
	
			initData(grpValues);
					
			console.getTabFolder_2().setSelection(tbtmNewItem);
		}
	}
	protected abstract void initData(Group grpValues) ;
	protected void refreshLangUI(){
		tbtmNewItem.setText(RedisClient.i18nFile.getText(I18nFile.DATA)+(console.getTabFolder_2().indexOf(tbtmNewItem)));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
	}
}
