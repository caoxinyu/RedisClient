package com.cxy.redisclient.presentation.hash;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.dto.HashInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.NewDataContent;
import com.cxy.redisclient.presentation.component.NewDataDialog;

public class NewHashDialog extends NewDataDialog {
	public NewHashDialog(Shell parent, Image image, int id, String server, int db,	String key) {
		super(parent, image, id, server, db, key, 654, 524, RedisClient.i18nFile.getText(I18nFile.NEWHASH), I18nFile.HASH);
		
	}

	@Override
	protected void createContents() {
		SelectionListener okSelection = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = ((NewHashContent)dataContent).getTable().getItems();
				String key = dataContent.getKey();
				Map<String, String> values = new HashMap<String, String>();

				if (items.length == 0)
					MessageDialog.openError((Shell) shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.INPUTHASH));
				else {
					okSelected(items, key, values);
				}

			}
		};
		
		okCancel.setOkSelection(okSelection);
		super.createContents();
	}

	@Override
	protected NewDataContent getDataContent(int id,
			String server, int db, String key, String dataTitle) {
		return new NewHashContent(id, server, db, key, dataTitle);
	}

	private void okSelected(TableItem[] items,
			String key, Map<String, String> values) {
		for (TableItem item : items) {
			if((item.getText(0).length() == 0 && item.getText(1).length() > 0) || (item.getText(0).length() > 0 && item.getText(1).length() == 0))
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.INPUTWHOLE) + item.getText(0) + " " + item.getText(1));
			if((item.getText(0).length() > 0 && item.getText(1).length() > 0))
				values.put(item.getText(0), item.getText(1));
		}
		
		
		setResult(new HashInfo(key, values, dataContent.getTTL()));
		shell.dispose();
	}

	

	
}
