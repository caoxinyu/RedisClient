package com.cxy.redisclient.presentation.console;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.protocol.RedisSession;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.service.ServerService;

public class Console {
	public CTabItem getTbtmNewItem() {
		return tbtmNewItem;
	}

	public int getId() {
		return id;
	}

	private CTabFolder tabFolder;
	private int id;
	private ServerService service = new ServerService();
	private CTabItem tbtmNewItem;
	private CTabItem tbtmNewItem_1;
	private StyledText cmdResult;
	private StyledText inputCmd;
	private RedisSession session;
	private Button btnExecButton;
	private Button btnExecSelectButton;
	private Button btnExecNextButton;
	private Server server;
	
	public Console(CTabFolder tabFolder, int id){
		this.tabFolder = tabFolder;
		this.id = id;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public CTabItem init(){
		server = service.listById(id);
		Image runImage = new Image(tabFolder.getShell().getDisplay(), getClass()
				.getResourceAsStream("/run.png"));
		Image consoleImage = new Image(tabFolder.getShell().getDisplay(), getClass()
				.getResourceAsStream("/console.png"));
		
		tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setShowClose(true);
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.CONSOLE));
		tbtmNewItem.setImage(consoleImage);
		
		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new GridLayout(3, false));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnExecButton = new Button(composite_4, SWT.NONE);
		btnExecButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNCURRENT));
		btnExecButton.setImage(runImage);
		
		btnExecSelectButton = new Button(composite_4, SWT.NONE);
		btnExecSelectButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNSELECT));
		btnExecSelectButton.setImage(runImage);
		
		btnExecNextButton = new Button(composite_4, SWT.NONE);
		btnExecNextButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNFOLLOW));
		btnExecNextButton.setImage(runImage);
		
		SashForm sashForm3 = new SashForm(composite_3, SWT.VERTICAL);
		sashForm3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		inputCmd = new StyledText(sashForm3, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		inputCmd.addLineStyleListener(new LineStyleListener()
		{
		    public void lineGetStyle(LineStyleEvent e)
		    {
		        StyleRange style = new StyleRange();
		        style.metrics = new GlyphMetrics(0, 0, Integer.toString(100000).length()*5);

		        e.bullet = new Bullet(ST.BULLET_DOT,style);
		    }
		});
		
		inputCmd.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				inputCmd.setFocus();
			}
		});
		
		CTabFolder tabFolder_2 = new CTabFolder(sashForm3, SWT.BORDER);
		
		tbtmNewItem_1 = new CTabItem(tabFolder_2, SWT.NONE);
		tbtmNewItem_1.setText(RedisClient.i18nFile.getText(I18nFile.RESULT));
		
		Composite composite_5 = new Composite(tabFolder_2, SWT.NONE);
		tbtmNewItem_1.setControl(composite_5);
		composite_5.setLayout(new GridLayout(1, false));
		
		cmdResult = new StyledText(composite_5, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		cmdResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cmdResult.setEditable(false);
		
		tabFolder.setSelection(tbtmNewItem);
		tabFolder_2.setSelection(tbtmNewItem_1);
		inputCmd.setFocus();
		
		sashForm3.setWeights(new int[] { 1, 1 });
		
		session = new RedisSession(server.getHost(), Integer.parseInt(server.getPort()));
		try {
			session.connect();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		tbtmNewItem.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				quit();
			}
		});
		
		btnExecButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String cmd = inputCmd.getLine(inputCmd.getLineAtOffset(inputCmd.getCaretOffset()));
				execCmd(cmd);
			}
		});
		
		btnExecSelectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] cmds = inputCmd.getSelectionText().split(inputCmd.getLineDelimiter());
				for(String cmd: cmds){
					if(execCmd(cmd))
						break;
				}
			}
		});
		
		btnExecNextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int start = inputCmd.getLineAtOffset(inputCmd.getCaretOffset());
				int end = inputCmd.getLineCount();
				for(int i = start; i < end; i ++){
					String cmd = inputCmd.getLine(i);
					if(execCmd(cmd))
						break;
				}
			}
		});
		
		return tbtmNewItem;
	}

	public CTabFolder getTabFolder() {
		return tabFolder;
	}

	public void setTabFolder(CTabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}

	public StyledText getCmdResult() {
		return cmdResult;
	}

	public void setCmdResult(StyledText cmdResult) {
		this.cmdResult = cmdResult;
	}

	public StyledText getInputCmd() {
		return inputCmd;
	}

	public void setInputCmd(StyledText inputCmd) {
		this.inputCmd = inputCmd;
	}

	public RedisSession getSession() {
		return session;
	}

	public void setSession(RedisSession session) {
		this.session = session;
	}

	void quit() {
		try {
			session.disconnect();
		} catch (IOException e1) {
			throw new RuntimeException(e1.getLocalizedMessage());
		}
		tbtmNewItem.dispose();
	}
	
	public void refreshLangUI(){
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.CONSOLE));
		tbtmNewItem_1.setText(RedisClient.i18nFile.getText(I18nFile.RESULT));
		btnExecButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNCURRENT));
		btnExecSelectButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNSELECT));
		btnExecNextButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNFOLLOW));
	}

	private boolean execCmd(String cmd) {
		if(cmd.trim().length() == 0){
			inputCmd.setFocus();
			return false;
		}
		
		Command command = getCommand(cmd);
		command.execute();
		return !command.canContinue();
	}

	private Command getCommand(String cmd){
		String[] strs = cmd.trim().split(" ");
		if(strs[0].equalsIgnoreCase("quit"))
			return new QuitCmd(this, cmd);
		else
			return new Command(this, cmd);
	}
}

