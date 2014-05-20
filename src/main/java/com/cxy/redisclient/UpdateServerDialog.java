package com.cxy.redisclient;

import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;

public class UpdateServerDialog extends AddServerDialog {
	private Server server;
	public UpdateServerDialog(Shell parent, int style, Server server) {
		super(parent, style);
		this.server = server;
	}

	@Override
	protected void createContents() {
		// TODO Auto-generated method stub
		super.createContents();
		text_3.setText(server.getName());
		text_4.setText(server.getHost());
		text_5.setText(server.getPort());
	}

	@Override
	protected String getTitle() {
		return "Update Server";
	}

	
}
