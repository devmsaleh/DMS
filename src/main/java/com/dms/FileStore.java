package com.dms;

import java.io.File;
import java.io.IOException;

public class FileStore {

	public static void main(String[] args) throws IOException {
		createStoreFolders("C:\\FileStore");
	}

	public static void createStoreFolders(String fileStorePath) {

		System.out.println("####### STARTED CREATING FOLDERS ##########");
		File level1Folder = null;
		File level2Folder = null;
		File rootFoler = new File(fileStorePath);
		if (!rootFoler.exists()) {
			rootFoler.mkdir();
		}
		for (int i = 0; i <= 50; i++) {
			level1Folder = new File(fileStorePath + "\\ST" + i);
			if (!level1Folder.exists())
				level1Folder.mkdirs();
			System.out.println("####### CREATED FOLDER: " + level1Folder.getPath());
			for (int j = 0; j <= 50; j++) {
				level2Folder = new File(fileStorePath + "\\ST" + i + "\\ST" + j);
				if (!level2Folder.exists())
					level2Folder.mkdirs();
			}
			System.out.println("####### CREATED SUB FOLDERS FOR: " + level1Folder.getPath());
		}
	}

}
