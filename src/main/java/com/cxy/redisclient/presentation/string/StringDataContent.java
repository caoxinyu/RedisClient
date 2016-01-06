package com.cxy.redisclient.presentation.string;

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
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.WatchDialog;
import com.cxy.redisclient.presentation.component.DataContent;
import com.cxy.redisclient.service.NodeService;

public class StringDataContent extends DataContent {
	private NodeService service = new NodeService();
	private String value;
	private Button btnOk;
	private Label label;
	private Button btnCancel;
	private Button btnWatch;
	private Button btnRefresh;

	public StringDataContent(CTabItem tabItem, Image image, int id,
			String server, int db, String key, String dataTitle) {
		super(tabItem, image, id, server, db, key, dataTitle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initData(Composite dataComposite) {
		label = new Label(dataComposite, SWT.NONE);
		label.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

		final Text text_value = new Text(dataComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		text_value.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		value = service.readString(id, db, key);
				
		text_value.setText(value);
		text_value.selectAll();
		text_value.setFocus();

		new Label(dataComposite, SWT.NONE);
		new Label(dataComposite, SWT.NONE);
		new Label(dataComposite, SWT.NONE);

		Composite composite = new Composite(dataComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(4, false));

		btnOk = new Button(composite, SWT.NONE);
		btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		setApply(false);

		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCancel.setEnabled(false);
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));

		text_value.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String newValue = text_value.getText() == null ? "" : text_value.getText();
				if (newValue.equals(value)) {
					setApply(false);
					btnCancel.setEnabled(false);
				} else {
					setApply(true);
					btnCancel.setEnabled(true);
				}
			}
		});

		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = getKey();
				value = text_value.getText();

				service.updateString(id, db, key, value);
				setApply(false);
				btnCancel.setEnabled(false);
			}
		});

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_value.setText(value);
				setApply(false);
				btnCancel.setEnabled(false);
			}
		});
		btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				value = service.readString(id, db, key);

				text_value.setText(value);
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnRefresh.setEnabled(true);
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));

		btnWatch = new Button(composite, SWT.NONE);
		btnWatch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		btnWatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WatchDialog dialog = new WatchDialog(shell.getParent()
						.getShell(), image, text_value.getText());
				dialog.open();
			}
		});
	}

	@Override
	public void refreshLangUI() {
		label.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		super.refreshLangUI();
	}

	public boolean isApply() {
		return btnOk.isEnabled();
	}

	@Override
	public Button getApplyButtion() {
		return btnOk;
	}
}
