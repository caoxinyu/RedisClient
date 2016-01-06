package com.cxy.redisclient.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cxy.redisclient.domain.Favorite;
import com.cxy.redisclient.integration.ConfigFile;

public class FavoriteService {
	public int add(int id, String name, String favorite) {
		try {
			int fid = Integer.parseInt(ConfigFile
					.readMaxId(ConfigFile.FAVORITE_MAXID)) + 1;
			ConfigFile.write(ConfigFile.FAVORITE + fid, favorite);
			ConfigFile.write(ConfigFile.FAVORITE_NAME + fid, name);
			ConfigFile.write(ConfigFile.FAVORITE_SERVER + fid,
					String.valueOf(id));

			
			ConfigFile.write(ConfigFile.FAVORITE_MAXID, String.valueOf(fid));

			return fid;
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public void delete(int fid) {
		try {
			ConfigFile.delete(ConfigFile.FAVORITE + fid);
			ConfigFile.delete(ConfigFile.FAVORITE_SERVER + fid);
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public Favorite listById(int fid) {
		try {
			Favorite favorite = null;
			if (ConfigFile.read(ConfigFile.FAVORITE + fid) != null)
				favorite = new Favorite(fid, Integer.parseInt(ConfigFile
						.read(ConfigFile.FAVORITE_SERVER + fid)),
						ConfigFile.read(ConfigFile.FAVORITE_NAME + fid),
						ConfigFile.read(ConfigFile.FAVORITE + fid));

			return favorite;
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public List<Favorite> listAll() {
		try {
			int amount = Integer.parseInt(ConfigFile
					.readMaxId(ConfigFile.FAVORITE_MAXID));
			List<Favorite> favorites = new ArrayList<Favorite>();
			for (int i = 1; i <= amount; i++) {
				Favorite favorite = listById(i);
				if (favorite != null)
					favorites.add(favorite);
			}

			return favorites;
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public void updateList(List<Favorite> favorites) {
		List<Favorite> allFavorite = listAll();
		for (Favorite favorite : allFavorite) {
			Favorite newFavorite = find(favorite.getFid(), favorites);
			if (newFavorite == null)
				delete(favorite.getFid());
			else {
				if (!favorite.getName().equals(newFavorite.getName()))
					updateName(favorite.getFid(), newFavorite.getName());
			}
		}
	}

	private Favorite find(int fid, List<Favorite> favorites) {
		for (Favorite favorite : favorites) {
			if (favorite.getFid() == fid)
				return favorite;
		}
		return null;
	}

	public void updateName(int fid, String name) {
		try {
			ConfigFile.write(ConfigFile.FAVORITE_NAME + fid, name);
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
}
