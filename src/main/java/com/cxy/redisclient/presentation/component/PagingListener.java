package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class PagingListener implements Listener {
	private Table table;
	private final int PAGE_SIZE = 20;
	private IPage page;
	private int count;
	
	public PagingListener(Table table, IPage page){
		this.table = table;
		this.page = page;
		this.count = (int) page.getCount();
		table.setItemCount(count);
	}
	@Override
	public void handleEvent(Event event) {
		TableItem item = (TableItem) event.item;
		int index = event.index;
		int page = index / PAGE_SIZE;
		int start = page * PAGE_SIZE;
		int end = start + PAGE_SIZE;
		end = Math.min (end, count);
		this.page.initPage(start, end);
		
		for (int i = start; i < end; i++) {
			item = table.getItem (i);
			item.setText (this.page.getText(i));
		}
	}
}
