package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.service.NodeService;

public class UpdateTTLTabItem extends TTLTabItem {
	private NodeService service = new NodeService();
	private Button btnApplyButton;
	
	public UpdateTTLTabItem(TabFolder parent, final int id, final int db, final String key) {
		super(parent);
		
		new Label(composite, SWT.NONE);
		
		btnApplyButton = new Button(composite, SWT.NONE);
		btnApplyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.expire(id, db, key, getTTL());
				btnApplyButton.setEnabled(false);
			}
		});
		btnApplyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnApplyButton.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		btnApplyButton.setEnabled(false);
		
		btnExpire.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnExpire.getSelection()){
					labelTTL.setEnabled(true);
					ttl.setEnabled(true);
					ttl.selectAll();
					ttl.setFocus();
					btnApplyButton.setEnabled(true);
				}else {
					labelTTL.setEnabled(false);
					ttl.setEnabled(false);
					btnApplyButton.setEnabled(true);
				}
					
			}
		});
		
		ttl.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				btnApplyButton.setEnabled(true);
			}
		});
		
		setTTL((int) service.getTTL(id, db, key));
	}

	public void setTTL(int ttl){
		if(ttl == -1){
			btnExpire.setSelection(false);
			this.ttl.setEnabled(false);
		}
		else{
			btnExpire.setSelection(true);
			labelTTL.setEnabled(true);
			this.ttl.setEnabled(true);
			this.ttl.setText(String.valueOf(ttl));
			btnApplyButton.setEnabled(false);
		}
	}

	public Button getBtnApplyButton() {
		return btnApplyButton;
	}
}
