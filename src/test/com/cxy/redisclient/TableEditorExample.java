package com.cxy.redisclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class TableEditorExample {
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    final Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
    table.setLinesVisible(true);
    for (int i = 0; i < 3; i++) {
      TableColumn column = new TableColumn(table, SWT.NONE);
      column.setWidth(100);
    }
    for (int i = 0; i < 3; i++) {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(new String[] { "" + i, "" + i, "" + i });
    }
    final TableEditor editor = new TableEditor(table);
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;
    table.addListener(SWT.MouseDown, new Listener() {
      public void handleEvent(Event event) {
        Rectangle clientArea = table.getClientArea();
        Point pt = new Point(event.x, event.y);
        int index = table.getTopIndex();
        while (index < table.getItemCount()) {
          boolean visible = false;
          final TableItem item = table.getItem(index);
          for (int i = 0; i < table.getColumnCount(); i++) {
            Rectangle rect = item.getBounds(i);
            if (rect.contains(pt)) {
              final int column = i;
              final Text text = new Text(table, SWT.NONE);
              Listener textListener = new Listener() {
                public void handleEvent(final Event e) {
                  switch (e.type) {
                  case SWT.FocusOut:
                    item.setText(column, text.getText());
                    text.dispose();
                    break;
                  case SWT.Traverse:
                    switch (e.detail) {
                    case SWT.TRAVERSE_RETURN:
                      item
                          .setText(column, text
                              .getText());
                    //FALL THROUGH
                    case SWT.TRAVERSE_ESCAPE:
                      text.dispose();
                      e.doit = false;
                    }
                    break;
                  }
                }
              };
              text.addListener(SWT.FocusOut, textListener);
              text.addListener(SWT.Traverse, textListener);
              editor.setEditor(text, item, i);
              text.setText(item.getText(i));
              text.selectAll();
              text.setFocus();
              return;
            }
            if (!visible && rect.intersects(clientArea)) {
              visible = true;
            }
          }
          if (!visible)
            return;
          index++;
        }
      }
    });
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}

