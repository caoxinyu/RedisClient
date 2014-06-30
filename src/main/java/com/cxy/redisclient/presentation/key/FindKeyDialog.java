package com.cxy.redisclient.presentation.key;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.dto.FindInfo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;

public class FindKeyDialog extends Dialog {

	protected FindInfo result = null;
	protected Shell shlFind;
	private Text pattern;
	private Button btnFind;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FindKeyDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlFind.open();
		shlFind.layout();
		Display display = getParent().getDisplay();
		while (!shlFind.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlFind = new Shell(getParent(), getStyle());
		shlFind.setSize(612, 369);
		shlFind.setText("Find");

		Rectangle screenSize = shlFind.getParent().getBounds();
		Rectangle shellSize = shlFind.getBounds();
		shlFind.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		shlFind.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shlFind, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmFind = new TabItem(tabFolder, SWT.NONE);
		tbtmFind.setText("Find");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmFind.setControl(composite);
		composite.setLayout(new GridLayout(6, true));
		
		Label lblFind = new Label(composite, SWT.NONE);
		lblFind.setText("Find key");
		
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
		grpFindDirection.setText("Find direction");
		
		final Button btnForward = new Button(grpFindDirection, SWT.RADIO);
		btnForward.setSelection(true);
		btnForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnForward.setText("Forward");
		
		Button btnBackward = new Button(grpFindDirection, SWT.RADIO);
		btnBackward.setText("Backward");
		
		Group grpKeyType = new Group(composite, SWT.NONE);
		grpKeyType.setLayout(new GridLayout(1, false));
		grpKeyType.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		grpKeyType.setText("Key type");
		
		final Button btnString = new Button(grpKeyType, SWT.CHECK);
		btnString.setSelection(true);
		btnString.setText("String");
		
		final Button btnHash = new Button(grpKeyType, SWT.CHECK);
		btnHash.setSelection(true);
		btnHash.setText("Hash");
		
		final Button btnList = new Button(grpKeyType, SWT.CHECK);
		btnList.setSelection(true);
		btnList.setText("List");
		
		final Button btnSet = new Button(grpKeyType, SWT.CHECK);
		btnSet.setSelection(true);
		btnSet.setText("Set");
		
		final Button btnSortedSet = new Button(grpKeyType, SWT.CHECK);
		btnSortedSet.setSelection(true);
		btnSortedSet.setText("Sorted Set");
		
		
		Composite composite1 = new Composite(shlFind, SWT.NONE);
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
					MessageDialog.openError(shlFind, "error","please select a key type at least!");
				else{
					if(btnForward.getSelection())
						result = new FindInfo(pattern.getText(), types, true);
					else
						result = new FindInfo(pattern.getText(), types, false);
					shlFind.dispose();
				}
				
			}
		});
		btnFind.setEnabled(false);
		btnFind.setText("Find");
		
		Button btnCancel = new Button(composite1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shlFind.dispose();
			}
		});
		btnCancel.setText("Cancel");
		
	}
}
