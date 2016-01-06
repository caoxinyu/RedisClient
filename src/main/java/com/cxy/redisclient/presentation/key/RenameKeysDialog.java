package com.cxy.redisclient.presentation.key;

import org.eclipse.jface.dialogs.MessageDialog;
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

import com.cxy.redisclient.domain.ContainerKey;
import com.cxy.redisclient.dto.RenameInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class RenameKeysDialog extends RedisClientDialog {
	private Text text_2;
	private String server;
	private int db;
	private String oldContainer;
	private Button button;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RenameKeysDialog(Shell parent, Image image, String server, int db, String oldContainer) {
		super(parent, image);
		this.server = server;
		this.db = db;
		this.oldContainer = oldContainer == null?"":oldContainer;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.RENAME));
		
		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tabFolder.setSize(290, 122);
		
		TabItem tbtmString = new TabItem(tabFolder, SWT.NONE);
		tbtmString.setText(RedisClient.i18nFile.getText(I18nFile.KEY));
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmString.setControl(composite);
		composite.setLayout(new GridLayout(4, true));
		
		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText(RedisClient.i18nFile.getText(I18nFile.SERVER));
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(server);
		
		Label lblDatabase = new Label(composite, SWT.NONE);
		lblDatabase.setText(RedisClient.i18nFile.getText(I18nFile.DATABASE));
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(String.valueOf(db));
		
		Label lblNewKey = new Label(composite, SWT.NONE);
		lblNewKey.setText(RedisClient.i18nFile.getText(I18nFile.NEWKEY));
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		text_2.setText(oldContainer);
		text_2.selectAll();
		text_2.setFocus();
		text_2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String newContainer = text_2.getText() == null?"":text_2.getText();
				if(newContainer.equals(oldContainer)) 
					button.setEnabled(false);
				else
					button.setEnabled(true);
			}
		});
		
		final Button btnCheckButton = new Button(composite, SWT.CHECK);
		btnCheckButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnCheckButton.setSelection(true);
		btnCheckButton.setText(RedisClient.i18nFile.getText(I18nFile.OVERWRITTEN));
		
		final Button btnCheckButton1 = new Button(composite, SWT.CHECK);
		btnCheckButton1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnCheckButton1.setSelection(true);
		btnCheckButton1.setText(RedisClient.i18nFile.getText(I18nFile.RENAMESUB));
		final boolean isKey = new ContainerKey(oldContainer).isKey();
		if(isKey)
			btnCheckButton1.setVisible(false);
		else
			btnCheckButton1.setVisible(true);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		button = new Button(composite_1, SWT.NONE);
		button.setEnabled(false);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String newContainer = text_2.getText();
				boolean overwritten = btnCheckButton.getSelection(); 
				boolean renameSub = btnCheckButton1.getSelection(); 
				
				if( newContainer.length() == 0){
					MessageDialog.openError(shell, RedisClient.i18nFile.getText(I18nFile.ERROR),RedisClient.i18nFile.getText(I18nFile.INPUTNEWKEY));
				} else if(isKey && newContainer.endsWith(":")){
					MessageDialog.openError(shell, RedisClient.i18nFile.getText(I18nFile.ERROR),
							RedisClient.i18nFile.getText(I18nFile.KEYENDERROR)+ ":");
					
				}else {
					result = new RenameInfo(newContainer, overwritten, renameSub);
					shell.dispose();
				}
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
