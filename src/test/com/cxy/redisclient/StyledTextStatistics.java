package com.cxy.redisclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class StyledTextStatistics {
  public static void main(String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);

    StyledText styledText = new StyledText(shell, SWT.V_SCROLL | SWT.BORDER);

    styledText.setText("12345\r\n67890\r\nabcde");
    
    styledText.setCaretOffset(7);
    
    System.out.println("Caret Offset: " + styledText.getCaretOffset());
    System.out.println("Total Lines of Text: " + styledText.getLineCount());
    System.out.println("Total Characters: " + styledText.getCharCount());
    System.out.println("Current Line: " + (styledText.getLineAtOffset(styledText.getCaretOffset()) + 1));

    styledText.setCaretOffset(styledText.getCharCount() + 1);
    
    
    styledText.setBounds(10,10,100,100);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();

  }
}
