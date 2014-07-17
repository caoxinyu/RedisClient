package com.cxy.redisclient.presentation.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.PagingListener;
import com.cxy.redisclient.presentation.component.TTLTabItem;
import com.cxy.redisclient.presentation.component.UpdateTTLTabItem;

public class UpdateListDialog extends NewListDialog {
	public UpdateListDialog(Shell parent, Image image, int id, String server, int db,
			String key) {
		super(parent, image, id, server, db, key);
	}
	@Override
	protected void createContents() {
		super.createContents();
		shell.setText(RedisClient.i18nFile.getText(I18nFile.LISTPROPERTY)+": "+key);
		grpWhenListNot.setVisible(false);
		text.setEditable(false);
		text.removeModifyListener(new ModifyKey());
		btnOk.setEnabled(true);
		table.addListener(SWT.SetData, new PagingListener(table, new ListPage(id, db, key)));
	}

	protected Table getTable() {
		return new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI | SWT.VIRTUAL);
	}
	protected TTLTabItem getTTLTabItem(TabFolder tabFolder) {
		return new UpdateTTLTabItem(tabFolder, id, db, key);
	}
}
