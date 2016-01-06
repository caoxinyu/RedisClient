package com.cxy.redisclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class GridLayoutDialog {
  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setSize(214, 236);
    shell.setLayout(new GridLayout(1, true));

    TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
    tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    
    TabItem tbtmFind = new TabItem(tabFolder, SWT.NONE);
    tbtmFind.setText("Find");
    
    Composite composite = new Composite(tabFolder, SWT.NONE);
    tbtmFind.setControl(composite);
    composite.setLayout(new GridLayout(6, true));
    
    Label lblKey = new Label(composite, SWT.NONE);
    lblKey.setText("KEY");
    
    Text txtKey = new Text(composite, SWT.BORDER);
    txtKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
    
    Group direction = new Group(composite, SWT.None);
    direction.setText("direction");
    direction.setLayout(new GridLayout(1, false));
    direction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
    
    Button btnRadioButton = new Button(direction, SWT.RADIO);
    btnRadioButton.setText("Radio Button");
    
    Button btnRadioButton_1 = new Button(direction, SWT.RADIO);
    btnRadioButton_1.setText("Radio Button");
   
    Group type = new Group(composite, SWT.None);
    type.setText("type");
    type.setLayout(new GridLayout(1, false));
    type.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
    
    Button btnCheckButton = new Button(type, SWT.CHECK);
    btnCheckButton.setBounds(0, 0, 143, 24);
    btnCheckButton.setText("Check Button");
    
    Button btnCheckButton_1 = new Button(type, SWT.CHECK);
    btnCheckButton_1.setBounds(0, 0, 143, 24);
    btnCheckButton_1.setText("Check Button");
    
    Button btnCheckButton_2 = new Button(type, SWT.CHECK);
    btnCheckButton_2.setBounds(0, 0, 143, 24);
    btnCheckButton_2.setText("Check Button");
    
    Button btnCheckButton_3 = new Button(type, SWT.CHECK);
    btnCheckButton_3.setBounds(0, 0, 143, 24);
    btnCheckButton_3.setText("Check Button");
    
    Button btnCheckButton_4 = new Button(type, SWT.CHECK);
    btnCheckButton_4.setBounds(0, 0, 143, 24);
    btnCheckButton_4.setText("Check Button");
    
    Composite composite1 = new Composite(shell, SWT.NONE);
    composite1.setLayout(new GridLayout(2, false));
    composite1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    
    Button btnFind = new Button(composite1, SWT.NONE);
    btnFind.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
    btnFind.setText("find");
    
    Button btnCancel = new Button(composite1, SWT.NONE);
    btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    btnCancel.setText("cancel");
    
    shell.pack();
    new Label(shell, SWT.NONE);
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}