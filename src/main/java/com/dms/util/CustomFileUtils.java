package com.dms.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CustomFileUtils {

    public static final String FILE_STORE_PATH = "C:\\FileStore";
    public static final String FILE_STORE_SUB_FOLDER_NAME = "ST";
    public static final int MIN_FOLDER_NUMBER = 0;
    public static final int MAX_FOLDER_NUMBER = 50;

    public static void main(String[] args) throws IOException {
        createStoreFolders(FILE_STORE_PATH);
    }

    public static String generateRandomFileName(String fileName) {
        return UUID.randomUUID()
            .toString() + "." + getFileExtensionFromFileName(fileName);
    }

    public static String getFileExtensionFromFileName(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        } else
            return null;
    }

    private static void createStoreFolders(String fileStorePath) {

        System.out.println("####### STARTED CREATING FOLDERS ##########");
        File level1Folder = null;
        File level2Folder = null;
        String level1Path = null;
        String level2Path = null;
        for (int i = MIN_FOLDER_NUMBER; i <= MAX_FOLDER_NUMBER; i++) {
            level1Path = fileStorePath + "\\" + FILE_STORE_SUB_FOLDER_NAME + i;
            level1Folder = new File(level1Path);
            level1Folder.mkdir();
            System.out.println("####### CREATED FOLDER: " + level1Folder.getPath());
            for (int j = MIN_FOLDER_NUMBER; j <= MAX_FOLDER_NUMBER; j++) {
                level2Path = fileStorePath + "\\" + FILE_STORE_SUB_FOLDER_NAME + i + "\\" + FILE_STORE_SUB_FOLDER_NAME + j;
                level2Folder = new File(level2Path);
                level2Folder.mkdir();
            }
            System.out.println("####### CREATED SUB FOLDERS FOR: " + level1Folder.getPath());
        }
    }

    public static String generateRandomStoreFolderPath() {
        int randomLevel1 = ThreadLocalRandom.current()
            .nextInt(MIN_FOLDER_NUMBER, MAX_FOLDER_NUMBER + 1);
        int randomLevel2 = ThreadLocalRandom.current()
            .nextInt(MIN_FOLDER_NUMBER, MAX_FOLDER_NUMBER + 1);
        return FILE_STORE_PATH + "\\" + FILE_STORE_SUB_FOLDER_NAME + randomLevel1 + "\\" + FILE_STORE_SUB_FOLDER_NAME + randomLevel2 + "\\";
    }

}
