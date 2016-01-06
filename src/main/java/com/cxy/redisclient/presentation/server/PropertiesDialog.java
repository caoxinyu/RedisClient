package com.cxy.redisclient.presentation.server;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class PropertiesDialog extends RedisClientDialog {

	private Map<String, String[]> values;
	private Server info;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PropertiesDialog(Shell parent, Image image, Server info, Map<String,  String[]> values) {
		super(parent, image);
		this.info = info;
		this.values = values;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setText(info.getName() + " " + RedisClient.i18nFile.getText(I18nFile.SERVERPROPERTY));
		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmServer = new TabItem(tabFolder, SWT.NONE);
		tbtmServer.setText(RedisClient.i18nFile.getText(I18nFile.GENERAL));
		
		Table tableServer = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmServer.setControl(tableServer);
		tableServer.setHeaderVisible(true);
		tableServer.setLinesVisible(true);
		EditListener listener = new EditListener(tableServer, false);
		tableServer.addListener(SWT.MouseDown, listener);
		
		TableColumn tblclmnKey = new TableColumn(tableServer, SWT.LEFT);
		tblclmnKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
		tblclmnKey.setWidth(250);
		
		TableColumn tblclmnValue = new TableColumn(tableServer, SWT.LEFT);
		tblclmnValue.setWidth(442);
		tblclmnValue.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		
		TableItem nameItem = new TableItem(tableServer, SWT.NONE);
		String[] nameKey = new String[]{RedisClient.i18nFile.getText(I18nFile.NAME), info.getName()};
		nameItem.setText(nameKey);
		
		TableItem hostItem = new TableItem(tableServer, SWT.NONE);
		String[] hostKey = new String[]{RedisClient.i18nFile.getText(I18nFile.HOST), info.getHost()};
		hostItem.setText(hostKey);
		
		TableItem portItem = new TableItem(tableServer, SWT.NONE);
		String[] portKey = new String[]{RedisClient.i18nFile.getText(I18nFile.PORT), info.getPort()};
		portItem.setText(portKey);
		
		Set<Entry<String, String[]>> set = values.entrySet();
		Iterator<Entry<String, String[]>> i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) i.next();
			TabItem tbtmServerInformation = new TabItem(tabFolder, SWT.NONE);
			tbtmServerInformation.setText(entry.getKey());
			Table table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
			tbtmServerInformation.setControl(table);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			EditListener listener1 = new EditListener(table, false);
			table.addListener(SWT.MouseDown, listener1);
			
			TableColumn clmnKey = new TableColumn(table, SWT.LEFT);
			clmnKey.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
			clmnKey.setWidth(250);
			
			TableColumn clmnValue = new TableColumn(table, SWT.LEFT);
			clmnValue.setWidth(442);
			clmnValue.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
			
			String[] keys = entry.getValue();
			for(String key: keys){
				String[] serverkeys = key.split(":");
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(serverkeys);
			}
		}
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton.setText(RedisClient.i18nFile.getText(I18nFile.OK));

		super.createContents();
	}
}
