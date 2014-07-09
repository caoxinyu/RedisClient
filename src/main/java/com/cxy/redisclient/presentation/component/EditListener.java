package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class EditListener implements Listener {
	private Table table;
	private TableEditor editor;
	
	public EditListener(Table table){
		this.table = table;
		editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}
	@Override
	public void handleEvent(Event event) {
		Rectangle clientArea = table.getClientArea();
		Point pt = new Point(event.x, event.y);
		int index = table.getTopIndex();
		int count = table.getItemCount();
		while (index <= count) {
			boolean visible = false;
			final TableItem item;
			if(index == count){
				boolean isNull = false;
				if(index == 0)
					isNull = false;
				else{
					TableItem prevItem = table.getItem(index - 1);
					for (int i = 0; i < table.getColumnCount(); i++){
						if(prevItem.getText(i).length() == 0){
							isNull = true;
							break;
						}
					}
				}
				if(!isNull)
					item = new TableItem(table, SWT.NONE);
				else
					return;
			}else
				item = table.getItem(index);
			
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
									item.setText(column, text.getText());
									// FALL THROUGH
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

}
