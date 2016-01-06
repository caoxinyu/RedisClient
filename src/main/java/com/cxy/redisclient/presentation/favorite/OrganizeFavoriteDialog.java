package com.cxy.redisclient.presentation.favorite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cxy.redisclient.domain.Favorite;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.component.RedisClientDialog;
import com.cxy.redisclient.service.FavoriteService;

public class OrganizeFavoriteDialog extends RedisClientDialog {
	private Table table;
	private FavoriteService service = new FavoriteService();
	private Button btnRenameButton;
	private Button btnRemoveButton;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public OrganizeFavoriteDialog(Shell parent, Image image) {
		super(parent, image);
		result = new ArrayList<Favorite>();
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				result = null;
				shell.dispose();
			}
		});
		shell.setText(RedisClient.i18nFile.getText(I18nFile.ORGANIZEFAVORITE));
		
		
		shell.setLayout(new GridLayout(1, false));
		
		Group grpFavorites = new Group(shell, SWT.NONE);
		grpFavorites.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpFavorites.setText(RedisClient.i18nFile.getText(I18nFile.FAVORITES));
		grpFavorites.setLayout(new GridLayout(4, false));
		
		table = new Table(grpFavorites, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2));
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
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.NAME));
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(187);
		tblclmnNewColumn_1.setText(RedisClient.i18nFile.getText(I18nFile.FAVORITE));
		
		btnRenameButton = new Button(grpFavorites, SWT.NONE);
		btnRenameButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRenameButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				Favorite favorite = (Favorite) items[0].getData();
				RenameFavoriteDialog dialog = new RenameFavoriteDialog(shell, image, favorite);
				String name =  (String) dialog.open();
				if(name != null) {
					items[0].setText(new String[] { name, favorite.getFavorite() });
					favorite.setName(name);
					items[0].setData(favorite);
				}
			}
		});
		btnRenameButton.setEnabled(false);
		btnRenameButton.setText(RedisClient.i18nFile.getText(I18nFile.RENAME));
		
		
		btnRemoveButton = new Button(grpFavorites, SWT.NONE);
		btnRemoveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
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
		btnRemoveButton.setText(RedisClient.i18nFile.getText(I18nFile.REMOVE));
				
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button btnOk = new Button(composite, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				
				for(TableItem item : items){
					((ArrayList<Favorite>) result).add((Favorite) item.getData());
				}
				
				shell.dispose();
			}
		});
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.OK));
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		
		List<Favorite> favorites = service.listAll();
		for(Favorite favorite: favorites){
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { favorite.getName(),
				favorite.getFavorite() });
			item.setData(favorite);
		}
		
		super.createContents();
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
