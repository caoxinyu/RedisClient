package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public class OKCancel {
	protected Shell shell;
	
	public void setShell(Shell shell) {
		this.shell = shell;
	}

	private Button btnOk;
	
	private SelectionListener okSelection;
	private SelectionListener cancelSelection;
	
	public void setOkSelection(SelectionListener okSelection) {
		this.okSelection = okSelection;
	}

	public void setCancelSelection(SelectionListener cancelSelection) {
		this.cancelSelection = cancelSelection;
	}
	
	public OKCancel() {
		this.cancelSelection = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		};
	}

	protected void initContents() {
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));

		btnOk = new Button(composite_1, SWT.NONE);
		btnOk.addSelectionListener(okSelection);
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.OK));

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(cancelSelection);
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
	}

	public Button getOkButton() {
		return btnOk;
	}
	
}
