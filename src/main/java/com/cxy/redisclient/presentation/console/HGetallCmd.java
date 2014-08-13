package com.cxy.redisclient.presentation.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.protocol.ResultType;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;

public class HGetallCmd extends Command {

	public HGetallCmd(Console console, String cmd) {
		super(console, cmd);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.execute();
		
		if(result.getType() != ResultType.Error && result.getResult().length() > 0){
			CTabItem tbtmNewItem = new CTabItem(console.getTabFolder_2(), SWT.NONE);
			tbtmNewItem.setText(RedisClient.i18nFile.getText(I18nFile.DATA)+(console.getTabFolder_2().getItemCount()-1));
			Composite composite = new Composite(console.getTabFolder_2(), SWT.NONE);
			tbtmNewItem.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			
			Group grpValues = new Group(composite, SWT.NONE);
			grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
					1));
			grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
			grpValues.setLayout(new GridLayout(4, false));
	
			Table table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
			table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 5));
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			EditListener listener = new EditListener(table, false);
			table.addListener(SWT.MouseDown, listener);
			
			TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
			tblclmnNewColumn.setWidth(132);
			tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.FIELD));
	
			TableColumn tblclmnMember = new TableColumn(table, SWT.NONE);
			tblclmnMember.setWidth(236);
			tblclmnMember.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
			
			String[] data = result.getResult().split("\n");
			
			for(int i = 0 ; i < data.length/2; i ++){
				TableItem item = new TableItem(table, SWT.NONE);
				String[] values = new String[]{data[i*2], data[i*2+1]};
				item.setText(values);
			}
					
			console.getTabFolder_2().setSelection(tbtmNewItem);
		}
	}

}
