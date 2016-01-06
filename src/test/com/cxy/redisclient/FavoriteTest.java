package com.cxy.redisclient;

import java.io.IOException;
import java.util.List;

import com.cxy.redisclient.domain.Favorite;
import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.service.FavoriteService;

import junit.framework.TestCase;

public class FavoriteTest extends TestCase {
	
	public void testAdd() throws IOException {
		FavoriteService service = new FavoriteService();
		service.add(1,"login", "local 2.6.12:db0:Login:");
		service.add(1, "com", "local 2.6.12:db0:com:");
		service.add(1, "tag", "local 2.6.12:db0:tag:");
	}

	public void testDelete() throws IOException {
		FavoriteService service = new FavoriteService();
		int fid = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.FAVORITE_MAXID));
		service.delete(fid);
	}

	public void testListById() throws IOException {
		FavoriteService service = new FavoriteService();
		int fid = service.add(1,"key", "local 2.6.12:db0:key:");

		Favorite favorite = service.listById(fid);
		assertTrue(favorite.getFavorite().equals("local 2.6.12:db0:key:"));
	}

	public void testListAll() throws IOException {
		FavoriteService service = new FavoriteService();
		List<Favorite> favorites = service.listAll();
		favorites.size();
	}

}
