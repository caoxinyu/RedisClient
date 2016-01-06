package com.cxy.redisclient.presentation.set;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.dto.SetInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.NewDataDialog;

public class NewSetDialog extends NewDataDialog {
	
	public NewSetDialog(Shell parent, Image image, int id, String server,
			int db, String key) {
		super(parent, image, id, server, db, key, 691, 540, RedisClient.i18nFile.getText(I18nFile.NEWSET), I18nFile.SET);
		
	}

	@Override
	protected NewSetContent getDataContent(int id,
			String server, int db, String key, String dataTitle) {
		return new NewSetContent(id, server, db, key, dataTitle);
	}

	@Override
	protected void createContents() {
		SelectionListener okSelection = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = ((NewSetContent)dataContent).getTable().getItems();
				String key = dataContent.getKey();
				Set<String> values = new HashSet<String>();

				if (items.length == 0)
					MessageDialog.openError((Shell) shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.INPUTSET));
				else {
					okSelected(items, key, values);
				}

			}
		};
		okCancel.setOkSelection(okSelection);
		super.createContents();
	}

	private void okSelected(TableItem[] items,
			String key, Set<String> values) {
		for (TableItem item : items) {
			if(item.getText().length() > 0)
				values.add(item.getText());
		}
		setResult(new SetInfo(key, values, dataContent.getTTL()));
		shell.dispose();
	}
}
