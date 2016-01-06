package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public class PagingListener implements Listener {
	private Table table;
	public static int PAGE_SIZE = ConfigFile.getPagesize();
	private IPage page;
	private int count;
	private boolean addHead = false;
	
	public PagingListener(Table table, IPage page){
		this.table = table;
		this.page = page;
		this.count = (int) page.getCount();
		table.setItemCount(count);
		this.addHead = false;
	}
	
	public PagingListener(Table table, IPage page, boolean addHead){
		this.table = table;
		this.page = page;
		this.addHead = addHead;
		
		if(!addHead){
			this.count = (int) page.getCount();
		}else{
			this.count = (int) page.getCount()+1;
		}
		this.table.setItemCount(count);
	}
	@Override
	public void handleEvent(Event event) {
		TableItem item = (TableItem) event.item;
		setPage(table.indexOf(item));
	}
	
	private void setPage(int index) {
		TableItem item;
		int start;
        int end;
        
        if(!addHead){
        	start = index / PAGE_SIZE * PAGE_SIZE;
        	end = Math.min(start + PAGE_SIZE, count);
        }else{
        	start = index / PAGE_SIZE * PAGE_SIZE-1;
        	if(start < 0)
        		start = 0;
        	end = Math.min(start + PAGE_SIZE, count-1);
        }
        
		this.page.initPage(start, end);
		
		
		for (int i = start; i < end; i++) {
			if(!addHead)
				item = table.getItem (i);
			else{
				item = table.getItem (i+1);
			}
			item.setText (this.page.getText(i));
		}
	}
	public void setCount(){
		this.count = (int) page.getCount();
		if(count == 0)
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.KEYNOTEXIST));
		table.setItemCount(count);
		
	}
}
