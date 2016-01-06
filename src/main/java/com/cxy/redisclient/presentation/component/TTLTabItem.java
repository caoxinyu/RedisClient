package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public class TTLTabItem extends TabItem {
	protected Button btnExpire;
	protected Text ttl;
	protected Composite composite;
	protected Label labelTTL;
	
	public TTLTabItem(TabFolder parent) {
		super(parent, SWT.None);
		this.setText(RedisClient.i18nFile.getText(I18nFile.TTL));
		
		composite = new Composite(parent, SWT.NONE);
		this.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		btnExpire = new Button(composite, SWT.CHECK);
		
		btnExpire.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		btnExpire.setText(RedisClient.i18nFile.getText(I18nFile.EXPIRE));
		
		labelTTL = new Label(composite, SWT.NONE);
		labelTTL.setEnabled(false);
		labelTTL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelTTL.setText(RedisClient.i18nFile.getText(I18nFile.TTLS));
		
		ttl = new Text(composite, SWT.BORDER);
		ttl.setEnabled(false);
		ttl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnExpire.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnExpire.getSelection()){
					labelTTL.setEnabled(true);
					ttl.setEnabled(true);
				}else {
					labelTTL.setEnabled(false);
					ttl.setEnabled(false);
				}
					
			}
		});
				
	}
	
	@Override
	protected void checkSubclass() {
		
	}

	public int getTTL(){
		if(btnExpire.getSelection()){
			int ttl;
			try{
				ttl = Integer.parseInt(this.ttl.getText());
			}catch(NumberFormatException e){
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.TTLEXCEPTION));
			}
			
			if(ttl <= 0)
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.TTLEXCEPTION));
			return ttl;
		}else
			return -1;
	}
	
	

}
