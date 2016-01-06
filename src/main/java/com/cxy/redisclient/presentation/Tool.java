package com.cxy.redisclient.presentation;

import org.eclipse.swt.custom.CTabItem;

public interface Tool {
	public int getId();
	public CTabItem getTbtmNewItem();
	public CTabItem init();
	public void refreshLangUI();

}