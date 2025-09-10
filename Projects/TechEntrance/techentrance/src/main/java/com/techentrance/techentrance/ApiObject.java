package com.techentrance.techentrance;

import java.util.*;

public class ApiObject {
    public String contents;
    public String names;
    public String types;
    public String publication_date;
    public String short_name;
    public String model_type;
    public String ID;
    public List<String> locations;

    public List<String> categories;
    public List<String> levels;
    public List<String> tags;
    public String refs;
    public String company;

    public ApiObject() {

    }
    public ApiObject(String contents, String names, String types, String publication_date, String short_name, String model_type, String ID, List<String> locations, List<String> categories, List<String> levels, List<String>  tags, String refs, String company) {
        this.contents = contents;
        this.names = names;
        this.types = types;
        this.publication_date = publication_date;
        this.short_name = short_name;
        this.model_type = model_type;
        this.ID = ID;
        this.locations = locations;
        this.categories = categories;
        this.levels = levels;
        this.tags = tags;
        this.refs = refs;
        this.company = company;
    }
}
