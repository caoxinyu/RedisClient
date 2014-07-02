package com.cxy.redisclient.presentation.list;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class UpdateListDialog extends NewListDialog {
	private List<String> values;
	public UpdateListDialog(Shell parent, int style, String server, int db,
			String key, List<String> values) {
		super(parent, style, server, db, key);
		this.values = values;
	}
	@Override
	protected void createContents() {
		super.createContents();
		shell.setText("List Properties");
		grpWhenListNot.setVisible(false);
		text.setEditable(false);
		text.removeModifyListener(new ModifyKey());
		btnOk.setEnabled(true);
		
		for (String value : values) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(value);
		}
	}

}
