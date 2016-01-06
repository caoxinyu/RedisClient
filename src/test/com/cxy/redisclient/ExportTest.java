package com.cxy.redisclient;

import java.io.IOException;

import com.cxy.redisclient.domain.ContainerKey;
import com.cxy.redisclient.service.ExportService;

import junit.framework.TestCase;

public class ExportTest extends TestCase {

	public void testExport() throws IOException {
		ExportService service = new ExportService("C:\\Users\\xinyu\\Desktop\\export", 5, 7, new ContainerKey(""));
		service.export();
	}

}
