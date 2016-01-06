package com.cxy.redisclient.presentation.component;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public abstract class RedisClientDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Image image;
	protected int minWidth = 350;
	protected int minHeight = 150;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RedisClientDialog(Shell parent, Image image) {
		super(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		this.image = image;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		shell = new Shell(getParent(), getStyle());
		shell.setImage(image);
		
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				MessageDialog.openError(shell, RedisClient.i18nFile.getText(I18nFile.ERROR), e.getLocalizedMessage());
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setMinimumSize(minWidth, minHeight);
		shell.pack();
		setMiddle();
	}

	protected void setMiddle() {
		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
	}

}
