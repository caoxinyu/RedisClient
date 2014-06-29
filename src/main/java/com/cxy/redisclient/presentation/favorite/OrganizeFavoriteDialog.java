package com.cxy.redisclient.presentation.favorite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.domain.Favorite;
import com.cxy.redisclient.service.FavoriteService;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class OrganizeFavoriteDialog extends Dialog {

	protected List<Favorite> result = new ArrayList<Favorite>();
	protected Shell shell;
	private Table table;
	private FavoriteService service = new FavoriteService();
	private Button btnRenameButton;
	private Button btnRemoveButton;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public OrganizeFavoriteDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public List<Favorite> open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				result = null;
				shell.dispose();
			}
		});
		shell.setSize(450, 300);
		shell.setText("Organize Favorites");
		
		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		shell.setLayout(new GridLayout(1, false));
		
		Group grpFavorites = new Group(shell, SWT.NONE);
		grpFavorites.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpFavorites.setSize(417, 231);
		grpFavorites.setText("Favorites");
		grpFavorites.setLayout(new GridLayout(2, false));
		
		table = new Table(grpFavorites, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setSize(308, 45);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Name");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(187);
		tblclmnNewColumn_1.setText("Favorite");
		
		Composite composite_1 = new Composite(grpFavorites, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(1, false));
		
		btnRenameButton = new Button(composite_1, SWT.NONE);
		btnRenameButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRenameButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				Favorite favorite = (Favorite) items[0].getData();
				RenameFavoriteDialog dialog = new RenameFavoriteDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, favorite);
				String name =  (String) dialog.open();
				if(name != null) {
					items[0].setText(new String[] { name, favorite.getFavorite() });
					favorite.setName(name);
					items[0].setData(favorite);
				}
			}
		});
		btnRenameButton.setEnabled(false);
		btnRenameButton.setText("Rename");
		
		btnRemoveButton = new Button(composite_1, SWT.NONE);
		btnRemoveButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				for(TableItem item : items){
					item.dispose();
				}
				tableItemSelected();
			}
		});
		btnRemoveButton.setEnabled(false);
		btnRemoveButton.setText("Remove");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnOk = new Button(composite, SWT.NONE);
		btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				
				for(TableItem item : items){
					result.add((Favorite) item.getData());
				}
				
				shell.dispose();
			}
		});
		btnOk.setText("OK");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnCancel.setText("Cancel");
		
		
		List<Favorite> favorites = service.listAll();
		for(Favorite favorite: favorites){
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { favorite.getName(),
				favorite.getFavorite() });
			item.setData(favorite);
		}
	}

	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		if(items.length == 1){
			btnRenameButton.setEnabled(true);
			btnRemoveButton.setEnabled(true);
		} else if(items.length > 1){
			btnRemoveButton.setEnabled(true);
			btnRenameButton.setEnabled(false);
		} else {
			btnRemoveButton.setEnabled(false);
			btnRenameButton.setEnabled(false);
		}
	}
}
