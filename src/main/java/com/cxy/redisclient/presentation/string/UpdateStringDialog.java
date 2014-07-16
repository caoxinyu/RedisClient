package com.cxy.redisclient.presentation.string;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public class UpdateStringDialog extends NewStringDialog {
	private String value;
	
	public UpdateStringDialog(Shell parent, Image image, String server, int db,
			String key, String value) {
		super(parent, image, server, db, key);
		this.value = value;
	}

	@Override
	protected void createContents() {
		super.createContents();
		shell.setText(RedisClient.i18nFile.getText(I18nFile.STRINGPROPERTY));
		
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
