package com.cxy.redisclient.presentation.console;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.protocol.Result;
import com.cxy.redisclient.presentation.RedisClient;

public class Command {
	protected Console console;
	protected String cmd;
	protected Result result;
	
	public Command(Console console, String cmd){
		this.console = console;
		this.cmd = cmd;
	}
	
	public void execute(){
		long start, end;
		start = System.currentTimeMillis();
		try {
			result = console.getSession().execute(cmd);
		} catch (IOException e1) {
			throw new RuntimeException(e1.getLocalizedMessage(),e1);
		}
		end = System.currentTimeMillis();
		console.getInputCmd().setFocus();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
		String time = df.format(new Date());
		
		printKeyValue(console.getCmdResult(), RedisClient.i18nFile.getText(I18nFile.COMMAND) + ": ", cmd);
		printKeyValue(console.getCmdResult(), RedisClient.i18nFile.getText(I18nFile.TIME) + ": ", time);
		printKeyValue(console.getCmdResult(), RedisClient.i18nFile.getText(I18nFile.DURATION) + "(ms): ", String.valueOf(end-start));
		if(printResult())
			printKeyValue(console.getCmdResult(), RedisClient.i18nFile.getText(I18nFile.RESULT) + ":\r\n", result.getResult());
		console.getCmdResult().append(console.getCmdResult().getLineDelimiter());
	}

	public boolean canContinue(){
		return true;
	}
	public boolean printResult(){
		return true;
	}
	private void printKeyValue(final StyledText styledText,String key, String value) {
		value += styledText.getLineDelimiter();
		
		StyleRange style = getKeyStyle();
		style.start = styledText.getCharCount();
		style.length = key.length();
		styledText.append(key);
		styledText.setStyleRange(style);
		styledText.setCaretOffset(styledText.getCharCount());
		styledText.showSelection();
		
		StyleRange style1 = getValueStyle();
		style1.start = styledText.getCharCount();
		style1.length = value.length();
		styledText.append(value);
		styledText.setStyleRange(style1);
		styledText.setCaretOffset(styledText.getCharCount());
		styledText.showSelection();
	}

	private StyleRange getValueStyle() {
		StyleRange style1 = new StyleRange();
		style1.fontStyle = SWT.NORMAL;
		style1.foreground = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		return style1;
	}

	private StyleRange getKeyStyle() {
		StyleRange style = new StyleRange();
		style.fontStyle = SWT.BOLD;
		style.foreground = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		return style;
	}
}
