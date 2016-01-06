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
	protected Text text;
	private boolean edit;
	private boolean multiline;
	
	public EditListener(Table table, boolean edit){
		this.table = table;
		this.edit = edit;
		editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}
	public EditListener(Table table, boolean edit, boolean multiline){
		this.table = table;
		this.edit = edit;
		this.multiline = multiline;
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
			while (index < count) {
				boolean visible = false;
				TableItem item = table.getItem(index);
				
				for (int i = 0; i < table.getColumnCount(); i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						beforeEdit();
						clickRow(item, i);
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
	protected void beforeEdit() {
		// TODO Auto-generated method stub
		
	}
	public void clickRow(final TableItem item, final int column) {
		if(multiline)
			text = new Text(table, SWT.NONE | SWT.MULTI);
		else
			text = new Text(table, SWT.NONE);
		text.setEditable(edit);
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
		editor.setEditor(text, item, column);
		text.setText(item.getText(column));
		text.selectAll();
		text.setFocus();
		beginEdit();
	}
	protected void beginEdit() {
		// TODO Auto-generated method stub
		
	}
	public Text getText(){
		return this.text;
	}
}
