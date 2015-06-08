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

import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public abstract class NewDataContent {
	protected Composite shell;
	private final String separator = ConfigFile.getSeparator();
	
	public Composite getShell() {
		return shell;
	}

	protected int id;
	protected String server;
	protected int db;
	protected String key;
	
	private String dataTitle;
	
	protected Text inputKey;
	
	private TabItem dataTabItem;
	private TabItem ttlTabItem;
	
	protected Button btnExpire;
	protected Text ttl;
	protected Label labelTTL;

	protected Composite ttlComposite;

	private Label label;

	private Label label_2;

	private Label lblKey;
	protected TabFolder tabFolder;
	
	public NewDataContent(int id, String server, int db, String key, String dataTitle){
		this.id = id;
		this.server = server;
		this.db = db;
		this.key = key;
		this.dataTitle = dataTitle;
	}
	
	public void setShell(Composite shell){
		this.shell = shell;
	}
	public void initContents(){
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite dataComposite = initDataTabItem();
		initData(dataComposite);
		
		initTTLTabItem();
	}

	protected Composite initDataTabItem(){
		dataTabItem = new TabItem(tabFolder, SWT.NONE);
		dataTabItem.setText(RedisClient.i18nFile.getText(dataTitle));

		Composite dataComposite = new Composite(tabFolder, SWT.NONE);
		dataTabItem.setControl(dataComposite);
		dataComposite.setLayout(new GridLayout(4, true));

		label = new Label(dataComposite, SWT.NONE);
		label.setText(RedisClient.i18nFile.getText(I18nFile.SERVER));

		Label label_1 = new Label(dataComposite, SWT.NONE);
		label_1.setText(server);

		label_2 = new Label(dataComposite, SWT.NONE);
		label_2.setText(RedisClient.i18nFile.getText(I18nFile.DATABASE));

		Label label_3 = new Label(dataComposite, SWT.NONE);
		label_3.setText(String.valueOf(db));

		lblKey = new Label(dataComposite, SWT.NONE);
		lblKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
		
		inputKey = new Text(dataComposite, SWT.BORDER);
		inputKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		inputKey.setText(key);
		inputKey.selectAll();
		inputKey.setFocus();
		
		return dataComposite;
	}
	
	protected void initTTLTabItem(){
		ttlTabItem = new TabItem(tabFolder, SWT.NONE);
		ttlTabItem.setText(RedisClient.i18nFile.getText(I18nFile.TTL));
		
		ttlComposite = new Composite(tabFolder, SWT.NONE);
		ttlTabItem.setControl(ttlComposite);
		ttlComposite.setLayout(new GridLayout(2, false));
		
		btnExpire = new Button(ttlComposite, SWT.CHECK);
		
		btnExpire.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		btnExpire.setText(RedisClient.i18nFile.getText(I18nFile.EXPIRE));
		
		labelTTL = new Label(ttlComposite, SWT.NONE);
		labelTTL.setEnabled(false);
		labelTTL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelTTL.setText(RedisClient.i18nFile.getText(I18nFile.TTLS));
		
		ttl = new Text(ttlComposite, SWT.BORDER);
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
	
	protected abstract void initData(Composite dataComposite);

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
	
	public String getKey(){
		String key = inputKey.getText();
		if(key.endsWith(separator)){
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.KEYENDERROR)+ separator);
		}
		
		return key;
	}
	
	public Text getInputKey(){
		return inputKey;
	}
	public void refreshLangUI(){
		dataTabItem.setText(RedisClient.i18nFile.getText(dataTitle));
		label.setText(RedisClient.i18nFile.getText(I18nFile.SERVER));
		label_2.setText(RedisClient.i18nFile.getText(I18nFile.DATABASE));
		lblKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
		
		ttlTabItem.setText(RedisClient.i18nFile.getText(I18nFile.TTL));
		btnExpire.setText(RedisClient.i18nFile.getText(I18nFile.EXPIRE));
		labelTTL.setText(RedisClient.i18nFile.getText(I18nFile.TTLS));
		shell.pack();
	}

	public int getId() {
		return id;
	}

	public int getDb() {
		return db;
	}
}
