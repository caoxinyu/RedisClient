package com.cxy.redisclient.presentation.key;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.dto.FindInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class FindKeyDialog extends RedisClientDialog {

	private Text pattern;
	private Button btnFind;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FindKeyDialog(Shell parent, Image image) {
		super(parent,  image);
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.FIND));

		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmFind = new TabItem(tabFolder, SWT.NONE);
		tbtmFind.setText(RedisClient.i18nFile.getText(I18nFile.FIND));
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmFind.setControl(composite);
		composite.setLayout(new GridLayout(6, true));
		
		Label lblFind = new Label(composite, SWT.NONE);
		lblFind.setText(RedisClient.i18nFile.getText(I18nFile.FINDDATA));
		
		pattern = new Text(composite, SWT.BORDER);
		pattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		pattern.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String p = pattern.getText() == null?"":pattern.getText();
				if(p.length() == 0)
					btnFind.setEnabled(false);
				else
					btnFind.setEnabled(true);
			}
		});
		pattern.setFocus();
		
		Group grpFindDirection = new Group(composite, SWT.NONE);
		grpFindDirection.setLayout(new GridLayout(1, false));
		grpFindDirection.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		grpFindDirection.setText(RedisClient.i18nFile.getText(I18nFile.FINDDIRECTION));
		
		final Button btnForward = new Button(grpFindDirection, SWT.RADIO);
		btnForward.setSelection(true);
		btnForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnForward.setText(RedisClient.i18nFile.getText(I18nFile.FORWARD));
		
		Button btnBackward = new Button(grpFindDirection, SWT.RADIO);
		btnBackward.setText(RedisClient.i18nFile.getText(I18nFile.BACKWARD));
		
		Group grpKeyType = new Group(composite, SWT.NONE);
		grpKeyType.setLayout(new GridLayout(1, false));
		grpKeyType.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		grpKeyType.setText(RedisClient.i18nFile.getText(I18nFile.KEYTYPE));
		
		final Button btnString = new Button(grpKeyType, SWT.CHECK);
		btnString.setSelection(true);
		btnString.setText(RedisClient.i18nFile.getText(I18nFile.STRING));
		
		final Button btnHash = new Button(grpKeyType, SWT.CHECK);
		btnHash.setSelection(true);
		btnHash.setText(RedisClient.i18nFile.getText(I18nFile.HASH));
		
		final Button btnList = new Button(grpKeyType, SWT.CHECK);
		btnList.setSelection(true);
		btnList.setText(RedisClient.i18nFile.getText(I18nFile.LIST));
		
		final Button btnSet = new Button(grpKeyType, SWT.CHECK);
		btnSet.setSelection(true);
		btnSet.setText(RedisClient.i18nFile.getText(I18nFile.SET));
		
		final Button btnSortedSet = new Button(grpKeyType, SWT.CHECK);
		btnSortedSet.setSelection(true);
		btnSortedSet.setText(RedisClient.i18nFile.getText(I18nFile.ZSET));
		
		
		Composite composite1 = new Composite(shell, SWT.NONE);
		composite1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
	    
		btnFind = new Button(composite1, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<NodeType> types = new ArrayList<NodeType>();
				if(btnString.getSelection())
					types.add(NodeType.STRING);
				if(btnHash.getSelection())
					types.add(NodeType.HASH);
				if(btnList.getSelection())
					types.add(NodeType.LIST);
				if(btnSet.getSelection())
					types.add(NodeType.SET);
				if(btnSortedSet.getSelection())
					types.add(NodeType.SORTEDSET);
				
				if(types.size() == 0)
					MessageDialog.openError(shell, RedisClient.i18nFile.getText(I18nFile.ERROR),RedisClient.i18nFile.getText(I18nFile.SELECTTYPE));
				else{
					if(btnForward.getSelection())
						result = new FindInfo(pattern.getText(), types, true);
					else
						result = new FindInfo(pattern.getText(), types, false);
					shell.dispose();
				}
				
			}
		});
		btnFind.setEnabled(false);
		btnFind.setText(RedisClient.i18nFile.getText(I18nFile.FIND));
		
		Button btnCancel = new Button(composite1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		super.createContents();
	}
}
