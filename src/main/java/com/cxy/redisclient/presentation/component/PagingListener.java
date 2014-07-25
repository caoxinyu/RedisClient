package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagingListener implements Listener {
	private static final Logger logger = LoggerFactory.getLogger(PagingListener.class);
	
	private Table table;
	private final int PAGE_SIZE = 2;
	private IPage page;
	private int count;
	private boolean addHead = false;
	
	public PagingListener(Table table, IPage page){
		this.table = table;
		this.page = page;
		this.count = (int) page.getCount();
		table.setItemCount(count);
		this.addHead = false;
		logger.info("constructor not add head, now addhead is " + addHead);
	}
	
	public PagingListener(Table table, IPage page, boolean addHead){
		this.table = table;
		this.page = page;
		this.count = (int) page.getCount();
		this.addHead = addHead;
		logger.info("constructor add head, now addhead is " + addHead);
		
		if(!addHead)
			table.setItemCount(count);
		else
			table.setItemCount(count + 1);
	}
	@Override
	public void handleEvent(Event event) {
		logger.info("trigger set page");
		TableItem item = (TableItem) event.item;
		setPage(table.indexOf(item));
	}
	
	private void setPage(int index) {
		TableItem item;
        int start = index / PAGE_SIZE * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, table.getItemCount());
		this.page.initPage(start, end);
		logger.info("set page, now addhead is " + addHead);
		
		for (int i = start; i < end; i++) {
			if(!addHead)
				item = table.getItem (i);
			else
				item = table.getItem (i+1);
			item.setText (this.page.getText(i));
		}
	}
	public void setCount(){
		this.count = (int) page.getCount();
		table.setItemCount(count);
	}
}
