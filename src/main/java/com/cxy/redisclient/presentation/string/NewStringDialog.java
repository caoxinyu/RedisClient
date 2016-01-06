package com.cxy.redisclient.presentation.string;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.dto.StringInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.NewDataDialog;

public class NewStringDialog extends NewDataDialog {
	
	public NewStringDialog(Shell parent, Image image, int id, String server,
			int db, String key) {
		super(parent, image, id, server, db, key, 622, 284, RedisClient.i18nFile.getText(I18nFile.NEWSTRING), I18nFile.STRING);
		
	}

	@Override
	protected NewStringContent getDataContent(int id,
			String server, int db, String key, String dataTitle) {
		return new NewStringContent(id, server, db, key, dataTitle);
	}

	@Override
	protected void createContents() {
		SelectionListener okSelection = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = dataContent.getKey();
				String value = ((NewStringContent)dataContent).getText().getText();
				
				if(value.length() == 0){
					MessageDialog.openError((Shell) shell, RedisClient.i18nFile.getText(I18nFile.ERROR),RedisClient.i18nFile.getText(I18nFile.INPUTSTRING));
				} else {
					okSelected(key, value);
				}
			}
		};
		okCancel.setOkSelection(okSelection);
		super.createContents();
	}
	private void okSelected(String key, String value) {
		setResult(new StringInfo(key, value, dataContent.getTTL()));
		shell.dispose();
	}
}
