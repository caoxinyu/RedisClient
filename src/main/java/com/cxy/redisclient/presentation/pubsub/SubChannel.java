package com.cxy.redisclient.presentation.pubsub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import redis.clients.jedis.JedisPubSub;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.service.PubSubService;

public class SubChannel {
	private int id;
	private String subChannel;
	private CTabFolder tabFolder;
	private CTabItem tbtmSubItem;
	private TableColumn tblclmnNewColumn;
	private TableColumn tblclmnNewColumn_1;
	private TableColumn tblclmnNewColumn_2;
	
	public SubChannel(int id, final String subChannel, CTabFolder tabFolder){
		this.id = id;
		this.subChannel = subChannel;
		this.tabFolder = tabFolder;
	}
	
	
	public CTabItem init(){
		tbtmSubItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmSubItem.setShowClose(true);
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSubItem.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		tbtmSubItem.setText(subChannel + " " + RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		
		final Table table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		EditListener listener = new EditListener(table, false, true);
		table.addListener(SWT.MouseDown, listener);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.TIME));
		
		tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		
		tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText(RedisClient.i18nFile.getText(I18nFile.MESSAGE));
		
		final JedisPubSub callback = new JedisPubSub() {

			@Override
			public void onMessage(String channel, String message) {
				messageReceived(table, channel, message);
			}

			private void messageReceived(final Table table,
					final String channel, final String message) {
				Display.getDefault().asyncExec(new Runnable() {
				    public void run() {
				    	TableItem item = new TableItem(table, SWT.None);
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
						String time = df.format(new Date());
						String[] str = new String[]{time, channel, message};
						item.setText(str);
						table.setSelection(item);
				    	
				    }
				});
				
			}

			@Override
			public void onPMessage(String pattern, String channel,
					String message) {
				messageReceived(table, channel, message);
			}

			@Override
			public void onSubscribe(String channel,
					int subscribedChannels) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onUnsubscribe(String channel,
					int subscribedChannels) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPUnsubscribe(String pattern,
					int subscribedChannels) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPSubscribe(String pattern,
					int subscribedChannels) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		final Thread t = new Thread(new Runnable() {
		    public void run() {
		    	PubSubService subscribe = new PubSubService();
		    	subscribe.subscribe(id, callback, subChannel);
		    }
		});
		t.start();
		
		tbtmSubItem.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				callback.punsubscribe();
			}
		});
		
		return tbtmSubItem;
	}
	
	public void refreshLangUI(){
		tbtmSubItem.setText(subChannel + " " + RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.TIME));
		tblclmnNewColumn_1.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		tblclmnNewColumn_2.setText(RedisClient.i18nFile.getText(I18nFile.MESSAGE));
	}
}
