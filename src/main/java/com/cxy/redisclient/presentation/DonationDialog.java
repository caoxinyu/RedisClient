package com.cxy.redisclient.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class DonationDialog extends RedisClientDialog {
	private Image code;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DonationDialog(Shell parent, Image image, Image code) {
		super(parent, image);
		this.code = code;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.DONATION));
		shell.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.WRAP);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText(RedisClient.i18nFile.getText(I18nFile.DONATIONMESSAGE));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label.setAlignment(SWT.CENTER);
		label.setImage(code);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnOk = new Button(composite_1, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.dispose();
			}
		});
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.OK));

		super.createContents();
	}

}
