package com.dms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.convert.IntegerConverter;
import javax.faces.convert.LongConverter;
import javax.faces.model.SelectItem;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.chips.Chips;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.dms.entities.ChoiceListItem;
import com.dms.entities.Property;
import com.dms.entities.User;
import com.dms.enums.PropertyTypeEnum;

/**
 *
 * @author mahsaleh
 */
public class UIUtils {

	final static String propertiesPanelGridId = "inputs";

	/**
	 * Generates the input id to be documentClassSymbolicName_propertySymbolicName
	 * 
	 * @param documentClassSymbolicName
	 * @param propertySymbolicName
	 * @return
	 */
	public static String generateInputId(String documentClassSymbolicName, String propertySymbolicName) {
		return documentClassSymbolicName + "_" + propertySymbolicName;
	}

	/**
	 * Creates a select one menu with the given choicelist items/values
	 * 
	 * @param symbolicName
	 * @param map
	 * @param addEmptyChoiceAtFirst
	 * @return
	 */
	public static SelectOneMenu createSelectOneMenu(String symbolicName, Map<String, String> map,
			boolean addEmptyChoiceAtFirst) {

		SelectOneMenu selectOneMenu = new SelectOneMenu();
		selectOneMenu.setStyleClass(InputsStyles.PRIMEFACES_MENU_STYLE_CLASS);
		selectOneMenu.setId(symbolicName);
		selectOneMenu.setFilter(true);
		selectOneMenu.setFilterMatchMode("contains");
		List<SelectItem> selectItems = new ArrayList<SelectItem>(map.size());

		if (addEmptyChoiceAtFirst)
			selectItems.add(new SelectItem("", ""));

		for (Map.Entry<String, String> entry : map.entrySet()) {
			selectItems.add(new SelectItem(entry.getValue(), entry.getKey()));
		}

		UISelectItems uiSelectItems = new UISelectItems();
		uiSelectItems.setValue(selectItems);
		selectOneMenu.getChildren().add(uiSelectItems);

		selectOneMenu.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.CHOICE_LIST);

		return selectOneMenu;

	}

	/**
	 * Generates properties inputs in add,edit,search,verify and add validation
	 * (add,edit only) </br>
	 * the inputs can be textfield or textarea or dropdown list or calendar pickup
	 * </br>
	 * in case of edit or verify document, we fill the inputs with the properties
	 * values
	 * 
	 * @param objectStore
	 * @param document
	 * @param documentClassName
	 * @param properties
	 * @param operationName
	 * @param propertiesPanelGrid
	 * @param parentDocumentClassName
	 * @param locale
	 * @param currentUser
	 * @throws java.lang.Exception
	 **/
	public static void generatePropertiesInputsForAdd(List<Property> properties, HtmlPanelGrid propertiesPanelGrid,
			Locale locale, User currentUser) throws Exception {

		propertiesPanelGrid.getChildren().clear();

		if (propertiesPanelGrid != null) {
			propertiesPanelGrid.setId(propertiesPanelGridId);
		}

		UIInput uiInput = null;

		for (Property property : properties) {

			if (property == null)
				continue;

			HtmlOutputLabel label = new HtmlOutputLabel();
			label.setStyle(InputsStyles.LABEL_STYLE);
			label.setValue(property.getDisplayNameArabic());

			if (property.isRequired())
				label.setStyleClass(InputsStyles.REQUIRED_LABEL_STYLE);
			else
				label.setStyleClass(InputsStyles.NOT_REQUIRED_LABEL_STYLE);

			propertiesPanelGrid.getChildren().add(label);

			if (property.getType().equals(PropertyTypeEnum.MULTI_TEXT.getValue())) {
				Chips input = new Chips();
				input.setId(property.getSymbolicName());
				input.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.MULTI_TEXT);
				uiInput = input;
			}

			if (property.getType().equals(PropertyTypeEnum.SMALL_TEXT.getValue())
					|| property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())
					|| property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())
					|| property.getType().equals(PropertyTypeEnum.SMALL_NUMBER.getValue())
					|| property.getType().equals(PropertyTypeEnum.LONG_NUMBER.getValue())) {
				InputText inputText = new InputText();
				inputText.setId(property.getSymbolicName());
				inputText.setStyle(InputsStyles.INPUT_TEXT_STYLE);
				if (property.getMaxLength() != 0) {
					inputText.setMaxlength(property.getMaxLength());
				}

				if (property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())
						|| property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {

					if (property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {

						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME,
								InputsDataTypes.HIJRI_DATE);
						inputText.setStyleClass(InputsStyles.HIJRI_DATE_STYLE_CLASS);

					}

					if (property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())) {

						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME,
								InputsDataTypes.GREG_DATE);
						inputText.setStyleClass(InputsStyles.GREG_DATE_STYLE_CLASS);
					}

				} else {
					if (property.getType().equals(PropertyTypeEnum.SMALL_NUMBER.getValue()))
						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME,
								InputsDataTypes.INTEGER);
					else if (property.getType().equals(PropertyTypeEnum.LONG_NUMBER.getValue()))
						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.LONG);
					else
						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.STRING);
				}

				uiInput = inputText;

			}

			if (property.getType().equals(PropertyTypeEnum.LONG_TEXT.getValue())) {
				InputTextarea inputTextArea = new InputTextarea();
				inputTextArea.setId(property.getSymbolicName());
				inputTextArea.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.STRING);
				inputTextArea.setStyle(InputsStyles.INPUT_TEXT_AREA_STYLE);
				uiInput = inputTextArea;
			}

			if (property.getType().equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())
					|| property.getType().equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue())
					|| property.getType().equals(PropertyTypeEnum.SUB_CHOICELIST.getValue())) {
				if (property.getType().equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue())
						|| property.getType().equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())) {
					SelectOneMenu selectOneMenu = createSelectOneMenu(property, true, properties, propertiesPanelGrid);
					uiInput = selectOneMenu;
				}
			}

			if (uiInput != null) {
				// validation

				if (property.getType().equals(PropertyTypeEnum.SMALL_NUMBER.getValue())
						&& (!property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())
								&& !property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue()))) {
					String converterMessage = "الحقل " + property.getDisplayNameArabic() + " يقبل أرقام فقط";
					uiInput.setConverter(createIntegerConverter());
					uiInput.setConverterMessage(converterMessage);
				}

				if (property.getType().equals(PropertyTypeEnum.LONG_NUMBER.getValue())
						&& !property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())
						&& !property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())) {
					String converterMessage = "الحقل " + property.getDisplayNameArabic() + " يقبل أرقام فقط";
					uiInput.setConverter(createLongConverter());
					uiInput.setConverterMessage(converterMessage);
				}

				addCustomValidations(property, uiInput);

				// required validation,too much text validation in (add,edit)

				// required validation
				String requiredMessage = "الحقل " + property.getDisplayNameArabic() + " إجباري";
				uiInput.setRequired(property.isRequired());
				uiInput.setRequiredMessage(requiredMessage);
				// too much text validation
				if (property.getType().equals(PropertyTypeEnum.LONG_TEXT.getValue())) {
					String tooMuchDataMessage = "النص المدخل كبير...يجب أن لا يتجاوز عدد الحروف : "
							+ property.getMaxLength();
					uiInput.addValidator(createLengthValidator(property.getMaxLength()));
					uiInput.setValidatorMessage(tooMuchDataMessage);
				}

				// setting property value,adding input to panelGrid
				uiInput.setValue(property.getValue());
				uiInput.getAttributes().put("title", label.getValue());
				propertiesPanelGrid.getChildren().add(uiInput);
			}
		} // end of properties for loop

	}

	public static void generatePropertiesInputsForSearch(List<Property> properties, HtmlPanelGrid propertiesPanelGrid,
			Locale locale, User currentUser) throws Exception {

		propertiesPanelGrid.getChildren().clear();

		if (propertiesPanelGrid != null) {
			propertiesPanelGrid.setId(propertiesPanelGridId);
		}

		UIInput uiInput = null; // UIInput is set to null in search and property
								// is hijriDate

		for (Property property : properties) {

			if (property == null)
				continue;

			HtmlOutputLabel label = new HtmlOutputLabel();
			label.setStyle(InputsStyles.LABEL_STYLE);
			label.setValue(property.getDisplayNameArabic());

			propertiesPanelGrid.getChildren().add(label);

			if (property.getType().equals(PropertyTypeEnum.SMALL_TEXT.getValue())
					|| property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())
					|| property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())
					|| property.getType().equals(PropertyTypeEnum.SMALL_NUMBER.getValue())
					|| property.getType().equals(PropertyTypeEnum.LONG_NUMBER.getValue())
					|| property.getType().equals(PropertyTypeEnum.MULTI_TEXT.getValue())) {
				InputText inputText = new InputText();
				inputText.setId(property.getSymbolicName());
				inputText.setStyle(InputsStyles.INPUT_TEXT_STYLE);
				if (property.getMaxLength() != 0) {
					inputText.setMaxlength(property.getMaxLength());
				}

				if (property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())
						|| property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {

					if (property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {
						propertiesPanelGrid.getChildren().add(createHijriDateSearchInputs(property));
					}

					if (property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())) {
						propertiesPanelGrid.getChildren().add(createGregDateSearchInputs(property));
					}

				} else {
					if (property.getType().equals(PropertyTypeEnum.SMALL_NUMBER.getValue()))
						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME,
								InputsDataTypes.INTEGER);
					else if (property.getType().equals(PropertyTypeEnum.LONG_NUMBER.getValue()))
						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.LONG);
					else
						inputText.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.STRING);
				}

				uiInput = inputText;
				if (property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())
						|| property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {
					uiInput = null;
				}
			}

			if (property.getType().equals(PropertyTypeEnum.LONG_TEXT.getValue())) {
				InputTextarea inputTextArea = new InputTextarea();
				inputTextArea.setId(property.getSymbolicName());
				inputTextArea.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.STRING);
				inputTextArea.setStyle(InputsStyles.INPUT_TEXT_AREA_STYLE);
				uiInput = inputTextArea;
			}

			if (property.getType().equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())
					|| property.getType().equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue())
					|| property.getType().equals(PropertyTypeEnum.SUB_CHOICELIST.getValue())) {
				if (property.getType().equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue())
						|| property.getType().equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())) {
					SelectOneMenu selectOneMenu = createSelectOneMenu(property, true, properties, propertiesPanelGrid);
					uiInput = selectOneMenu;
				}
			}

			if (uiInput != null) {

				if (property.getType().equals(PropertyTypeEnum.SMALL_NUMBER.getValue())
						&& (!property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())
								&& !property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue()))) {

					String converterMessage = "الحقل " + property.getDisplayNameArabic() + " يقبل أرقام فقط";
					uiInput.setConverter(createIntegerConverter());
					uiInput.setConverterMessage(converterMessage);
				}

				if (property.getType().equals(PropertyTypeEnum.LONG_NUMBER.getValue())
						&& !property.getType().equals(PropertyTypeEnum.HIJRI_DATE.getValue())
						&& !property.getType().equals(PropertyTypeEnum.GREG_DATE.getValue())) {

					String converterMessage = "الحقل " + property.getDisplayNameArabic() + " يقبل أرقام فقط";
					uiInput.setConverter(createLongConverter());
					uiInput.setConverterMessage(converterMessage);
				}

				uiInput.setValue(property.getValue());
				uiInput.getAttributes().put("title", label.getValue());
				propertiesPanelGrid.getChildren().add(uiInput);
			}
		} // end of properties for loop

	}

	private static void setTabIndexForPropertiesInputs(HtmlPanelGrid propertiesPanelGrid) {
		int tabIndex = 0;
		for (UIComponent uIComponent : propertiesPanelGrid.getChildren()) {
			if (uIComponent instanceof HtmlInputText || uIComponent instanceof HtmlInputTextarea
					|| uIComponent instanceof SelectOneMenu || uIComponent instanceof HtmlPanelGroup) {
				tabIndex++;

				if (uIComponent instanceof HtmlInputText) {
					HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
					htmlInputText.setTabindex(String.valueOf(tabIndex));
				} else if (uIComponent instanceof HtmlInputTextarea) {
					HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) uIComponent;
					htmlInputTextarea.setTabindex(String.valueOf(tabIndex));
				}

				else if (uIComponent instanceof SelectOneMenu) {
					SelectOneMenu selectOneMenu = (SelectOneMenu) uIComponent;
					selectOneMenu.setTabindex(String.valueOf(tabIndex));
				}

				else if (uIComponent instanceof HtmlPanelGroup) {
					HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) uIComponent;
					for (UIComponent uIComponent2 : htmlPanelGroup.getChildren()) {
						if (uIComponent2 instanceof HtmlInputText) {
							HtmlInputText htmlInputText = (HtmlInputText) uIComponent2;
							if (StringUtils.isBlank(htmlInputText.getTabindex())) {
								htmlInputText.setTabindex(String.valueOf(tabIndex));
								tabIndex++;
							}
						}
					}
				}
			}
		}
	}

	private static void disableAllPropertiesInputs(HtmlPanelGrid propertiesPanelGrid) {

		for (UIComponent uIComponent : propertiesPanelGrid.getChildren()) {
			if (uIComponent instanceof HtmlInputText || uIComponent instanceof HtmlInputTextarea
					|| uIComponent instanceof SelectOneMenu) {

				if (uIComponent instanceof HtmlInputText) {
					HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
					htmlInputText.setDisabled(true);
				} else if (uIComponent instanceof HtmlInputTextarea) {
					HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) uIComponent;
					htmlInputTextarea.setDisabled(true);
				}

				else if (uIComponent instanceof SelectOneMenu) {
					SelectOneMenu selectOneMenu = (SelectOneMenu) uIComponent;
					selectOneMenu.setDisabled(true);
				}
			}
		}
	}

	// adds hidden field for display document link that results from internal
	// integration in add document
	private static void addDisplayDocumentFields(HtmlPanelGrid propertiesPanelGrid) {

	}

	// adds hidden field for display document id that results from internal
	// integration in add document
	private static void addHiddenFieldsForDocumentId(HtmlPanelGrid propertiesPanelGrid) {

	}

	// creates a blur listener for main property in the document class so that
	// when user navigates away from it, internal search in the document class
	// occurs to retrieve document that matches the given property value
	private static void addIntegrationBlurListener(Property property, UIInput uiInput,
			HtmlPanelGrid propertiesPanelGrid, String documentClassName, List<String> propertiesToSelect,
			String parentDocumentClassName) {

	}

	/**
	 * applied in add,edit documents to add custom validation for specific
	 * properties like year validation,commerce and civil record validation
	 * 
	 * @param property
	 * @param uiInput
	 **/
	public static void addCustomValidations(Property property, UIInput uiInput) {

	}

	/**
	 * Checks for specific property names or specific property description that
	 * marks the property as civil record property
	 * 
	 * @param propertySymbolicName
	 * @param description
	 * @return
	 */
	public static boolean isCivilRecordProperty(String propertySymbolicName, String description) {

		return false;
	}

	/**
	 * Checks for specific property names or specific property description that
	 * marks the property as commerce record property
	 * 
	 * @param propertySymbolicName
	 * @param description
	 * @return
	 */
	public static boolean isCommercialRecordProperty(String propertySymbolicName, String description) {

		return false;
	}

	/**
	 * Generates properties inputs for statistical report page, and create dropdown
	 * for each input that has search conditions based on the property data type
	 * 
	 * @param documentClassName
	 * @param properties
	 * @param propertiesPanelGrid
	 * @param sumMap
	 * @param maxMap
	 * @param minMap
	 * @param locale
	 * @throws Exception
	 */

	/**
	 * Creates labels and textfields with date pickup to input start data and end
	 * date
	 * 
	 * @param property
	 * @return
	 */
	public static HtmlPanelGroup createHijriDateSearchInputs(Property property) {

		HtmlPanelGroup htmlPanelGroup = new HtmlPanelGroup();
		htmlPanelGroup.setLayout("block");
		htmlPanelGroup.setId(property.getSymbolicName() + Constants.HIJRI_DATE_SEARCH_DIV_CONTAINS);
		// panelGrid.setColumns(4);

		HtmlOutputLabel labelFrom = new HtmlOutputLabel();
		labelFrom.setStyle("margin-left:5px;");
		labelFrom.setValue("من");

		InputText inputTextFrom = new InputText();
		inputTextFrom.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.HIJRI_DATE);
		inputTextFrom.setStyleClass(InputsStyles.HIJRI_DATE_STYLE_CLASS);
		inputTextFrom.setStyle(InputsStyles.HIJRI_DATE_INPUT_TEXT_STYLE_FROM);
		inputTextFrom.setId(property.getSymbolicName() + Constants.HIJRI_DATE_SEARCH_INPUT_FROM_CONTAINS);

		HtmlOutputLabel labelTo = new HtmlOutputLabel();
		labelTo.setStyle("margin-left:5px;margin-right:5px;");
		labelTo.setValue("إلى");

		InputText inputTextTo = new InputText();
		inputTextTo.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.HIJRI_DATE);
		inputTextTo.setStyleClass(InputsStyles.HIJRI_DATE_STYLE_CLASS);
		inputTextTo.setStyle(InputsStyles.HIJRI_DATE_INPUT_TEXT_STYLE_TO);
		inputTextTo.setId(property.getSymbolicName() + Constants.HIJRI_DATE_SEARCH_INPUT_TO_CONTAINS);

		htmlPanelGroup.getChildren().add(labelFrom);
		htmlPanelGroup.getChildren().add(inputTextFrom);
		htmlPanelGroup.getChildren().add(labelTo);
		htmlPanelGroup.getChildren().add(inputTextTo);
		return htmlPanelGroup;
	}

	public static HtmlPanelGroup createGregDateSearchInputs(Property property) {

		HtmlPanelGroup htmlPanelGroup = new HtmlPanelGroup();
		htmlPanelGroup.setLayout("block");
		htmlPanelGroup.setId(property.getSymbolicName() + Constants.GREG_DATE_SEARCH_DIV_CONTAINS);
		// panelGrid.setColumns(4);

		HtmlOutputLabel labelFrom = new HtmlOutputLabel();
		labelFrom.setStyle("margin-left:5px;");
		labelFrom.setValue("من");

		InputText inputTextFrom = new InputText();
		inputTextFrom.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.GREG_DATE);
		inputTextFrom.setStyleClass(InputsStyles.GREG_DATE_STYLE_CLASS);
		inputTextFrom.setStyle(InputsStyles.GREG_DATE_INPUT_TEXT_STYLE_FROM);
		inputTextFrom.setId(property.getSymbolicName() + Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS);

		HtmlOutputLabel labelTo = new HtmlOutputLabel();
		labelTo.setStyle("margin-left:5px;margin-right:5px;");
		labelTo.setValue("إلى");

		InputText inputTextTo = new InputText();
		inputTextTo.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.GREG_DATE);
		inputTextTo.setStyleClass(InputsStyles.GREG_DATE_STYLE_CLASS);
		inputTextTo.setStyle(InputsStyles.GREG_DATE_INPUT_TEXT_STYLE_TO);
		inputTextTo.setId(property.getSymbolicName() + Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS);

		htmlPanelGroup.getChildren().add(labelFrom);
		htmlPanelGroup.getChildren().add(inputTextFrom);
		htmlPanelGroup.getChildren().add(labelTo);
		htmlPanelGroup.getChildren().add(inputTextTo);
		return htmlPanelGroup;
	}

	/**
	 * Converts the input data to long data type
	 * 
	 * @return
	 */
	public static LongConverter createLongConverter() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		LongConverter longConverter = (LongConverter) application.createConverter(LongConverter.CONVERTER_ID);
		return longConverter;
	}

	/**
	 * Converts the input data to integer data type
	 * 
	 * @return
	 */
	public static IntegerConverter createIntegerConverter() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		IntegerConverter integerConverter = (IntegerConverter) application
				.createConverter(IntegerConverter.CONVERTER_ID);
		return integerConverter;
	}

	/**
	 * Creates a custom jsf validation to be added to the input
	 * 
	 * @param validatorId
	 * @return
	 */
	public static Validator createValidator(String validatorId) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		return application.createValidator(validatorId);
	}

	/**
	 * Creates a length validator for the input to specify the maximum length
	 * allowed for the input
	 * 
	 * @param maxLength
	 * @return
	 */
	public static LengthValidator createLengthValidator(int maxLength) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		LengthValidator lengthValidator = (LengthValidator) application.createValidator(LengthValidator.VALIDATOR_ID);
		lengthValidator.setMaximum(maxLength);
		return lengthValidator;
	}

	/**
	 * Creates the select one menu that contains the search criterias for the string
	 * property
	 * 
	 * @param propertySymbolicName
	 * @return
	 */
	public static SelectOneMenu createSelectOneMenuCriteriasForString(String propertySymbolicName) {

		SelectOneMenu selectOneMenu = new SelectOneMenu();

		return selectOneMenu;

	}

	/**
	 * Creates the select one menu that contains the search criterias for the
	 * integer/long property
	 * 
	 * @param propertySymbolicName
	 * @return
	 */
	public static SelectOneMenu createSelectOneMenuCriteriasForNumber(String propertySymbolicName) {

		SelectOneMenu selectOneMenu = new SelectOneMenu();

		return selectOneMenu;

	}

	/**
	 * Creates select one menu for given property
	 * 
	 * @param property
	 * @param addEmptyFirstItem
	 * @param properties
	 * @param propertiesPanelGrid
	 * @return
	 */
	public static SelectOneMenu createSelectOneMenu(Property property, boolean addEmptyFirstItem,
			List<Property> properties, HtmlPanelGrid propertiesPanelGrid) {

		SelectOneMenu selectOneMenu = null;

		try {

			selectOneMenu = new SelectOneMenu();
			selectOneMenu.setStyleClass(InputsStyles.PRIMEFACES_MENU_STYLE_CLASS);
			selectOneMenu.setId(property.getSymbolicName());
			selectOneMenu.setFilter(true);
			selectOneMenu.setFilterMatchMode("contains");
			List<SelectItem> selectItems = new ArrayList<SelectItem>(property.getChoiceListItemsList().size());

			if (addEmptyFirstItem) {
				SelectItem selectItemEmpty = new SelectItem("", "اختر من القائمة");
				// selectItemEmpty.setDisabled(true);
				// selectItemEmpty.setNoSelectionOption(true);
				selectItems.add(selectItemEmpty);
			}

			for (ChoiceListItem choiceListItem : property.getChoiceListItemsList()) {
				selectItems.add(new SelectItem(choiceListItem.getValue(), choiceListItem.getValue()));
			}

			UISelectItems uiSelectItems = new UISelectItems();
			uiSelectItems.setValue(selectItems);
			selectOneMenu.getChildren().add(uiSelectItems);

			selectOneMenu.getAttributes().put(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME, InputsDataTypes.CHOICE_LIST);

			if (property.isMainChoiceList()) {

			}

		} catch (Exception e) {

		}

		return selectOneMenu;

	}

	/**
	 * Updates the select one menu items with the given new items and removes the
	 * old items
	 * 
	 * @param selectOneMenu
	 * @param selectItemsMap
	 * @param addEmptyFirstItem
	 * @return
	 */
	public static SelectOneMenu updateSelectOneMenuItems(SelectOneMenu selectOneMenu,
			Map<String, String> selectItemsMap, boolean addEmptyFirstItem) {

		selectOneMenu.getChildren().clear();

		List<SelectItem> selectItems = new ArrayList<SelectItem>(selectItemsMap.size());

		if (addEmptyFirstItem) {
			selectItems.add(new SelectItem("", ""));
		} else {
			selectOneMenu.getAttributes().put(InputsDataTypes.SELECT_MENU_NO_EMPTY_FIRST_ITEM_ATTR, true);
		}

		for (Map.Entry<String, String> entry : selectItemsMap.entrySet()) {
			selectItems.add(new SelectItem(entry.getValue(), entry.getKey()));
		}

		UISelectItems uiSelectItems = new UISelectItems();
		uiSelectItems.setValue(selectItems);
		selectOneMenu.getChildren().add(uiSelectItems);

		return selectOneMenu;

	}

	/**
	 * Removes the select items from the select menu
	 * 
	 * @param selectOneMenu
	 */
	public static void clearSelectOneMenu(SelectOneMenu selectOneMenu) {

		selectOneMenu.getChildren().clear();

		List<SelectItem> selectItems = new ArrayList<SelectItem>(1);
		selectItems.add(new SelectItem("", ""));

		UISelectItems uiSelectItems = new UISelectItems();
		uiSelectItems.setValue(selectItems);
		selectOneMenu.getChildren().add(uiSelectItems);

		selectOneMenu.setValue("");

	}

	/**
	 * Gets the UIComponent by its id
	 * 
	 * @param componentId
	 * @param components
	 * @return
	 */
	public static UIComponent getComponentById(String componentId, List<UIComponent> components) {

		for (UIComponent component : components) {
			if (component.getId().equalsIgnoreCase(componentId))
				return component;
		}

		return null;

	}

	public static UIComponent getComponentByIdPart(String componentIdPart, List<UIComponent> components) {

		for (UIComponent component : components) {
			if (component.getId().toLowerCase().contains(componentIdPart.toLowerCase()))
				return component;
		}

		return null;

	}

	/**
	 * Resets all the properties inputs
	 * 
	 * @param propertiesPanelGrid
	 */
	public static void clearPrpertiesInputs(HtmlPanelGrid propertiesPanelGrid) {
		for (UIComponent uiComponent : propertiesPanelGrid.getChildren()) {
			if (uiComponent instanceof UIInput) {
				clearUIInput(uiComponent);
			} else if (uiComponent instanceof HtmlPanelGroup) {
				HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) uiComponent;
				for (UIComponent uiComponent2 : htmlPanelGroup.getChildren()) {
					if (uiComponent2 instanceof UIInput) {
						clearUIInput(uiComponent2);
					}
				}
			}
		}

	}

	public static void clearUIInput(UIComponent uiComponent) {

		if (uiComponent instanceof HtmlInputText) {
			HtmlInputText htmlInputText = (HtmlInputText) uiComponent;
			if (htmlInputText.isDisabled()) {
				return;
			}
		}

		if (uiComponent instanceof InputText) {
			InputText inputText = (InputText) uiComponent;
			if (inputText.isDisabled()) {
				return;
			}
		}

		if (uiComponent instanceof UIInput) {
			UIInput input = (UIInput) uiComponent;
			System.out.println("###### input class: " + input.getClass().getName());
			if (input.getClass().getName().equals(Chips.class.getName())) {
				input.setValue(null);
			} else {
				input.setValue("");
			}

			// in case of select menu that don't have an empty first item
			if (input.getAttributes().get(InputsDataTypes.SELECT_MENU_NO_EMPTY_FIRST_ITEM_ATTR) != null) {
				SelectOneMenu selectOneMenu = (SelectOneMenu) input;
				clearSelectOneMenu(selectOneMenu);
			}
		}
	}

	private static List<String> getPropertiesToSelect(List<Property> properties) {
		List<String> propertiesToSelect = new ArrayList<String>();
		for (Property property : properties) {
			propertiesToSelect.add(property.getSymbolicName());
		}
		return propertiesToSelect;
	}

	// used in add,edit

	/**
	 * Extracts the value for each property input
	 * 
	 * @param propertiesPanelGrid
	 * @param documentClassName
	 * @param documentId
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getPropertiesInputsValues(HtmlPanelGrid propertiesPanelGrid,
			String documentClassName, String documentId) throws Exception {

		Map<String, Object> propertiesMap = new HashMap<String, Object>();

		for (UIComponent uiComponent : propertiesPanelGrid.getChildren()) {
			if (uiComponent instanceof UIInput && uiComponent instanceof HtmlInputHidden == false) {
				getPropertyNameAndValueFromUIInput(uiComponent, documentClassName, documentId, propertiesMap);
			} else if (uiComponent instanceof HtmlPanelGroup) {
				HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) uiComponent;
				for (UIComponent uiComponent2 : htmlPanelGroup.getChildren()) {
					if (uiComponent2 instanceof UIInput && uiComponent2 instanceof HtmlInputHidden == false) {
						getPropertyNameAndValueFromUIInput(uiComponent2, documentClassName, documentId, propertiesMap);
					}
				}
			}
		}

		return propertiesMap;

	}

	public static int extractDigitsFromString(String str) {
		String numberOnly = str.replaceAll("[^0-9]", "");
		return Integer.parseInt(numberOnly);
	}

	public static String extractLettersFromString(String str) {
		String lettersOnly = str.replaceAll("\\d", "");
		return lettersOnly;
	}

	private static void getPropertyNameAndValueFromUIInput(UIComponent uiComponent, String documentClassName,
			String documentId, Map<String, Object> propertiesMap) {

		String valueStr = "";
		Object valueObj = null;
		String dataType = "";
		String propertyName = "";
		UIInput input = (UIInput) uiComponent;
		propertyName = input.getId();
		if (input.getValue() != null && !input.getValue().toString().trim().equals("")) {
			valueStr = input.getValue().toString().trim();
			valueObj = valueStr;
		}
		if (input.getAttributes().get(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME) != null) {
			dataType = input.getAttributes().get(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME).toString().trim();
		}

		System.out.println("###### processing property: " + propertyName + ",dataType: " + dataType + ",input: "
				+ input.getClass());

		if (valueObj != null) {
			if (StringUtils.isNotBlank(dataType) && (dataType.equalsIgnoreCase(InputsDataTypes.HIJRI_DATE)
					|| dataType.equalsIgnoreCase(InputsDataTypes.GREG_DATE))) {
				valueStr = valueStr.replaceAll("/", "");
				valueObj = Integer.parseInt(valueStr);
			}

			if (StringUtils.isNotBlank(dataType) && dataType.equalsIgnoreCase(InputsDataTypes.INTEGER)) {
				try {
					valueStr = valueStr.replaceAll("\\.", "");
					valueObj = Integer.parseInt(valueStr);
				} catch (NumberFormatException nfe) {

					throw nfe;
				}
			}

			if (StringUtils.isNotBlank(dataType) && dataType.equalsIgnoreCase(InputsDataTypes.LONG)) {
				try {
					valueStr = valueStr.replaceAll("\\.", "");
					valueObj = Long.parseLong(valueStr);
				} catch (NumberFormatException nfe) {

					throw nfe;
				}
				valueObj = valueObj.toString(); // store it as string because
												// long is not supported data
												// type in filenet
			}

			if (StringUtils.isNotBlank(dataType) && dataType.equalsIgnoreCase(InputsDataTypes.MULTI_TEXT)) {
				if (input.getValue() != null) {
					System.out.println("######## value is: " + input.getValue());
					System.out.println("######## value class is: " + input.getValue().getClass());
					List<String> valuesList = (List<String>) input.getValue();
					valueObj = valuesList;
				}
			}

		}

		propertiesMap.put(propertyName, valueObj);

	}

	/**
	 * Validate that the passed hijri date is entered in valid format YYYY/MM/DD
	 * 
	 * @param dateStr
	 * @return
	 */
	public static boolean isValidDate(String dateStr) {
		// validate if there's value passed otherwise, the required validation
		// is taken care of from jsf side
		if (StringUtils.isNotBlank(dateStr)) {
			dateStr = dateStr.trim();
			if (!dateStr.contains("/") && dateStr.split("/").length != 3) {
				return false;
			}
			dateStr = dateStr.replaceAll("/", "");
			try {
				Integer.parseInt(dateStr);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param panel
	 * @param WhereStatements
	 * @return
	 */
	public static List<String> generateHijriDateSearchWhereStatements(UIPanel panel, List<String> WhereStatements,
			List<Property> propertiesList) {

		return generateHijriDateSearchWhereStatements(panel, WhereStatements, null, null, propertiesList);

	}

	public static List<String> generateGregDateSearchWhereStatements(UIPanel panel, List<String> WhereStatements,
			List<Property> propertiesList) {

		return generateGregDateSearchWhereStatements(panel, WhereStatements, null, null, propertiesList);

	}

	/**
	 * Generates the hijri date search where statement as less than and greater then
	 * conditions
	 * 
	 * @param panel
	 * @param WhereStatements
	 * @param criteras
	 * @param objectStore
	 * @param documentClassName
	 * @return
	 */
	public static List<String> generateHijriDateSearchWhereStatements(UIPanel panel, List<String> WhereStatements,
			List<SearchCriteria> criteras, String documentClassName, List<Property> propertiesList) {

		String whereStatement = "";
		String propertyName = "";
		String fromValue = "";
		String toValue = "";
		String todayDate = "123456789";
		boolean atLeastOneDateWasEntered = false;
		for (UIComponent searchComponenet : panel.getChildren()) {

			if (searchComponenet instanceof UIInput) {
				UIInput input = (UIInput) searchComponenet;
				propertyName = getHijriDateInputPropertyName(input.getId());
				if (input.getValue() != null && StringUtils.isNotBlank(input.getValue().toString())) {
					atLeastOneDateWasEntered = true;
					int date = Integer.parseInt(input.getValue().toString().replaceAll("/", ""));
					if (input.getId().contains(Constants.HIJRI_DATE_SEARCH_INPUT_FROM_CONTAINS)) {
						fromValue = input.getValue().toString();
						propertyName = input.getId().replaceAll(Constants.HIJRI_DATE_SEARCH_INPUT_FROM_CONTAINS, "");
						propertyName = getColumnName(propertyName, propertiesList);
						whereStatement = SearchService.constructIntWhere(propertyName,
								SearchService.GREATER_THAN_OR_EQUAL_CONDITION_KEYWORD, date);
						WhereStatements.add(whereStatement);
					} else if (input.getId().contains(Constants.HIJRI_DATE_SEARCH_INPUT_TO_CONTAINS)) {
						toValue = input.getValue().toString();
						propertyName = input.getId().replaceAll(Constants.HIJRI_DATE_SEARCH_INPUT_TO_CONTAINS, "");
						propertyName = getColumnName(propertyName, propertiesList);
						whereStatement = SearchService.constructIntWhere(propertyName,
								SearchService.LESS_THAN_OR_EQUAL_CONDITION_KEYWORD, date);
						WhereStatements.add(whereStatement);
					}
				}

			}
		}

		if (criteras != null && atLeastOneDateWasEntered) {
			if (StringUtils.isNotBlank(fromValue) && StringUtils.isBlank(toValue)) {
				toValue = todayDate;
			}

			if (StringUtils.isNotBlank(toValue) && StringUtils.isBlank(fromValue)) {
				fromValue = "oldestDate";
				// for some reason the to date order is reversed in this case
				String[] toValueArray = toValue.split("/");
				toValue = toValueArray[2] + "/" + toValueArray[1] + "/" + toValueArray[0];
			}

			String propertyDisplayName = "propertyDisplayName";
			String propertyValue = fromValue + " - " + toValue;
			criteras.add(new SearchCriteria(propertyDisplayName, "fromTo", propertyValue));
		}

		return WhereStatements;

	}

	public static String getColumnName(String propertyName, List<Property> propertiesList) {
		if (propertiesList == null)
			return propertyName;
		for (Property property : propertiesList) {
			if (property.getSymbolicName().equalsIgnoreCase(propertyName))
				return property.getColumnName();
		}
		return propertyName;
	}

	public static List<String> generateGregDateSearchWhereStatements(UIPanel panel, List<String> WhereStatements,
			List<SearchCriteria> criteras, String documentClassName, List<Property> propertiesList) {

		String whereStatement = "";
		String propertyName = "";
		String fromValue = "";
		String toValue = "";
		String todayDate = "1234123";
		boolean atLeastOneDateWasEntered = false;
		for (UIComponent searchComponenet : panel.getChildren()) {

			if (searchComponenet instanceof UIInput) {
				UIInput input = (UIInput) searchComponenet;
				propertyName = getGregDateInputPropertyName(input.getId());
				if (input.getValue() != null && StringUtils.isNotBlank(input.getValue().toString())) {
					atLeastOneDateWasEntered = true;
					int date = Integer.parseInt(input.getValue().toString().replaceAll("/", ""));
					if (input.getId().contains(Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS)) {
						fromValue = input.getValue().toString();
						propertyName = input.getId().replaceAll(Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS, "");
						propertyName = getColumnName(propertyName, propertiesList);
						whereStatement = SearchService.constructIntWhere(propertyName,
								SearchService.GREATER_THAN_OR_EQUAL_CONDITION_KEYWORD, date);
						WhereStatements.add(whereStatement);
					} else if (input.getId().contains(Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS)) {
						toValue = input.getValue().toString();
						propertyName = input.getId().replaceAll(Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS, "");
						propertyName = getColumnName(propertyName, propertiesList);
						whereStatement = SearchService.constructIntWhere(propertyName,
								SearchService.LESS_THAN_OR_EQUAL_CONDITION_KEYWORD, date);
						WhereStatements.add(whereStatement);
					}
				}

			}
		}

		if (criteras != null && atLeastOneDateWasEntered) {
			if (StringUtils.isNotBlank(fromValue) && StringUtils.isBlank(toValue)) {
				toValue = todayDate;
			}

			if (StringUtils.isNotBlank(toValue) && StringUtils.isBlank(fromValue)) {
				fromValue = "oldestDate";
				// for some reason the to date order is reversed in this case
				String[] toValueArray = toValue.split("/");
				toValue = toValueArray[2] + "/" + toValueArray[1] + "/" + toValueArray[0];
			}

			String propertyDisplayName = "propertyDisplayName";
			String propertyValue = fromValue + " - " + toValue;
			criteras.add(new SearchCriteria(propertyDisplayName, "fromTo", propertyValue));
		}

		return WhereStatements;

	}

	/**
	 * Generates the hijri date search where statement as less than and greater then
	 * conditions
	 * 
	 * @param panel
	 * @param WhereStatements
	 * @param criteras
	 * @param objectStore
	 * @param documentClassName
	 * @param propsColumnNames
	 * @return
	 */
	public static List<String> generateHijriDateSearchWhereStatementsDB(UIPanel panel, List<String> WhereStatements,
			List<SearchCriteria> criteras, String documentClassName, Map<String, String> propsColumnNames) {

		return WhereStatements;

	}

	public static List<String> generateGregDateSearchWhereStatementsDB(UIPanel panel, List<String> WhereStatements,
			List<SearchCriteria> criteras, String documentClassName, Map<String, String> propsColumnNames) {

		String whereStatement = "";
		String propertySymbolicName = "";
		String fromValue = "";
		String toValue = "";
		String todayDate = "21321231";
		boolean atLeastOneDateWasEntered = false;
		String columnName;
		for (UIComponent searchComponenet : panel.getChildren()) {

			if (searchComponenet instanceof UIInput) {
				UIInput input = (UIInput) searchComponenet;
				propertySymbolicName = getGregDateInputPropertyName(input.getId());
				if (input.getValue() != null && StringUtils.isNotBlank(input.getValue().toString())) {
					atLeastOneDateWasEntered = true;
					int date = Integer.parseInt(input.getValue().toString().replaceAll("/", ""));
					if (input.getId().contains(Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS)) {
						fromValue = input.getValue().toString();
						propertySymbolicName = input.getId().replaceAll(Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS,
								"");
						columnName = propsColumnNames.get(propertySymbolicName);
						whereStatement = SearchService.constructIntWhere(columnName,
								SearchService.GREATER_THAN_OR_EQUAL_CONDITION_KEYWORD, date);
						WhereStatements.add(whereStatement);
					} else if (input.getId().contains(Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS)) {
						toValue = input.getValue().toString();
						propertySymbolicName = input.getId().replaceAll(Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS,
								"");
						columnName = propsColumnNames.get(propertySymbolicName);
						whereStatement = SearchService.constructIntWhere(columnName,
								SearchService.LESS_THAN_OR_EQUAL_CONDITION_KEYWORD, date);
						WhereStatements.add(whereStatement);
					}
				}

			}
		}

		if (criteras != null && atLeastOneDateWasEntered) {
			if (StringUtils.isNotBlank(fromValue) && StringUtils.isBlank(toValue)) {
				toValue = todayDate;
			}

			if (StringUtils.isNotBlank(toValue) && StringUtils.isBlank(fromValue)) {
				fromValue = "oldestDate";
				// for some reason the to date order is reversed in this case
				String[] toValueArray = toValue.split("/");
				toValue = toValueArray[2] + "/" + toValueArray[1] + "/" + toValueArray[0];
			}

			String propertyDisplayName = "propertyDisplayName";
			String propertyValue = fromValue + " - " + toValue;
			criteras.add(new SearchCriteria(propertyDisplayName, "fromTo", propertyValue));
		}

		return WhereStatements;

	}

	private static String getHijriDateInputPropertyName(String inputId) {
		String propertyName = "";
		if (inputId.contains(Constants.HIJRI_DATE_SEARCH_INPUT_FROM_CONTAINS)) {
			propertyName = inputId.replaceAll(Constants.HIJRI_DATE_SEARCH_INPUT_FROM_CONTAINS, "");
		} else if (inputId.contains(Constants.HIJRI_DATE_SEARCH_INPUT_TO_CONTAINS)) {
			propertyName = inputId.replaceAll(Constants.HIJRI_DATE_SEARCH_INPUT_TO_CONTAINS, "");
		}
		return propertyName;
	}

	private static String getGregDateInputPropertyName(String inputId) {
		String propertyName = "";
		if (inputId.contains(Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS)) {
			propertyName = inputId.replaceAll(Constants.GREG_DATE_SEARCH_INPUT_FROM_CONTAINS, "");
		} else if (inputId.contains(Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS)) {
			propertyName = inputId.replaceAll(Constants.GREG_DATE_SEARCH_INPUT_TO_CONTAINS, "");
		}
		return propertyName;
	}

	/**
	 * Creates list of where statements for each property input
	 * 
	 * @param propertiesPanelGrid
	 * @return
	 */
	public static List<String> prepareSeachProperties(HtmlPanelGrid propertiesPanelGrid,
			List<Property> propertiesList) {

		List<String> propertiesWhereStatementsList = new ArrayList<String>();

		for (UIComponent uiComponent : propertiesPanelGrid.getChildren()) {

			if (uiComponent instanceof UIInput) {
				processUIComponentForSearch(uiComponent, propertiesWhereStatementsList, propertiesList);
			} else if (uiComponent instanceof HtmlPanelGroup) {
				// handle hijri date search from to
				HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) uiComponent;
				String id = htmlPanelGroup.getId();
				if (id.toLowerCase().contains(Constants.HIJRI_DATE_SEARCH_DIV_CONTAINS.toLowerCase())) {
					generateHijriDateSearchWhereStatements(htmlPanelGroup, propertiesWhereStatementsList,
							propertiesList);
				} else if (id.toLowerCase().contains(Constants.GREG_DATE_SEARCH_DIV_CONTAINS.toLowerCase())) {
					generateGregDateSearchWhereStatements(htmlPanelGroup, propertiesWhereStatementsList,
							propertiesList);
				}

			}

		}

		return propertiesWhereStatementsList;
	}

	public static void processUIComponentForSearch(UIComponent uiComponent, List<String> propertiesWhereStatementsList,
			List<Property> propertiesList) {

		UIInput input = null;
		if (uiComponent instanceof UIInput) {
			input = (UIInput) uiComponent;
		} else {
			return;
		}

		String whereStatement = "";
		String dataType = "";
		String value = "";
		String propertyName = input.getId();
		// create search statement for the property input only and not the
		// proeprty criteria dropdown
		if (propertyName.contains(Constants.CRITERIA_ID_PREFIX))
			return;

		if (input.getValue() != null && !input.getValue().toString().trim().equals(""))
			value = input.getValue().toString().trim();
		if (input.getAttributes().get(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME) != null) {
			dataType = input.getAttributes().get(InputsDataTypes.DATA_TYPE_ATTRIBUTE_NAME).toString();
		}

		if (StringUtils.isBlank(dataType)) {
			if (input.getClass().getSimpleName().equalsIgnoreCase(SelectOneMenu.class.getSimpleName())) {
				dataType = InputsDataTypes.CHOICE_LIST;
			}
		}

		if (StringUtils.isBlank(value))
			return;

		propertyName = getColumnName(propertyName, propertiesList);
		whereStatement = SearchService.createPropertyWhereStatement(propertyName, value, dataType, false);
		if (StringUtils.isNotBlank(whereStatement))
			propertiesWhereStatementsList.add(whereStatement);

	}

	public static void processCustomSearchInputs(UIComponent uiComponent, List<UIComponent> uIComponents,
			List<String> propertiesWhereStatementsList) {
		String whereStatement = null;
		UIInput input1 = null;
		if (uiComponent instanceof UIInput) {
			input1 = (UIInput) uiComponent;
		} else {
			return;
		}

		if (StringUtils.isNotBlank(whereStatement))
			propertiesWhereStatementsList.add(whereStatement);
	}

	// propertiesPanelGrid has the input and a dropdown for search critera for
	// the property
	// if the property is input text or input text area and not choice list and
	// not hijri date

	/**
	 * Creates list of where statements for each property in statistical report page
	 * to be used in filenet apis search
	 * 
	 * @param propertiesPanelGrid
	 * @param criterias
	 * @param objectStore
	 * @param documentClassName
	 * @return
	 * @throws Exception
	 */

	/**
	 * Creates list of where statements for each property in statistical report page
	 * to be used in database search
	 * 
	 * @param propertiesPanelGrid
	 * @param criterias
	 * @param objectStore
	 * @param documentClassName
	 * @param propsColumnNames
	 * @return
	 * @throws Exception
	 */

	/**
	 * Gets the selected search criteria for the given property
	 * 
	 * @param propertiesPanelGrid
	 * @param propertySymbolicName
	 * @return
	 */
	public static String getCriteriaValueForProperty(HtmlPanelGrid propertiesPanelGrid, String propertySymbolicName) {

		String value = "";
		String inputId = "";
		String criteriaInputId = Constants.CRITERIA_ID_PREFIX + propertySymbolicName;
		for (UIComponent uiComponent : propertiesPanelGrid.getChildren()) {
			if (uiComponent instanceof HtmlPanelGrid) {
				HtmlPanelGrid panelGrid = (HtmlPanelGrid) uiComponent;
				for (UIComponent columnElement : panelGrid.getChildren()) {
					if (columnElement instanceof SelectOneMenu == false) {
						continue;
					}
					inputId = "";
					value = "";
					UIInput input = (UIInput) columnElement;
					inputId = input.getId();
					if (inputId.equalsIgnoreCase(criteriaInputId)) {
						if (input.getValue() != null) {
							value = input.getValue().toString().trim();
							return value;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Generates search results table columns for the given document class
	 * properties </br>
	 * and add custom columns in case of displaying document versions page or in
	 * case of verify documents search page
	 * 
	 * @param propertiesList
	 * @param operationName
	 * @return
	 */
	public static List<ColumnModel> generateTableColumns(List<Property> propertiesList) {
		List<ColumnModel> columns = new ArrayList<ColumnModel>(propertiesList.size());
		for (Property property : propertiesList) {
			ColumnModel columnModel = new ColumnModel(property.getDisplayNameArabic(), property.getSymbolicName());
			columns.add(columnModel);
		}

		ColumnModel columnModel = new ColumnModel();
		columnModel.setTextOnly(false);
		String htmlContent = "<p:commandButton value=\"Hello\" />";
		columnModel.setHtmlContent(htmlContent);
		columns.add(columnModel);

		return columns;

	}

	/**
	 * Adds custom column in search result table in search,verify search,document
	 * versions pages </br>
	 * 1- <b>Search</b> : add custom colum for create date </br>
	 * 2- <b>Verify Search</b> : add custom colum for document status
	 * (approved/rejected) </br>
	 * 3- <b>Document Versions</b> : add custom colum for version number,version
	 * date and modify user </br>
	 * 
	 * @param columns
	 * @param operationName
	 */
	public static void addCustomColumnsToSearchResultTable(List<ColumnModel> columns, String operationName) {

	}

}
