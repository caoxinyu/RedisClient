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
		shell.setSize(450, 300);
		shell.setText("Organize Favorites");
		
		Rectangle screenSize = shell.getParent().getBounds();
		Rectangle shellSize = shell.getBounds();
		shell.setLocation(screenSize.x + screenSize.width / 2 - shellSize.width / 2,
				screenSize.y + screenSize.height / 2 - shellSize.height / 2);
		
		Group grpFavorites = new Group(shell, SWT.NONE);
		grpFavorites.setText("Favorites");
		grpFavorites.setBounds(10, 0, 424, 238);
		
		btnRenameButton = new Button(grpFavorites, SWT.NONE);
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
		btnRenameButton.setBounds(346, 23, 68, 23);
		btnRenameButton.setText("Rename");
		
		btnRemoveButton = new Button(grpFavorites, SWT.NONE);
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
		btnRemoveButton.setBounds(346, 62, 68, 23);
		btnRemoveButton.setText("Remove");
		
		Button btnOk = new Button(shell, SWT.NONE);
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
		btnOk.setBounds(104, 244, 68, 23);
		btnOk.setText("OK");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnCancel.setBounds(276, 244, 68, 23);
		btnCancel.setText("Cancel");
		
		table = new Table(grpFavorites, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.setBounds(10, 23, 322, 205);
		table.setHeaderVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Name");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(187);
		tblclmnNewColumn_1.setText("Favorite");
		
		
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
