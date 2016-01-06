package com.cxy.redisclient.presentation.server;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public class UpdateServerDialog extends AddServerDialog {
	private Server server;
	public UpdateServerDialog(Shell parent, Image image, Server server) {
		super(parent, image);
		this.server = server;
	}

	@Override
	protected void createContents() {
		super.createContents();
		text_3.setText(server.getName());
		text_3.selectAll();
		text_3.setFocus();
		text_4.setText(server.getHost());
		text_4.selectAll();
		text_5.setText(server.getPort());
		text_5.selectAll();
		if(server.getPassword() != null){
			text_6.setText(server.getPassword());
			text_6.selectAll();
		}
	}

	@Override
	protected String getTitle() {
		return RedisClient.i18nFile.getText(I18nFile.UPDATESERVER);
	}

	
}
