package com.example.uvik.model;

import java.io.Serializable;
import java.util.List;

public class GetResponse implements Serializable {

    private NormalizedInput normalizedInput;
    private List<Offices> offices;
    private List<Officials> officials;

    public NormalizedInput getNormalizedInput() {
        return normalizedInput;
    }

    public void setNormalizedInput(NormalizedInput normalizedInput) {
        this.normalizedInput = normalizedInput;
    }

    public List<Offices> getOffices() {
        return offices;
    }

    public void setOffices(List<Offices> offices) {
        this.offices = offices;
    }

    public List<Officials> getOfficials() {
        return officials;
    }

    public void setOfficials(List<Officials> officials) {
        this.officials = officials;
    }
}
