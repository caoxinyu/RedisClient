package com.cxy.redisclient.presentation.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.protocol.ResultType;
import com.cxy.redisclient.presentation.RedisClient;

public abstract class DataCommand extends Command {

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
			
			initData(composite);
					
			console.getTabFolder_2().setSelection(tbtmNewItem);
		}
	}
	protected abstract void initData(Composite composite) ;
	protected void refreshLangUI(){
		tbtmNewItem.setText(RedisClient.i18nFile.getText(I18nFile.DATA)+(console.getTabFolder_2().indexOf(tbtmNewItem)));
	}
}
