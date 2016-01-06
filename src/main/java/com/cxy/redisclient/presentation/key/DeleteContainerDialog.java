package com.cxy.redisclient.presentation.key;

import org.eclipse.swt.SWT;
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

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class DeleteContainerDialog extends RedisClientDialog {

	private Button btnDeleteSubcontainerUnder;
	private Image questionImage;
	private int containerNo;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DeleteContainerDialog(Shell parent, Image image, Image questionImage, int containerNo) {
		super(parent, image);
		this.questionImage = questionImage;
		this.containerNo = containerNo;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		shell.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setImage(questionImage);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		if(containerNo == -1)
			lblNewLabel.setText(RedisClient.i18nFile.getText(I18nFile.DELETECONTAINERS));
		else if(containerNo == 1)
			lblNewLabel.setText(RedisClient.i18nFile.getText(I18nFile.DELETECONTAINER));
		else
			lblNewLabel.setText(RedisClient.i18nFile.getText(I18nFile.DELETEKEYS));
		
		if(containerNo != 0){
			btnDeleteSubcontainerUnder = new Button(shell, SWT.CHECK);
			btnDeleteSubcontainerUnder.setSelection(true);
			btnDeleteSubcontainerUnder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			btnDeleteSubcontainerUnder.setText(RedisClient.i18nFile.getText(I18nFile.DELETESUBCONTAINER));
		}else
			btnDeleteSubcontainerUnder = null;
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button button = new Button(composite_1, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(btnDeleteSubcontainerUnder!= null)
					result = btnDeleteSubcontainerUnder.getSelection();
				else
					result = false;
				
				shell.dispose();
			}
		});
		button.setText(RedisClient.i18nFile.getText(I18nFile.OK));
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});
		button_1.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));

		super.createContents();
	}
}
