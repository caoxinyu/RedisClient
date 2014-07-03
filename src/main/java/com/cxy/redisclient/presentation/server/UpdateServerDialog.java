package com.cxy.redisclient.presentation.server;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;

public class UpdateServerDialog extends AddServerDialog {
	private Server server;
	public UpdateServerDialog(Shell parent, Image image, Server server) {
		super(parent, image);
		this.server = server;
	}

	@Override
	protected void createContents() {
		// TODO Auto-generated method stub
		super.createContents();
		text_3.setText(server.getName());
		text_3.selectAll();
		text_3.setFocus();
		text_4.setText(server.getHost());
		text_4.selectAll();
		text_5.setText(server.getPort());
		text_5.selectAll();
	}

	@Override
	protected String getTitle() {
		return "Update Server";
	}

	
}
