package com.example.traveling_app.model.filter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

// Group FilterItem by category;
public class FilterItemGroup {
    private final String key;
    private final String title;
    private int selectedItemIndex = -1;
    private final ArrayList<FilterItem> filterItems;
    private Consumer<Integer> onSelectedItemChanged;
    private Consumer<Integer> onItemUnselected;

    private Consumer<FilterItem> onItemAdded = f -> {};

    public FilterItemGroup(String key, String title) {
        this.key = key;
        this.title = title;
        filterItems = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public String getTitle() { return title; }

    public FilterItem get(int index) {
        return filterItems.get(index);
    }

    public int size() {
        return filterItems.size();
    }

    public FilterItem getSelectedItem() {
        if (selectedItemIndex == -1)
            return null;
        return filterItems.get(selectedItemIndex);
    }

    public void changeSelectedItem(FilterItem filterItem) throws IllegalArgumentException {
        if (filterItem.ofGroup() != this)
            throw new IllegalArgumentException(filterItem + " doesn't belong to " + getTitle());
        int index = filterItem.getIndexInGroup();
        changeSelectedItem(index);
    }


    public void changeSelectedItem(int newIndex) throws IllegalArgumentException {
        if (newIndex >= filterItems.size())
            throw new IndexOutOfBoundsException("Allow range is [" + 0 + " - " + size() + "]");
        int previousIndex = selectedItemIndex;
        if (previousIndex != -1)
            unselectItem();
        filterItems.get(newIndex).setSelected(true);
        selectedItemIndex = newIndex;
        if (onSelectedItemChanged != null)
            onSelectedItemChanged.accept(newIndex);
    }

    public boolean unselectItem() {
        int previousIndex = selectedItemIndex;
        if (previousIndex == -1)
            return false;
        filterItems.get(previousIndex).setSelected(false);
        selectedItemIndex = -1;
        if (onItemUnselected != null)
            onItemUnselected.accept(previousIndex);
        return true;
    }

    public void setOnSelectedItemChanged(Consumer<Integer> listener) {
        this.onSelectedItemChanged = listener;
    }

    public void setOnItemUnselected(Consumer<Integer> listener) {
        this.onItemUnselected = listener;
    }

    public void setOnItemAddedItem(Consumer<FilterItem> listener) {
        this.onItemAdded = listener;
        if (listener != null)
            filterItems.forEach(onItemAdded);
    }

    public boolean isSatisfied(Object value) {
        if (selectedItemIndex == -1)
            return true;
        return filterItems.get(selectedItemIndex).isSatisfied(value);
    }

    int addFilterItem(FilterItem filterItem) {
        Optional<Integer> newIndex = filterItems.stream().filter(f -> f.equals(filterItem)).findFirst().map(f -> f.getIndexInGroup());

        if (newIndex.isPresent()) {
            return newIndex.get();
        }
        else {
            filterItems.add(filterItem);
            if (onItemAdded != null)
                onItemAdded.accept(filterItem);
            return filterItems.size() - 1;
        }

    }
}