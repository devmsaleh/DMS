package com.dms.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dms.dao.UtilsRepository;
import com.dms.enums.ColumnDatabaseTypeEnum;
import com.dms.enums.DocumentClassTableNameEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

	private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

	@Autowired
	private UtilsRepository utilsRepository;

	@Before
	public void beforeTestRuns() {

	}

	@Test
	public void testAddNewRecord() {
		for (int i = 0; i < 500000; i++) {
			System.out.println(i);
			utilsRepository.insertTestValue(RandomStringUtils.randomAlphabetic(2000),
					RandomStringUtils.randomAlphabetic(2000), RandomStringUtils.randomAlphabetic(300),
					Integer.parseInt(RandomStringUtils.randomNumeric(8)),
					Integer.parseInt(RandomStringUtils.randomNumeric(8)));
		}
	}

	@Test
	public void testCreateNewColumns() {
		for (int i = 0; i < 50; i++) {
			String name = RandomStringUtils.randomAlphabetic(7);
			String type = ColumnDatabaseTypeEnum.LONG_TEXT.getValue();
			System.out.println(i + " >>>> name: " + name + ",type: " + type);
			utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(), name, type);
		}

		for (int i = 0; i < 50; i++) {
			String name = RandomStringUtils.randomAlphabetic(7);
			String type = ColumnDatabaseTypeEnum.SMALL_TEXT.getValue();
			System.out.println(i + " >>>> name: " + name + ",type: " + type);
			utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(), name, type);
		}

		for (int i = 0; i < 50; i++) {
			String name = RandomStringUtils.randomAlphabetic(7);
			String type = ColumnDatabaseTypeEnum.SMALL_TEXT.getValue();
			System.out.println(i + " >>>> name: " + name + ",type: " + type);
			utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(), name, type);
		}

		for (int i = 0; i < 50; i++) {
			String name = RandomStringUtils.randomAlphabetic(7);
			String type = ColumnDatabaseTypeEnum.GREG_DATE.getValue();
			System.out.println(i + " >>>> name: " + name + ",type: " + type);
			utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(), name, type);
		}

		for (int i = 0; i < 50; i++) {
			String name = RandomStringUtils.randomAlphabetic(7);
			String type = ColumnDatabaseTypeEnum.HIJRI_DATE.getValue();
			System.out.println(i + " >>>> name: " + name + ",type: " + type);
			utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(), name, type);
		}

		for (int i = 0; i < 50; i++) {
			String name = RandomStringUtils.randomAlphabetic(7);
			String type = ColumnDatabaseTypeEnum.NORMAL_CHOICELIST.getValue();
			System.out.println(i + " >>>> name: " + name + ",type: " + type);
			utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(), name, type);
		}

	}

}
