package com.cxy.redisclient.presentation.pubsub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.Tool;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.service.PubSubService;
import com.cxy.redisclient.service.ServerService;

public class Publish  implements Tool{
	private CTabFolder tabFolder;
	private int id;
	private Server server;
	private ServerService service = new ServerService();
	private PubSubService publish = new PubSubService();
	
	private CTabItem tbtmNewItem;
	private Button btnNewButton;
	private Label label;
	private Label label1;
	private TableColumn tblclmnNewColumn_1;
	private TableColumn tblclmnNewColumn_2;
	private TableColumn tblclmnNewColumn;
	private Composite composite_3;
	
	public Publish(CTabFolder tabFolder, int id) {
		this.tabFolder = tabFolder;
		this.id = id;
	}
	public CTabItem init(){
		server = service.listById(id);
		Image pubImage = new Image(tabFolder.getShell().getDisplay(),
				getClass().getResourceAsStream("/publish.png"));
		
		tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setShowClose(true);
		composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.PUBLISH));
		tbtmNewItem.setImage(pubImage);
		
		SashForm sashForm_2 = new SashForm(composite_3, SWT.NONE);
		sashForm_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_4 = new Composite(sashForm_2, SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		
		Composite composite_5 = new Composite(composite_4, SWT.NONE);
		composite_5.setLayout(new GridLayout(3, false));
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		label = new Label(composite_5, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		
		final Text channel = new Text(composite_5, SWT.BORDER);
		channel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		channel.setBounds(0, 0, 73, 21);
		channel.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				channel.setFocus();
			}
		});
		
		Composite composite_6 = new Composite(composite_4, SWT.NONE);
		composite_6.setLayout(new GridLayout(2, false));
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		label1 = new Label(composite_6, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		label1.setText(RedisClient.i18nFile.getText(I18nFile.MESSAGE));
		
		final Text message = new Text(composite_6, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		message.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		message.setBounds(0, 0, 73, 21);
		
		final Table table = new Table(sashForm_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		EditListener listener = new EditListener(table, false, true);
		table.addListener(SWT.MouseDown, listener);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.TIME));
		
		tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		
		tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText(RedisClient.i18nFile.getText(I18nFile.MESSAGE));
		sashForm_2.setWeights(new int[] {1, 2});
		
		btnNewButton = new Button(composite_5, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setBounds(0, 0, 75, 25);
		btnNewButton.setText(RedisClient.i18nFile.getText(I18nFile.PUBLISH));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(channel.getText().length() > 0 && message.getText().length() > 0){
					publish.publish(id, channel.getText(), message.getText());
					TableItem item = new TableItem(table, SWT.None);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
					String time = df.format(new Date());
					String[] str = new String[]{time, channel.getText(), message.getText()};
					item.setText(str);
					table.setSelection(item);
					channel.selectAll();
					message.selectAll();
					channel.setFocus();
				}
				
			}
		});
		
		tabFolder.setSelection(tbtmNewItem);
		channel.setFocus();
		
		return tbtmNewItem;
	}
	@Override
	public void refreshLangUI(){
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.PUBLISH));
		btnNewButton.setText(RedisClient.i18nFile.getText(I18nFile.PUBLISH));
		label.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		label1.setText(RedisClient.i18nFile.getText(I18nFile.MESSAGE));
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.TIME));
		tblclmnNewColumn_1.setText(RedisClient.i18nFile.getText(I18nFile.CHANNEL));
		tblclmnNewColumn_2.setText(RedisClient.i18nFile.getText(I18nFile.MESSAGE));
		composite_3.pack();
	}
	@Override
	public int getId() {
		return id;
	}
	@Override
	public CTabItem getTbtmNewItem() {
		return tbtmNewItem;
	}
}
