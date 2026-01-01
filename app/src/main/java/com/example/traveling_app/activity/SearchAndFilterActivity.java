package com.example.traveling_app.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.traveling_app.R;
import com.example.traveling_app.fragment.FilterFragment;
import com.example.traveling_app.fragment.SearchFragment;
import com.example.traveling_app.fragment.SearchResultFragment;
import com.example.traveling_app.model.filter.FilterItem;
import com.example.traveling_app.model.filter.FilterItemGroup;
import com.example.traveling_app.model.filter.IntegerRangeFilterItem;
import com.example.traveling_app.model.filter.KeywordFilterItem;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class SearchAndFilterActivity extends AppCompatActivity {

    private final ArrayList<FilterItemGroup> filterGroups = new ArrayList<>();
    private final SearchFragment searchFragment = new SearchFragment();
    private final FilterFragment filterFragment = new FilterFragment();
    private final SearchResultFragment searchResultFragment = new SearchResultFragment();
    private ArrayList<String> recentSearch;
    private static final Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private static final int MAX_RECENT_SEARCH = 5;
    private static final String RECENT_SEARCH_SHARED_REF_KEY = "recentSearch";
    public static final String TYPE_FILTER_PARAMS = "type_filter";

    private String keyword = "";

    public SearchAndFilterActivity() {
        FilterItemGroup prices = new FilterItemGroup("salePrice", "Giá cả"),
                type = new FilterItemGroup("type", "Loại hình"),
                province = new FilterItemGroup("address", "Tỉnh thành");

        new IntegerRangeFilterItem(prices, 500000, 1000000, "VND");
        new IntegerRangeFilterItem(prices, 1000000, 2000000, "VND");
        new IntegerRangeFilterItem(prices, 2000000, 3000000, "VND");
        new KeywordFilterItem(province, "Đà Nẵng");
        new KeywordFilterItem(province, "Hà Nội");
        new KeywordFilterItem(province, "Phú Quốc");
        new KeywordFilterItem(type, "Núi");
        new KeywordFilterItem(type, "Biển");
        new KeywordFilterItem(type, "Văn hóa");
        new KeywordFilterItem(type, "Đảo");
        filterGroups.add(province);
        filterGroups.add(prices);
        filterGroups.add(type);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        FragmentManager fragmentManager = getSupportFragmentManager();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(TYPE_FILTER_PARAMS) != null) {
            String keyword = getIntent().getStringExtra(TYPE_FILTER_PARAMS);
            FilterItemGroup typeFilterItemGroup = new FilterItemGroup("type", null);
            FilterItem filterItem = new KeywordFilterItem(typeFilterItemGroup, keyword);
            filterItem.selectSelf();
            filterGroups.add(typeFilterItemGroup);
            fragmentManager.beginTransaction().replace(R.id.content, searchResultFragment).commit();
            return;
        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        recentSearch = gson.fromJson(sharedPreferences.getString(RECENT_SEARCH_SHARED_REF_KEY, "[]"), ArrayList.class);

        fragmentManager.beginTransaction().replace(R.id.content, searchFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public Stream<FilterItem> getStreamOfSelectedFilterItem() {
        return filterGroups.stream().map(FilterItemGroup::getSelectedItem).filter(Objects::nonNull);
    }


    public Stream<String> getStreamOfRecentSearch() {
        return recentSearch.stream();
    }


    public void switchToFilterFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, filterFragment).addToBackStack(null).commit();
    }

    public void switchToSearchResultFragment(String keyword) {
        if (keyword.length() > 0 && !recentSearch.contains(keyword)) {
            if (recentSearch.size() >= MAX_RECENT_SEARCH)
                recentSearch.remove(MAX_RECENT_SEARCH - 1);
            recentSearch.add(0, keyword);
            sharedPreferences.edit().putString(RECENT_SEARCH_SHARED_REF_KEY, gson.toJson(recentSearch)).commit();
        }

        this.keyword = keyword.toLowerCase();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, searchResultFragment).addToBackStack(null).commit();
    }

    public Stream<FilterItemGroup> getStreamOfFilterItemGroups() {
        return filterGroups.stream();
    }


    public String getKeyword() {
        return keyword;
    }
    public void clearRecentSearch() {
        recentSearch.clear();
        sharedPreferences.edit().putString(RECENT_SEARCH_SHARED_REF_KEY, gson.toJson(recentSearch)).commit();
    }


}