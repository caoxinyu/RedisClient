package com.cxy.redisclient.presentation.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.cxy.redisclient.presentation.component.PagingListener;

public class UpdateListDialog extends NewListDialog {
	public UpdateListDialog(Shell parent, Image image, int id, String server, int db,
			String key) {
		super(parent, image, id, server, db, key);
	}
	@Override
	protected void createContents() {
		super.createContents();
		shell.setText("List Properties");
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
}
