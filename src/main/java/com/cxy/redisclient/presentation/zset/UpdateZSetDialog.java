package com.cxy.redisclient.presentation.zset;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import redis.clients.jedis.Tuple;

public class UpdateZSetDialog extends NewZSetDialog {
	private Set<Tuple> values;
	
	public UpdateZSetDialog(Shell parent, Image image, String server, int db,
			String key, Set<Tuple> values) {
		super(parent, image, server, db, key);
		this.values = values;
	}
	
	@Override
	protected void createContents() {
		super.createContents();
		shell.setText("Sorted Set Properties");
		text.setEditable(false);
		text.removeModifyListener(new ModifyKey());
		btnOk.setEnabled(true);
		
		Iterator<Tuple> i = values.iterator();
		
		while(i.hasNext()) {
			Tuple entry = i.next();
			TableItem item = new TableItem(table, SWT.NONE);
			String[] zvalues = new String[]{Double.toString(entry.getScore()), entry.getElement()};
			
			item.setText(zvalues);
		}
	}

	
}
