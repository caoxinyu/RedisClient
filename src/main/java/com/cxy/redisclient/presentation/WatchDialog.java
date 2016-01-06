package com.cxy.redisclient.presentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import sun.misc.BASE64Decoder;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.component.RedisClientDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class WatchDialog extends RedisClientDialog {
	private String value;
	private int currentTextType = 0;
	private int currentImageType = -1;
	private Text text;
	private Label label;
	private boolean currentText = true;

	public WatchDialog(Shell parent, Image image, String value) {
		super(parent, image);
		this.value = value;
	}

	@Override
	protected void createContents() {
		shell.setText(RedisClient.i18nFile.getText(I18nFile.DATAWATCHER));
		shell.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Group grpValueType = new Group(composite, SWT.NONE);
		grpValueType.setLayout(new GridLayout(2, false));
		grpValueType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpValueType.setText(RedisClient.i18nFile.getText(I18nFile.DATATYPE));

		Button btnRadioButton = new Button(grpValueType, SWT.RADIO);
		btnRadioButton.setSelection(true);
		btnRadioButton.setText(RedisClient.i18nFile.getText(I18nFile.TEXT));
		final Combo textType = new Combo(grpValueType, SWT.DROP_DOWN | SWT.READ_ONLY);
		textType.setItems(new String[] { RedisClient.i18nFile.getText(I18nFile.PLAINTEXT), RedisClient.i18nFile.getText(I18nFile.JSON), RedisClient.i18nFile.getText(I18nFile.XML) });
		textType.select(0);

		Button btnRadioButton_1 = new Button(grpValueType, SWT.RADIO);
		btnRadioButton_1.setText(RedisClient.i18nFile.getText(I18nFile.IMAGE));
		final Combo imageType = new Combo(grpValueType, SWT.DROP_DOWN | SWT.READ_ONLY);
		imageType.setEnabled(false);
		imageType.setItems(new String[] { RedisClient.i18nFile.getText(I18nFile.BASE64IMAGE) });

		final Group grpValue = new Group(composite, SWT.NONE);
		grpValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpValue.setText(RedisClient.i18nFile.getText(I18nFile.DATA));
		grpValue.setBounds(0, 0, 70, 81);
		grpValue.setLayout(new GridLayout(1, false));

		text = new Text(grpValue, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text.setText(value);

		btnRadioButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!currentText) {
					imageType.setEnabled(false);
					textType.setEnabled(true);
					if (label != null && !label.isDisposed()) {
						label.setVisible(false);
						label.dispose();
					}
					text = new Text(grpValue, SWT.BORDER | SWT.V_SCROLL	| SWT.H_SCROLL | SWT.MULTI);
					text.setEditable(false);
					text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					text.setText(value);
					tranformText(textType, text);
					currentText = true;
				}
			}
		});
		btnRadioButton_1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (currentText) {
					imageType.setEnabled(true);
					textType.setEnabled(false);
					if (text != null && !text.isDisposed()) {
						text.setVisible(false);
						text.dispose();
					}
					label = new Label(grpValue, SWT.NONE);
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					label.setAlignment(SWT.CENTER);
					tranformImage(imageType, label);
					currentText = false;
				}
			}
		});
		textType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tranformText(textType, text);
				currentTextType = textType.getSelectionIndex();
			}
		});
		imageType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tranformImage(imageType, label);
				currentImageType = imageType.getSelectionIndex();
			}
		});

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

	private void tranformImage(Combo imageType, Label label) {
		int index = imageType.getSelectionIndex();
		if (index == 0) {
			BASE64Decoder decode = new BASE64Decoder();

			try {
				byte[] b = decode.decodeBuffer(value);
				ByteArrayInputStream bais = new ByteArrayInputStream(b);
				Image img = new Image(shell.getDisplay(), bais);
				label.setImage(img);
				
			} catch (SWTException e) {
				imageType.select(currentImageType);
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.IMAGEEXCEPTION));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		shell.pack();
		setMiddle();
	}

	private void tranformText(final Combo textType, final Text text) {
		int index = textType.getSelectionIndex();
		if (index == 0) {
			text.setText(value);
		} else if (index == 1) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			try {
				JsonElement je = jp.parse(value);
				String prettyJsonString = gson.toJson(je);
				text.setText(prettyJsonString);
			} catch (JsonSyntaxException e) {
				textType.select(currentTextType);
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.JSONEXCEPTION));
			}

		} else if (index == 2) {
			SAXReader reader = new SAXReader();

			StringReader in = new StringReader(value);
			Document doc;
			try {
				doc = reader.read(in);

				OutputFormat formater = OutputFormat.createPrettyPrint();

				StringWriter out = new StringWriter();

				XMLWriter writer = new XMLWriter(out, formater);

				writer.write(doc);

				writer.close();
				text.setText(out.getBuffer().toString());
			} catch (DocumentException e) {
				textType.select(currentTextType);
				throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.XMLEXCEPTION));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		shell.pack();
		setMiddle();
	}
}
