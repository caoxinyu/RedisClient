package com.cxy.redisclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableMultiCheckSelectionBorder {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Table table = new Table(shell, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION | SWT.CHECK);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		String[] titles = { " ", "C", "!", "Description", "Resource",
				"In Folder", "Location" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}

		for (int i = 0; i < 10; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "x");
			item.setText(1, "y");
			item.setText(2, "!");
			item.setText(3, "dddddddddddddddddddd");
			item.setText(4, "rrrrrrrrrrrrrrrrrrr");
			item.setText(5, "some.folder");
			item.setText(6, "line " + i + " in nowhere");
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		table.setSize(table.computeSize(SWT.DEFAULT, 200));
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}