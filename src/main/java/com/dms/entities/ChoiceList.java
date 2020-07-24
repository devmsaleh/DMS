package com.dms.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ChoiceList implements Serializable {

    private static final long serialVersionUID = -6672905513651418297L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(nullable = false, unique = true)
    private String displayNameArabic;

    private String displayNameEnglish;

    private boolean mainSub;

    @OneToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLastModified;

    @OneToOne(fetch = FetchType.LAZY)
    private User lastModifiedBy;

    @OneToMany(mappedBy = "choiceList", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChoiceListItem> itemsList = new HashSet<ChoiceListItem>();

    public ChoiceList() {

    }

    public ChoiceList(Long id) {
        this.id = id;
    }

    public Set<ChoiceListItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(Set<ChoiceListItem> itemsList) {
        this.itemsList = itemsList;
    }

    public String getDisplayNameArabic() {
        return displayNameArabic;
    }

    public void setDisplayNameArabic(String displayNameArabic) {
        this.displayNameArabic = displayNameArabic;
    }

    public String getDisplayNameEnglish() {
        return displayNameEnglish;
    }

    public void setDisplayNameEnglish(String displayNameEnglish) {
        this.displayNameEnglish = displayNameEnglish;
    }

    public boolean isMainSub() {
        return mainSub;
    }

    public void setMainSub(boolean mainSub) {
        this.mainSub = mainSub;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}
