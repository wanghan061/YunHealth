package com.coder.yun.view;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.coder.yun.R;

public class SearchView extends LinearLayout implements View.OnClickListener {


    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    // private ImageView ivDelete;
    private ImageView search;

    /**
     * 返回按钮
     */


    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 弹出列表
     */
    private ListView lvTips;

    /**
     * 提示adapter （推荐adapter）
     */
    private ArrayAdapter<String> mHintAdapter;

    /**
     * 自动补全adapter 只显示名字
     */
    private ArrayAdapter<String> mAutoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        initViews();
    }

    private void initViews(){
        etInput = (EditText) findViewById(R.id.search_et_input);
        search = (ImageView) findViewById(R.id.search_in);

        //        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //set edit text
//                String text = lvTips.getAdapter().getItem(i).toString();
//                etInput.setText(text);
//                etInput.setSelection(text.length());
//                //hint list view gone and result list view show
//                lvTips.setVisibility(View.GONE);
//                notifyStartSearching(text);
//            }
//        });

        search.setOnClickListener(this);
    }

    //        etInput.addTextChangedListener(new EditChangedListener());
//       // etInput.setOnClickListener(this);
//        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    lvTips.setVisibility(GONE);
//                    notifyStartSearching(etInput.getText().toString());
//                }
//                return true;
//            }
//        });
//    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
//    private void notifyStartSearching(String text){
//        if (mListener != null) {
//            mListener.onSearch(etInput.getText().toString());
//        }
//        //隐藏软键盘
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }

    /**
     * 设置热搜版提示 adapter
     */
//    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
//        this.mHintAdapter = adapter;
//        if (lvTips.getAdapter() == null) {
//            lvTips.setAdapter(mHintAdapter);
//        }
//    }

    public  void setHint(Context context,String s){
        etInput.setHint(s);
    }



    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    private class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 提示列表项点击时回调方法 (提示/自动补全)
//         */
//        void onTipsItemClick(String text);
    }

}
