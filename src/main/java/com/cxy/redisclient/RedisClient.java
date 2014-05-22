package com.cxy.redisclient;

import java.io.IOException;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.dto.ContainerInfo;
import com.cxy.redisclient.dto.StringInfo;
import com.cxy.redisclient.service.NodeService;
import com.cxy.redisclient.service.ServerService;

public class RedisClient {

	private static final String DB_PREFIX = "db";
	private static final String NODE_TYPE = "type";
	private static final String NODE_ID = "id";
	private static final String ITEM_OPENED = "open";
	protected Shell shlRedisClient;
	protected Menu menu_server;
	protected Menu menu;
	protected Tree tree;
	protected Menu menu_null;
	protected ServerService service1 = new ServerService();
	protected NodeService service2 = new NodeService();
	private Table table;
	private Text text;
	private Menu menu_DB;
	private MenuItem mntmRename_1;
	private MenuItem mntmDelete_2;
	private MenuItem mntmRename_2;
	private MenuItem mntmDelete_3;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RedisClient window = new RedisClient();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * 
	 * @throws IOException
	 */
	public void open() {
		Display display = Display.getDefault();
		try {
			createContents();
		} catch (IOException e) {
			MessageDialog
					.openError(shlRedisClient, "exception", e.getMessage());
		}
		shlRedisClient.open();
		shlRedisClient.layout();
		while (!shlRedisClient.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Create contents of the window.
	 * 
	 * @throws IOException
	 */
	protected void createContents() throws IOException {
		initShell();

		initMenu();

		initSash();

	}

	private void initShell() {
		shlRedisClient = new Shell();
		shlRedisClient.setSize(577, 439);
		shlRedisClient.setText("Redis client");
		shlRedisClient.setLayout(new FillLayout(SWT.HORIZONTAL));
	}

	private void initSash() throws IOException {

		SashForm sashForm_2 = new SashForm(shlRedisClient, SWT.SMOOTH
				| SWT.VERTICAL);
		sashForm_2.setSashWidth(0);

		text = new Text(sashForm_2, SWT.BORDER | SWT.SEARCH);
		text.setEditable(false);
		SashForm sashForm = new SashForm(sashForm_2, SWT.NONE);

		tree = new Tree(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		initMenuDB();

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				NodeType type = (NodeType) items[0].getData(NODE_TYPE);
				switch (type) {
				case SERVER:
					serverItemSelected(items[0]);

					break;
				case DATABASE:
					treeItemSelected(items[0]);
					break;
				case CONTAINER:
					treeItemSelected(items[0]);
					break;
				default:
					break;
				}
			}
		});

		SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

		initTable(sashForm_1);

		TabFolder tabFolder = new TabFolder(sashForm_1, SWT.NONE);

		sashForm_1.setWeights(new int[] { 1, 1 });

		sashForm.setWeights(new int[] { 1, 3 });
		sashForm_2.setWeights(new int[] { 22, 368 });

		initMenuNull();

		initMenuServer();

		initServers();
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

	private void initMenuDB() {
		menu_DB = new Menu(tree);

		MenuItem mntmNew_1 = new MenuItem(menu_DB, SWT.CASCADE);
		mntmNew_1.setText("new");

		Menu menu_1 = new Menu(mntmNew_1);
		mntmNew_1.setMenu(menu_1);

		MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				addStringSelected(items[0]);
			}
		});
		menuItem_1.setText("String");

		MenuItem menuItem_2 = new MenuItem(menu_1, SWT.NONE);
		menuItem_2.setText("List");

		MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
		menuItem_3.setText("Set");

		MenuItem mntmSortedSet = new MenuItem(menu_1, SWT.NONE);
		mntmSortedSet.setText("Sorted Set");

		MenuItem mntmHash_1 = new MenuItem(menu_1, SWT.NONE);
		mntmHash_1.setText("Hash");

		mntmRename_1 = new MenuItem(menu_DB, SWT.NONE);
		mntmRename_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				renameContainerSelected(items[0]);
			}
		});
		mntmRename_1.setText("rename");

		mntmDelete_2 = new MenuItem(menu_DB, SWT.NONE);
		mntmDelete_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				deleteKeysSelected(items[0]);
			}
		});
		mntmDelete_2.setText("delete");
	}

	private void initTable(SashForm sashForm_1) {
		table = new Table(sashForm_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem[] tableItems = table.getSelection();
				TreeItem[] treeItems = tree.getSelection();

				if (treeItems.length > 0) {
					for (TreeItem treeItem : treeItems[0].getItems()) {
						String treeText = treeItem.getText();
						String tableText = tableItems[0].getText(0);
						if (treeText.equals(tableText)) {
							treeItemSelected(treeItem);
							break;
						}

					}
				}
			}
		});
		table.setHeaderVisible(true);

		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("name");

		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(100);
		tblclmnType.setText("type");

		TableColumn tblclmnSize = new TableColumn(table, SWT.NONE);
		tblclmnSize.setWidth(100);
		tblclmnSize.setText("size");
	}

	private void initMenuServer() {
		menu_server = new Menu(tree);

		MenuItem mntmRename = new MenuItem(menu_server, SWT.NONE);
		mntmRename.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmRename.setText("update");

		final MenuItem mntmDelete = new MenuItem(menu_server, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete.setText("remove");
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				Point point = new Point(arg0.x, arg0.y);
				TreeItem selectedItem = tree.getItem(point);
				if (arg0.button == 3) {
					if (selectedItem == null)
						tree.setMenu(menu_null);
					else {
						NodeType type = (NodeType) selectedItem
								.getData(NODE_TYPE);

						if (type == NodeType.SERVER)
							tree.setMenu(menu_server);
						else if (type == NodeType.DATABASE
								|| type == NodeType.CONTAINER) {
							if (type == NodeType.DATABASE) {
								mntmRename_1.setEnabled(false);
								mntmDelete_2.setEnabled(false);
							} else {
								mntmRename_1.setEnabled(true);
								mntmDelete_2.setEnabled(true);
							}
							tree.setMenu(menu_DB);
						}

					}
				}
			}
		});
	}

	private void initMenuNull() {
		menu_null = new Menu(tree);

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
				refreshTree();
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

		MenuItem mntmEdit = new MenuItem(menu_1, SWT.NONE);
		mntmEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateServer();
			}
		});
		mntmEdit.setText("Update");

		MenuItem mntmDelete_1 = new MenuItem(menu_1, SWT.NONE);
		mntmDelete_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeServer();
			}
		});
		mntmDelete_1.setText("Remove");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlRedisClient.close();
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmedit = new MenuItem(menu, SWT.CASCADE);
		mntmedit.setText("&Edit");

		Menu menu_3 = new Menu(mntmedit);
		mntmedit.setMenu(menu_3);

		MenuItem mntmcut = new MenuItem(menu_3, SWT.NONE);
		mntmcut.setText("&Cut");

		MenuItem mntmCopy = new MenuItem(menu_3, SWT.NONE);
		mntmCopy.setText("C&opy");

		MenuItem mntmPaste = new MenuItem(menu_3, SWT.NONE);
		mntmPaste.setText("&Paste");

		new MenuItem(menu_3, SWT.SEPARATOR);

		MenuItem mntmFind = new MenuItem(menu_3, SWT.NONE);
		mntmFind.setText("Find");

		MenuItem mntmReplace = new MenuItem(menu_3, SWT.NONE);
		mntmReplace.setText("Replace");

		new MenuItem(menu_3, SWT.SEPARATOR);

		MenuItem mntmOptions = new MenuItem(menu_3, SWT.NONE);
		mntmOptions.setText("Options");

		MenuItem mntmTool = new MenuItem(menu, SWT.CASCADE);
		mntmTool.setText("Data");

		Menu menu_4 = new Menu(mntmTool);
		mntmTool.setMenu(menu_4);

		MenuItem mntmAdd = new MenuItem(menu_4, SWT.CASCADE);
		mntmAdd.setText("New");

		Menu menu_5 = new Menu(mntmAdd);
		mntmAdd.setMenu(menu_5);

		MenuItem mntmString = new MenuItem(menu_5, SWT.NONE);
		mntmString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				if (items.length > 0
						&& (items[0].getData(NODE_TYPE) == NodeType.DATABASE || items[0]
								.getData(NODE_TYPE) == NodeType.CONTAINER))
					addStringSelected(items[0]);
				else
					MessageDialog.openError(shlRedisClient, "error",
							"please select a holder to add string");
			}
		});
		mntmString.setText("String");

		MenuItem mntmList = new MenuItem(menu_5, SWT.NONE);
		mntmList.setText("List");

		MenuItem mntmSet = new MenuItem(menu_5, SWT.NONE);
		mntmSet.setText("Set");

		MenuItem mntmSortset = new MenuItem(menu_5, SWT.NONE);
		mntmSortset.setText("Sorted Set");

		MenuItem mntmHash = new MenuItem(menu_5, SWT.NONE);
		mntmHash.setText("Hash");

		mntmRename_2 = new MenuItem(menu_4, SWT.NONE);
		mntmRename_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				if (items.length > 0
						&& items[0].getData(NODE_TYPE) == NodeType.CONTAINER)
					renameContainerSelected(items[0]);
				else
					MessageDialog.openError(shlRedisClient, "error",
							"please select a container to rename");
			}
		});
		mntmRename_2.setText("Rename");

		mntmDelete_3 = new MenuItem(menu_4, SWT.NONE);
		mntmDelete_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				NodeType type = (NodeType) items[0].getData(NODE_TYPE);
				if(type == NodeType.CONTAINER)
					deleteKeysSelected(items[0]);
				else
					MessageDialog.openError(shlRedisClient, "error",
							"please select a container to delete");
			}
		});
		mntmDelete_3.setText("Delete");

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

	private void initServers() throws IOException {
		java.util.List<Server> servers = service1.listAll();

		for (Server server : servers) {
			addServerTreeItem(server);
		}
	}

	private void initDBs(int server, TreeItem serverItem) throws IOException {
		for (int i = 0; i < service1.listDBs(server); i++) {
			TreeItem dbItem = new TreeItem(serverItem, SWT.NONE);
			dbItem.setText(DB_PREFIX + i);
			dbItem.setData(NODE_ID, i);
			dbItem.setData(NODE_TYPE, NodeType.DATABASE);
		}
	}

	private TreeItem addServerTreeItem(Server server) throws IOException {
		TreeItem treeItem = new TreeItem(tree, 0);
		treeItem.setText(server.getName());
		treeItem.setData(NODE_ID, server.getId());
		treeItem.setData(NODE_TYPE, NodeType.SERVER);

		initDBs(server.getId(), treeItem);

		return treeItem;
	}

	private void refreshTree() {
		tree.removeAll();
		try {
			initServers();
		} catch (IOException e) {
			MessageDialog
					.openError(shlRedisClient, "exception", e.getMessage());
		}
		table.removeAll();
		text.setText("");
	}

	private void addServer() {
		AddServerDialog dialog = new AddServerDialog(shlRedisClient,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		Server server = (Server) dialog.open();

		if (server != null) {
			try {
				server.setId(service1.add(server.getName(), server.getHost(),
						server.getPort()));
				TreeItem item = addServerTreeItem(server);
				serverItemSelected(item);
			} catch (IOException e) {
				MessageDialog.openError(shlRedisClient, "exception",
						e.getMessage());
			}

		}

	}

	private void updateServer() {
		TreeItem[] items = tree.getSelection();
		if (items.length != 0) {
			NodeType type = (NodeType) items[0].getData(NODE_TYPE);
			if (type == NodeType.SERVER) {
				int id = (Integer) items[0].getData(NODE_ID);
				try {
					Server server = service1.listById(id);
					UpdateServerDialog dialog = new UpdateServerDialog(
							shlRedisClient, SWT.DIALOG_TRIM
									| SWT.APPLICATION_MODAL, server);
					server = (Server) dialog.open();
					if (server != null) {
						service1.update(id, server.getName(), server.getHost(),
								server.getPort());
						items[0].setText(server.getName());
						items[0].removeAll();
						initDBs(id, items[0]);
						serverItemSelected(items[0]);
					}
				} catch (IOException e) {
					MessageDialog.openError(shlRedisClient, "exception",
							e.getMessage());
				}
			} else
				MessageDialog.openError(shlRedisClient, "error",
						"please select a server to update!");
		}
	}

	private void removeServer() {
		TreeItem[] items = tree.getSelection();
		if (items.length != 0) {
			NodeType type = (NodeType) items[0].getData(NODE_TYPE);
			if (type == NodeType.SERVER) {
				boolean ok = MessageDialog.openConfirm(shlRedisClient,
						"remove server", "Are you sure remove this server?");
				if (ok) {
					int id = ((Integer) (items[0].getData(NODE_ID))).intValue();
					items[0].dispose();
					try {
						service1.delete(id);
					} catch (IOException e) {
						MessageDialog.openError(shlRedisClient, "exception",
								e.getMessage());
					}
				}
			} else
				MessageDialog.openError(shlRedisClient, "error",
						"please select a server to remove!");
		}
	}

	private void treeItemSelected(TreeItem itemSelected) {
		tree.setSelection(itemSelected);
		ContainerInfo info = new ContainerInfo();
		parseContainer(itemSelected, info);
		String container = (info.getContainer() == null) ? "" : info
				.getContainer();
		text.setText(info.getServerName() + ":" + DB_PREFIX + info.getDb()
				+ ":" + container);
		table.removeAll();

		Set<Node> cnodes = service2.listContainers(info.getId(), info.getDb(),
				info.getContainer());

		if (itemSelected.getData(ITEM_OPENED) == null
				|| ((Boolean) (itemSelected.getData(ITEM_OPENED)) == false)) {
			itemSelected.removeAll();

			for (Node node : cnodes) {
				TreeItem item = new TreeItem(itemSelected, SWT.NONE);
				item.setText(node.getKey());
				item.setData(NODE_TYPE, node.getType());
				item.setExpanded(true);
			}

			itemSelected.setData(ITEM_OPENED, true);
		}
		for (Node node : cnodes) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { node.getKey(),
					node.getType().toString() });
		}

		Set<Node> knodes = service2.listContainerKeys(info.getId(),
				info.getDb(), info.getContainer());

		for (Node node1 : knodes) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { node1.getKey(),
					node1.getType().toString() });
		}
	}

	private void addStringSelected(TreeItem item) {
		ContainerInfo cinfo = new ContainerInfo();
		parseContainer(item, cinfo);
		AddStringDialog dialog = new AddStringDialog(shlRedisClient,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, cinfo.getServerName(),
				cinfo.getDb(), cinfo.getContainer());
		StringInfo info = (StringInfo) dialog.open();
		if (info != null)
			service2.addKey(cinfo.getId(), cinfo.getDb(), info.getKey(),
					info.getValue());
		item.setData(ITEM_OPENED, false);
		treeItemSelected(item);
	}

	private void serverItemSelected(TreeItem selectedItem) {
		tree.setSelection(selectedItem);
		text.setText(selectedItem.getText() + ":");
		table.removeAll();
		try {
			int dbs = service1.listDBs((Integer) selectedItem.getData(NODE_ID));
			for (int i = 0; i < dbs; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] { DB_PREFIX + i,
						NodeType.DATABASE.toString() });
				item.setData(NODE_ID, i);
			}

		} catch (IOException e1) {
			MessageDialog.openError(shlRedisClient, "exception",
					e1.getMessage());
		}
	}

	private void renameContainerSelected(TreeItem item) {
		ContainerInfo info = new ContainerInfo();
		parseContainer(item, info);

		RenameKeysDialog dialog = new RenameKeysDialog(shlRedisClient,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, info.getServerName(),
				info.getDb(), info.getContainer());
		String newContainer = (String) dialog.open();
		if (newContainer != null) {
			service2.renameContainer(info.getId(), info.getDb(),
					info.getContainer(), newContainer);
		}
		item.getParentItem().setData(ITEM_OPENED, false);
		treeItemSelected(item.getParentItem());
	}

	private void deleteKeysSelected(TreeItem item) {
		boolean ok = MessageDialog.openConfirm(shlRedisClient,
					"delete keys", "Are you sure delete all keys under it?");
		if (ok) {
			ContainerInfo info = new ContainerInfo();
			parseContainer(item, info);
			service2.deleteContainer(info.getId(), info.getDb(), info.getContainer());;
			item.dispose();
			
		}
	}
}
