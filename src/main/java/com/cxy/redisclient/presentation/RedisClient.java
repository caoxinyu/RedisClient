package com.cxy.redisclient.presentation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cxy.redisclient.domain.ContainerKey;
import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Favorite;
import com.cxy.redisclient.domain.Language;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.dto.ContainerKeyInfo;
import com.cxy.redisclient.dto.FindInfo;
import com.cxy.redisclient.dto.HashInfo;
import com.cxy.redisclient.dto.ListInfo;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.dto.RenameInfo;
import com.cxy.redisclient.dto.SetInfo;
import com.cxy.redisclient.dto.StringInfo;
import com.cxy.redisclient.dto.ZSetInfo;
import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.favorite.AddFavoriteDialog;
import com.cxy.redisclient.presentation.favorite.OrganizeFavoriteDialog;
import com.cxy.redisclient.presentation.hash.NewHashDialog;
import com.cxy.redisclient.presentation.hash.UpdateHashDialog;
import com.cxy.redisclient.presentation.key.DeleteContainerDialog;
import com.cxy.redisclient.presentation.key.FindKeyDialog;
import com.cxy.redisclient.presentation.key.RenameKeysDialog;
import com.cxy.redisclient.presentation.list.NewListDialog;
import com.cxy.redisclient.presentation.list.UpdateListDialog;
import com.cxy.redisclient.presentation.server.AddServerDialog;
import com.cxy.redisclient.presentation.server.PropertiesDialog;
import com.cxy.redisclient.presentation.server.UpdateServerDialog;
import com.cxy.redisclient.presentation.set.NewSetDialog;
import com.cxy.redisclient.presentation.set.UpdateSetDialog;
import com.cxy.redisclient.presentation.string.NewStringDialog;
import com.cxy.redisclient.presentation.string.UpdateStringDialog;
import com.cxy.redisclient.presentation.zset.NewZSetDialog;
import com.cxy.redisclient.presentation.zset.UpdateZSetDialog;
import com.cxy.redisclient.service.ExportService;
import com.cxy.redisclient.service.FavoriteService;
import com.cxy.redisclient.service.HashService;
import com.cxy.redisclient.service.ImportService;
import com.cxy.redisclient.service.ListService;
import com.cxy.redisclient.service.NodeService;
import com.cxy.redisclient.service.ServerService;
import com.cxy.redisclient.service.SetService;
import com.cxy.redisclient.service.ZSetService;

public class RedisClient {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
	
	private Shell shell;
	private PasteBuffer pBuffer = new PasteBuffer();;
	private FindBuffer fBuffer = null;
	private NavHistory history = new NavHistory();
	private boolean flatView = ConfigFile.getFlatView();
	public static Language language = ConfigFile.getLanguage();
	public static final I18nFile i18nFile = new I18nFile();
	
	private TreeItem rootRedisServers;

	private Item[] itemsSelected = new Item[]{rootRedisServers};
	private TreeItem treeItemSelected;
	
	private static final String DB_PREFIX = "db";
	private static final String NODE_TYPE = "type";
	private static final String NODE_ID = "id";
	private static final String ITEM_OPENED = "open";
	private static final String FAVORITE = "favorite";
	private static final String ORDER = "order";

	private Tree tree;
	private Table table;
	private Text text;

	private Menu menuTreeServer;
	private Menu menuTableServer;
	private Menu menu;
	private Menu menu_null;
	private Menu menuTreeDBContainer;
	private Menu menuTableDBContainer;
	private Menu menu_key;
	private Menu menu_Multi;
	private Menu menuData;
	private Menu menuFavorite;
	private Menu menuServer;

	private ServerService service1 = new ServerService();
	private NodeService service2 = new NodeService();
	private FavoriteService service3 = new FavoriteService();
	private ListService service4 = new ListService();
	private SetService service5 = new SetService();
	private ZSetService service6 = new ZSetService();
	private HashService service7 = new HashService();

	
	private Image redisImage;
	private Image dbImage;
	private Image containerImage;
	private Image strImage;
	private Image setImage;
	private Image listImage;
	private Image zsetImage;
	private Image hashImage;
	private Image leftImage;
	private Image rightImage;
	private Image upImage;
	private Image refreshImage;
	private Image iconImage;
	private Image codeImage;
	private Image questionImage;

	private TableColumn tblclmnName;
	private TableColumn tblclmnType;
	private TableColumn tblclmnSize;
	private Button btnBackward;
	private Button btnForward;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RedisClient window = new RedisClient();
		window.open();
	}

	/**
	 * Open the window.
	 * 
	 */
	public void open() {
		Display display = null;

		display = Display.getDefault();
		createContents();

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				MessageDialog.openError(shell, i18nFile.getText(I18nFile.ERROR), e.getLocalizedMessage());
			}
		}
		display.dispose();
	}

	/**
	 * Create contents of the window.
	 * 
	 */
	protected void createContents() {
		initShell();

		initMenu();

		initImage();

		initSash();
	}

	private void initImage() {
		redisImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/redis.png"));
		dbImage = new Image(shell.getDisplay(), getClass().getResourceAsStream(
				"/db.png"));
		strImage = new Image(shell.getDisplay(), getClass().getResourceAsStream(
				"/string.png"));
		setImage = new Image(shell.getDisplay(), getClass().getResourceAsStream(
				"/set.png"));
		listImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/list.png"));
		zsetImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/zset.png"));
		hashImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/hash.png"));
		containerImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/container.png"));
		
		leftImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/left.png"));
		rightImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/right.png"));
		upImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/up.png"));
		refreshImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/refresh.png"));
		
		iconImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/icon.png"));
		
		codeImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/code.png"));
		
		questionImage = new Image(shell.getDisplay(), getClass()
				.getResourceAsStream("/question.png"));
	}

	private void initShell() {
		shell = new Shell();
		shell.setSize(1074, 772);
		shell.setText("RedisClient");
		shell.setLayout(new GridLayout(1, false));
		
	}

	private void initSash() {
		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.verticalSpacing = 0;
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		gl_composite_1.horizontalSpacing = 0;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(5, false);
		gl_composite.marginBottom = 5;
		gl_composite.verticalSpacing = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		btnBackward = new Button(composite, SWT.CENTER);
		btnBackward.setEnabled(false);
		btnBackward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = history.backward();
				if(!item.isDisposed()) {
					tree.setSelection(item);
					treeItemSelected(false);
					if(!history.canBackward())
						btnBackward.setEnabled(false);
					
					btnForward.setEnabled(true);
					
				}else {
					MessageDialog.openInformation(shell, i18nFile.getText(I18nFile.ERROR), i18nFile.getText(I18nFile.OBJECTDELETE));
				}
			}
		});
		btnBackward.setImage(leftImage);
		
		btnForward = new Button(composite, SWT.NONE);
		btnForward.setEnabled(false);
		btnForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = history.forward();
				btnBackward.setEnabled(true);
				if(!item.isDisposed()) {
					tree.setSelection(item);
					treeItemSelected(false);
					if(!history.canForward())
						btnForward.setEnabled(false);
					
				}else {
					MessageDialog.openInformation(shell, i18nFile.getText(I18nFile.ERROR), i18nFile.getText(I18nFile.OBJECTDELETE));
				}
			}
		});
		btnForward.setImage(rightImage);
		
		Button btnUP = new Button(composite, SWT.CENTER);
		btnUP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				if(items[0] != rootRedisServers){
					history.add(items[0].getParentItem());
					btnBackward.setEnabled(true);
					btnForward.setEnabled(false);
					tree.setSelection(items[0].getParentItem());
					treeItemSelected(false);
				}
			}
		});
		btnUP.setImage(upImage);
		
		Button btnRefresh = new Button(composite, SWT.CENTER);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeItemSelected(true);
			}
		});
		btnRefresh.setImage(refreshImage);
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setEditable(false);
				
		SashForm sashForm = new SashForm(composite_1, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initTree(sashForm);

		initRootItem();

		initMenuData();

		menuTreeDBContainer = initMenuTreeDB();

		menuTableDBContainer = initMenuTableDB();
		
		initMenuMulti();

		SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

		initTable(sashForm_1);

		sashForm_1.setWeights(new int[] { 1 });

		sashForm.setWeights(new int[] { 1, 3 });

		initMenuNull();

		menuTreeServer = initMenuTreeServer();
		menuTableServer = initMenuTableServer();

		initServers();
		
		tree.select(rootRedisServers);
				
		treeItemSelected(false);
		
		history.add(rootRedisServers);
		
		shell.setImage(iconImage);
		
	}

	private void initMenuMulti() {
		menu_Multi = new Menu(shell);
		
		MenuItem mntmDelete_5 = new MenuItem(menu_Multi, SWT.NONE);
		mntmDelete_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteKeys();
			}
		});
		mntmDelete_5.setText(i18nFile.getText(I18nFile.DELETE));
		
		new MenuItem(menu_Multi, SWT.SEPARATOR);
		
		MenuItem mntmCut_2 = new MenuItem(menu_Multi, SWT.NONE);
		mntmCut_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cut();
			}
		});
		mntmCut_2.setText(i18nFile.getText(I18nFile.CUT));
		
		MenuItem mntmCopy_3 = new MenuItem(menu_Multi, SWT.NONE);
		mntmCopy_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy_3.setText(i18nFile.getText(I18nFile.COPY));
		
		new MenuItem(menu_Multi, SWT.SEPARATOR);
		
		MenuItem mntmExport_2 = new MenuItem(menu_Multi, SWT.NONE);
		mntmExport_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				export();
			}
		});
		mntmExport_2.setText(i18nFile.getText(I18nFile.EXPORT));
	}

	private void initMenuData() {
		menu_key = new Menu(shell);

		MenuItem mntmRename = new MenuItem(menu_key, SWT.NONE);
		mntmRename.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameKey();
			}
		});
		mntmRename.setText(i18nFile.getText(I18nFile.RENAME));

		MenuItem mntmDelete_4 = new MenuItem(menu_key, SWT.NONE);
		mntmDelete_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteOneKey();
			}
		});
		mntmDelete_4.setText(i18nFile.getText(I18nFile.DELETE));
		
		MenuItem mntmProperties_1 = new MenuItem(menu_key, SWT.NONE);
		mntmProperties_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dataProperties();
				
			}
		});
		mntmProperties_1.setText(i18nFile.getText(I18nFile.PROPERTIES));

		new MenuItem(menu_key, SWT.SEPARATOR);

		MenuItem menuItem = new MenuItem(menu_key, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		menuItem.setText(i18nFile.getText(I18nFile.ADDFAVORITES));

		new MenuItem(menu_key, SWT.SEPARATOR);

		MenuItem mntmCut_1 = new MenuItem(menu_key, SWT.NONE);
		mntmCut_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cut();
			}
		});
		mntmCut_1.setText(i18nFile.getText(I18nFile.CUT));

		MenuItem mntmCopy_2 = new MenuItem(menu_key, SWT.NONE);
		mntmCopy_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy_2.setText(i18nFile.getText(I18nFile.COPY));
		
		new MenuItem(menu_key, SWT.SEPARATOR);
		
		MenuItem mntmExport_3 = new MenuItem(menu_key, SWT.NONE);
		mntmExport_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				export();
			}
			
		});
		mntmExport_3.setText(i18nFile.getText(I18nFile.EXPORT));
	}

	private void initTree(SashForm sashForm) {
		tree = new Tree(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectTreeItem();
				
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (arg0.button == 3) {
					Point point = new Point(arg0.x, arg0.y);
					TreeItem selectedItem = tree.getItem(point);
					
					selectTreeItem();

					if (selectedItem == rootRedisServers || selectedItem == null){
						tree.setMenu(menu_null);
					} else {
						NodeType type = (NodeType) selectedItem.getData(NODE_TYPE);

						if (type == NodeType.ROOT){
							tree.setMenu(menu_null);
						}else if (type == NodeType.SERVER){
							updateMenuServer(false, menuTreeServer);
							tree.setMenu(menuTreeServer);
						}else if (type == NodeType.DATABASE
								|| type == NodeType.CONTAINER) {
							updateMenuDBContainer(type, menuTreeDBContainer);
							tree.setMenu(menuTreeDBContainer);
						}

					}
					
				}
			}
		});
	}

	private void initRootItem() {
		rootRedisServers = new TreeItem(tree, SWT.NONE);
		rootRedisServers.setImage(redisImage);
		rootRedisServers.setText(i18nFile.getText(I18nFile.REDISSERVERS));
		rootRedisServers.setData(NODE_TYPE, NodeType.ROOT);
		rootRedisServers.setExpanded(true);
		rootRedisServers.setData(ITEM_OPENED, true);
	}

	private void parseContainer(TreeItem item, ContainerKeyInfo info) {
		TreeItem parent = item.getParentItem();
		if (item.getData(NODE_TYPE) == NodeType.CONTAINER) {
			String container = item.getText();
			if (info.getContainerStr() != null){
				if(!flatView)
					info.setContainer(new ContainerKey(container + ":" + info.getContainerStr()));
				else
					info.setContainer(new ContainerKey(container + info.getContainerStr()));
			}else{
				if(!flatView)
					info.setContainer(new ContainerKey(container + ":"));
				else
					info.setContainer(new ContainerKey(container));
			}
			parseContainer(parent, info);
		} else if (item.getData(NODE_TYPE) == NodeType.DATABASE) {
			int db = (Integer) item.getData(NODE_ID);
			info.setDb(db);
			parseContainer(parent, info);
		} else if (item.getData(NODE_TYPE) == NodeType.SERVER) {
			int id = (Integer) item.getData(NODE_ID);
			info.setId(id);
			info.setServerName(item.getText());
			return;
		}
	}

	private Menu initMenuTreeDB() {
		Menu menu = initMenuTableDB();
		
		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem mntmRefresh_2 = new MenuItem(menu, SWT.NONE);
		mntmRefresh_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				dbContainerTreeItemSelected(items[0], true);
			}
		});
		mntmRefresh_2.setText(i18nFile.getText(I18nFile.REFRESH));

		return menu;
	}

	private Menu initMenuTableDB() {
		Menu menu_dbContainer = new Menu(shell);

		MenuItem mntmNew_1 = new MenuItem(menu_dbContainer, SWT.CASCADE);
		mntmNew_1.setText(i18nFile.getText(I18nFile.NEW));

		Menu menu_1 = new Menu(mntmNew_1);
		mntmNew_1.setMenu(menu_1);

		MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newString();
			}
		});
		menuItem_1.setText(i18nFile.getText(I18nFile.STRING));
		menuItem_1.setImage(strImage);

		MenuItem menuItem_2 = new MenuItem(menu_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newList();
			}
		});
		menuItem_2.setText(i18nFile.getText(I18nFile.LIST));
		menuItem_2.setImage(listImage);

		MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSet();
			}
		});
		menuItem_3.setText(i18nFile.getText(I18nFile.SET));
		menuItem_3.setImage(setImage);

		MenuItem mntmSortedSet = new MenuItem(menu_1, SWT.NONE);
		mntmSortedSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newZSet();
			}
		});
		mntmSortedSet.setText(i18nFile.getText(I18nFile.ZSET));
		mntmSortedSet.setImage(zsetImage);

		MenuItem mntmHash_1 = new MenuItem(menu_1, SWT.NONE);
		mntmHash_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newHash();
			}
		});
		mntmHash_1.setText(i18nFile.getText(I18nFile.HASH));
		mntmHash_1.setImage(hashImage);

		MenuItem mntmRename_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmRename_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameContainer();
			}
		});
		mntmRename_1.setText(i18nFile.getText(I18nFile.RENAME));

		MenuItem mntmDelete_2 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmDelete_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteOneContainer();
			}
		});
		mntmDelete_2.setText(i18nFile.getText(I18nFile.DELETE));
		
		MenuItem mntmProperties_3 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmProperties_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dbContainerProperties();
			}
		});
		mntmProperties_3.setText(i18nFile.getText(I18nFile.PROPERTIES));

		new MenuItem(menu_dbContainer, SWT.SEPARATOR);

		MenuItem mntmAddToFavorites = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmAddToFavorites.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		mntmAddToFavorites.setText(i18nFile.getText(I18nFile.ADDFAVORITES));

		new MenuItem(menu_dbContainer, SWT.SEPARATOR);

		MenuItem mntmCut = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmCut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				cut();
			}
		});
		mntmCut.setText(i18nFile.getText(I18nFile.CUT));

		MenuItem mntmCopy_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmCopy_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy_1.setText(i18nFile.getText(I18nFile.COPY));

		MenuItem mntmPaste_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmPaste_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				paste();
			}
		});
		mntmPaste_1.setEnabled(false);
		mntmPaste_1.setText(i18nFile.getText(I18nFile.PASTE));

		new MenuItem(menu_dbContainer, SWT.SEPARATOR);
		
		MenuItem mntmImport_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmImport_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importFile();
			}
		});
		mntmImport_1.setEnabled(false);
		mntmImport_1.setText(i18nFile.getText(I18nFile.IMPORT));
		
		MenuItem mntmExport_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmExport_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				export();
			}
		});
		mntmExport_1.setText(i18nFile.getText(I18nFile.EXPORT));
		
		new MenuItem(menu_dbContainer, SWT.SEPARATOR);
		
		MenuItem mntmFind_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmFind_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		mntmFind_1.setText(i18nFile.getText(I18nFile.FIND));
		
		MenuItem mntmFindNext_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmFindNext_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindNext_1.setText(i18nFile.getText(I18nFile.FINDFORWARD));
		
		MenuItem mntmFindBackward_3 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmFindBackward_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward_3.setText(i18nFile.getText(I18nFile.FINDBACKWARD));
		
		return menu_dbContainer;
	}

	

	private void initTable(SashForm sashForm_1) {
		table = new Table(sashForm_1, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				logger.debug("table double clicked");
				TreeItem[] treeItems = tree.getSelection();
				Point point = new Point(e.x, e.y);
				TableItem selectedItem = table.getItem(point);

				if (selectedItem != null){
					NodeType type = (NodeType) selectedItem.getData(NODE_TYPE);
					
					if(type == NodeType.CONTAINER || type == NodeType.DATABASE || type == NodeType.SERVER) {
						boolean find = false;
						for (TreeItem treeItem : treeItems[0].getItems()) {
							String treeText = treeItem.getText();
							String tableText = selectedItem.getText(0);
							
							if (treeText.equals(tableText)) {
								find = true;
								
								if (type == NodeType.CONTAINER || type == NodeType.DATABASE)
									dbContainerTreeItemSelected(treeItem, false);
								else if (type == NodeType.SERVER)
									serverTreeItemSelected(treeItem, false);
								
								history.add(treeItem);
								btnBackward.setEnabled(true);
								btnForward.setEnabled(false);
								break;
							}
						}
						if(!find)
							MessageDialog.openInformation(shell, i18nFile.getText(I18nFile.INFORMATION), i18nFile.getText(I18nFile.NEWKEYFOUND)+text.getText());
					}else {
						
						dataProperties();
						
					}
				} 
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TableItem selectedItem = table.getItem(point);
				if (e.button == 3) {
					if (selectedItem == null)
						table.setMenu(menu_null);
					else {
						NodeType type = (NodeType) selectedItem.getData(NODE_TYPE);
						
						if (type == NodeType.ROOT)
							table.setMenu(menu_null);
						else if (type == NodeType.SERVER){
							updateMenuServer(true, menuTableServer);
							table.setMenu(menuTableServer);
						}else if (type == NodeType.CONTAINER) {
							TableItem[] items = table.getSelection();
							if(items.length == 1){
								updateMenuDBContainer(type, menuTableDBContainer);
								table.setMenu(menuTableDBContainer);
							}else{
								table.setMenu(menu_Multi);
							}
						} else if (type == NodeType.DATABASE){
							updateMenuDBContainer(type, menuTableDBContainer);
							table.setMenu(menuTableDBContainer);
						} else {
							TableItem[] items = table.getSelection();
							if(items.length == 1)
								table.setMenu(menu_key);
							else
								table.setMenu(menu_Multi);
						}

					}
				}
			}
		});
		table.setHeaderVisible(true);

		tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(150);
		tblclmnName.setText(i18nFile.getText(I18nFile.NAME));
		tblclmnName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnSelected(tblclmnName, OrderBy.NAME);
			}
		});

		tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(150);
		tblclmnType.setText(i18nFile.getText(I18nFile.TYPE));
		tblclmnType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnSelected(tblclmnType, OrderBy.TYPE);
			}
		});

		tblclmnSize = new TableColumn(table, SWT.NONE);
		tblclmnSize.setWidth(100);
		tblclmnSize.setText(i18nFile.getText(I18nFile.SIZE));
		tblclmnSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnSelected(tblclmnSize, OrderBy.SIZE);
			}
		});

	}

	protected void updateMenuServer(boolean isTable, Menu menu) {
		if(isTable && table.getSelectionCount() > 1){
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(true);
			menu.getItem(2).setEnabled(false);
			menu.getItem(4).setEnabled(false);
			menu.getItem(5).setEnabled(false);
			menu.getItem(6).setEnabled(false);
		}else{
			menu.getItem(0).setEnabled(true);
			menu.getItem(1).setEnabled(true);
			menu.getItem(2).setEnabled(true);
			menu.getItem(4).setEnabled(true);
			menu.getItem(5).setEnabled(true);
			menu.getItem(6).setEnabled(true);
		}
	}

	protected void dbContainerProperties() {
		TreeItem treeItem;
		
		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem) {
			treeItem = (TreeItem) itemsSelected[0];
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);
		}

		parseContainer(treeItem, cinfo);
		
		NodeType type = (NodeType) treeItem.getData(NODE_TYPE);
		
		String str;
		String container;
		
		if(type == NodeType.DATABASE){
			str = i18nFile.getText(I18nFile.TYPE)+":\t" + i18nFile.getText(I18nFile.DATABASE) + "\n" + i18nFile.getText(I18nFile.LOCATION)+":\t"+ getLocation(cinfo)  + "\n" + i18nFile.getText(I18nFile.KEY) + ":\t";
			container = "";
		}
		else{
			str = i18nFile.getText(I18nFile.TYPE)+":\t" + i18nFile.getText(I18nFile.CONTAINER) + "\n" + i18nFile.getText(I18nFile.LOCATION)+":\t"+ getLocation(cinfo)  + "\n" + i18nFile.getText(I18nFile.KEY) + ":\t";
			container = cinfo.getContainerStr();
		}
		
		Set<Node> nodes = service2.listContainerAllKeys(cinfo.getId(), cinfo.getDb(), container);
		str += nodes.size();
		
		String properties;
		
		if(type == NodeType.DATABASE)
			properties = i18nFile.getText(I18nFile.DBPROPERTIES);
		else
			properties = i18nFile.getText(I18nFile.CONTAINERPROPERTIES);
		MessageDialog.openInformation(shell, properties, str);
		
	}

	private void tableItemSelected() {
		logger.debug("table selected");
		
		TableItem[] items = table.getSelection();
		if(items.length == 1){	
			itemsSelected = items;
			TreeItem[] treeItems = tree.getSelection();
			treeItemSelected = treeItems[0];
			
			NodeType type = (NodeType) items[0].getData(NODE_TYPE);
			switch (type) {
			case SERVER:
				serverItemSelected();
				break;
			case DATABASE:
			case CONTAINER:
				dbContainerItemSelected(items[0]);
				break;
			default:
				dataItemSelected();
				break;
			}
		}else if(items.length > 1) {
			NodeType type = (NodeType) items[0].getData(NODE_TYPE);
			switch (type) {
			case SERVER:
				itemsSelected = items;
				TreeItem[] treeItems = tree.getSelection();
				treeItemSelected = treeItems[0];
				serverItemsSelected();
				break;
			case DATABASE:
				for(TableItem item: items){
					if(item != itemsSelected[0]){
						itemsSelected[0] = item;
						table.setSelection(item);
						break;
					}
				}
				break;
			default:
				itemsSelected = items;
				TreeItem[] treeItems1 = tree.getSelection();
				treeItemSelected = treeItems1[0];
				containerItemsSelected();
				break;
			
			}
			
		}else {
			TreeItem[] treeItems = tree.getSelection();
			itemsSelected = treeItems;
			
			NodeType type = (NodeType) itemsSelected[0].getData(NODE_TYPE);
			switch (type) {
			case SERVER:
				serverItemSelected();
				break;
			case DATABASE:
			case CONTAINER:
				dbContainerItemSelected(itemsSelected[0]);
				break;
			default:
				dataItemSelected();
				break;
			}
		}
	}
	
	private void containerItemsSelected() {
		menuServer.getItem(1).setEnabled(false);
		menuServer.getItem(2).setEnabled(false);
		menuServer.getItem(3).setEnabled(false);

		menuData.getItem(0).setEnabled(false);
		menuData.getItem(1).setEnabled(false);
		menuData.getItem(2).setEnabled(true);
		menuData.getItem(3).setEnabled(false);

		menuData.getItem(5).setEnabled(true);
		menuData.getItem(6).setEnabled(true);
		menuData.getItem(7).setEnabled(false);
		
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(true);
		
		menuFavorite.getItem(0).setEnabled(false);
	}

	private void serverItemsSelected() {
		menuServer.getItem(1).setEnabled(false);
		menuServer.getItem(2).setEnabled(true);
		menuServer.getItem(3).setEnabled(false);

		menuData.getItem(0).setEnabled(false);
		menuData.getItem(1).setEnabled(false);
		menuData.getItem(2).setEnabled(false);
		menuData.getItem(3).setEnabled(false);

		menuData.getItem(5).setEnabled(false);
		menuData.getItem(6).setEnabled(false);
		menuData.getItem(7).setEnabled(false);
		
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(false);
		
		menuFavorite.getItem(0).setEnabled(false);
	}

	private void dataProperties() {
		TableItem[] items = table.getSelection();
		NodeType type = (NodeType) items[0].getData(NODE_TYPE);
		
		TreeItem treeItem = getTreeItemByTableItem(items[0]);
		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		parseContainer(treeItem, cinfo);
		String key = cinfo.getContainerStr() + items[0].getText();
		
		if(type == NodeType.STRING) {
			String value = service2.readString(cinfo.getId(), cinfo.getDb(), key);
			
			UpdateStringDialog dialog = new UpdateStringDialog(shell,
					iconImage,  cinfo.getServerName(),
					cinfo.getDb(), key, value);
			StringInfo info = (StringInfo) dialog.open();
			
			if(info != null)
				service2.updateString(cinfo.getId(), cinfo.getDb(), info.getKey(), info.getValue());
		} else if(type == NodeType.HASH) {
			Map<String, String> value = service7.read(cinfo.getId(), cinfo.getDb(), key);
			
			UpdateHashDialog dialog = new UpdateHashDialog(shell,
					iconImage, cinfo.getServerName(),
					cinfo.getDb(), key, value);
			
			HashInfo info = (HashInfo) dialog.open();
			if (info != null) {
				service7.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
						info.getValues());
				treeItem.setData(ITEM_OPENED, false);
				dbContainerTreeItemSelected(treeItem, false);
			}
		} else if(type == NodeType.LIST) {
			UpdateListDialog dialog = new UpdateListDialog(shell,
					iconImage, cinfo.getId(), cinfo.getServerName(),
					cinfo.getDb(), key);
			
			ListInfo info = (ListInfo)dialog.open();
			
			if (info != null) {
				service4.update(cinfo.getId(), cinfo.getDb(), info.getKey(),
						info.getValues(), info.isHeadTail());
				treeItem.setData(ITEM_OPENED, false);
				dbContainerTreeItemSelected(treeItem, false);
			}
		} else if(type == NodeType.SET) {
			UpdateSetDialog dialog = new UpdateSetDialog(shell,
					iconImage, cinfo.getId(), cinfo.getServerName(),
					cinfo.getDb(), key);
			
			SetInfo info = (SetInfo) dialog.open();
			if (info != null) {
				service5.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
						info.getValues());
				treeItem.setData(ITEM_OPENED, false);
				dbContainerTreeItemSelected(treeItem, false);
			}
		} else if(type == NodeType.SORTEDSET) {
			UpdateZSetDialog dialog = new UpdateZSetDialog(shell,
					iconImage, cinfo.getId(), cinfo.getServerName(),
					cinfo.getDb(), key);
			
			ZSetInfo info = (ZSetInfo) dialog.open();
			if (info != null) {
				service6.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
						info.getValues());
				treeItem.setData(ITEM_OPENED, false);
				dbContainerTreeItemSelected(treeItem, false);
			}
		}
	}

	protected void dataItemSelected() {
		menuServer.getItem(1).setEnabled(false);
		menuServer.getItem(2).setEnabled(false);
		menuServer.getItem(3).setEnabled(false);

		menuData.getItem(0).setEnabled(false);
		menuData.getItem(1).setEnabled(true);
		menuData.getItem(2).setEnabled(true);
		menuData.getItem(3).setEnabled(true);

		menuData.getItem(5).setEnabled(true);
		menuData.getItem(6).setEnabled(true);
		menuData.getItem(7).setEnabled(false);
		
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(false);
		
		menuFavorite.getItem(0).setEnabled(true);
		
	}

	private Menu initMenuTreeServer() {
		Menu menu_server = initMenuTableServer();
		
		new MenuItem(menu_server, SWT.SEPARATOR);

		MenuItem mntmRefresh_3 = new MenuItem(menu_server, SWT.NONE);
		mntmRefresh_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				serverTreeItemSelected(items[0], true);
			}
		});
		mntmRefresh_3.setText(i18nFile.getText(I18nFile.REFRESH));

		return menu_server;
	}

	private Menu initMenuTableServer() {
		Menu menu_server_1 = new Menu(shell);

		MenuItem mntmUpdate = new MenuItem(menu_server_1, SWT.NONE);
		mntmUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmUpdate.setText(i18nFile.getText(I18nFile.UPDATE));

		MenuItem mntmDelete = new MenuItem(menu_server_1, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete.setText(i18nFile.getText(I18nFile.REMOVE));

		MenuItem mntmProperties_4 = new MenuItem(menu_server_1, SWT.NONE);
		mntmProperties_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				serverProperties();
			}
		});
		mntmProperties_4.setText(i18nFile.getText(I18nFile.PROPERTIES));
		
		new MenuItem(menu_server_1, SWT.SEPARATOR);
		
		MenuItem menuItem_2 = new MenuItem(menu_server_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		menuItem_2.setText(i18nFile.getText(I18nFile.FIND));
		
		MenuItem mntmFindForward = new MenuItem(menu_server_1, SWT.NONE);
		mntmFindForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindForward.setText(i18nFile.getText(I18nFile.FINDFORWARD));
		
		MenuItem mntmFindBackward = new MenuItem(menu_server_1, SWT.NONE);
		mntmFindBackward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward.setText(i18nFile.getText(I18nFile.FINDBACKWARD));
		
		return menu_server_1;
	}

	private void initMenuNull() {
		menu_null = new Menu(shell);

		MenuItem mntmNewConnection = new MenuItem(menu_null, SWT.NONE);
		mntmNewConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addServer();
			}
		});
		mntmNewConnection.setText(i18nFile.getText(I18nFile.ADDSERVER));
		
		new MenuItem(menu_null, SWT.SEPARATOR);
		
		MenuItem mntmFind = new MenuItem(menu_null, SWT.NONE);
		mntmFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		mntmFind.setText(i18nFile.getText(I18nFile.FIND));
		
		MenuItem mntmFindNext = new MenuItem(menu_null, SWT.NONE);
		mntmFindNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindNext.setText(i18nFile.getText(I18nFile.FINDFORWARD));
		
		MenuItem mntmFindBackward_2 = new MenuItem(menu_null, SWT.NONE);
		mntmFindBackward_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward_2.setText(i18nFile.getText(I18nFile.FINDBACKWARD));
		
		new MenuItem(menu_null, SWT.SEPARATOR);

		MenuItem mntmRefresh = new MenuItem(menu_null, SWT.NONE);
		mntmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				rootTreeItemSelected(true);
			}
		});
		mntmRefresh.setText(i18nFile.getText(I18nFile.REFRESH));
	}

	private void initMenu() {
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmServer = new MenuItem(menu, SWT.CASCADE);
		mntmServer.setText(i18nFile.getText(I18nFile.SERVER));

		menuServer = new Menu(mntmServer);
		mntmServer.setMenu(menuServer);

		MenuItem mntmNew = new MenuItem(menuServer, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addServer();
			}
		});
		mntmNew.setText(i18nFile.getText(I18nFile.ADD));

		MenuItem mntmEdit = new MenuItem(menuServer, SWT.NONE);
		mntmEdit.setEnabled(false);
		mntmEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmEdit.setText(i18nFile.getText(I18nFile.UPDATE));

		MenuItem mntmDelete_1 = new MenuItem(menuServer, SWT.NONE);
		mntmDelete_1.setEnabled(false);
		mntmDelete_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete_1.setText(i18nFile.getText(I18nFile.REMOVE));
		
		MenuItem mntmProperties = new MenuItem(menuServer, SWT.NONE);
		mntmProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				serverProperties();
			}
		});
		mntmProperties.setEnabled(false);
		mntmProperties.setText(i18nFile.getText(I18nFile.PROPERTIES));

		new MenuItem(menuServer, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menuServer, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		mntmExit.setText(i18nFile.getText(I18nFile.EXIT));

		MenuItem mntmData = new MenuItem(menu, SWT.CASCADE);
		mntmData.setText(i18nFile.getText(I18nFile.DATA));

		menuData = new Menu(mntmData);
		mntmData.setMenu(menuData);

		MenuItem mntmAdd = new MenuItem(menuData, SWT.CASCADE);
		mntmAdd.setEnabled(false);
		mntmAdd.setText(i18nFile.getText(I18nFile.NEW));

		Menu menu_5 = new Menu(mntmAdd);
		mntmAdd.setMenu(menu_5);

		MenuItem mntmString = new MenuItem(menu_5, SWT.NONE);
		mntmString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newString();
			}
		});
		mntmString.setText(i18nFile.getText(I18nFile.STRING)+"\tAlt+1");
		mntmString.setAccelerator(SWT.ALT+'1');
		mntmString.setImage(strImage);
		

		MenuItem mntmList = new MenuItem(menu_5, SWT.NONE);
		mntmList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newList();
			}
		});
		mntmList.setText(i18nFile.getText(I18nFile.LIST)+"\tAlt+2");
		mntmList.setAccelerator(SWT.ALT+'2');
		mntmList.setImage(listImage);

		MenuItem mntmSet = new MenuItem(menu_5, SWT.NONE);
		mntmSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSet();
			}
		});
		mntmSet.setText(i18nFile.getText(I18nFile.SET)+"\tAlt+3");
		mntmSet.setAccelerator(SWT.ALT+'3');
		mntmSet.setImage(setImage);

		MenuItem mntmSortset = new MenuItem(menu_5, SWT.NONE);
		mntmSortset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newZSet();
			}
		});
		mntmSortset.setText(i18nFile.getText(I18nFile.ZSET)+"\tAlt+4");
		mntmSortset.setAccelerator(SWT.ALT+'4');
		mntmSortset.setImage(zsetImage);
		
		MenuItem mntmHash = new MenuItem(menu_5, SWT.NONE);
		mntmHash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newHash();
			}
		});
		mntmHash.setText(i18nFile.getText(I18nFile.HASH) + "\tAlt+5");
		mntmHash.setAccelerator(SWT.ALT+'5');
		mntmHash.setImage(hashImage);
		
		MenuItem mntmRename_2 = new MenuItem(menuData, SWT.NONE);
		mntmRename_2.setEnabled(false);
		mntmRename_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(itemsSelected[0] instanceof TreeItem)
					renameContainer();
				else
					renameKey();
			}
		});
		mntmRename_2.setText(i18nFile.getText(I18nFile.RENAME));

		MenuItem mntmDelete_3 = new MenuItem(menuData, SWT.NONE);
		mntmDelete_3.setEnabled(false);
		mntmDelete_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(itemsSelected[0] instanceof TreeItem){
					deleteOneContainer();
				}else {
					if(itemsSelected.length == 1){
						NodeType type = (NodeType) itemsSelected[0].getData(NODE_TYPE);
						if(type == NodeType.CONTAINER){
							deleteOneContainer();
						}else{
							deleteOneKey();
						}
					}else if(itemsSelected.length > 1){
						deleteKeys();
					}
				}
					
			}
		});
		mntmDelete_3.setText(i18nFile.getText(I18nFile.DELETE)+"\tDel");
		mntmDelete_3.setAccelerator(SWT.DEL);
		
		MenuItem mntmProperties_2 = new MenuItem(menuData, SWT.NONE);
		mntmProperties_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NodeType type = (NodeType) itemsSelected[0].getData(NODE_TYPE);
				
				if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
					dbContainerProperties();
				else
					dataProperties();

			}
		});
		mntmProperties_2.setEnabled(false);
		mntmProperties_2.setText(i18nFile.getText(I18nFile.PROPERTIES));

		new MenuItem(menuData, SWT.SEPARATOR);

		MenuItem mntmcut = new MenuItem(menuData, SWT.NONE);
		mntmcut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cut();
			}
		});
		mntmcut.setEnabled(false);
		mntmcut.setText(i18nFile.getText(I18nFile.CUT)+"\tCtrl+X");
		mntmcut.setAccelerator(SWT.CTRL + 'X');

		MenuItem mntmCopy = new MenuItem(menuData, SWT.NONE);
		mntmCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy.setEnabled(false);
		mntmCopy.setText(i18nFile.getText(I18nFile.COPY)+"\tCtrl+C");
		mntmCopy.setAccelerator(SWT.CTRL + 'C');

		MenuItem mntmPaste = new MenuItem(menuData, SWT.NONE);
		mntmPaste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				paste();
			}
		});
		mntmPaste.setEnabled(false);
		mntmPaste.setText(i18nFile.getText(I18nFile.PASTE)+"\tCtrl+V");
		mntmPaste.setAccelerator(SWT.CTRL + 'V');

		new MenuItem(menuData, SWT.SEPARATOR);

		MenuItem mntmImport = new MenuItem(menuData, SWT.NONE);
		mntmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importFile();
			}
		});
		mntmImport.setEnabled(false);
		mntmImport.setText(i18nFile.getText(I18nFile.IMPORT));

		MenuItem mntmExport = new MenuItem(menuData, SWT.NONE);
		mntmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				export();
			}
		});
		mntmExport.setEnabled(false);
		mntmExport.setText(i18nFile.getText(I18nFile.EXPORT));

		new MenuItem(menuData, SWT.SEPARATOR);
		
		MenuItem mntmFind_2 = new MenuItem(menuData, SWT.NONE);
		mntmFind_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		mntmFind_2.setText(i18nFile.getText(I18nFile.FIND)+"\tCtrl+F");
		mntmFind_2.setAccelerator(SWT.CTRL + 'F');
		
		MenuItem mntmFindNext_2 = new MenuItem(menuData, SWT.NONE);
		mntmFindNext_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindNext_2.setText(i18nFile.getText(I18nFile.FINDFORWARD)+"\tF3");
		mntmFindNext_2.setAccelerator(SWT.F3);
		
		MenuItem mntmFindBackward_1 = new MenuItem(menuData, SWT.NONE);
		mntmFindBackward_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward_1.setText(i18nFile.getText(I18nFile.FINDBACKWARD)+"\tCtrl+F3");
		mntmFindBackward_1.setAccelerator(SWT.CTRL+SWT.F3);
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText(i18nFile.getText(I18nFile.VIEW));
		
		Menu menu_1 = new Menu(mntmView);
		mntmView.setMenu(menu_1);
		
		MenuItem mntmHierarchy = new MenuItem(menu_1, SWT.RADIO);
		mntmHierarchy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(flatView == true){
					TreeItem[] treeItems = tree.getSelection();

					ContainerKeyInfo cinfo = new ContainerKeyInfo();
					parseContainer(treeItems[0], cinfo);
					
					flatView = false;
					updateView(cinfo);
				}
			}
		});
		if(!flatView)
			mntmHierarchy.setSelection(true);
		mntmHierarchy.setText(i18nFile.getText(I18nFile.HIERARCHY));
		
		MenuItem mntmFlat = new MenuItem(menu_1, SWT.RADIO);
		mntmFlat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(flatView == false){
					TreeItem[] treeItems = tree.getSelection();

					ContainerKeyInfo cinfo = new ContainerKeyInfo();
					parseContainer(treeItems[0], cinfo);
					
					flatView = true;
					updateView(cinfo);
				}
			}
		});
		if(flatView)
			mntmFlat.setSelection(true);
		mntmFlat.setText(i18nFile.getText(I18nFile.FLAT));
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmEnglish = new MenuItem(menu_1, SWT.RADIO);
		mntmEnglish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(language == Language.Chinese){
					language = Language.English;
					refreshLangUI();
				}
			}
		});
		if(language == Language.English)
			mntmEnglish.setSelection(true);
		mntmEnglish.setText("English");
		
		MenuItem menuItem = new MenuItem(menu_1, SWT.RADIO);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(language == Language.English){
					language = Language.Chinese;
					refreshLangUI();
				}
			}
		});
		if(language == Language.Chinese)
			menuItem.setSelection(true);
		menuItem.setText("中文");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeItemSelected(true);
			}
		});
		menuItem_1.setText(i18nFile.getText(I18nFile.REFRESH)+"\tF5");
		menuItem_1.setAccelerator(SWT.F5);

		MenuItem mntmFavorites = new MenuItem(menu, SWT.CASCADE);
		mntmFavorites.setText(i18nFile.getText(I18nFile.FAVORITES));

		menuFavorite = new Menu(mntmFavorites);
		mntmFavorites.setMenu(menuFavorite);

		MenuItem mntmAdd_Favorite = new MenuItem(menuFavorite, SWT.NONE);
		mntmAdd_Favorite.setEnabled(false);
		mntmAdd_Favorite.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		mntmAdd_Favorite.setText(i18nFile.getText(I18nFile.ADD));

		MenuItem mntmOrganize = new MenuItem(menuFavorite, SWT.NONE);
		mntmOrganize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OrganizeFavoriteDialog dialog = new OrganizeFavoriteDialog(
						shell, iconImage);

				@SuppressWarnings("unchecked")
				List<Favorite> favorites = (List<Favorite>) dialog.open();
				if (favorites != null) {
					service3.updateList(favorites);

					removeFavoriteMenuItem();
					addFavoriteMenuItem();
				}

			}
		});
		mntmOrganize.setText(i18nFile.getText(I18nFile.ORGANIZE));

		addFavoriteMenuItem();

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText(i18nFile.getText(I18nFile.HELP));

		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);
		
		MenuItem mntmDonation = new MenuItem(menu_2, SWT.NONE);
		mntmDonation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DonationDialog dialog = new DonationDialog(shell, iconImage, codeImage);
				dialog.open();
			}
		});
		mntmDonation.setText(i18nFile.getText(I18nFile.DONATION));
		
		new MenuItem(menu_2, SWT.SEPARATOR);

		MenuItem mntmAbout = new MenuItem(menu_2, SWT.NONE);
		mntmAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AboutDialog dialog = new AboutDialog(shell, iconImage);
				dialog.open();
			}
		});
		mntmAbout.setText(i18nFile.getText(I18nFile.ABOUT));
	}

	protected void findForward() {
		if(fBuffer == null)
			find();		
		else {
			Node node = service2.findNext(fBuffer.getFindNode(), fBuffer.getSearchFrom(), fBuffer.getId(), fBuffer.getDb(), fBuffer.getContainer(), fBuffer.getSearchNodeType(), fBuffer.getPattern(), true);
			if(node != null) {
				TreeItem selected = gotoDBContainer(node.getId(), node.getDb(), node.getKey(), true, true);
				history.add(selected);
				btnBackward.setEnabled(true);
				btnForward.setEnabled(false);
				
				fBuffer.setFindNode(node);
			} else {
				boolean ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.FINDFORWARD), i18nFile.getText(I18nFile.FINDAGAIN));
				if(ok){
					Set<Node> nodes = service2.find(fBuffer.getSearchFrom(), fBuffer.getId(), fBuffer.getDb(), fBuffer.getContainer(), fBuffer.getSearchNodeType(), fBuffer.getPattern(), true);
					if(!nodes.isEmpty()) {
						Node node1 = nodes.iterator().next();
						TreeItem selected = gotoDBContainer(node1.getId(), node1.getDb(), node1.getKey(), true, true);
						history.add(selected);
						btnBackward.setEnabled(true);
						btnForward.setEnabled(false);
						
						fBuffer.setFindNode(node1);
					}else{
						MessageDialog.openInformation(shell, i18nFile.getText(I18nFile.FINDRESULTS),
								i18nFile.getText(I18nFile.NOFOUND));
					}
				}
			}
		}
	}
	
	protected void findBackward() {
		if(fBuffer == null)
			find();		
		else {
			Node node = service2.findNext(fBuffer.getFindNode(), fBuffer.getSearchFrom(), fBuffer.getId(), fBuffer.getDb(), fBuffer.getContainer(), fBuffer.getSearchNodeType(), fBuffer.getPattern(), false);
			if(node != null) {
				TreeItem selected = gotoDBContainer(node.getId(), node.getDb(), node.getKey(), true, true);
				
				history.add(selected);
				btnBackward.setEnabled(true);
				btnForward.setEnabled(false);
				
				fBuffer.setFindNode(node);
			} else {
				boolean ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.FINDBACKWARD), i18nFile.getText(I18nFile.FINDAGAIN));
				if(ok){
					Set<Node> nodes = service2.find(fBuffer.getSearchFrom(), fBuffer.getId(), fBuffer.getDb(), fBuffer.getContainer(), fBuffer.getSearchNodeType(), fBuffer.getPattern(), false);
					if(!nodes.isEmpty()) {
						Node node1 = nodes.iterator().next();
						TreeItem selected = gotoDBContainer(node1.getId(), node1.getDb(), node1.getKey(), true, true);
						
						history.add(selected);
						btnBackward.setEnabled(true);
						btnForward.setEnabled(false);
						
						fBuffer.setFindNode(node1);
					}else{
						MessageDialog.openInformation(shell, i18nFile.getText(I18nFile.FINDRESULTS),
								i18nFile.getText(I18nFile.NOFOUND));
					}
				}
			}
		}
	}

	protected void find() {
		FindKeyDialog dialog = new FindKeyDialog(shell, iconImage);
		FindInfo info = (FindInfo) dialog.open();
		if(info != null) {
			TreeItem treeItem;
			
			ContainerKeyInfo cinfo = new ContainerKeyInfo();
			if (itemsSelected[0] instanceof TreeItem) {
				treeItem = (TreeItem) itemsSelected[0];
			} else {
				treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);
			}

			parseContainer(treeItem, cinfo);
			NodeType searchFrom = (NodeType) treeItem.getData(NODE_TYPE);
			
			Set<Node> nodes = service2.find(searchFrom, cinfo.getId(), cinfo.getDb(), cinfo.getContainerStr(), info.getSearchNodeType(), info.getPattern(), info.isForward());
			if(!nodes.isEmpty()) {
				
				Node node = nodes.iterator().next();
				TreeItem selected = gotoDBContainer(node.getId(), node.getDb(), node.getKey(), true, true);
				history.add(selected);
				btnBackward.setEnabled(true);
				btnForward.setEnabled(false);
				
				fBuffer = new FindBuffer(node, searchFrom, cinfo.getId(), cinfo.getDb(), cinfo.getContainerStr(), info.getSearchNodeType(), info.getPattern());
			}else{
				MessageDialog.openInformation(shell, i18nFile.getText(I18nFile.FINDRESULTS),
						i18nFile.getText(I18nFile.NOFOUND));
			}
		}
	}

	private void export() {
		FileDialog dialog = new FileDialog(shell,SWT.SAVE);
		dialog.setText(i18nFile.getText(I18nFile.EXPORTREDIS));
		String[] filterExt = { "*.*" };
		dialog.setFilterExtensions(filterExt);
		String file = dialog.open();
		if(file != null){
			File exportFile = new File(file);
		
			boolean ok = false;
			boolean exist = exportFile.exists();
			if(exist)
				ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.FILEEXIST),
						i18nFile.getText(I18nFile.FILEREPLACE));
			if(!exist || ok) {
				for(Item item: itemsSelected){
					TreeItem treeItem;
					
					ContainerKeyInfo cinfo = new ContainerKeyInfo();
					if (item instanceof TreeItem) {
						treeItem = (TreeItem) item;
					} else {
						treeItem = getTreeItemByTableItem((TableItem) item);
					}
	
					parseContainer(treeItem, cinfo);
				
					exportOne(cinfo, file, item);
				}
			}
		}
	}

	private void exportOne(ContainerKeyInfo cinfo, String file, Item item) {
		ContainerKey containerKey = cinfo.getContainer();
		
		if(item instanceof TableItem){
			NodeType type = (NodeType) item.getData(NODE_TYPE);
			if(type != NodeType.CONTAINER){
				String con = containerKey == null?"":containerKey.getContainerKey();
				containerKey = new ContainerKey(con + item.getText());
			}
		}
			
		ExportService service = new ExportService(file, cinfo.getId(), cinfo.getDb(), containerKey);
		try {
			service.export();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	private void importFile() {
		TreeItem treeItem;
		
		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem) {
			treeItem = (TreeItem) itemsSelected[0];
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);
		}

		parseContainer(treeItem, cinfo);
		
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText(i18nFile.getText(I18nFile.IMPORTREDIS));
		String[] filterExt = { "*.*" };
		dialog.setFilterExtensions(filterExt);
		String file = dialog.open();
		if(file != null) {
			ImportService service = new ImportService(file, cinfo.getId(), cinfo.getDb());
			try {
				service.importFile();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			
			dbContainerTreeItemSelected(treeItem, true);
		}
	}

	private void removeFavoriteMenuItem() {
		int num = menuFavorite.getItemCount();
		if (num > 2) {
			MenuItem[] items = menuFavorite.getItems();
			for (int i = 2; i < num; i++) {
				items[i].dispose();
			}
		}

	}

	private void addFavoriteMenuItem() {
		List<Favorite> favorites = service3.listAll();
		if (favorites.size() > 0) {
			new MenuItem(menuFavorite, SWT.SEPARATOR);
			for (Favorite favorite : favorites) {
				final MenuItem menuItem = new MenuItem(menuFavorite, SWT.NONE);
				menuItem.setText(favorite.getName());
				menuItem.setData(FAVORITE, favorite);
				menuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Favorite favorite = (Favorite) menuItem
								.getData(FAVORITE);
						int sid = favorite.getServerID();

						String[] containers = favorite.getFavorite().split(":");
						
						String container = "";
						for(int i = 2; i < containers.length; i++){
							container += containers[i] + ":";
						}
						TreeItem selected = gotoDBContainer(sid,Integer.parseInt(containers[1].replaceFirst(DB_PREFIX, "")), container, favorite.isData(), false);
						history.add(selected);
						btnBackward.setEnabled(true);
						btnForward.setEnabled(false);
					}
				});
			}
		}
	}

	private void initServers() {
		java.util.List<Server> servers = service1.listAll();

		for (Server server : servers) {
			addServerTreeItem(server);
		}
		rootRedisServers.setExpanded(true);
		rootRedisServers.setData(ITEM_OPENED, true);
	}

	private TreeItem addServerTreeItem(Server server) {
		TreeItem treeItem = new TreeItem(rootRedisServers, 0);
		treeItem.setText(server.getName());
		treeItem.setData(NODE_ID, server.getId());
		treeItem.setData(NODE_TYPE, NodeType.SERVER);
		treeItem.setImage(redisImage);

		return treeItem;
	}

	private void addServer() {
		AddServerDialog dialog = new AddServerDialog(shell,
				iconImage);
		Server server = (Server) dialog.open();

		if (server != null) {
			server.setId(service1.add(server.getName(), server.getHost(),
					server.getPort(), server.getPassword()));
			TreeItem item = addServerTreeItem(server);
			serverTreeItemSelected(item, false);
			history.add(item); 
			btnBackward.setEnabled(true);
			btnForward.setEnabled(false);
		}
		
	}

	private void updateServer() {
		int id = (Integer) itemsSelected[0].getData(NODE_ID);

		Server server = service1.listById(id);
		UpdateServerDialog dialog = new UpdateServerDialog(shell,
				iconImage, server);
		server = (Server) dialog.open();
		if (server != null) {
			service1.update(id, server.getName(), server.getHost(),
					server.getPort(), server.getPassword());
			TreeItem treeItem = null;
			if(itemsSelected[0] instanceof TableItem){
				treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);
				itemsSelected[0].setText(server.getName());
			}else
				treeItem = (TreeItem) itemsSelected[0];
			
			treeItem.setText(server.getName());
			serverTreeItemSelected(treeItem, true);
		}
	}

	private void removeServer() {
		if(itemsSelected.length == 1){
			boolean ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.REMOVESERVER),
					i18nFile.getText(I18nFile.CONFIRMREMOVESERVER));
			if (ok) {
				removeOneServer(itemsSelected[0]);
			}
		} else if(itemsSelected.length > 1){
			boolean ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.REMOVESERVER),
					i18nFile.getText(I18nFile.CONFIRMREMOVESERVER));
			if (ok) {
				for(Item item: itemsSelected)
					removeOneServer(item);
			}
		}
	}

	private void removeOneServer(Item item) {
		int id = ((Integer) (item.getData(NODE_ID))).intValue();
		service1.delete(id);
		if (item instanceof TableItem) {
			getTreeItemByTableItem((TableItem) item).dispose();
		}
		item.dispose();
	}

	private TreeItem getTreeItemByTableItem(TableItem tableItem) {
		TreeItem[] treeItems = tree.getSelection();
		TreeItem treeItem = treeItems[0];
		NodeType tableItmeType = (NodeType) tableItem.getData(NODE_TYPE);
		
		if(tableItmeType != NodeType.DATABASE && tableItmeType != NodeType.SERVER && tableItmeType != NodeType.CONTAINER)
			return treeItem;
		TreeItem[] subTreeItems = treeItem.getItems();

		for (TreeItem item : subTreeItems) {
			NodeType type = (NodeType) item.getData(NODE_TYPE);

			if (type == NodeType.CONTAINER) {
				if (item.getText().equals(tableItem.getText()))
					return item;
			} else {
				int treeid = (Integer) item.getData(NODE_ID);
				int tableid = (Integer) tableItem.getData(NODE_ID);
				if (treeid == tableid)
					return item;
			}
		}
		return null;
	}

	private void renameContainer() {
		TreeItem treeItem;

		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem)
			treeItem = (TreeItem) itemsSelected[0];
		else
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);

		parseContainer(treeItem, cinfo);

		RenameKeysDialog dialog = new RenameKeysDialog(shell,
				iconImage, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainerStr());
		RenameInfo rinfo = (RenameInfo) dialog.open();

		if (rinfo != null) {
			Set<String> result = service2.renameContainer(cinfo.getId(),
					cinfo.getDb(), cinfo.getContainerStr(),
					rinfo.getNewContainer(), rinfo.isOverwritten(), rinfo.isRenameSub());
			treeItem.getParentItem().setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem.getParentItem(), false);
			if (!rinfo.isOverwritten() && result.size() > 0) {
				String failString = i18nFile.getText(I18nFile.RENAMEFAIL);
				for (String container : result)
					failString += container + "\n";
				MessageDialog.openError(shell, i18nFile.getText(I18nFile.RENAMERESULT),
						failString);
			}
		}
	}

	private void deleteCotainer(Item item, boolean deleteSub) {
			TreeItem treeItem;

			ContainerKeyInfo cinfo = new ContainerKeyInfo();
			if (item instanceof TreeItem)
				treeItem = (TreeItem) item;
			else
				treeItem = getTreeItemByTableItem((TableItem) item);

			parseContainer(treeItem, cinfo);

			service2.deleteContainer(cinfo.getId(), cinfo.getDb(),
					cinfo.getContainerStr(), deleteSub);
			
			if (item instanceof TableItem) {
				treeItem.dispose();
			}
			item.dispose();
	}

	private void addFavorite() {
		TreeItem treeItem;
		String fullContainer;
		
		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem) {
			treeItem = (TreeItem) itemsSelected[0];
			fullContainer = text.getText();
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);
			NodeType type = (NodeType) itemsSelected[0].getData(NODE_TYPE);
			if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
				fullContainer = text.getText() + itemsSelected[0].getText() + ":";
			else
				fullContainer = text.getText() + itemsSelected[0].getText();
		}

		parseContainer(treeItem, cinfo);

		AddFavoriteDialog dialog = new AddFavoriteDialog(shell,
				iconImage, fullContainer);
		String name = (String) dialog.open();
		if (name != null)
			service3.add(cinfo.getId(), name, fullContainer);

		removeFavoriteMenuItem();
		addFavoriteMenuItem();
	}

	private void treeItemSelected(boolean refresh) {
		TreeItem[] items = tree.getSelection();
		NodeType type = (NodeType) items[0].getData(NODE_TYPE);

		if(itemsSelected[0] == items[0] && !refresh)
			return;
		
		switch (type) {
		case ROOT:
			rootTreeItemSelected(refresh);
			break;
		case SERVER:
			serverTreeItemSelected(items[0], refresh);
			break;
		case DATABASE:
		case CONTAINER:
			dbContainerTreeItemSelected(items[0], refresh);
			break;
		default:
			break;
		}
	}

	private void dbContainerTreeItemSelected(TreeItem itemSelected,
			boolean refresh) {
		itemsSelected = new Item[]{itemSelected};
		tree.setSelection(itemSelected);
		ContainerKeyInfo info = new ContainerKeyInfo();
		parseContainer(itemSelected, info);
		text.setText(getLocation(info));

		dbContainerItemSelected(itemSelected);

		NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);
		if(!(type == NodeType.CONTAINER && flatView)){
			Set<Node> cnodes = service2.listContainers(info.getId(), info.getDb(),
					info.getContainerStr(), flatView);
	
			if (itemSelected.getData(ITEM_OPENED) == null
					|| ((Boolean) (itemSelected.getData(ITEM_OPENED)) == false)) {
				itemSelected.removeAll();
	
				for (Node node : cnodes) {
					TreeItem item = new TreeItem(itemSelected, SWT.NONE);
					item.setText(node.getKey());
					item.setData(NODE_TYPE, node.getType());
					item.setImage(containerImage);
	
				}
				itemSelected.setExpanded(true);
				itemSelected.setData(ITEM_OPENED, true);
			} else if (refresh) {
				for(Node node: cnodes){
					if(!findItemByNode(itemSelected, node)){
						TreeItem item = new TreeItem(itemSelected, SWT.NONE);
						item.setText(node.getKey());
						item.setData(NODE_TYPE, node.getType());
						item.setImage(containerImage);
					}
				}
				
				TreeItem[] items = itemSelected.getItems();
				for(TreeItem item: items){
					if(!findNodeByItem(cnodes, item)){
						item.dispose();
					}
				}
			}
		}

		tableItemOrderSelected(info, Order.Ascend, OrderBy.NAME);
		table.setSortColumn(null);
	}

	private String getLocation(ContainerKeyInfo info) {
		return info.getServerName() + ":" + DB_PREFIX + info.getDb()
				+ ":" + info.getContainerStr();
	}
	
	private boolean findItemByNode(TreeItem itemSelected, Node node) {
		TreeItem[] items = itemSelected.getItems();
		
		for(TreeItem item: items){
			if(item.getText().equals(node.getKey()))
				return true;
		}
		return false;
		
	}
	
	private boolean findNodeByItem(Set<Node> nodes, TreeItem item) {
		for (Node node : nodes) {
			if(node.getKey().equals(item.getText()))
				return true;
		}
		return false;
	}

	private void dbContainerItemSelected(Item itemSelected) {
		menuServer.getItem(1).setEnabled(false);
		menuServer.getItem(2).setEnabled(false);
		menuServer.getItem(3).setEnabled(false);

		NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);

		menuData.getItem(0).setEnabled(true);
		if (type == NodeType.CONTAINER) {
			menuData.getItem(1).setEnabled(true);
			menuData.getItem(2).setEnabled(true);
			menuData.getItem(3).setEnabled(true);
			menuData.getItem(5).setEnabled(true);
			menuData.getItem(6).setEnabled(true);
			if(pBuffer.canPaste())
				menuData.getItem(7).setEnabled(true);
			else
				menuData.getItem(7).setEnabled(false);
			
			menuData.getItem(9).setEnabled(false);
			menuData.getItem(10).setEnabled(true);
		} else {
			menuData.getItem(1).setEnabled(false);
			menuData.getItem(2).setEnabled(false);
			menuData.getItem(3).setEnabled(true);
			menuData.getItem(5).setEnabled(false);
			menuData.getItem(6).setEnabled(true);
			if(pBuffer.canPaste())
				menuData.getItem(7).setEnabled(true);
			else
				menuData.getItem(7).setEnabled(false);
			
			menuData.getItem(9).setEnabled(true);
			menuData.getItem(10).setEnabled(true);
		}

		menuFavorite.getItem(0).setEnabled(true);
	}

	private void tableItemOrderSelected(ContainerKeyInfo info, Order order,
			OrderBy orderBy) {
		table.removeAll();
		
		if(!flatView){
			Set<Node> cnodes = service2.listContainers(info.getId(), info.getDb(),
					info.getContainerStr(), flatView, order);
	
			for (Node node : cnodes) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] { node.getKey(),
						node.getType().toString() });
				item.setImage(containerImage);
				item.setData(NODE_TYPE, node.getType());
			}
		}
		
		Set<DataNode> knodes = service2.listContainerKeys(info.getId(),
				info.getDb(), info.getContainerStr(), false, order, orderBy);

		for (DataNode node1 : knodes) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { node1.getKey(),
					node1.getType().toString(), String.valueOf(node1.getSize()) });
			switch (node1.getType()) {
			case STRING:
				item.setImage(strImage);
				break;
			case SET:
				item.setImage(setImage);
				break;
			case LIST:
				item.setImage(listImage);
				break;
			case HASH:
				item.setImage(hashImage);
				break;
			case SORTEDSET:
				item.setImage(zsetImage);
				break;
			default:
				break;
			}
			item.setData(NODE_TYPE, node1.getType());
		}
	}

	private void rootTreeItemSelected(boolean refresh) {
		itemsSelected = new Item[]{rootRedisServers};
		tree.setSelection(rootRedisServers);
		text.setText("");
		table.removeAll();

		rootItemSelected();

		if (rootRedisServers.getData(ITEM_OPENED) == null
				|| ((Boolean) (rootRedisServers.getData(ITEM_OPENED)) == false)) {
			rootRedisServers.removeAll();
			initServers();
		} else if (refresh){
			
		}
		

		java.util.List<Server> servers = service1.listAll();

		for (Server server : servers) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { server.getName(),
					NodeType.SERVER.toString() });
			item.setImage(redisImage);
			item.setData(NODE_ID, server.getId());
			item.setData(NODE_TYPE, NodeType.SERVER);
		}

	}

	private void rootItemSelected() {
		menuServer.getItem(1).setEnabled(false);
		menuServer.getItem(2).setEnabled(false);
		menuServer.getItem(3).setEnabled(false);

		menuData.getItem(0).setEnabled(false);
		menuData.getItem(1).setEnabled(false);
		menuData.getItem(2).setEnabled(false);
		menuData.getItem(3).setEnabled(false);
		menuData.getItem(5).setEnabled(false);
		menuData.getItem(6).setEnabled(false);
		menuData.getItem(7).setEnabled(false);
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(false);
		
		menuFavorite.getItem(0).setEnabled(false);
	}

	private void serverTreeItemSelected(TreeItem selectedItem, boolean refresh) {
		itemsSelected = new Item[]{selectedItem};
		tree.setSelection(selectedItem);
		text.setText(selectedItem.getText() + ":");
		table.removeAll();

		serverItemSelected();

		int amount = service1.listDBs((Integer) selectedItem.getData(NODE_ID));
		
		if (selectedItem.getData(ITEM_OPENED) == null
				|| ((Boolean) (selectedItem.getData(ITEM_OPENED)) == false)) {
			selectedItem.removeAll();
			
			for (int i = 0; i < amount; i++) {
				TreeItem dbItem = new TreeItem(selectedItem, SWT.NONE);
				dbItem.setText(DB_PREFIX + i);
				dbItem.setData(NODE_ID, i);
				dbItem.setData(NODE_TYPE, NodeType.DATABASE);
				dbItem.setImage(dbImage);
			}
			selectedItem.setExpanded(true);
			selectedItem.setData(ITEM_OPENED, true);
		} else if (refresh){
			
		}

		for (int i = 0; i < amount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { DB_PREFIX + i,
					NodeType.DATABASE.toString() });
			item.setData(NODE_ID, i);
			item.setImage(dbImage);
			item.setData(NODE_ID, i);
			item.setData(NODE_TYPE, NodeType.DATABASE);
		}

	}

	private void serverItemSelected() {
		menuServer.getItem(1).setEnabled(true);
		menuServer.getItem(2).setEnabled(true);
		menuServer.getItem(3).setEnabled(true);

		menuData.getItem(0).setEnabled(false);
		menuData.getItem(1).setEnabled(false);
		menuData.getItem(2).setEnabled(false);
		menuData.getItem(3).setEnabled(false);
		menuData.getItem(5).setEnabled(false);
		menuData.getItem(6).setEnabled(false);
		menuData.getItem(7).setEnabled(false);
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(false);
		
		menuFavorite.getItem(0).setEnabled(false);
	}

	private void newString() {
		TreeItem treeItem;

		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem)
			treeItem = (TreeItem) itemsSelected[0];
		else
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);

		parseContainer(treeItem, cinfo);

		NewStringDialog dialog = new NewStringDialog(shell,
				iconImage,  cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainerStr());
		StringInfo info = (StringInfo) dialog.open();
		if (info != null) {
			service2.addString(cinfo.getId(), cinfo.getDb(), info.getKey(),
					info.getValue());

			treeItem.setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem, false);
		}

	}

	private void newList() {
		TreeItem treeItem;

		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem)
			treeItem = (TreeItem) itemsSelected[0];
		else
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);

		parseContainer(treeItem, cinfo);

		NewListDialog dialog = new NewListDialog(shell,
				iconImage, cinfo.getId(), cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainerStr());
		ListInfo info = (ListInfo) dialog.open();
		if (info != null) {
			service4.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
					info.getValues(), info.isHeadTail(), info.isExist());
			treeItem.setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem, false);
		}
	}

	private void newSet() {
		TreeItem treeItem;

		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem)
			treeItem = (TreeItem) itemsSelected[0];
		else
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);

		parseContainer(treeItem, cinfo);

		NewSetDialog dialog = new NewSetDialog(shell, iconImage, cinfo.getServerName(), cinfo.getDb(),
				cinfo.getContainerStr());
		SetInfo info = (SetInfo) dialog.open();
		if (info != null) {
			service5.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
					info.getValues());
			treeItem.setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem, false);
		}
	}

	private void newZSet() {
		TreeItem treeItem;

		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem)
			treeItem = (TreeItem) itemsSelected[0];
		else
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);

		parseContainer(treeItem, cinfo);

		NewZSetDialog dialog = new NewZSetDialog(shell,
				iconImage,  cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainerStr());
		ZSetInfo info = (ZSetInfo) dialog.open();
		if (info != null) {
			service6.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
					info.getValues());
			treeItem.setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem, false);
		}

	}

	private void newHash() {
		TreeItem treeItem;

		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem)
			treeItem = (TreeItem) itemsSelected[0];
		else
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);

		parseContainer(treeItem, cinfo);

		NewHashDialog dialog = new NewHashDialog(shell,
				iconImage, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainerStr());
		HashInfo info = (HashInfo) dialog.open();
		if (info != null) {
			service7.add(cinfo.getId(), cinfo.getDb(), info.getKey(),
					info.getValues());
			treeItem.setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem, false);
		}

	}

	private void columnSelected(final TableColumn tblclmn, OrderBy orderBy) {
		TreeItem[] items = tree.getSelection();
		if (items.length > 0) {
			NodeType type = (NodeType) items[0].getData(NODE_TYPE);
			if (type == NodeType.CONTAINER || type == NodeType.DATABASE) {
				Order order = (Order) tblclmn.getData(ORDER);
				if (order == null) {
					tblclmn.setData(ORDER, Order.Ascend);
					table.setSortColumn(tblclmn);
					table.setSortDirection(SWT.UP);
				} else if (order == Order.Ascend) {
					tblclmn.setData(ORDER, Order.Descend);
					table.setSortColumn(tblclmn);
					table.setSortDirection(SWT.DOWN);
				} else {
					tblclmn.setData(ORDER, Order.Ascend);
					table.setSortColumn(tblclmn);
					table.setSortDirection(SWT.UP);
				}

				ContainerKeyInfo info = new ContainerKeyInfo();
				parseContainer(items[0], info);

				tableItemOrderSelected(info, (Order) tblclmn.getData(ORDER),
						orderBy);
			}
		}
	}

	private void renameKey() {
		ContainerKeyInfo cinfo = new ContainerKeyInfo();
		TreeItem[] items = tree.getSelection();

		parseContainer(items[0], cinfo);

		String key = cinfo.getContainerStr();
		key += itemsSelected[0].getText();
		
		RenameKeysDialog dialog = new RenameKeysDialog(shell,
				iconImage, cinfo.getServerName(),
				cinfo.getDb(), key);
		RenameInfo rinfo = (RenameInfo) dialog.open();

		if (rinfo != null) {
			boolean result = service2.renameKey(cinfo.getId(),
					cinfo.getDb(), key,
					rinfo.getNewContainer(), rinfo.isOverwritten());
			dbContainerTreeItemSelected(items[0], false);
			
			if (!rinfo.isOverwritten() && !result) {
				String failString = i18nFile.getText(I18nFile.RENAMEKEYFAIL);
				MessageDialog.openError(shell, i18nFile.getText(I18nFile.RENAMEKEYRESULT),
						failString);
			}
		}
	}

	private void deleteKey(Item item) {
			ContainerKeyInfo cinfo = new ContainerKeyInfo();
			TreeItem[] items = tree.getSelection();

			parseContainer(items[0], cinfo);

			String key = cinfo.getContainerStr();
			key += item.getText();

			service2.deleteKey(cinfo.getId(), cinfo.getDb(), key);
			item.dispose();
	}
	
	private void cut() {
		pBuffer = new PasteBuffer();
		
		for(Item item: itemsSelected) {
			TreeItem treeItem;
			
			ContainerKeyInfo cinfo = new ContainerKeyInfo();
			if (item instanceof TreeItem) {
				treeItem = (TreeItem) item;
			} else {
				treeItem = getTreeItemByTableItem((TableItem) item);
			}
	
			parseContainer(treeItem, cinfo);
	
			if(item instanceof TreeItem)
				pBuffer.cut(cinfo, treeItem);
			else {
				NodeType type = (NodeType) item.getData(NODE_TYPE);
				if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
					pBuffer.cut(cinfo, treeItem);
				else{
					cinfo.setContainer(cinfo.getContainer(), item.getText());
					pBuffer.cut(cinfo, treeItem);
				}
			}
		}
	}

	private void copy() {
		pBuffer = new PasteBuffer();
		for(Item item: itemsSelected) {
			TreeItem treeItem;
			
			ContainerKeyInfo cinfo = new ContainerKeyInfo();
			if (item instanceof TreeItem) {
				treeItem = (TreeItem) item;
			} else {
				treeItem = getTreeItemByTableItem((TableItem) item);
			}
	
			parseContainer(treeItem, cinfo);
	
			if(item instanceof TreeItem)
				pBuffer.copy(cinfo);
			else {
				NodeType type = (NodeType) item.getData(NODE_TYPE);
				if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
					pBuffer.copy(cinfo);
				else{
					cinfo.setContainer(cinfo.getContainer(), item.getText());
					pBuffer.copy(cinfo);
				}
			}
		}
	}

	private void paste() {
		TreeItem treeItem;
		
		ContainerKeyInfo target = new ContainerKeyInfo();
		if (itemsSelected[0] instanceof TreeItem) {
			treeItem = (TreeItem) itemsSelected[0];
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]);
		}

		parseContainer(treeItem, target);

		do{
			ContainerKeyInfo source = pBuffer.paste();
			pasteOne(target, source);
		}while(pBuffer.hasNext());
	}

	private void pasteOne(ContainerKeyInfo target, ContainerKeyInfo source) {
		if(!pBuffer.isCopy() && ! source.getContainer().isKey()){
			 pBuffer.getCutItem().dispose();
		}
		
		if(source.getContainer().isKey()){
			String newKey = service2.pasteKey(source.getId(), source.getDb(), source.getContainerStr(), target.getId(), target.getDb(), target.getContainerStr()+source.getContainer().getKeyOnly(), pBuffer.isCopy(), true);
			if(newKey == null)
				gotoDBContainer(target.getId(), target.getDb(), target.getContainerStr() + source.getContainer().getKeyOnly(), true, true);
			else
				gotoDBContainer(target.getId(), target.getDb(), newKey, true, true);
		}else{
			service2.pasteContainer(source.getId(), source.getDb(), source.getContainerStr(), target.getId(), target.getDb(), target.getContainerStr(), pBuffer.isCopy(), true);
			gotoDBContainer(target.getId(), target.getDb(), target.getContainerStr(), false, true);
		}
	}

	private void updateMenuDBContainer(NodeType type, Menu menu) {
		if (type == NodeType.DATABASE) {
			menu.getItem(1).setEnabled(false);
			menu.getItem(2).setEnabled(false);
			menu.getItem(7).setEnabled(false);
			menu.getItem(8).setEnabled(true);
			if(pBuffer.canPaste())
				menu.getItem(9).setEnabled(true);
			else
				menu.getItem(9).setEnabled(false);
			
			menu.getItem(11).setEnabled(true);
		} else {
			menu.getItem(1).setEnabled(true);
			menu.getItem(2).setEnabled(true);
			menu.getItem(7).setEnabled(true);
			menu.getItem(8).setEnabled(true);
			if(pBuffer.canPaste())
				menu.getItem(9).setEnabled(true);
			else
				menu.getItem(9).setEnabled(false);
			
			menu.getItem(11).setEnabled(false);
		}
	}

	private TreeItem gotoDBContainer(int id, int db, String container, boolean isKey, boolean refresh)  {
		rootTreeItemSelected(false);
		TreeItem dbItem = findDBTreeItem(id, db);
		TreeItem dataItemSelected = dbItem;
		
		dbContainerTreeItemSelected(dbItem,	refresh);
		TreeItem[] dataItems = dbItem.getItems();
			
		if(!flatView){
			String[] containers = container.split(":");
			if(!isKey){
				for (int i = 0; i < containers.length; i++) {
					for (TreeItem dataItem : dataItems) {
						if (dataItem.getText().equals(containers[i])) {
							tree.setSelection(dataItem);
							tree.setFocus();
							dbContainerTreeItemSelected(dataItem, refresh);
							dataItems = dataItem.getItems();
							dataItemSelected = dataItem;
							break;
						}
					}
				}
			} else {
				for (int i = 0; i < containers.length - 1; i++) {
					for (TreeItem dataItem : dataItems) {
						if (dataItem.getText().equals(containers[i])) {
							tree.setSelection(dataItem);
							dbContainerTreeItemSelected(dataItem, false);
							dataItems = dataItem.getItems();
							dataItemSelected = dataItem;
							break;
						}
					}
				}
				TableItem[] tableItems = table.getItems();
				for(TableItem tableItem : tableItems) {
					NodeType type = (NodeType) tableItem.getData(NODE_TYPE);
					if(type != NodeType.SERVER && type != NodeType.DATABASE && type != NodeType.CONTAINER && tableItem.getText().equals(containers[containers.length -1])){
						table.setSelection(tableItem);
						table.setFocus();
						tableItemSelected();
						break;
					}
						
				}
	
			}
		}else{
			if(!isKey){
				for (TreeItem dataItem : dataItems) {
					if (dataItem.getText().equals(container)) {
						tree.setSelection(dataItem);
						tree.setFocus();
						dbContainerTreeItemSelected(dataItem, refresh);
						dataItems = dataItem.getItems();
						dataItemSelected = dataItem;
						break;
					}
				}
			} else {
				for (TreeItem dataItem : dataItems) {
					String containerOnly = new ContainerKey(container).getContainerOnly();
					if (dataItem.getText().equals(containerOnly)) {
						tree.setSelection(dataItem);
						dbContainerTreeItemSelected(dataItem, false);
						dataItems = dataItem.getItems();
						dataItemSelected = dataItem;
						break;
					}
				}

				TableItem[] tableItems = table.getItems();
				for(TableItem tableItem : tableItems) {
					NodeType type = (NodeType) tableItem.getData(NODE_TYPE);
					String keyOnly = new ContainerKey(container).getKeyOnly();
					if(type != NodeType.SERVER && type != NodeType.DATABASE && type != NodeType.CONTAINER && tableItem.getText().equals(keyOnly)){
						table.setSelection(tableItem);
						table.setFocus();
						tableItemSelected();
						break;
					}
						
				}
	
			}
		}

		return dataItemSelected;
	}
	
	private TreeItem findServerTreeItem(int id) {
		TreeItem[] treeItems = rootRedisServers.getItems();
		for (TreeItem treeItem : treeItems) {
			int serverId = (Integer) treeItem.getData(NODE_ID);
			if (serverId == id) 
				return treeItem;
		}
		throw new RuntimeException(i18nFile.getText(I18nFile.FINDSERVER));
	}
	
	private TreeItem findDBTreeItem(int id, int db)  {
		TreeItem server = findServerTreeItem(id);
		
		serverTreeItemSelected(server, false);
		TreeItem[] dbItems = server.getItems();
		for (TreeItem dbItem : dbItems) {
			if (dbItem.getText().equals(DB_PREFIX+db)) 
				return dbItem;
		}
		throw new RuntimeException(i18nFile.getText(I18nFile.FINDDB));
	}

	private void selectTreeItem() {
		logger.debug("tree selected");
		TreeItem[] items = tree.getSelection();

		if((itemsSelected[0] instanceof TreeItem && items[0] != itemsSelected[0]) ) {
			history.add(items[0]);
			btnBackward.setEnabled(true);
			btnForward.setEnabled(false);
		} else if(itemsSelected[0] instanceof TableItem  && items[0] != treeItemSelected){
			history.add(items[0]);
			btnBackward.setEnabled(true);
			btnForward.setEnabled(false);
		}
		treeItemSelected(false);
	}

	private void serverProperties() {
		int id = (Integer) itemsSelected[0].getData(NODE_ID);
		Server info = service1.listById(id);
		
		Map<String, String[]> values = service1.listInfo(id);
		
		PropertiesDialog dialog = new PropertiesDialog(shell,
				iconImage,  info, values);
		dialog.open();
	}

	private void deleteOneContainer() {
		DeleteContainerDialog dialog = new DeleteContainerDialog(shell, iconImage, questionImage, 1);
		Boolean deleteSub = (Boolean) dialog.open();
		if (deleteSub != null) {
			TreeItem treeItem;
			if(itemsSelected[0] instanceof TableItem)
				treeItem = getTreeItemByTableItem((TableItem) itemsSelected[0]).getParentItem();
			else
				treeItem = ((TreeItem) itemsSelected[0]).getParentItem();
			
			deleteCotainer(itemsSelected[0], deleteSub);
		
			treeItem.setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem, true);
		}
	}

	private void deleteOneKey() {
		boolean ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.DELETEKEY),
				i18nFile.getText(I18nFile.CONFIRMDELETEKEY));
		if (ok) 
			deleteKey(itemsSelected[0]);
	}

	private void deleteKeys() {
		TableItem containerItem = findContainerTableItem();
		
		DeleteContainerDialog dialog = new DeleteContainerDialog(shell, iconImage, questionImage, containerItem == null? 0: -1);
		Boolean deleteSub = (Boolean) dialog.open();
		if (deleteSub != null) {
			TreeItem treeItem = null;
			if(containerItem != null)
				treeItem = getTreeItemByTableItem(containerItem).getParentItem();
			
			for(Item item: itemsSelected){
				NodeType type = (NodeType) item.getData(NODE_TYPE);
				if(type == NodeType.CONTAINER){
					deleteCotainer(item, deleteSub);
				}else{
					deleteKey(item);
				}
			}
			if(containerItem != null){
				treeItem.setData(ITEM_OPENED, false);
				dbContainerTreeItemSelected(treeItem, true);
			}
			
		}
	}
	
	private TableItem findContainerTableItem() {
		for(Item item: itemsSelected){
			NodeType type = (NodeType) item.getData(NODE_TYPE);
			if(type == NodeType.CONTAINER)
				return (TableItem) item;
		}
		return null;
	}

	private void refreshDB() {
		TreeItem[] serverItems = rootRedisServers.getItems();
		for(TreeItem item: serverItems){
			if(item.getData(ITEM_OPENED) != null && ((Boolean) (item.getData(ITEM_OPENED)) == true)){
				TreeItem[] dbItems = item.getItems();
				for(TreeItem dbItem: dbItems){
					if(dbItem.getData(ITEM_OPENED) != null && ((Boolean) (dbItem.getData(ITEM_OPENED)) == true))
						dbItem.setData(ITEM_OPENED, false);;
						dbItem.removeAll();
						dbItem.setExpanded(false);
				}
				
			}
		}
		rootTreeItemSelected(true);
		history.clear();
		history.add(rootRedisServers);
		btnBackward.setEnabled(false);
		btnForward.setEnabled(false);
	}

	private void refreshLangUI() {
		menu.dispose();
		initMenu();
		rootRedisServers.setText(i18nFile.getText(I18nFile.REDISSERVERS));
		menu_key.dispose();
		initMenuData();
		menuTreeDBContainer.dispose();
		menuTreeDBContainer = initMenuTreeDB();
		menuTableDBContainer.dispose();
		menuTableDBContainer = initMenuTableDB();
		menu_Multi.dispose();
		initMenuMulti();
		menu_null.dispose();
		initMenuNull();
		menuTreeServer.dispose();
		menuTreeServer = initMenuTreeServer();
		menuTableServer.dispose();
		menuTableServer = initMenuTableServer();
		
		tblclmnName.setText(i18nFile.getText(I18nFile.NAME));
		tblclmnType.setText(i18nFile.getText(I18nFile.TYPE));
		tblclmnSize.setText(i18nFile.getText(I18nFile.SIZE));
		
		ConfigFile.setLanguage(language);
	}

	private void updateView(ContainerKeyInfo cinfo) {
		refreshDB();
		if(cinfo.getId()!= -1 && cinfo.getDb() != -1)
			gotoDBContainer(cinfo.getId(), cinfo.getDb(), cinfo.getContainerStr(), false, false);
		
		ConfigFile.setFlatView(flatView);
	}
}
