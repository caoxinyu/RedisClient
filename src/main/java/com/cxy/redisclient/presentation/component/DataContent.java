package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;

import com.cxy.redisclient.domain.RedisObject;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.service.NodeService;

public abstract class DataContent extends NewDataContent {
	private CTabItem tabItem;
	protected Image image;
	private TabItem objectTabItem;
	private Label label1;
	private Label label2;
	
	public CTabItem getTabItem() {
		return tabItem;
	}

	private NodeService service = new NodeService();
	private Button btnApply;
	private Button btnRefresh;
	
	@Override
	public void initContents() {
		super.initContents();
		initObjectTabItem();
	}

	protected void initObjectTabItem() {
		RedisObject object = service.getObjectInfo(id, db, key);
		
		objectTabItem = new TabItem(tabFolder, SWT.NONE);
		objectTabItem.setText(RedisClient.i18nFile.getText(I18nFile.OBJECT));
		
		Composite objectComposite = new Composite(tabFolder, SWT.NONE);
		objectTabItem.setControl(objectComposite);
		objectComposite.setLayout(new GridLayout(4, true));
		
		label1 = new Label(objectComposite, SWT.NONE);
		label1.setText(RedisClient.i18nFile.getText(I18nFile.REFCOUNT));

		Label label_1 = new Label(objectComposite, SWT.NONE);
		label_1.setText(object.getRefCount().toString());
		
		label2 = new Label(objectComposite, SWT.NONE);
		label2.setText(RedisClient.i18nFile.getText(I18nFile.ENCODING));

		Label label_2 = new Label(objectComposite, SWT.NONE);
		label_2.setText(object.getEncoding());
	}

	@Override
	protected Composite initDataTabItem() {
		Composite composite = super.initDataTabItem();
		inputKey.setEditable(false);
		
		return composite;
	}

	@Override
	public void refreshLangUI() {
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		objectTabItem.setText(RedisClient.i18nFile.getText(I18nFile.OBJECT));
		label1.setText(RedisClient.i18nFile.getText(I18nFile.REFCOUNT));
		label2.setText(RedisClient.i18nFile.getText(I18nFile.ENCODING));
		
		super.refreshLangUI();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initTTLTabItem() {
		super.initTTLTabItem();
		new Label(ttlComposite, SWT.NONE);
		
		Composite composite = new Composite(ttlComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		btnApply = new Button(composite, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					service.expire(id, db, key, getTTL());
				}finally{
					setTTLApply(false);
				}
			}
		});
		btnApply.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		setTTLApply(false);
		
		btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					setTTL((int) service.getTTL(id, db, key));
				}finally{
					setTTLApply(false);
				}
			}
		});
		btnExpire.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnExpire.getSelection()){
					labelTTL.setEnabled(true);
					ttl.setEnabled(true);
					ttl.selectAll();
					ttl.setFocus();
					setTTLApply(true);
				}else {
					labelTTL.setEnabled(false);
					ttl.setEnabled(false);
					setTTLApply(true);
				}
					
			}
		});
		
		ttl.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setTTLApply(true);
			}
		});
		
		setTTL((int) service.getTTL(id, db, key));
	}

	public DataContent(CTabItem tabItem, Image image, int id, String server, int db, String key, String dataTitle) {
		super(id, server, db, key, dataTitle);
		this.tabItem = tabItem;
		this.tabItem.setData(this);
		this.image = image;
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
			setTTLApply(false);
		}
	}

	public Button getTTLBtnApplyButton() {
		return btnApply;
	}
	public abstract Button getApplyButtion();
	public void setApply(boolean apply){
		getApplyButtion().setEnabled(apply);
		tabItem.setShowClose(!apply);
	}
	public void setTTLApply(boolean apply){
		getTTLBtnApplyButton().setEnabled(apply);
		tabItem.setShowClose(!apply);
	}
	public boolean canClose(){
		boolean apply = getApplyButtion() == null?false:getApplyButtion().isEnabled();
		boolean ttlApply =  getTTLBtnApplyButton().isEnabled();
		
		return !apply && !ttlApply;
	}
	
}
