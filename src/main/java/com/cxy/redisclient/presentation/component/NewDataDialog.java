package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class NewDataDialog extends RedisClientDialog {
	private String title;
	private int width;
	private int height;
	private Text inputKey;
	private Button btnOk;
	
	private String key;
	
	protected NewDataContent dataContent;
	protected OKCancel okCancel = new OKCancel();
	
	protected final class ModifyKey implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			String newKey = inputKey.getText() == null ? "" : inputKey.getText();
			if (newKey.equals(key))
				btnOk.setEnabled(false);
			else
				btnOk.setEnabled(true);
		}
	}
	
	public NewDataDialog(Shell parent, Image image, int id, String server, int db, String key, int width, int height, String title, String dataTitle) {
		super(parent, image);
		this.width = width;
		this.height = height;
		this.key = key;
		this.title = title;
		
		this.dataContent = getDataContent(id, server, db, key, dataTitle);
	}

	protected void createContents() {
		shell.setText(title);
		shell.setLayout(new GridLayout(1, false));

		dataContent.setShell(shell);
		okCancel.setShell(shell);
		
		shell.setSize(width, height);
		
		dataContent.initContents();
		okCancel.initContents();
		
		inputKey = dataContent.getInputKey();
		btnOk = okCancel.getOkButton();
		
		minWidth = 600;
		minHeight = 500;
		
		super.createContents();
	}
	
	protected abstract NewDataContent getDataContent(int id, String server, int db, String key, String dataTitle);
	
	public void setResult(Object result){
		this.result = result;
	}
}
