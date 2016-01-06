package com.cxy.redisclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class TableWithPageSize {

  static final int PAGE_SIZE = 64;

  static final int COUNT = 100000;

  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setLayout(new RowLayout(SWT.VERTICAL));
    final Table table = new Table(shell, SWT.VIRTUAL | SWT.BORDER);
    table.addListener(SWT.SetData, new Listener() {
      public void handleEvent(Event event) {
        TableItem item = (TableItem) event.item;
        int index = table.indexOf(item);
        int start = index / PAGE_SIZE * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, table.getItemCount());
        for (int i = start; i < end; i++) {
          item = table.getItem(i);
          item.setText("Item " + i);
        }
      }
    });
    table.setLayoutData(new RowData(200, 200));
    long t1 = System.currentTimeMillis();
    table.setItemCount(COUNT);
    long t2 = System.currentTimeMillis();
    System.out.println("Items: " + COUNT + ", Time: " + (t2 - t1) + " (ms) [page=" + PAGE_SIZE
        + "]");
    shell.layout();
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
