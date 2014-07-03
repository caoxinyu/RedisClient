package com.cxy.redisclient.presentation.set;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class UpdateSetDialog extends NewSetDialog {
	private Set<String> values;
	
	public UpdateSetDialog(Shell parent, Image image, String server, int db,
			String key, Set<String> values) {
		super(parent, image, server, db, key);
		this.values = values;
	}

	@Override
	protected void createContents() {
		super.createContents();
		shell.setText("Set Properties");
		text.setEditable(false);
		text.removeModifyListener(new ModifyKey());
		btnOk.setEnabled(true);
		
		for (String value : values) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(value);
		}
	}

}
