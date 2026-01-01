package com.example.traveling_app.model.filter;

public class KeywordFilterItem extends FilterItem {
    private final String value;
    public KeywordFilterItem(FilterItemGroup filterItemGroup, String value) {
        this.value = value;
	    this.filterItemGroup = filterItemGroup;
    	index = filterItemGroup.addFilterItem(this);
    }

    @Override
    public boolean isSatisfied(Object obj) {
        if (obj instanceof String value)
            return this.value.equals(value);
        return false;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KeywordFilterItem keywordFilterItem)
            return keywordFilterItem.ofGroup() == this.ofGroup() && this.value.equals(keywordFilterItem.value);
        return false;
    }
    
}
