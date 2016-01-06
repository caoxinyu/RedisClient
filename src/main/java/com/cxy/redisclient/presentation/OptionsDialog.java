package com.cxy.redisclient.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.presentation.component.PagingListener;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class OptionsDialog extends RedisClientDialog {

	private Text size;
	private Text separator;
	private Text t1;
	private Text t2;

	public OptionsDialog(Shell parent, Image image) {
		super(parent, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.OPTIONS));
		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initConnection(tabFolder);
		
		initNamespace(tabFolder);
		
		initPaging(tabFolder);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button btnOk = new Button(composite_1, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if((Boolean) t1.getData()){
					int time1;
					try{
						time1 = Integer.parseInt(t1.getText());
					}catch(NumberFormatException e){
						throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.TIMEEXCEPTION));
					}
					
					if(time1 <= 0)
						throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.TIMEEXCEPTION));
					
					ConfigFile.setT1(time1);
					JedisCommand.timeout = ConfigFile.getT1();
				}
				
				if((Boolean) t2.getData()){
					int time2;
					try{
						time2 = Integer.parseInt(t2.getText());
					}catch(NumberFormatException e){
						throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.TIMEEXCEPTION));
					}
					
					if(time2 <= 0)
						throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.TIMEEXCEPTION));
					
					ConfigFile.setT2(time2);
				}
				
				if((Boolean) separator.getData()){
					String sep = separator.getText();
					if(sep.length() == 0)
						sep = ConfigFile.SEP;
					
					ConfigFile.setSeparator(sep);
				}
				
				if((Boolean) size.getData()){
					int psize;
					try{
						psize = Integer.parseInt(size.getText());
					}catch(NumberFormatException e){
						throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SIZEEXCEPTION));
					}
					
					if(psize <= 0)
						throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SIZEEXCEPTION));
					
					ConfigFile.setPagesize(psize);
					PagingListener.PAGE_SIZE = ConfigFile.getPagesize();
				}	
				
				shell.dispose();	
			}
		});
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.OK));
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				shell.dispose();
			}
		});
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		super.createContents();
	}

	private void initPaging(TabFolder tabFolder) {
		TabItem tbtmPaging = new TabItem(tabFolder, SWT.NONE);
		tbtmPaging.setText(RedisClient.i18nFile.getText(I18nFile.PAGING));
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmPaging.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblPaging = new Label(composite, SWT.NONE);
		lblPaging.setText(RedisClient.i18nFile.getText(I18nFile.PAGESIZE));
		
		size = new Text(composite, SWT.BORDER);
		size.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		size.setText(String.valueOf(ConfigFile.getPagesize()));
		size.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				size.setData(true);
			}
		});
		size.setData(false);
	}

	private void initNamespace(TabFolder tabFolder) {
		TabItem tbtmNamespace = new TabItem(tabFolder, SWT.NONE);
		tbtmNamespace.setText(RedisClient.i18nFile.getText(I18nFile.NAMESPACE));
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNamespace.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblSeparator = new Label(composite, SWT.NONE);
		lblSeparator.setText(RedisClient.i18nFile.getText(I18nFile.SEPARATOR));
		
		separator = new Text(composite, SWT.BORDER);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		separator.setText(ConfigFile.getSeparator());
		separator.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				separator.setData(true);
			}
		});
		separator.setData(false);
	}

	private void initConnection(TabFolder tabFolder) {
		TabItem tbtmConnection = new TabItem(tabFolder, SWT.NONE);
		tbtmConnection.setText(RedisClient.i18nFile.getText(I18nFile.CONNECTION));
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmConnection.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblt1 = new Label(composite, SWT.NONE);
		lblt1.setText(RedisClient.i18nFile.getText(I18nFile.COMMANDTIMEOUT));
		
		t1 = new Text(composite, SWT.BORDER);
		t1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		t1.setText(String.valueOf(ConfigFile.getT1()));
		t1.setFocus();
		t1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				t1.setData(true);
			}
		});
		t1.setData(false);
		
		Label lblt12 = new Label(composite, SWT.NONE);
		lblt12.setText(RedisClient.i18nFile.getText(I18nFile.CONSOLETIMEOUT));
		
		t2 = new Text(composite, SWT.BORDER);
		t2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		t2.setText(String.valueOf(ConfigFile.getT2()));
		t2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				t2.setData(true);
			}
		});
		t2.setData(false);
	}

}
