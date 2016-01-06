package com.cxy.redisclient;

import java.io.IOException;

import com.cxy.redisclient.service.ImportService;

import junit.framework.TestCase;

public class ImportTest extends TestCase {

	public void testImportFile() throws IOException {
		ImportService service = new ImportService("C:\\Users\\xinyu\\Desktop\\export", 6, 12);
		service.importFile();
	}

}
