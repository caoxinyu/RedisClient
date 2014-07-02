package com.cxy.redisclient.presentation.hash;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class UpdateHashDialog extends NewHashDialog {
	private Map<String, String> value;
	
	public UpdateHashDialog(Shell parent, int style, String server, int db,
			String key, Map<String , String> value) {
		super(parent, style, server, db, key);
		this.value = value;
	}

	@Override
	protected void createContents() {
		super.createContents();
		shlNewHash.setText("Hash Properties");
		text.setEnabled(false);
		text.removeModifyListener(new ModifyKey());
		
		btnOk.setEnabled(true);
		
		Set<Entry<String, String>> set = value.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) i.next();
			TableItem item = new TableItem(table, SWT.NONE);
			String[] values = new String[]{entry.getKey(), entry.getValue()};
			
			item.setText(values);
		}
	}

}
