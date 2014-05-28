package com.cxy.redisclient;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class AddFavoriteDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String container;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddFavoriteDialog(Shell parent, int style, String container) {
		super(parent, style);
		setText("SWT Dialog");
		this.container = container;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(326, 201);
		shell.setText("Add favorite");

		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 300, 121);
		
		TabItem tbtmString = new TabItem(tabFolder, SWT.NONE);
		tbtmString.setText("Favorite");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmString.setControl(composite);
		
		Label lblNewKey = new Label(composite, SWT.NONE);
		lblNewKey.setText("Name");
		lblNewKey.setBounds(10, 44, 45, 13);
		
		final Text text_2 = new Text(composite, SWT.BORDER);
		text_2.setText(container==null?"": container);
		text_2.setBounds(62, 41, 220, 19);
		text_2.selectAll();
		text_2.setFocus();
		
		Button button = new Button(shell, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String name = text_2.getText();
				
				if( name.length() == 0){
					MessageDialog.openError(shell, "error","please input favorite name!");
				} else {
					result = name;
					shell.dispose();
				}
			}
		});
		button.setText("OK");
		button.setBounds(61, 144, 68, 23);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});
		button_1.setText("Cancel");
		button_1.setBounds(188, 144, 68, 23);
	}

}
