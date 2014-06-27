package com.cxy.redisclient.presentation;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Favorite;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.dto.ContainerInfo;
import com.cxy.redisclient.dto.FindInfo;
import com.cxy.redisclient.dto.HashInfo;
import com.cxy.redisclient.dto.ListInfo;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.dto.RenameInfo;
import com.cxy.redisclient.dto.SetInfo;
import com.cxy.redisclient.dto.StringInfo;
import com.cxy.redisclient.dto.ZSetInfo;
import com.cxy.redisclient.presentation.favorite.AddFavoriteDialog;
import com.cxy.redisclient.presentation.favorite.OrganizeFavoriteDialog;
import com.cxy.redisclient.presentation.hash.NewHashDialog;
import com.cxy.redisclient.presentation.key.FindKeyDialog;
import com.cxy.redisclient.presentation.key.RenameKeysDialog;
import com.cxy.redisclient.presentation.list.NewListDialog;
import com.cxy.redisclient.presentation.server.AddServerDialog;
import com.cxy.redisclient.presentation.server.UpdateServerDialog;
import com.cxy.redisclient.presentation.set.NewSetDialog;
import com.cxy.redisclient.presentation.string.NewStringDialog;
import com.cxy.redisclient.presentation.zset.NewZSetDialog;
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
	private Shell shlRedisClient;
	private PasteBuffer pBuffer = new PasteBuffer();
	private FindBuffer fBuffer = null;
	private NavHistory history = new NavHistory();
	
	private Item itemSelected;
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

	private TreeItem rootRedisServers;

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

	private TableColumn tblclmnName;
	private TableColumn tblclmnType;
	private TableColumn tblclmnSize;
	private Menu menu_dbContainer;
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

		shlRedisClient.open();
		shlRedisClient.layout();

		while (!shlRedisClient.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				MessageDialog.openError(shlRedisClient, "Error", e.getMessage());
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

		initImage();

		initMenu();

		initSash();
	}

	private void initImage() {
		redisImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/redis.png"));
		dbImage = new Image(shlRedisClient.getDisplay(), getClass().getResourceAsStream(
				"/db.png"));
		strImage = new Image(shlRedisClient.getDisplay(), getClass().getResourceAsStream(
				"/string.png"));
		setImage = new Image(shlRedisClient.getDisplay(), getClass().getResourceAsStream(
				"/set.png"));
		listImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/list.png"));
		zsetImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/zset.png"));
		hashImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/hash.png"));
		containerImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/container.png"));
		
		leftImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/left.png"));
		rightImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/right.png"));
		upImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/up.png"));
		refreshImage = new Image(shlRedisClient.getDisplay(), getClass()
				.getResourceAsStream("/refresh.png"));
	}

	private void initShell() {
		shlRedisClient = new Shell();
		shlRedisClient.setSize(1074, 772);
		shlRedisClient.setText("Redis client");
		shlRedisClient.setLayout(new GridLayout(1, false));
	}

	private void initSash() {
		Composite composite_1 = new Composite(shlRedisClient, SWT.NONE);
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
					MessageDialog.openInformation(shlRedisClient, "error", "container is deleted!");
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
					MessageDialog.openInformation(shlRedisClient, "error", "container is deleted!");
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
	}

	private void initMenuData() {
		menu_key = new Menu(shlRedisClient);

		MenuItem mntmRename = new MenuItem(menu_key, SWT.NONE);
		mntmRename.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameKey();
			}
		});
		mntmRename.setText("rename");

		MenuItem mntmDelete_4 = new MenuItem(menu_key, SWT.NONE);
		mntmDelete_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteKey();
			}
		});
		mntmDelete_4.setText("delete");
		
				MenuItem mntmProperties_1 = new MenuItem(menu_key, SWT.NONE);
				mntmProperties_1.setText("properties");

		new MenuItem(menu_key, SWT.SEPARATOR);

		MenuItem menuItem = new MenuItem(menu_key, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		menuItem.setText("add to favorites");

		new MenuItem(menu_key, SWT.SEPARATOR);

		MenuItem mntmCut_1 = new MenuItem(menu_key, SWT.NONE);
		mntmCut_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cut();
			}
		});
		mntmCut_1.setText("cut");

		MenuItem mntmCopy_2 = new MenuItem(menu_key, SWT.NONE);
		mntmCopy_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy_2.setText("copy");
	}

	private void initTree(SashForm sashForm) {
		tree = new Tree(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				
				if((itemSelected instanceof TreeItem && items[0] != itemSelected) ) {
					history.add(items[0]);
					btnBackward.setEnabled(true);
					btnForward.setEnabled(false);
					treeItemSelected(false);
				} else if(itemSelected instanceof TableItem  && items[0] != treeItemSelected){
					history.add(items[0]);
					btnBackward.setEnabled(true);
					btnForward.setEnabled(false);
					treeItemSelected(false);
				}
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				Point point = new Point(arg0.x, arg0.y);
				TreeItem selectedItem = tree.getItem(point);
				if (arg0.button == 3) {
					if (selectedItem == rootRedisServers || selectedItem == null)
						tree.setMenu(menu_null);
					else {
						//itemSelected = selectedItem;

						NodeType type = (NodeType) selectedItem
								.getData(NODE_TYPE);

						if (type == NodeType.ROOT)
							tree.setMenu(menu_null);
						else if (type == NodeType.SERVER)
							tree.setMenu(menuTreeServer);
						else if (type == NodeType.DATABASE
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
		rootRedisServers.setText("Redis Servers");
		rootRedisServers.setData(NODE_TYPE, NodeType.ROOT);
		rootRedisServers.setExpanded(true);
		rootRedisServers.setData(ITEM_OPENED, true);
	}

	private void parseContainer(TreeItem item, ContainerInfo info) {
		TreeItem parent = item.getParentItem();
		if (item.getData(NODE_TYPE) == NodeType.CONTAINER) {
			String container = item.getText();
			if (info.getContainer() != null)
				info.setContainer(container + ":" + info.getContainer());
			else
				info.setContainer(container + ":");

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
		mntmRefresh_2.setText("refresh");

		return menu;
	}

	private Menu initMenuTableDB() {
		menu_dbContainer = new Menu(shlRedisClient);

		MenuItem mntmNew_1 = new MenuItem(menu_dbContainer, SWT.CASCADE);
		mntmNew_1.setText("new");

		Menu menu_1 = new Menu(mntmNew_1);
		mntmNew_1.setMenu(menu_1);

		MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newString();
			}
		});
		menuItem_1.setText("String");
		menuItem_1.setImage(strImage);

		MenuItem menuItem_2 = new MenuItem(menu_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newList();
			}
		});
		menuItem_2.setText("List");
		menuItem_2.setImage(listImage);

		MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSet();
			}
		});
		menuItem_3.setText("Set");
		menuItem_3.setImage(setImage);

		MenuItem mntmSortedSet = new MenuItem(menu_1, SWT.NONE);
		mntmSortedSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newZSet();
			}
		});
		mntmSortedSet.setText("Sorted Set");
		mntmSortedSet.setImage(zsetImage);

		MenuItem mntmHash_1 = new MenuItem(menu_1, SWT.NONE);
		mntmHash_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newHash();
			}
		});
		mntmHash_1.setText("Hash");
		mntmHash_1.setImage(hashImage);

		MenuItem mntmRename_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmRename_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameContainer();
			}
		});
		mntmRename_1.setText("rename");

		MenuItem mntmDelete_2 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmDelete_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteCotainer();
			}
		});
		mntmDelete_2.setText("delete");
		
		MenuItem mntmProperties_3 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmProperties_3.setText("properties");

		new MenuItem(menu_dbContainer, SWT.SEPARATOR);

		MenuItem mntmAddToFavorites = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmAddToFavorites.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		mntmAddToFavorites.setText("add to favorites");

		new MenuItem(menu_dbContainer, SWT.SEPARATOR);

		MenuItem mntmCut = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmCut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				cut();
			}
		});
		mntmCut.setText("cut");

		MenuItem mntmCopy_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmCopy_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy_1.setText("copy");

		MenuItem mntmPaste_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmPaste_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				paste();
			}
		});
		mntmPaste_1.setEnabled(false);
		mntmPaste_1.setText("paste");

		new MenuItem(menu_dbContainer, SWT.SEPARATOR);
		
		MenuItem mntmImport_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmImport_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importFile();
			}
		});
		mntmImport_1.setEnabled(false);
		mntmImport_1.setText("import");
		
		MenuItem mntmExport_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmExport_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				export();
			}
		});
		mntmExport_1.setText("export");
		
		new MenuItem(menu_dbContainer, SWT.SEPARATOR);
		
		MenuItem mntmFind_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmFind_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		mntmFind_1.setText("find");
		
		MenuItem mntmFindNext_1 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmFindNext_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindNext_1.setText("find forward");
		
		MenuItem mntmFindBackward_3 = new MenuItem(menu_dbContainer, SWT.NONE);
		mntmFindBackward_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward_3.setText("find backward");
		
		return menu_dbContainer;
	}

	

	private void initTable(SashForm sashForm_1) {
		table = new Table(sashForm_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				itemSelected = items[0];
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

			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TreeItem[] treeItems = tree.getSelection();
				Point point = new Point(e.x, e.y);
				TableItem selectedItem = table.getItem(point);

				if (selectedItem != null){
					boolean find = false;
					for (TreeItem treeItem : treeItems[0].getItems()) {
						String treeText = treeItem.getText();
						String tableText = selectedItem.getText(0);
						String type = selectedItem.getText(1);
						if (treeText.equals(tableText)) {
							find = true;
							
							if (type.equals(NodeType.DATABASE.toString())
									|| type.equals(NodeType.CONTAINER
											.toString()))
								dbContainerTreeItemSelected(treeItem, false);
							else if (type.equals(NodeType.SERVER.toString()))
								serverTreeItemSelected(treeItem, false);
							
							history.add(treeItem);
							btnBackward.setEnabled(true);
							btnForward.setEnabled(false);
							break;
						}
					}
					if(!find)
						MessageDialog.openInformation(shlRedisClient, "information", "New key found, please refresh container: "+text.getText());
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
						else if (type == NodeType.SERVER)
							table.setMenu(menuTableServer);
						else if (type == NodeType.DATABASE
								|| type == NodeType.CONTAINER) {
							updateMenuDBContainer(type, menuTableDBContainer);
							table.setMenu(menuTableDBContainer);
						} else {
							table.setMenu(menu_key);
						}

					}
				}
			}
		});
		table.setHeaderVisible(true);

		tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(150);
		tblclmnName.setText("name");
		tblclmnName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnSelected(tblclmnName, OrderBy.NAME);
			}
		});

		tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(150);
		tblclmnType.setText("type");
		tblclmnType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnSelected(tblclmnType, OrderBy.TYPE);
			}
		});

		tblclmnSize = new TableColumn(table, SWT.NONE);
		tblclmnSize.setWidth(100);
		tblclmnSize.setText("size");
		tblclmnSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnSelected(tblclmnSize, OrderBy.SIZE);
			}
		});

	}

	protected void dataItemSelected() {
		menuServer.getItem(1).setEnabled(false);
		menuServer.getItem(2).setEnabled(false);
		menuServer.getItem(3).setEnabled(false);

		menuData.getItem(0).setEnabled(false);
		menuData.getItem(1).setEnabled(true);
		menuData.getItem(2).setEnabled(true);

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
		mntmRefresh_3.setText("refresh");

		return menu_server;
	}

	private Menu initMenuTableServer() {
		Menu menu_server_1 = new Menu(shlRedisClient);

		MenuItem mntmUpdate = new MenuItem(menu_server_1, SWT.NONE);
		mntmUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmUpdate.setText("update");

		MenuItem mntmDelete = new MenuItem(menu_server_1, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete.setText("remove");

		MenuItem mntmProperties_4 = new MenuItem(menu_server_1, SWT.NONE);
		mntmProperties_4.setText("properties");
		
		new MenuItem(menu_server_1, SWT.SEPARATOR);
		
		MenuItem menuItem_2 = new MenuItem(menu_server_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		menuItem_2.setText("find");
		
		MenuItem mntmFindForward = new MenuItem(menu_server_1, SWT.NONE);
		mntmFindForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindForward.setText("find forward");
		
		MenuItem mntmFindBackward = new MenuItem(menu_server_1, SWT.NONE);
		mntmFindBackward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward.setText("find backward");
		
		return menu_server_1;
	}

	private void initMenuNull() {
		menu_null = new Menu(shlRedisClient);

		MenuItem mntmNewConnection = new MenuItem(menu_null, SWT.NONE);
		mntmNewConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addServer();
			}
		});
		mntmNewConnection.setText("add server");
		
		new MenuItem(menu_null, SWT.SEPARATOR);
		
		MenuItem mntmFind = new MenuItem(menu_null, SWT.NONE);
		mntmFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		mntmFind.setText("find");
		
		MenuItem mntmFindNext = new MenuItem(menu_null, SWT.NONE);
		mntmFindNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindNext.setText("find forward");
		
		MenuItem mntmFindBackward_2 = new MenuItem(menu_null, SWT.NONE);
		mntmFindBackward_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward_2.setText("find  backward");
		
		new MenuItem(menu_null, SWT.SEPARATOR);

		MenuItem mntmRefresh = new MenuItem(menu_null, SWT.NONE);
		mntmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				rootTreeItemSelected(true);
			}
		});
		mntmRefresh.setText("refresh");
	}

	private void initMenu() {
		menu = new Menu(shlRedisClient, SWT.BAR);
		shlRedisClient.setMenuBar(menu);

		MenuItem mntmServer = new MenuItem(menu, SWT.CASCADE);
		mntmServer.setText("&Server");

		menuServer = new Menu(mntmServer);
		mntmServer.setMenu(menuServer);

		MenuItem mntmNew = new MenuItem(menuServer, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addServer();
			}
		});
		mntmNew.setText("Add");

		MenuItem mntmEdit = new MenuItem(menuServer, SWT.NONE);
		mntmEdit.setEnabled(false);
		mntmEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmEdit.setText("Update");

		MenuItem mntmDelete_1 = new MenuItem(menuServer, SWT.NONE);
		mntmDelete_1.setEnabled(false);
		mntmDelete_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete_1.setText("Remove");
		
		MenuItem mntmProperties = new MenuItem(menuServer, SWT.NONE);
		mntmProperties.setEnabled(false);
		mntmProperties.setText("Properties");

		new MenuItem(menuServer, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menuServer, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlRedisClient.close();
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmData = new MenuItem(menu, SWT.CASCADE);
		mntmData.setText("Data");

		menuData = new Menu(mntmData);
		mntmData.setMenu(menuData);

		MenuItem mntmAdd = new MenuItem(menuData, SWT.CASCADE);
		mntmAdd.setEnabled(false);
		mntmAdd.setText("New");

		Menu menu_5 = new Menu(mntmAdd);
		mntmAdd.setMenu(menu_5);

		MenuItem mntmString = new MenuItem(menu_5, SWT.NONE);
		mntmString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newString();
			}
		});
		mntmString.setText("String\tAlt+1");
		mntmString.setAccelerator(SWT.ALT+'1');
		mntmString.setImage(strImage);
		

		MenuItem mntmList = new MenuItem(menu_5, SWT.NONE);
		mntmList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newList();
			}
		});
		mntmList.setText("List\tAlt+2");
		mntmList.setAccelerator(SWT.ALT+'2');
		mntmList.setImage(listImage);

		MenuItem mntmSet = new MenuItem(menu_5, SWT.NONE);
		mntmSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSet();
			}
		});
		mntmSet.setText("Set\tAlt+3");
		mntmSet.setAccelerator(SWT.ALT+'3');
		mntmSet.setImage(setImage);

		MenuItem mntmSortset = new MenuItem(menu_5, SWT.NONE);
		mntmSortset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newZSet();
			}
		});
		mntmSortset.setText("Sorted Set\tAlt+4");
		mntmSortset.setAccelerator(SWT.ALT+'4');
		mntmSortset.setImage(zsetImage);
		
		MenuItem mntmHash = new MenuItem(menu_5, SWT.NONE);
		mntmHash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newHash();
			}
		});
		mntmHash.setText("Hash\tAlt+5");
		mntmHash.setAccelerator(SWT.ALT+'5');
		mntmHash.setImage(hashImage);
		
		MenuItem mntmRename_2 = new MenuItem(menuData, SWT.NONE);
		mntmRename_2.setEnabled(false);
		mntmRename_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(itemSelected instanceof TreeItem)
					renameContainer();
				else
					renameKey();
			}
		});
		mntmRename_2.setText("Rename");

		MenuItem mntmDelete_3 = new MenuItem(menuData, SWT.NONE);
		mntmDelete_3.setEnabled(false);
		mntmDelete_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(itemSelected instanceof TreeItem)
					deleteCotainer();
				else {
					NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);
					if(type == NodeType.CONTAINER)
						deleteCotainer();
					else
						deleteKey();
				}
					
			}
		});
		mntmDelete_3.setText("Delete\t Del");
		mntmDelete_3.setAccelerator(SWT.DEL);
		
		MenuItem mntmProperties_2 = new MenuItem(menuData, SWT.NONE);
		mntmProperties_2.setEnabled(false);
		mntmProperties_2.setText("Properties");

		new MenuItem(menuData, SWT.SEPARATOR);

		MenuItem mntmcut = new MenuItem(menuData, SWT.NONE);
		mntmcut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cut();
			}
		});
		mntmcut.setEnabled(false);
		mntmcut.setText("Cut\tCtrl+X");
		mntmcut.setAccelerator(SWT.CTRL + 'X');

		MenuItem mntmCopy = new MenuItem(menuData, SWT.NONE);
		mntmCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copy();
			}
		});
		mntmCopy.setEnabled(false);
		mntmCopy.setText("Copy\tCtrl+C");
		mntmCopy.setAccelerator(SWT.CTRL + 'C');

		MenuItem mntmPaste = new MenuItem(menuData, SWT.NONE);
		mntmPaste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				paste();
			}
		});
		mntmPaste.setEnabled(false);
		mntmPaste.setText("Paste\tCtrl+V");
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
		mntmImport.setText("Import");

		MenuItem mntmExport = new MenuItem(menuData, SWT.NONE);
		mntmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				export();
			}
		});
		mntmExport.setEnabled(false);
		mntmExport.setText("Export");

		new MenuItem(menuData, SWT.SEPARATOR);
		
		MenuItem mntmFind_2 = new MenuItem(menuData, SWT.NONE);
		mntmFind_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		mntmFind_2.setText("Find\tCtrl+F");
		mntmFind_2.setAccelerator(SWT.CTRL + 'F');
		
		MenuItem mntmFindNext_2 = new MenuItem(menuData, SWT.NONE);
		mntmFindNext_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findForward();
			}
		});
		mntmFindNext_2.setText("Find forward\tF3");
		mntmFindNext_2.setAccelerator(SWT.F3);
		
		MenuItem mntmFindBackward_1 = new MenuItem(menuData, SWT.NONE);
		mntmFindBackward_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBackward();
			}
		});
		mntmFindBackward_1.setText("Find backward\tCtrl+F3");
		mntmFindBackward_1.setAccelerator(SWT.CTRL+SWT.F3);
		
		new MenuItem(menuData, SWT.SEPARATOR);

		MenuItem mntmRefresh_1 = new MenuItem(menuData, SWT.NONE);
		mntmRefresh_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeItemSelected(true);
			}
		});
		mntmRefresh_1.setText("Refresh\tF5");
		mntmRefresh_1.setAccelerator(SWT.F5);

		MenuItem mntmFavorites = new MenuItem(menu, SWT.CASCADE);
		mntmFavorites.setText("Favorites");

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
		mntmAdd_Favorite.setText("Add");

		MenuItem mntmOrganize = new MenuItem(menuFavorite, SWT.NONE);
		mntmOrganize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OrganizeFavoriteDialog dialog = new OrganizeFavoriteDialog(
						shlRedisClient, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);

				List<Favorite> favorites = (List<Favorite>) dialog.open();
				if (favorites != null) {
					service3.updateList(favorites);

					removeFavoriteMenuItem();
					addFavoriteMenuItem();
				}

			}
		});
		mntmOrganize.setText("Organize");

		addFavoriteMenuItem();

		MenuItem mntmTool = new MenuItem(menu, SWT.CASCADE);
		mntmTool.setText("Tool");

		Menu menu_6 = new Menu(mntmTool);
		mntmTool.setMenu(menu_6);

		MenuItem mntmSet_2 = new MenuItem(menu_6, SWT.CASCADE);
		mntmSet_2.setText("Set");

		Menu menu_3 = new Menu(mntmSet_2);
		mntmSet_2.setMenu(menu_3);

		MenuItem mntmDiff = new MenuItem(menu_3, SWT.NONE);
		mntmDiff.setText("Difference");

		MenuItem mntmInter = new MenuItem(menu_3, SWT.NONE);
		mntmInter.setText("Intersection");

		MenuItem mntmUnion = new MenuItem(menu_3, SWT.NONE);
		mntmUnion.setText("Union");

		new MenuItem(menu_6, SWT.SEPARATOR);

		MenuItem mntmPubsub = new MenuItem(menu_6, SWT.NONE);
		mntmPubsub.setText("Pub/Sub");

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");

		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);

		MenuItem mntmReportBug = new MenuItem(menu_2, SWT.NONE);
		mntmReportBug.setText("Report bug");

		MenuItem mntmDonation = new MenuItem(menu_2, SWT.NONE);
		mntmDonation.setText("Donation");

		new MenuItem(menu_2, SWT.SEPARATOR);

		MenuItem mntmAbout = new MenuItem(menu_2, SWT.NONE);
		mntmAbout.setText("About");
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
				boolean ok = MessageDialog.openConfirm(shlRedisClient, "find forward", "All result found, find again?");
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
						MessageDialog.openInformation(shlRedisClient, "find results",
								"No match result found!");
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
				boolean ok = MessageDialog.openConfirm(shlRedisClient, "find backward", "All result found, find again?");
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
						MessageDialog.openInformation(shlRedisClient, "find results",
								"No match result found!");
					}
				}
			}
		}
	}

	protected void find() {
		FindKeyDialog dialog = new FindKeyDialog(shlRedisClient, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		FindInfo info = (FindInfo) dialog.open();
		if(info != null) {
			TreeItem treeItem;
			
			ContainerInfo cinfo = new ContainerInfo();
			if (itemSelected instanceof TreeItem) {
				treeItem = (TreeItem) itemSelected;
			} else {
				treeItem = getTreeItemByTableItem((TableItem) itemSelected);
			}

			parseContainer(treeItem, cinfo);
			NodeType searchFrom = (NodeType) treeItem.getData(NODE_TYPE);
			
			Set<Node> nodes = service2.find(searchFrom, cinfo.getId(), cinfo.getDb(), cinfo.getContainer(), info.getSearchNodeType(), info.getPattern(), info.isForward());
			if(!nodes.isEmpty()) {
				
				Node node = nodes.iterator().next();
				TreeItem selected = gotoDBContainer(node.getId(), node.getDb(), node.getKey(), true, true);
				history.add(selected);
				btnBackward.setEnabled(true);
				btnForward.setEnabled(false);
				
				fBuffer = new FindBuffer(node, searchFrom, cinfo.getId(), cinfo.getDb(), cinfo.getContainer(), info.getSearchNodeType(), info.getPattern());
			}else{
				MessageDialog.openInformation(shlRedisClient, "find results",
						"No match result found!");
			}
		}
	}

	private void export() {
		TreeItem treeItem;
		
		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
		}

		parseContainer(treeItem, cinfo);
		
		FileDialog dialog = new FileDialog(shlRedisClient,SWT.SAVE);
		dialog.setText("Export redis data file");
		String[] filterExt = { "*.*" };
		dialog.setFilterExtensions(filterExt);
		String file = dialog.open();
		if(file != null){
			File exportFile = new File(file);
		
			boolean ok = false;
			boolean exist = exportFile.exists();
			if(exist)
				ok = MessageDialog.openConfirm(shlRedisClient, "file exists",
						"File exists, are you sure replace this file?");
			if(!exist || ok) {
				ExportService service = new ExportService(file, cinfo.getId(), cinfo.getDb(), cinfo.getContainer());
				try {
					service.export();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}
	}
	private void importFile() {
		TreeItem treeItem;
		
		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
		}

		parseContainer(treeItem, cinfo);
		
		FileDialog dialog = new FileDialog(shlRedisClient, SWT.OPEN);
		dialog.setText("Import redis data file");
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
		AddServerDialog dialog = new AddServerDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		Server server = (Server) dialog.open();

		if (server != null) {
			server.setId(service1.add(server.getName(), server.getHost(),
					server.getPort()));
			TreeItem item = addServerTreeItem(server);
			serverTreeItemSelected(item, false);
		}
	}

	private void updateServer() {
		int id = (Integer) itemSelected.getData(NODE_ID);

		Server server = service1.listById(id);
		UpdateServerDialog dialog = new UpdateServerDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, server);
		server = (Server) dialog.open();
		if (server != null) {
			service1.update(id, server.getName(), server.getHost(),
					server.getPort());
			itemSelected.setText(server.getName());
			rootTreeItemSelected(true);
		}
	}

	private void removeServer() {
		boolean ok = MessageDialog.openConfirm(shlRedisClient, "remove server",
				"Are you sure remove this server?");
		if (ok) {
			int id = ((Integer) (itemSelected.getData(NODE_ID))).intValue();
			service1.delete(id);
			if (itemSelected instanceof TableItem) {
				getTreeItemByTableItem((TableItem) itemSelected).dispose();
			}
			itemSelected.dispose();
		}
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

		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem)
			treeItem = (TreeItem) itemSelected;
		else
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);

		parseContainer(treeItem, cinfo);

		RenameKeysDialog dialog = new RenameKeysDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainer());
		RenameInfo rinfo = (RenameInfo) dialog.open();

		if (rinfo != null) {
			Set<String> result = service2.renameContainer(cinfo.getId(),
					cinfo.getDb(), cinfo.getContainer(),
					rinfo.getNewContainer(), rinfo.isOverwritten());
			treeItem.getParentItem().setData(ITEM_OPENED, false);
			dbContainerTreeItemSelected(treeItem.getParentItem(), false);
			if (!rinfo.isOverwritten() && result.size() > 0) {
				String failString = "Rename following keys failed because of exist:\n";
				for (String container : result)
					failString += container + "\n";
				MessageDialog.openError(shlRedisClient, "rename keys result",
						failString);
			}
		}
	}

	private void deleteCotainer() {
		boolean ok = MessageDialog.openConfirm(shlRedisClient, "delete keys",
				"Are you sure delete all keys under it?");
		if (ok) {
			TreeItem treeItem;

			ContainerInfo cinfo = new ContainerInfo();
			if (itemSelected instanceof TreeItem)
				treeItem = (TreeItem) itemSelected;
			else
				treeItem = getTreeItemByTableItem((TableItem) itemSelected);

			parseContainer(treeItem, cinfo);

			service2.deleteContainer(cinfo.getId(), cinfo.getDb(),
					cinfo.getContainer());
			if (itemSelected instanceof TableItem) {
				treeItem.dispose();
			}
			itemSelected.dispose();
			treeItemSelected(false);
		}
	}

	private void addFavorite() {
		TreeItem treeItem;
		String fullContainer;
		
		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
			fullContainer = text.getText();
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
			NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);
			if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
				fullContainer = text.getText() + itemSelected.getText() + ":";
			else
				fullContainer = text.getText() + itemSelected.getText();
		}

		parseContainer(treeItem, cinfo);

		AddFavoriteDialog dialog = new AddFavoriteDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, fullContainer);
		String name = (String) dialog.open();
		if (name != null)
			service3.add(cinfo.getId(), name, fullContainer);

		removeFavoriteMenuItem();
		addFavoriteMenuItem();
	}

	private void treeItemSelected(boolean refresh) {
		TreeItem[] items = tree.getSelection();
		NodeType type = (NodeType) items[0].getData(NODE_TYPE);

		if(itemSelected == items[0] && !refresh)
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
		this.itemSelected = itemSelected;
		tree.setSelection(itemSelected);
		ContainerInfo info = new ContainerInfo();
		parseContainer(itemSelected, info);
		String container = (info.getContainer() == null) ? "" : info
				.getContainer();
		text.setText(info.getServerName() + ":" + DB_PREFIX + info.getDb()
				+ ":" + container);

		dbContainerItemSelected(itemSelected);

		Set<Node> cnodes = service2.listContainers(info.getId(), info.getDb(),
				info.getContainer());

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
		

		tableItemOrderSelected(info, Order.Ascend, OrderBy.NAME);
		table.setSortColumn(null);
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

	private void tableItemOrderSelected(ContainerInfo info, Order order,
			OrderBy orderBy) {
		Set<Node> cnodes = service2.listContainers(info.getId(), info.getDb(),
				info.getContainer(), order);

		table.removeAll();
		for (Node node : cnodes) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { node.getKey(),
					node.getType().toString() });
			item.setImage(containerImage);
			item.setData(NODE_TYPE, node.getType());
		}

		Set<DataNode> knodes = service2.listContainerKeys(info.getId(),
				info.getDb(), info.getContainer(), order, orderBy);

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
		itemSelected = rootRedisServers;
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
		menuData.getItem(5).setEnabled(false);
		menuData.getItem(6).setEnabled(false);
		menuData.getItem(7).setEnabled(false);
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(false);
		
		menuFavorite.getItem(0).setEnabled(false);
	}

	private void serverTreeItemSelected(TreeItem selectedItem, boolean refresh) {
		this.itemSelected = selectedItem;
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
		menuData.getItem(5).setEnabled(false);
		menuData.getItem(6).setEnabled(false);
		menuData.getItem(7).setEnabled(false);
		menuData.getItem(9).setEnabled(false);
		menuData.getItem(10).setEnabled(false);
		
		menuFavorite.getItem(0).setEnabled(false);
	}

	private void newString() {
		TreeItem treeItem;

		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem)
			treeItem = (TreeItem) itemSelected;
		else
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);

		parseContainer(treeItem, cinfo);

		NewStringDialog dialog = new NewStringDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainer());
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

		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem)
			treeItem = (TreeItem) itemSelected;
		else
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);

		parseContainer(treeItem, cinfo);

		NewListDialog dialog = new NewListDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainer());
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

		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem)
			treeItem = (TreeItem) itemSelected;
		else
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);

		parseContainer(treeItem, cinfo);

		NewSetDialog dialog = new NewSetDialog(shlRedisClient, SWT.SHELL_TRIM
				| SWT.APPLICATION_MODAL, cinfo.getServerName(), cinfo.getDb(),
				cinfo.getContainer());
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

		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem)
			treeItem = (TreeItem) itemSelected;
		else
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);

		parseContainer(treeItem, cinfo);

		NewZSetDialog dialog = new NewZSetDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainer());
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

		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem)
			treeItem = (TreeItem) itemSelected;
		else
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);

		parseContainer(treeItem, cinfo);

		NewHashDialog dialog = new NewHashDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainer());
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

				ContainerInfo info = new ContainerInfo();
				parseContainer(items[0], info);

				tableItemOrderSelected(info, (Order) tblclmn.getData(ORDER),
						orderBy);
			}
		}
	}

	private void renameKey() {
		ContainerInfo cinfo = new ContainerInfo();
		TreeItem[] items = tree.getSelection();

		parseContainer(items[0], cinfo);

		String key = cinfo.getContainer() == null?"": cinfo.getContainer();
		key += itemSelected.getText();
		
		RenameKeysDialog dialog = new RenameKeysDialog(shlRedisClient,
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), key);
		RenameInfo rinfo = (RenameInfo) dialog.open();

		if (rinfo != null) {
			boolean result = service2.renameKey(cinfo.getId(),
					cinfo.getDb(), key,
					rinfo.getNewContainer(), rinfo.isOverwritten());
			dbContainerTreeItemSelected(items[0], false);
			
			if (!rinfo.isOverwritten() && !result) {
				String failString = "Rename key failed because of exist";
				MessageDialog.openError(shlRedisClient, "rename keys result",
						failString);
			}
		}
	}

	private void deleteKey() {
		boolean ok = MessageDialog.openConfirm(shlRedisClient, "delete key",
				"Are you sure delete this key?");
		if (ok) {
			ContainerInfo cinfo = new ContainerInfo();
			TreeItem[] items = tree.getSelection();

			parseContainer(items[0], cinfo);

			String key = cinfo.getContainer() == null?"": cinfo.getContainer();
			key += itemSelected.getText();

			service2.deleteKey(cinfo.getId(), cinfo.getDb(), key);
			itemSelected.dispose();
			
			treeItemSelected(false);
		}
	}
	
	private void cut() {
		TreeItem treeItem;
		
		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
		}

		parseContainer(treeItem, cinfo);

		if(itemSelected instanceof TreeItem)
			pBuffer.cut(cinfo, treeItem);
		else {
			NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);
			if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
				pBuffer.cut(cinfo, treeItem);
			else
				pBuffer.cut(cinfo, itemSelected.getText(), treeItem);
		}
	}
	
	private void copy() {
		TreeItem treeItem;
		
		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
		}

		parseContainer(treeItem, cinfo);

		if(itemSelected instanceof TreeItem)
			pBuffer.copy(cinfo);
		else {
			NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);
			if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
				pBuffer.copy(cinfo);
			else
				pBuffer.copy(cinfo, itemSelected.getText());
		}
	}
	
	private void paste() {
		TreeItem treeItem;
		
		ContainerInfo target = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
		}

		parseContainer(treeItem, target);

		ContainerInfo source = pBuffer.paste();
		
		if(!pBuffer.isCopy() && ! pBuffer.isKey()){
			 pBuffer.getCutItem().dispose();
		}
		
		if(pBuffer.isKey()){
			String newKey = service2.pasteKey(source.getId(), source.getDb(), source.getContainer()+pBuffer.getKey(), target.getId(), target.getDb(), target.getContainer()+pBuffer.getKey(), pBuffer.isCopy(), true);
			if(newKey == null)
				gotoDBContainer(target.getId(), target.getDb(), target.getContainer() + pBuffer.getKey(), true, true);
			else
				gotoDBContainer(target.getId(), target.getDb(), newKey, true, true);
		}else{
			service2.pasteContainer(source.getId(), source.getDb(), source.getContainer(), target.getId(), target.getDb(), target.getContainer(), pBuffer.isCopy(), true);
			gotoDBContainer(target.getId(), target.getDb(), target.getContainer(), false, true);
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

	private TreeItem gotoDBContainer(int id, int db, String container, boolean isKey, boolean refresh) {
		rootTreeItemSelected(false);
		TreeItem dbItem = findDBTreeItem(id, db);
		TreeItem dataItemSelected = dbItem;
		
		if(dbItem != null) {
			dbContainerTreeItemSelected(dbItem,	refresh);
			TreeItem[] dataItems = dbItem.getItems();
						
			String[] containers = container.split(":");
			if(!isKey){
				for (int i = 0; i < containers.length; i++) {
					for (TreeItem dataItem : dataItems) {
						if (dataItem.getText().equals(containers[i])) {
							tree.setSelection(dataItem);
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
						dataItemSelected();
						break;
					}
						
				}
			}
		}

		return dataItemSelected;
	}
	
	private TreeItem findServerTreeItem(int id){
		TreeItem[] treeItems = rootRedisServers.getItems();
		for (TreeItem treeItem : treeItems) {
			int serverId = (Integer) treeItem.getData(NODE_ID);
			if (serverId == id) 
				return treeItem;
		}
		return null;
	}
	
	private TreeItem findDBTreeItem(int id, int db) {
		TreeItem server = findServerTreeItem(id);
		if(server == null)
			return null;
		else {
			serverTreeItemSelected(server, false);
			TreeItem[] dbItems = server.getItems();
			for (TreeItem dbItem : dbItems) {
				if (dbItem.getText().equals(DB_PREFIX+db)) 
					return dbItem;
			}
			return null;
		}
	}
}
