package com.dms.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("localeBean")
@Scope("session")
public class LocaleBean implements Serializable {

	private Locale locale;

	@PostConstruct
	public void init() {
		locale = new Locale("ar");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public Locale getLocale() {
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public boolean isCurrentLocaleArabic() {
		return locale.getLanguage().equalsIgnoreCase("ar");
	}

	public boolean isCurrentLocaleEnglish() {
		return locale.getLanguage().equalsIgnoreCase("en");
	}

	public void setLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public void change(String lang) {
		try {
			locale = new Locale(lang);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
