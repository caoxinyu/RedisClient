package com.cxy.redisclient.presentation.component;

public interface IPage {
	public void initPage(int start, int end);
	public String[] getText(int row);
	public long getCount();
}
