package com.cxy.redisclient.presentation.pubsub;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.Tool;
import com.cxy.redisclient.service.ServerService;

public class Subscribe implements Tool {
	private CTabFolder tabFolder;
	private int id;
	private Server server;
	private ServerService service = new ServerService();
	
	private CTabItem tbtmNewItem;
	private Composite composite_3;
	private Label lblNewLabel;
	private Button btnNewButton;
	
	private List<SubChannel> subChannels = new ArrayList<SubChannel>();
	
	public Subscribe(CTabFolder tabFolder, int id) {
		this.tabFolder = tabFolder;
		this.id = id;
	}
	@Override
	public int getId() {
		return id;
	}

	@Override
	public CTabItem getTbtmNewItem() {
		return tbtmNewItem;
	}

	@Override
	public CTabItem init() {
		server = service.listById(id);
		Image subImage = new Image(tabFolder.getShell().getDisplay(),
				getClass().getResourceAsStream("/subscribe.png"));
		
		tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setShowClose(true);
		composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.SUBSCRIBE));
		tbtmNewItem.setImage(subImage);
		
		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_4.setLayout(new GridLayout(3, false));
		
		lblNewLabel = new Label(composite_4, SWT.NONE);
		lblNewLabel.setBounds(0, 0, 55, 15);
		lblNewLabel.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		
		final Text channel = new Text(composite_4, SWT.BORDER);
		channel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		channel.setBounds(0, 0, 88, 23);
		channel.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				channel.setFocus();
			}
		});
		
		btnNewButton = new Button(composite_4, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setBounds(0, 0, 75, 25);
		btnNewButton.setText(RedisClient.i18nFile.getText(I18nFile.SUBSCRIBE));
		
		Composite composite_5 = new Composite(composite_3, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_5.setLayout(new GridLayout(1, false));
		
		final CTabFolder tabFolder_2 = new CTabFolder(composite_5, SWT.BORDER);
		tabFolder_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder_2.setBounds(0, 0, 156, 125);
		tabFolder_2.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(channel.getText().length() > 0){
					final String subChannel = channel.getText();
										
					SubChannel subedChannel = new SubChannel(id, subChannel, tabFolder_2);
					final CTabItem tbtmSubItem = subedChannel.init();
					subChannels.add(subedChannel);
					
					tbtmSubItem.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(DisposeEvent e) {
							subChannels.remove(tbtmSubItem);
						}
					});
					tabFolder_2.setSelection(tbtmSubItem);
					channel.setFocus();
				}
			}
		});
		
		tabFolder.setSelection(tbtmNewItem);
		channel.setFocus();
		
		return tbtmNewItem;
	}

	@Override
	public void refreshLangUI() {
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.SUBSCRIBE));
		lblNewLabel.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		btnNewButton.setText(RedisClient.i18nFile.getText(I18nFile.SUBSCRIBE));
		for(SubChannel subedChannel: subChannels){
			subedChannel.refreshLangUI();
		}
		composite_3.pack();
	}

}
