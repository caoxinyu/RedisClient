package com.cxy.redisclient.presentation.string;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.NewDataContent;

public class NewStringContent extends NewDataContent {
	private Text text_value;
	public NewStringContent(int id, String server, int db, String key,
			String dataTitle) {
		super(id, server, db, key, dataTitle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void initData(Composite dataComposite) {
		Label label = new Label(dataComposite, SWT.NONE);
		label.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		
		text_value = new Text(dataComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		text_value.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	}
	public Text getText() {
		return text_value;
	}
}
