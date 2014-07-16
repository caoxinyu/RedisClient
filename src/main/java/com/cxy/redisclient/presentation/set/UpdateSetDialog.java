package com.cxy.redisclient.presentation.set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.PagingListener;

public class UpdateSetDialog extends NewSetDialog {
	private int id;
	public UpdateSetDialog(Shell parent, Image image, int id, String server, int db,
			String key) {
		super(parent, image, server, db, key);
		this.id = id;
	}

	@Override
	protected void createContents() {
		super.createContents();
		shell.setText(RedisClient.i18nFile.getText(I18nFile.SETPROPERTY));
		text.setEditable(false);
		text.removeModifyListener(new ModifyKey());
		btnOk.setEnabled(true);
		
		table.addListener(SWT.SetData, new PagingListener(table, new SetPage(id, db, key)));
	}

	protected Table getTable() {
		return new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI | SWT.VIRTUAL);
	}
}
