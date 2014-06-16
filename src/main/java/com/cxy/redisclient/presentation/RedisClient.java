package com.cxy.redisclient.presentation;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
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
import com.cxy.redisclient.presentation.key.RenameKeysDialog;
import com.cxy.redisclient.presentation.list.NewListDialog;
import com.cxy.redisclient.presentation.server.AddServerDialog;
import com.cxy.redisclient.presentation.server.UpdateServerDialog;
import com.cxy.redisclient.presentation.set.NewSetDialog;
import com.cxy.redisclient.presentation.string.NewStringDialog;
import com.cxy.redisclient.presentation.zset.NewZSetDialog;
import com.cxy.redisclient.service.FavoriteService;
import com.cxy.redisclient.service.HashService;
import com.cxy.redisclient.service.ListService;
import com.cxy.redisclient.service.NodeService;
import com.cxy.redisclient.service.ServerService;
import com.cxy.redisclient.service.SetService;
import com.cxy.redisclient.service.ZSetService;

public class RedisClient {
	private Shell shlRedisClient;

	private Item itemSelected;

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
	private Menu menu_Data;
	private Menu menu_favorite;

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

	private TableColumn tblclmnName;
	private TableColumn tblclmnType;
	private TableColumn tblclmnSize;

	private MenuItem mntmEdit;
	private MenuItem mntmDelete_1;
	private MenuItem mntmProperties;

	private MenuItem mntmAdd;
	private MenuItem mntmRename_2;
	private MenuItem mntmDelete_3;

	private MenuItem mntmAdd_Favorite;

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
				MessageDialog
						.openError(shlRedisClient, "Error", e.getMessage());
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

		initSash();

	}

	private void initImage() {
		redisImage = new Image(tree.getDisplay(), getClass()
				.getResourceAsStream("/redis.png"));
		dbImage = new Image(tree.getDisplay(), getClass().getResourceAsStream(
				"/db.png"));
		strImage = new Image(tree.getDisplay(), getClass().getResourceAsStream(
				"/string.png"));
		setImage = new Image(tree.getDisplay(), getClass().getResourceAsStream(
				"/set.png"));
		listImage = new Image(tree.getDisplay(), getClass()
				.getResourceAsStream("/list.png"));
		zsetImage = new Image(tree.getDisplay(), getClass()
				.getResourceAsStream("/zset.png"));
		hashImage = new Image(tree.getDisplay(), getClass()
				.getResourceAsStream("/hash.png"));
		containerImage = new Image(tree.getDisplay(), getClass()
				.getResourceAsStream("/container.png"));
	}

	private void initShell() {
		shlRedisClient = new Shell();
		shlRedisClient.setSize(852, 624);
		shlRedisClient.setText("Redis client");
		shlRedisClient.setLayout(new FillLayout(SWT.HORIZONTAL));
	}

	private void initSash() {
		SashForm sashForm_2 = new SashForm(shlRedisClient, SWT.SMOOTH
				| SWT.VERTICAL);
		sashForm_2.setSashWidth(0);

		text = new Text(sashForm_2, SWT.BORDER | SWT.SEARCH);
		text.setEditable(false);
		SashForm sashForm = new SashForm(sashForm_2, SWT.NONE);

		initTree(sashForm);

		initImage();

		initRootItem();

		initMenuData();

		menuTreeDBContainer = initMenuTreeDB();

		menuTableDBContainer = initMenuTableDB();

		SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

		initTable(sashForm_1);

		sashForm_1.setWeights(new int[] { 1 });

		sashForm.setWeights(new int[] { 1, 3 });
		sashForm_2.setWeights(new int[] { 22, 368 });

		initMenuNull();

		menuTreeServer = initMenuTreeServer();
		menuTableServer = initMenuTableServer();

		initServers();
	}

	private void initMenuData() {
		menu_Data = new Menu(shlRedisClient);

		MenuItem mntmRename = new MenuItem(menu_Data, SWT.NONE);
		mntmRename.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameKey();
			}
		});
		mntmRename.setText("rename");

		MenuItem mntmDelete_4 = new MenuItem(menu_Data, SWT.NONE);
		mntmDelete_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteKey();
			}
		});
		mntmDelete_4.setText("delete");
		
				MenuItem mntmProperties_1 = new MenuItem(menu_Data, SWT.NONE);
				mntmProperties_1.setText("properties");

		new MenuItem(menu_Data, SWT.SEPARATOR);

		MenuItem menuItem = new MenuItem(menu_Data, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		menuItem.setText("add to favorites");

		new MenuItem(menu_Data, SWT.SEPARATOR);

		MenuItem mntmCut_1 = new MenuItem(menu_Data, SWT.NONE);
		mntmCut_1.setText("cut");

		MenuItem mntmCopy_2 = new MenuItem(menu_Data, SWT.NONE);
		mntmCopy_2.setText("copy");

		MenuItem mntmPaste_2 = new MenuItem(menu_Data, SWT.NONE);
		mntmPaste_2.setText("paste");
	}

	private void initTree(SashForm sashForm) {
		tree = new Tree(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeItemSelected(false);
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				Point point = new Point(arg0.x, arg0.y);
				TreeItem selectedItem = tree.getItem(point);
				if (arg0.button == 3) {
					if (selectedItem == null)
						tree.setMenu(menu_null);
					else {
						itemSelected = selectedItem;

						NodeType type = (NodeType) selectedItem
								.getData(NODE_TYPE);

						if (type == NodeType.ROOT)
							tree.setMenu(menu_null);
						else if (type == NodeType.SERVER)
							tree.setMenu(menuTreeServer);
						else if (type == NodeType.DATABASE
								|| type == NodeType.CONTAINER) {
							if (type == NodeType.DATABASE) {
								menuTreeDBContainer.getItem(1)
										.setEnabled(false);
								menuTreeDBContainer.getItem(2)
										.setEnabled(false);
							} else {
								menuTreeDBContainer.getItem(1).setEnabled(true);
								menuTreeDBContainer.getItem(2).setEnabled(true);
							}
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
		Menu menu_DbContainer = new Menu(shlRedisClient);

		MenuItem mntmNew_1 = new MenuItem(menu_DbContainer, SWT.CASCADE);
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

		MenuItem menuItem_2 = new MenuItem(menu_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newList();
			}
		});
		menuItem_2.setText("List");

		MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSet();
			}
		});
		menuItem_3.setText("Set");

		MenuItem mntmSortedSet = new MenuItem(menu_1, SWT.NONE);
		mntmSortedSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newZSet();
			}
		});
		mntmSortedSet.setText("Sorted Set");

		MenuItem mntmHash_1 = new MenuItem(menu_1, SWT.NONE);
		mntmHash_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newHash();
			}
		});
		mntmHash_1.setText("Hash");

		MenuItem mntmRename_1 = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmRename_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameContainer();
			}
		});
		mntmRename_1.setText("rename");

		MenuItem mntmDelete_2 = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmDelete_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteCotainer();
			}
		});
		mntmDelete_2.setText("delete");
		
		MenuItem mntmProperties_3 = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmProperties_3.setText("properties");

		new MenuItem(menu_DbContainer, SWT.SEPARATOR);

		MenuItem mntmAddToFavorites = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmAddToFavorites.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		mntmAddToFavorites.setText("add to favorites");

		new MenuItem(menu_DbContainer, SWT.SEPARATOR);

		MenuItem mntmCut = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmCut.setText("cut");

		MenuItem mntmCopy_1 = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmCopy_1.setText("copy");

		MenuItem mntmPaste_1 = new MenuItem(menu_DbContainer, SWT.NONE);
		mntmPaste_1.setEnabled(false);
		mntmPaste_1.setText("paste");

		return menu_DbContainer;
	}

	private void initTable(SashForm sashForm_1) {
		table = new Table(sashForm_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				itemSelected = items[0];
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

				if (treeItems.length > 0 && selectedItem != null) {
					for (TreeItem treeItem : treeItems[0].getItems()) {
						String treeText = treeItem.getText();
						String tableText = selectedItem.getText(0);
						String type = selectedItem.getText(1);
						if (treeText.equals(tableText)) {
							if (type.equals(NodeType.DATABASE.toString())
									|| type.equals(NodeType.CONTAINER
											.toString()))
								dbContainerTreeItemSelected(treeItem, false);
							else if (type.equals(NodeType.SERVER.toString()))
								serverTreeItemSelected(treeItem, false);
							break;
						}

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
						String type = selectedItem.getText(1);

						if (type == NodeType.ROOT.toString())
							table.setMenu(menu_null);
						else if (type == NodeType.SERVER.toString())
							table.setMenu(menuTableServer);
						else if (type == NodeType.DATABASE.toString()
								|| type == NodeType.CONTAINER.toString()) {
							if (type == NodeType.DATABASE.toString()) {
								menuTableDBContainer.getItem(1).setEnabled(
										false);
								menuTableDBContainer.getItem(2).setEnabled(
										false);
							} else {
								menuTableDBContainer.getItem(1)
										.setEnabled(true);
								menuTableDBContainer.getItem(2)
										.setEnabled(true);
							}
							table.setMenu(menuTableDBContainer);
						} else {
							table.setMenu(menu_Data);
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
		mntmEdit.setEnabled(false);
		mntmDelete_1.setEnabled(false);
		mntmProperties.setEnabled(false);

		mntmAdd.setEnabled(false);
		mntmRename_2.setEnabled(true);
		mntmDelete_3.setEnabled(true);

		mntmAdd_Favorite.setEnabled(true);
		
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
		Menu menu_server = new Menu(shlRedisClient);

		MenuItem mntmUpdate = new MenuItem(menu_server, SWT.NONE);
		mntmUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmUpdate.setText("update");

		MenuItem mntmDelete = new MenuItem(menu_server, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete.setText("remove");

		MenuItem mntmProperties_4 = new MenuItem(menu_server, SWT.NONE);
		mntmProperties_4.setText("properties");
		
		return menu_server;
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

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("&Server");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmNew = new MenuItem(menu_1, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addServer();
			}
		});
		mntmNew.setText("Add");

		mntmEdit = new MenuItem(menu_1, SWT.NONE);
		mntmEdit.setEnabled(false);
		mntmEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmEdit.setText("Update");

		mntmDelete_1 = new MenuItem(menu_1, SWT.NONE);
		mntmDelete_1.setEnabled(false);
		mntmDelete_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete_1.setText("Remove");
		
				mntmProperties = new MenuItem(menu_1, SWT.NONE);
				mntmProperties.setEnabled(false);
				mntmProperties.setText("Properties");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlRedisClient.close();
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmData = new MenuItem(menu, SWT.CASCADE);
		mntmData.setText("Data");

		Menu menu_4 = new Menu(mntmData);
		mntmData.setMenu(menu_4);

		mntmAdd = new MenuItem(menu_4, SWT.CASCADE);
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
		mntmString.setText("String");

		MenuItem mntmList = new MenuItem(menu_5, SWT.NONE);
		mntmList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newList();
			}
		});
		mntmList.setText("List");

		MenuItem mntmSet = new MenuItem(menu_5, SWT.NONE);
		mntmSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSet();
			}
		});
		mntmSet.setText("Set");

		MenuItem mntmSortset = new MenuItem(menu_5, SWT.NONE);
		mntmSortset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newZSet();
			}
		});
		mntmSortset.setText("Sorted Set");

		MenuItem mntmHash = new MenuItem(menu_5, SWT.NONE);
		mntmHash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newHash();
			}
		});
		mntmHash.setText("Hash");

		mntmRename_2 = new MenuItem(menu_4, SWT.NONE);
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

		mntmDelete_3 = new MenuItem(menu_4, SWT.NONE);
		mntmDelete_3.setEnabled(false);
		mntmDelete_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(itemSelected instanceof TreeItem)
					deleteCotainer();
				else
					deleteKey();
			}
		});
		mntmDelete_3.setText("Delete");
		
		MenuItem mntmProperties_2 = new MenuItem(menu_4, SWT.NONE);
		mntmProperties_2.setEnabled(false);
		mntmProperties_2.setText("Properties");

		new MenuItem(menu_4, SWT.SEPARATOR);

		MenuItem mntmcut = new MenuItem(menu_4, SWT.NONE);
		mntmcut.setEnabled(false);
		mntmcut.setText("&Cut");

		MenuItem mntmCopy = new MenuItem(menu_4, SWT.NONE);
		mntmCopy.setEnabled(false);
		mntmCopy.setText("C&opy");

		MenuItem mntmPaste = new MenuItem(menu_4, SWT.NONE);
		mntmPaste.setEnabled(false);
		mntmPaste.setText("&Paste");

		new MenuItem(menu_4, SWT.SEPARATOR);

		MenuItem mntmFind = new MenuItem(menu_4, SWT.NONE);
		mntmFind.setText("Find");

		MenuItem mntmReplace = new MenuItem(menu_4, SWT.NONE);
		mntmReplace.setText("Find next");

		new MenuItem(menu_4, SWT.SEPARATOR);

		MenuItem mntmImport = new MenuItem(menu_4, SWT.NONE);
		mntmImport.setEnabled(false);
		mntmImport.setText("Import");

		MenuItem mntmExport = new MenuItem(menu_4, SWT.NONE);
		mntmExport.setEnabled(false);
		mntmExport.setText("Export");

		new MenuItem(menu_4, SWT.SEPARATOR);

		MenuItem mntmRefresh_1 = new MenuItem(menu_4, SWT.NONE);
		mntmRefresh_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeItemSelected(true);
			}
		});
		mntmRefresh_1.setText("Refresh");

		MenuItem mntmFavorites = new MenuItem(menu, SWT.CASCADE);
		mntmFavorites.setText("Favorites");

		menu_favorite = new Menu(mntmFavorites);
		mntmFavorites.setMenu(menu_favorite);

		mntmAdd_Favorite = new MenuItem(menu_favorite, SWT.NONE);
		mntmAdd_Favorite.setEnabled(false);
		mntmAdd_Favorite.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addFavorite();
			}
		});
		mntmAdd_Favorite.setText("Add");

		MenuItem mntmOrganize = new MenuItem(menu_favorite, SWT.NONE);
		mntmOrganize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OrganizeFavoriteDialog dialog = new OrganizeFavoriteDialog(
						shlRedisClient, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

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

	private void removeFavoriteMenuItem() {
		int num = menu_favorite.getItemCount();
		if (num > 2) {
			MenuItem[] items = menu_favorite.getItems();
			for (int i = 2; i < num; i++) {
				items[i].dispose();
			}
		}

	}

	private void addFavoriteMenuItem() {
		List<Favorite> favorites = service3.listAll();
		if (favorites.size() > 0) {
			new MenuItem(menu_favorite, SWT.SEPARATOR);
			for (Favorite favorite : favorites) {
				final MenuItem menuItem = new MenuItem(menu_favorite, SWT.NONE);
				menuItem.setText(favorite.getName());
				menuItem.setData(FAVORITE, favorite);
				menuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Favorite favorite = (Favorite) menuItem
								.getData(FAVORITE);
						int sid = favorite.getServerID();

						rootTreeItemSelected(false);
						TreeItem[] treeItems = rootRedisServers.getItems();
						for (TreeItem treeItem : treeItems) {
							int serverId = (Integer) treeItem.getData(NODE_ID);
							if (serverId == sid) {
								serverTreeItemSelected(treeItem, false);
								String[] containers = favorite.getFavorite()
										.split(":");
								TreeItem[] dbItems = treeItem.getItems();
								for (TreeItem dbItem : dbItems) {
									if (dbItem.getText().equals(containers[1])) {
										tree.setSelection(dbItem);
										dbContainerTreeItemSelected(dbItem,
												false);
										TreeItem[] dataItems = dbItem
												.getItems();
										if(!favorite.isData()){
											for (int i = 2; i < containers.length; i++) {
												for (TreeItem dataItem : dataItems) {
													if (dataItem.getText().equals(containers[i])) {
														tree.setSelection(dataItem);
														dbContainerTreeItemSelected(dataItem, false);
														dataItems = dataItem.getItems();
														break;
													}
												}
											}
										} else {
											for (int i = 2; i < containers.length - 1; i++) {
												for (TreeItem dataItem : dataItems) {
													if (dataItem.getText().equals(containers[i])) {
														tree.setSelection(dataItem);
														dbContainerTreeItemSelected(dataItem, false);
														dataItems = dataItem.getItems();
														break;
													}
												}
											}
											TableItem[] tableItems = table.getItems();
											for(TableItem tableItem : tableItems) {
												NodeType type = (NodeType) tableItem.getData(NODE_TYPE);
												if(type != NodeType.SERVER && type != NodeType.DATABASE && type != NodeType.CONTAINER && tableItem.getText().equals(containers[containers.length -1])){
													table.setSelection(tableItem);
													break;
												}
													
											}
										}
									}
								}

							}
						}

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

	private void addDBTreeItem(int server, TreeItem serverItem) {
		int amount = service1.listDBs(server);
		for (int i = 0; i < amount; i++) {
			TreeItem dbItem = new TreeItem(serverItem, SWT.NONE);
			dbItem.setText(DB_PREFIX + i);
			dbItem.setData(NODE_ID, i);
			dbItem.setData(NODE_TYPE, NodeType.DATABASE);
			dbItem.setImage(dbImage);
		}
		if (amount > 0) {
			serverItem.setExpanded(true);
			serverItem.setData(ITEM_OPENED, true);
		}
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, server);
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
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

		}
	}

	private void addFavorite() {
		TreeItem treeItem;
		String container;
		
		ContainerInfo cinfo = new ContainerInfo();
		if (itemSelected instanceof TreeItem) {
			treeItem = (TreeItem) itemSelected;
			container = text.getText();
		} else {
			treeItem = getTreeItemByTableItem((TableItem) itemSelected);
			NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);
			if(type == NodeType.CONTAINER || type == NodeType.DATABASE)
				container = text.getText() + itemSelected.getText() + ":";
			else
				container = text.getText() + itemSelected.getText();
		}

		parseContainer(treeItem, cinfo);

		AddFavoriteDialog dialog = new AddFavoriteDialog(shlRedisClient,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, container);
		String name = (String) dialog.open();
		if (name != null)
			service3.add(cinfo.getId(), name, container);

		removeFavoriteMenuItem();
		addFavoriteMenuItem();
	}

	private void treeItemSelected(boolean refresh) {
		TreeItem[] items = tree.getSelection();
		NodeType type = (NodeType) items[0].getData(NODE_TYPE);

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

		if (refresh)
			itemSelected.setData(ITEM_OPENED, false);

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
		}

		tableItemOrderSelected(info, Order.Ascend, OrderBy.NAME);
		table.setSortColumn(null);
	}

	private void dbContainerItemSelected(Item itemSelected) {
		mntmEdit.setEnabled(false);
		mntmDelete_1.setEnabled(false);
		mntmProperties.setEnabled(false);

		NodeType type = (NodeType) itemSelected.getData(NODE_TYPE);

		mntmAdd.setEnabled(true);
		if (type == NodeType.CONTAINER) {
			mntmRename_2.setEnabled(true);
			mntmDelete_3.setEnabled(true);
		} else {
			mntmRename_2.setEnabled(false);
			mntmDelete_3.setEnabled(false);
		}

		mntmAdd_Favorite.setEnabled(true);
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

		if (refresh)
			rootRedisServers.setData(ITEM_OPENED, false);

		if (rootRedisServers.getData(ITEM_OPENED) == null
				|| ((Boolean) (rootRedisServers.getData(ITEM_OPENED)) == false)) {
			rootRedisServers.removeAll();
			initServers();

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
		mntmEdit.setEnabled(false);
		mntmDelete_1.setEnabled(false);
		mntmProperties.setEnabled(false);

		mntmAdd.setEnabled(false);
		mntmRename_2.setEnabled(false);
		mntmDelete_3.setEnabled(false);

		mntmAdd_Favorite.setEnabled(false);
	}

	private void serverTreeItemSelected(TreeItem selectedItem, boolean refresh) {
		this.itemSelected = selectedItem;
		tree.setSelection(selectedItem);
		text.setText(selectedItem.getText() + ":");
		table.removeAll();

		serverItemSelected();

		if (refresh)
			selectedItem.setData(ITEM_OPENED, false);

		if (selectedItem.getData(ITEM_OPENED) == null
				|| ((Boolean) (selectedItem.getData(ITEM_OPENED)) == false)) {
			selectedItem.removeAll();
			addDBTreeItem((Integer) selectedItem.getData(NODE_ID), selectedItem);
		}

		int dbs = service1.listDBs((Integer) selectedItem.getData(NODE_ID));
		for (int i = 0; i < dbs; i++) {
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
		mntmEdit.setEnabled(true);
		mntmDelete_1.setEnabled(true);
		mntmProperties.setEnabled(true);

		mntmAdd.setEnabled(false);
		mntmRename_2.setEnabled(false);
		mntmDelete_3.setEnabled(false);

		mntmAdd_Favorite.setEnabled(false);
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
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

		NewSetDialog dialog = new NewSetDialog(shlRedisClient, SWT.DIALOG_TRIM
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
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
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
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

		}
	}
}
