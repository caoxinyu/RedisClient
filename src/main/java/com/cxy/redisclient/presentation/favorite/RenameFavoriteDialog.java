package com.cxy.redisclient.presentation.favorite;

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

import com.cxy.redisclient.domain.Favorite;

public class RenameFavoriteDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Favorite oldFavorite;
	private Text text;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RenameFavoriteDialog(Shell parent, int style, Favorite favorite) {
		super(parent, style);
		setText("SWT Dialog");
		this.oldFavorite = favorite;
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
		shell.setText("Update favorite");

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
		lblNewKey.setBounds(10, 13, 51, 13);
		
		final Text text_2 = new Text(composite, SWT.BORDER);
		text_2.setText(oldFavorite.getName());
		text_2.setBounds(62, 10, 220, 19);
		text_2.selectAll();
		text_2.setFocus();
		
		Label lblFavorite = new Label(composite, SWT.NONE);
		lblFavorite.setText("Favorite");
		lblFavorite.setBounds(10, 55, 51, 13);
		
		text = new Text(composite, SWT.BORDER);
		text.setEditable(false);
		text.setText(oldFavorite.getFavorite());
		text.setBounds(62, 52, 220, 19);
		
		
		Button button = new Button(shell, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String name = text_2.getText();
				
				if( name.length() == 0 ){
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
