package com.dms.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dms.entities.Document;
import com.dms.entities.Property;
import com.dms.enums.PropertyTypeEnum;
import com.dms.util.Constants;
import com.dms.util.GeneralUtils;

@Repository
@Transactional(rollbackFor = Exception.class)
public class UtilsRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public int createNewDocumentColumn(String documentTableName, String columnName, String dataType) {
		Query query = entityManager
				.createNativeQuery("ALTER TABLE " + documentTableName + " ADD COLUMN " + columnName + " " + dataType);
		return query.executeUpdate();
	}

	public int insertDocument(String documentTableName, List<Property> properties) {
		String columns = GeneralUtils.generateColumnsString(properties, Constants.OPERATION_ADD_DOCUMENT);
		String values = generateValuesString(properties);
		String queryStr = "insert into " + documentTableName + " (" + columns + ")" + " values(" + values + ")";
		System.out.println("########## queryStr: " + queryStr);
		Query query = entityManager.createNativeQuery(queryStr);
		for (int i = 0; i < properties.size(); i++) {
			System.out.println("######## setting " + properties.get(i).getDisplayNameArabic() + "("
					+ properties.get(i).getColumnName() + ")" + " with value: " + properties.get(i).getValue());
			query.setParameter(i + 1, properties.get(i).getValue());
		}
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Document> findDocuments(String documentTableName, List<Property> propertiesList, String whereCondition,
			String columnNames, int pageNumber) {

		int numberOfSystemProperties = 7;
		if (pageNumber > 0) {
			pageNumber = pageNumber - 1;
		}
		int firstResult = pageNumber * 10;
		String orderBy = " order by id desc ";
		String queryStr = "select " + columnNames + " from " + documentTableName + " where 1=1 " + whereCondition
				+ orderBy;
		System.out.println("########## queryStr: " + queryStr);
		Query query = entityManager.createNativeQuery(queryStr);
		List<Object[]> resultArray = query.setMaxResults(10).setFirstResult(firstResult).getResultList();
		System.out.println("########## resultArray: " + resultArray.size());
		List<Document> resultList = new ArrayList<Document>(resultArray.size());
		Document document = null;
		Object value = null;
		for (Iterator<Object[]> iterator = resultArray.iterator(); iterator.hasNext();) {
			Object[] dataArray = (Object[]) iterator.next();
			document = new Document();
			document.setFullPath((String) dataArray[0]);
			document.setContentSize(((BigInteger) dataArray[1]).longValue());
			document.setOriginalFileName((String) dataArray[2]);
			document.setMimeType((String) dataArray[3]);
			document.setFileName((String) dataArray[4]);
			document.setId(((BigInteger) dataArray[5]).longValue());
			document.setDateCreated((Date) dataArray[6]);
			for (int i = 0; i < propertiesList.size(); i++) {
				value = dataArray[i + numberOfSystemProperties];
				if (propertiesList.get(i).getType().equalsIgnoreCase(PropertyTypeEnum.GREG_DATE.getValue())
						|| propertiesList.get(i).getType().equalsIgnoreCase(PropertyTypeEnum.HIJRI_DATE.getValue())) {
					if (value != null)
						value = GeneralUtils.formatIntDate((int) value);
				}
				document.getCustomPropValues().put(propertiesList.get(i).getSymbolicName(), value);
				System.out.println("######### property " + propertiesList.get(i).getDisplayNameArabic() + "("
						+ propertiesList.get(i).getColumnName() + ") value is: " + value);
			}
			resultList.add(document);
		}
		return resultList;
	}

	private String generateValuesString(List<Property> properties) {
		StringBuffer values = new StringBuffer();
		for (int i = 0; i < properties.size(); i++) {
			values.append("?");
			if (i < properties.size() - 1)
				values.append(",");
		}
		return values.toString();
	}

	public int insertTestValue(String text1, String text2, String text3, int date1, int date2) {
		String fileName = "1cc82f9d-e9d5-434a-8fe6-c20a2012bafa.jpg";
		String fullPath = "C:\\FileStore\\ST44\\ST44\\1cc82f9d-e9d5-434a-8fe6-c20a2012bafa.jpg";
		String mimeType = "image/jpeg";
		String originalFileName = "section1_bg.jpg";
		String date = "2018-05-09 09:28:56";
		String queryStr = "INSERT document (`content_size`, `test1`,`test2`,`test3`,`date_created`,`file_name`,`full_path`,`mime_type`,`original_file_name`,`test11`,`test111`) VALUES ('152924598', '"
				+ text1 + "','" + date1 + "','" + date2 + "'" + ",'" + date + "'" + ",'" + fileName + "'" + ",'"
				+ fullPath + "'" + ",'" + mimeType + "'" + ",'" + originalFileName + "'" + ",'" + text2 + "'" + ",'"
				+ text3 + "'" + ")";
		// System.out.println("######### queryStr: " + queryStr);
		Query query = entityManager.createNativeQuery(queryStr);
		return query.executeUpdate();
	}

	public static void main(String[] args) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property());
	}

}
