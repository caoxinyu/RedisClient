package com.cxy.redisclient.presentation.zset;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.dto.ZSetInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.NewDataDialog;

public class NewZSetDialog extends NewDataDialog {
	public NewZSetDialog(Shell parent, Image image, int id, String server,
			int db, String key) {
		super(parent, image, id, server, db, key, 586, 531, RedisClient.i18nFile.getText(I18nFile.NEWZSET), I18nFile.ZSET);
		
	}

	@Override
	protected NewZSetContent getDataContent(int id,
			String server, int db, String key, String dataTitle) {
		return new NewZSetContent(id, server, db, key, dataTitle);
	}

	@Override
	protected void createContents() {
		SelectionListener okSelection = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = ((NewZSetContent)dataContent).getTable().getItems();
				String key = dataContent.getKey();
				Map<String, Double> values = new HashMap<String, Double>();

				if (items.length == 0)
					MessageDialog.openError((Shell) shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.INPUTZSET));
				else {
					okSelected(items, key, values);
				}

			}
		};
		okCancel.setOkSelection(okSelection);
		super.createContents();
	}
	private void okSelected(TableItem[] items,
			String key, Map<String, Double> values) {
		for (TableItem item : items) {
			if(item.getText(0).length() == 0 && item.getText(1).length() > 0)
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SCOREERROR) + item.getText(1));
			if((item.getText(0).length() > 0))
				try{
					values.put(item.getText(1),	Double.valueOf(item.getText(0)));
				}catch(NumberFormatException e){
					throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SCOREERROR) + e.getLocalizedMessage());
				}
		}
		setResult(new ZSetInfo(key, values, dataContent.getTTL()));
		shell.dispose();
	}
	
}
