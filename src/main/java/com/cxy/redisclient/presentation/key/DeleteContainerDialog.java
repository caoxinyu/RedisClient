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
		shell.setSize(500, 200);
		shell.setText("Delete");
		shell.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setImage(questionImage);
		lblNewLabel_1.setBounds(0, 0, 61, 17);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 61, 17);
		if(containerNo == -1)
			lblNewLabel.setText("Are you sure delete these containers and keys?");
		else if(containerNo == 1)
			lblNewLabel.setText("Are you sure delete this container?");
		else
			lblNewLabel.setText("Are you sure delete these keys?");
		
		if(containerNo != 0){
			btnDeleteSubcontainerUnder = new Button(shell, SWT.CHECK);
			btnDeleteSubcontainerUnder.setSelection(true);
			btnDeleteSubcontainerUnder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			btnDeleteSubcontainerUnder.setBounds(0, 0, 98, 17);
			btnDeleteSubcontainerUnder.setText("Delete subcontainer under the container");
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
		button.setText("OK");
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.dispose();
			}
		});
		button_1.setText("Cancel");

		super.createContents();
	}
}
