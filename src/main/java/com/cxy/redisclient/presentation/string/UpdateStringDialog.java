package com.cxy.redisclient.presentation.string;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Shell;

public class UpdateStringDialog extends NewStringDialog {
	private String value;
	
	public UpdateStringDialog(Shell parent, int style, String server, int db,
			String key, String value) {
		super(parent, style, server, db, key);
		this.value = value;
	}

	@Override
	protected void createContents() {
		super.createContents();
		shlNString.setText("String Properties");
		
		text_key.setEditable(false);
		text_key.removeModifyListener(new ModifyKey());
		
		text_value.setText(value);
		text_value.selectAll();
		text_value.setFocus();
		text_value.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String newValue = text_value.getText() == null?"":text_value.getText();
				if(newValue.equals(value)) 
					btnOk.setEnabled(false);
				else
					btnOk.setEnabled(true);
			}
		});
	}

}
