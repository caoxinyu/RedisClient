package com.cxy.redisclient.presentation.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.dto.ListInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.NewDataDialog;

public class NewListDialog extends NewDataDialog {
	public NewListDialog(Shell parent, Image image, int id, String server,
			int db, String key) {
		super(parent, image, id, server, db, key, 966, 638, RedisClient.i18nFile.getText(I18nFile.NEWLIST), I18nFile.LIST);
	}

	@Override
	protected NewListContent getDataContent(int id,
			String server, int db, String key, String dataTitle) {
		return new NewListContent(id, server, db, key, dataTitle);
	}

	@Override
	protected void createContents() {
		SelectionListener okSelection = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = ((NewListContent)dataContent).getTable().getItems();
				String key = dataContent.getKey();
				List<String> values = new ArrayList<String>();

				if (items.length == 0)
					MessageDialog.openError((Shell) shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.INPUTLIST));
				else {
					okSelected(items, key, values);
				}

			}
		};
		okCancel.setOkSelection(okSelection);
		super.createContents();
	}

	protected void okSelected(TableItem[] items,
			String key, List<String> values) {
		for (TableItem item : items) {
			if(item.getText().length() > 0)
				values.add(item.getText());
		}
		setResult(new ListInfo(key, values, ((NewListContent)dataContent).isHeadTail(), ((NewListContent)dataContent).isExist(), dataContent.getTTL()));
		shell.dispose();
	}
}
