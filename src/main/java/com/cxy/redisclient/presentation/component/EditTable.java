package com.cxy.redisclient.presentation.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class EditTable extends Table {
	public EditTable(Composite parent, int style) {
		super(parent, style);
		this.addListener(SWT.MouseDown, new EditListener(this));
	}
	protected void checkSubclass() { 
	} 
}
