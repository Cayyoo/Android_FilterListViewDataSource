package com.example.filterlistviewdatasource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] arrStr;//城市名数组
    private ArrayAdapter arrayAdapter;
    private List<String> listStr=new ArrayList<>();//将数组转为集合

    private CharacterParser characterParser;//拼写转换

    private ClearEditText cet_edit;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListView();
        initFilter();
    }

    private void initListView() {
        //1、定义每个元素的内容
        arrStr = getResources().getStringArray(R.array.city_name);

        //将数组转换为集合
        for (String strCityName:arrStr) {
            listStr.add(strCityName);
            Collections.addAll(listStr);
        }

        //2、将元素属性及元素内容包装为ArrayAdapter，数组、集合均可做为数据源
        //arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listStr);
        //arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, listStr);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.tv_title, arrStr);

        //3、为ListView设置Adapter
        mListView = (ListView) findViewById(R.id.listview);

        assert mListView != null;
        mListView.setAdapter(arrayAdapter);
    }


    /**
     * 过滤搜索内容
     */
    private void initFilter() {
        cet_edit= (ClearEditText) this.findViewById(R.id.cet_edit);

        cet_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*cet_edit.setClearEditTextListener(new ClearEditText.ClearEditTextListener() {
            @Override
            public void onClear() {

            }
        });*/

    }

    /**
     * 过滤
     */
    public void filterData(String filterStr){
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        List<String> filterListStr=new ArrayList<>();//定义过滤后的数据源

        if (TextUtils.isEmpty(filterStr)){
            filterListStr=listStr;
        }else {
            filterListStr.clear();

            for (String string : listStr){
                if (string.contains(filterStr) || characterParser.getSelling(string).startsWith(filterStr)){
                    filterListStr.add(string);
                }
            }
        }

        //设置新的适配器
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.tv_title, filterListStr);
        mListView.setAdapter(arrayAdapter);
    }

}
