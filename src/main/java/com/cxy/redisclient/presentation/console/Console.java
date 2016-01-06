package com.cxy.redisclient.presentation.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.protocol.RedisSession;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.Tool;
import com.cxy.redisclient.service.ServerService;

public class Console implements Tool {
	public CTabItem getTbtmNewItem() {
		return tbtmNewItem;
	}

	@Override
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
	private StyledText text = null;
	private RedisSession session;
	private Button btnExecButton;
	private Button btnExecSelectButton;
	private Button btnExecNextButton;
	private Server server;
	private Composite composite_4;
	private CTabFolder tabFolder_2;
	private List<DataCommand> dataCmds = new ArrayList<DataCommand>();
	private Menu menu;
	
	public Console(CTabFolder tabFolder, int id) {
		this.tabFolder = tabFolder;
		this.id = id;
	}

	@Override
	public CTabItem init(){
		server = service.listById(id);
		Image runImage = new Image(tabFolder.getShell().getDisplay(),
				getClass().getResourceAsStream("/run.png"));
		Image consoleImage = new Image(tabFolder.getShell().getDisplay(),
				getClass().getResourceAsStream("/console.png"));

		initMenu();
		
		tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setShowClose(true);
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		tbtmNewItem.setText(server.getName() +" "+RedisClient.i18nFile.getText(I18nFile.CONSOLE));
		tbtmNewItem.setImage(consoleImage);

		composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new GridLayout(3, false));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnExecButton = new Button(composite_4, SWT.NONE);
		btnExecButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNCURRENT));
		btnExecButton.setImage(runImage);
		btnExecButton.setToolTipText(RedisClient.i18nFile.getText(I18nFile.RUNCURRENTTIP)+"\tF7");

		btnExecSelectButton = new Button(composite_4, SWT.NONE);
		btnExecSelectButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNSELECT));
		btnExecSelectButton.setImage(runImage);
		btnExecSelectButton.setToolTipText(RedisClient.i18nFile.getText(I18nFile.RUNSELECTTIP)+"\tF8");

		btnExecNextButton = new Button(composite_4, SWT.NONE);
		btnExecNextButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNFOLLOW));
		btnExecNextButton.setImage(runImage);
		btnExecNextButton.setToolTipText(RedisClient.i18nFile.getText(I18nFile.RUNFOLLOWTIP)+"\tF9");

		SashForm sashForm3 = new SashForm(composite_3, SWT.VERTICAL);
		sashForm3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		inputCmd = new StyledText(sashForm3, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		inputCmd.addLineStyleListener(new LineStyleListener()
		{
		    public void lineGetStyle(LineStyleEvent e)
		    {
		        StyleRange style = new StyleRange();
		        style.metrics = new GlyphMetrics(0, 0, Integer.toString(100000).length()*5);

				e.bullet = new Bullet(ST.BULLET_DOT, style);
			}
		});

		inputCmd.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				inputCmd.setFocus();
			}
		});
		inputCmd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				text = inputCmd;
				final Clipboard cb = new Clipboard(tabFolder.getShell().getDisplay());
				TextTransfer transfer = TextTransfer.getInstance();
		        String data = (String) cb.getContents(transfer);
		        if(data != null)
		        	menu.getItem(2).setEnabled(true);
		        else
		        	menu.getItem(2).setEnabled(false);
		        
				if(inputCmd.getSelectionText().length() > 0){
					menu.getItem(0).setEnabled(true);
					menu.getItem(1).setEnabled(true);
				}else{
					menu.getItem(0).setEnabled(false);
					menu.getItem(1).setEnabled(false);
				}
				inputCmd.setMenu(menu);
					
			}
		});
		
		tabFolder_2 = new CTabFolder(sashForm3, SWT.BORDER);

		tbtmNewItem_1 = new CTabItem(tabFolder_2, SWT.NONE);
		tbtmNewItem_1.setText(RedisClient.i18nFile.getText(I18nFile.RESULT));

		Composite composite_5 = new Composite(tabFolder_2, SWT.NONE);
		tbtmNewItem_1.setControl(composite_5);
		composite_5.setLayout(new GridLayout(1, false));
		
		cmdResult = new StyledText(composite_5, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		cmdResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cmdResult.setEditable(false);
		cmdResult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				text = cmdResult;
				menu.getItem(0).setEnabled(false);
				menu.getItem(2).setEnabled(false);
				if(cmdResult.getSelectionText().length() > 0){
					menu.getItem(1).setEnabled(true);
				}else{
					menu.getItem(1).setEnabled(false);
				}
				cmdResult.setMenu(menu);
					
			}
		});
		
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
				execCurrent();
			}
		});

		btnExecSelectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				execSelect();
			}
		});

		btnExecNextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				execNext();
			}
		});

		inputCmd.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
				case SWT.F7:
					execCurrent();
					break;
				case SWT.F8:
					execSelect();
					break;
				case SWT.F9:
					execNext();
					break;
				default:
					// ignore everything else
				}
			}
		});
		
		return tbtmNewItem;
	}

	private void initMenu() {
		menu = new Menu(tabFolder.getShell());

		MenuItem mntmCut = new MenuItem(menu, SWT.NONE);
		mntmCut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.cut();
			}
		});
		mntmCut.setText(RedisClient.i18nFile.getText(I18nFile.CUT));
		
		MenuItem mntmCopy = new MenuItem(menu, SWT.NONE);
		mntmCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.copy();
			}
		});
		mntmCopy.setText(RedisClient.i18nFile.getText(I18nFile.COPY));
		
		MenuItem mntmPaste = new MenuItem(menu, SWT.NONE);
		mntmPaste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.paste();
			}
		});
		mntmPaste.setText(RedisClient.i18nFile.getText(I18nFile.PASTE));
		
		MenuItem mntmSelectAll = new MenuItem(menu, SWT.NONE);
		mntmSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.selectAll();
			}
		});
		mntmSelectAll.setText(RedisClient.i18nFile.getText(I18nFile.SELECTALL));
		
		MenuItem mntmClear = new MenuItem(menu, SWT.NONE);
		mntmClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
			}
		});
		mntmClear.setText(RedisClient.i18nFile.getText(I18nFile.CLEAR));
	}

	public CTabFolder getTabFolder_2() {
		return tabFolder_2;
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

	@Override
	public void refreshLangUI() {
		tbtmNewItem.setText(server.getName() + " " + RedisClient.i18nFile.getText(I18nFile.CONSOLE));
		tbtmNewItem_1.setText(RedisClient.i18nFile.getText(I18nFile.RESULT));
		btnExecButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNCURRENT));
		btnExecButton.setToolTipText(RedisClient.i18nFile.getText(I18nFile.RUNCURRENTTIP)+"\tF7");
		btnExecSelectButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNSELECT));
		btnExecSelectButton.setToolTipText(RedisClient.i18nFile.getText(I18nFile.RUNSELECTTIP)+"\tF8");
		btnExecNextButton.setText(RedisClient.i18nFile.getText(I18nFile.RUNFOLLOW));
		btnExecNextButton.setToolTipText(RedisClient.i18nFile.getText(I18nFile.RUNFOLLOWTIP)+"\tF9");
		for(DataCommand dataCommand: dataCmds)
			dataCommand.refreshLangUI();
		menu.dispose();
		initMenu();
		composite_4.pack();
	}

	private boolean execCmd(String cmd) {
		if (cmd.trim().length() == 0) {
			inputCmd.setFocus();
			return false;
		}

		Command command = getCommand(cmd);
		command.execute();
		if(command instanceof DataCommand)
			dataCmds.add((DataCommand) command);
		return !command.canContinue();
	}

	private Command getCommand(String cmd) {
		String[] strs = cmd.trim().split(" ");
		if (strs[0].equalsIgnoreCase("quit"))
			return new QuitCmd(this, cmd);
		else if (strs[0].equalsIgnoreCase("hgetall"))
			return new HGetallCmd(this, cmd);
		else if (strs[0].equalsIgnoreCase("info"))
			return new InfoCmd(this, cmd);
		else if (strs[0].equalsIgnoreCase("lrange"))
			return new LRangeCmd(this, cmd);
		else if (strs[0].equalsIgnoreCase("smembers"))
			return new SMembersCmd(this, cmd);
		else if (strs[0].equalsIgnoreCase("zrange"))
			return new ZRangeCmd(this, cmd);
		else
			return new Command(this, cmd);
	}

	private void execCurrent() {
		clearData();
		String cmd = inputCmd.getLine(inputCmd.getLineAtOffset(inputCmd
				.getCaretOffset()));
		execCmd(cmd);
	}

	private void execSelect() {
		clearData();
		String[] cmds = inputCmd.getSelectionText().split(
				inputCmd.getLineDelimiter());
		for (String cmd : cmds) {
			if (execCmd(cmd))
				break;
		}
	}

	private void execNext() {
		clearData();
		int start = inputCmd.getLineAtOffset(inputCmd.getCaretOffset());
		int end = inputCmd.getLineCount();
		for (int i = start; i < end; i++) {
			String cmd = inputCmd.getLine(i);
			if (execCmd(cmd))
				break;
		}
	}
	private void clearData(){
		CTabItem[] items = tabFolder_2.getItems();
		if(items.length > 1){
			for(int i = 1; i < items.length; i ++)
				items[i].dispose();
		}
		dataCmds.clear();
	}
}
