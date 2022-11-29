package com.example.uvik.model;

import java.io.Serializable;

public class MainModelData implements Serializable {

    private String officeName;
    private String officialName;
    private String party;

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
