package com.cxy.redisclient.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.component.RedisClientDialog;

public class AboutDialog extends RedisClientDialog {

	private final class OpenUrl extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			Program.launch(arg0.text);
		}
	}

	private OpenUrl openUrl = new OpenUrl();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent, Image image) {
		super(parent, image);
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.ABOUTREDISCLIENT));
		shell.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		final int width = image.getBounds().width;
	    final int height = image.getBounds().height;
		final Image scaled050 = new Image(shell.getDisplay(),
		        image.getImageData().scaledTo((int)(width*0.5),(int)(height*0.5)));
		
		Label btnNewButton_1 = new Label(composite, SWT.FLAT);
		btnNewButton_1.setBounds(0, 0, 80, 27);
		btnNewButton_1.setImage(scaled050);
		
		
		Link lblNewLabel = new Link(composite, SWT.NONE);
		lblNewLabel.addSelectionListener(openUrl);
		lblNewLabel.setFont(SWTResourceManager.getFont("Arial", 20, SWT.NORMAL));
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblNewLabel.setBounds(0, 0, 61, 17);
		lblNewLabel.setText("<a href=\"https://github.com/caoxinyu/RedisClient\">RedisClient for Windows</a>");
		
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText(RedisClient.i18nFile.getText(I18nFile.VERSION));
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setText("2.0.0");
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setText(RedisClient.i18nFile.getText(I18nFile.DEVELOPER));
		
		Link link = new Link(composite, SWT.NONE);
		link.addSelectionListener(openUrl);
		link.setText("<a href=\"mailto:caoxinyu@gmail.com\">" + RedisClient.i18nFile.getText(I18nFile.CAOXINYU) + "</a>");
		new Label(composite, SWT.NONE);
		
		Label lblReportIssue = new Label(composite, SWT.NONE);
		lblReportIssue.setText(RedisClient.i18nFile.getText(I18nFile.ISSUE));
		
		Link link_1 = new Link(composite, SWT.NONE);
		link_1.addSelectionListener(openUrl);
		link_1.setText("<a href=\"https://github.com/caoxinyu/redisclient/issues?state=open\">" + RedisClient.i18nFile.getText(I18nFile.CLICK) + "</a>");
		new Label(composite, SWT.NONE);
		
		Label lblStar = new Label(composite, SWT.NONE);
		lblStar.setText(RedisClient.i18nFile.getText(I18nFile.STAR));
		
		Link link_2 = new Link(composite, SWT.NONE);
		link_2.addSelectionListener(openUrl);
		link_2.setText("<a href=\"https://github.com/caoxinyu/RedisClient/stargazers\">" + RedisClient.i18nFile.getText(I18nFile.CLICK) + "</a>");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.dispose();
			}
		});
		btnNewButton.setText(RedisClient.i18nFile.getText(I18nFile.OK));

		super.createContents();
	}
}
