package com.codingending.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义SearchView
 * @author CodingEnding
 * 博客地址 https://blog.csdn.net/CodingEnding
 */

public class FairySearchView extends LinearLayout{
    private static final String TAG="FairySearchView";

    private boolean showBackButton=false;//是否显示左侧[返回]按钮
    private boolean showSearchIcon=true;//是否显示输入框左侧的[搜索]图标
    private boolean showClearButton=true;//是否在输入内容后展示[清除]按钮
    private boolean showCancelButton=true;//是否显示右侧的[取消]按钮

    private int backIcon=R.drawable.ic_back;//返回按钮图标
    private int searchIcon=R.drawable.ic_search_gray;//搜索按钮图标
    private int clearIcon=R.drawable.ic_clear;//清除按钮图标
    private String cancelText=getResources().getString(R.string.btn_cancel_text);
    private int cancelTextSize=getResources().getDimensionPixelSize(R.dimen.btn_cancel_text_size);
    private int cancelTextColor=getResources().getColor(R.color.btn_cancel_text);
    private String searchText="";
    private int searchTextSize=getResources().getDimensionPixelSize(R.dimen.edit_text_size);
    private int searchTextColor=getResources().getColor(R.color.edit_text);
    private String searchHint=getResources().getString(R.string.edit_hint);
    private int searchHintColor=getResources().getColor(R.color.edit_hint_text);
    private int searchViewHeight=getResources().getDimensionPixelSize(R.dimen.search_view_height);

    private int maxSearchLength =-1;//输入内容最大长度（默认不设限制）
    private int searchPaddingLeft=getResources().getDimensionPixelSize(R.dimen.edit_padding_left);//输入框左侧内边距（在没有左侧搜索图标时使用）

    private OnBackClickListener onBackClickListener;
    private OnClearClickListener onClearClickListener;
    private OnCancelClickListener onCancelClickListener;
    private OnEditChangeListener onEditChangeListener;
    private OnEnterClickListener onEnterClickListener;

    private View searchView;//主体View
    private ImageButton backBtn;
    private EditText searchEditText;
    private ImageButton clearBtn;
    private TextView cancelView;

    public FairySearchView(Context context) {
        this(context,null);
    }

    public FairySearchView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FairySearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        initViews(context);
    }

    //初始化属性
    private void initAttrs(Context context,AttributeSet attrs){
        if(attrs!=null){
            Resources resources=getResources();
            TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.FairySearchView);
            showBackButton=typedArray.getBoolean(R.styleable.FairySearchView_showBackButton,false);
            showSearchIcon=typedArray.getBoolean(R.styleable.FairySearchView_showSearchIcon,true);
            showCancelButton=typedArray.getBoolean(R.styleable.FairySearchView_showCancelButton,true);
            showClearButton=typedArray.getBoolean(R.styleable.FairySearchView_showClearButton,true);

            backIcon=typedArray.getResourceId(R.styleable.FairySearchView_backIcon,R.drawable.ic_back);
            searchIcon=typedArray.getResourceId(R.styleable.FairySearchView_searchIcon,R.drawable.ic_search_gray);
            clearIcon=typedArray.getResourceId(R.styleable.FairySearchView_clearIcon,R.drawable.ic_clear);
            cancelText=getOrDefault(typedArray.getString(R.styleable.FairySearchView_cancelText),resources.getString(R.string.btn_cancel_text));
            cancelTextSize=typedArray.getDimensionPixelSize(R.styleable.FairySearchView_cancelTextSize,resources.getDimensionPixelSize(R.dimen.btn_cancel_text_size));
            cancelTextColor=typedArray.getColor(R.styleable.FairySearchView_cancelTextColor,resources.getColor(R.color.btn_cancel_text));
            searchText=typedArray.getString(R.styleable.FairySearchView_searchText);
            searchTextSize=typedArray.getDimensionPixelSize(R.styleable.FairySearchView_searchTextSize,resources.getDimensionPixelSize(R.dimen.edit_text_size));
            searchTextColor=typedArray.getColor(R.styleable.FairySearchView_searchTextColor,resources.getColor(R.color.edit_text));
            searchHint=getOrDefault(typedArray.getString(R.styleable.FairySearchView_searchHint),resources.getString(R.string.edit_hint));
            searchHintColor=typedArray.getColor(R.styleable.FairySearchView_searchHintColor,resources.getColor(R.color.edit_hint_text));
            searchViewHeight=typedArray.getDimensionPixelSize(R.styleable.FairySearchView_searchViewHeight,resources.getDimensionPixelSize(R.dimen.search_view_height));
            maxSearchLength=typedArray.getInteger(R.styleable.FairySearchView_maxSearchLength,-1);//默认不限制

            typedArray.recycle();//回收资源，否则再次使用会出错
        }
    }

    //初始化Views
    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_fairy_search_view,this,true);
        searchView=findViewById(R.id.layout_search_view);
        backBtn=findViewById(R.id.btn_back);
        searchEditText=findViewById(R.id.edit_search);
        clearBtn=findViewById(R.id.btn_clear);
        cancelView=findViewById(R.id.text_view_cancel);

        //设置资源
        backBtn.setImageResource(backIcon);
        clearBtn.setImageResource(clearIcon);
        cancelView.setTextSize(TypedValue.COMPLEX_UNIT_PX,cancelTextSize);
        cancelView.setTextColor(cancelTextColor);
        cancelView.setText(cancelText);
        searchEditText.setTextColor(searchTextColor);
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,searchTextSize);
        searchEditText.setText(searchText);
        searchEditText.setHintTextColor(searchHintColor);
        searchEditText.setHint(searchHint);
        limitEditLength(maxSearchLength);//限制输入内容最大长度（默认不限制）
        limitSearchViewHeight(searchViewHeight);//设置输入框高度

        //显示或隐藏控件
        backBtn.setVisibility(showBackButton?VISIBLE:GONE);
        cancelView.setVisibility(showCancelButton?VISIBLE:GONE);
        showOrHideSearchIcon(searchEditText,showSearchIcon,searchIcon);
        showOrHideClearButton(clearBtn,showClearButton,searchText);

        //设置监听器
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBackClickListener!=null){
                    onBackClickListener.onClick();
                }
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)&&showClearButton){
                    clearBtn.setVisibility(VISIBLE);
                }else{
                    clearBtn.setVisibility(GONE);
                }
                if(onEditChangeListener!=null){
                    onEditChangeListener.onEditChanged(s.toString());
                }
            }
        });
        //输入法右下角回车/搜索按钮被点击
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v,int actionId,KeyEvent event) {
                if(onEnterClickListener!=null&&actionId==EditorInfo.IME_ACTION_SEARCH){
                    onEnterClickListener.onEnterClick(searchEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });
        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClearClickListener!=null){
                    onClearClickListener.onClick(searchEditText.getText().toString());
                }else{
                    clear();//默认实现
                }
            }
        });
        cancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCancelClickListener!=null){
                    onCancelClickListener.onClick();
                }
            }
        });
    }

    private void showOrHideClearButton(ImageButton button,boolean isShow,String text){
        if(isShow&&!TextUtils.isEmpty(text)){
            button.setVisibility(VISIBLE);
        }else{
            button.setVisibility(GONE);
        }
    }

    private void showOrHideSearchIcon(EditText view,boolean isShow,int imageRes){
        if(isShow){
            view.setCompoundDrawablesWithIntrinsicBounds(imageRes,0,0,0);
            view.setPadding(0,0,0,0);//清除内边距
        }else{
            view.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            view.setPadding(searchPaddingLeft,0,0,0);//设置左侧内边距
        }
    }

    //如果目标字符串为空，就获取一个默认字符
    private String getOrDefault(String target,String defaultStr){
        if(TextUtils.isEmpty(target)){
            return defaultStr;
        }
        return target;
    }

    //清除输入框中的内容
    private void clear(){
        searchText="";
        searchEditText.setText("");
    }

    //设置输入框的最大内容长度
    private void limitEditLength(int length){
        if(length>0){
            searchEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSearchLength)});
        }
    }

    //设置输入框高度
    private void limitSearchViewHeight(int searchViewHeight){
        LayoutParams layoutParams= (LayoutParams) searchView.getLayoutParams();
        layoutParams.height=searchViewHeight;
        searchView.setLayoutParams(layoutParams);
    }

    /************************************向外界暴露的方法*******************************/

    //设置输入内容
    public void setSearchText(String text){
        searchText=text;
        searchEditText.setText(text);
    }

    //获得输入内容
    public String getSearchText(){
        return searchEditText.getText().toString();
    }

    /**
     * 设置输入内容的文字大小
     * @param searchTextSize 文字大小（单位为px）
     */
    public void setSearchTextSize(int searchTextSize) {
        this.searchTextSize = searchTextSize;
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,searchTextSize);
    }

    public void setSearchTextColor(@ColorInt int searchTextColor) {
        this.searchTextColor = searchTextColor;
        searchEditText.setTextColor(searchTextColor);
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
        searchEditText.setHint(searchHint);
    }

    public void setSearchHintColor(int searchHintColor) {
        this.searchHintColor = searchHintColor;
        searchEditText.setHintTextColor(searchHintColor);
    }

    /**
     * 设置SearchView的高度（单位为px）
     */
    public void setSearchViewHeight(int searchViewHeight) {
        this.searchViewHeight = searchViewHeight;
        limitSearchViewHeight(searchViewHeight);
    }

    public int getMaxSearchLength() {
        return maxSearchLength;
    }

    //限制输入内容的最大长度
    public void setMaxSearchLength(int maxSearchLength) {
        if(maxSearchLength >0){
            this.maxSearchLength = maxSearchLength;
            limitEditLength(maxSearchLength);
        }
    }

    public void setBackIcon(@DrawableRes int backIcon) {
        this.backIcon = backIcon;
        backBtn.setImageResource(backIcon);
    }

    public void setSearchIcon(@DrawableRes int searchIcon) {
        this.searchIcon = searchIcon;
        searchEditText.setCompoundDrawablesWithIntrinsicBounds(searchIcon,0,0,0);
    }

    public void setClearIcon(@DrawableRes int clearIcon) {
        this.clearIcon = clearIcon;
        clearBtn.setImageResource(clearIcon);
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
        cancelView.setText(cancelText);
    }

    /**
     * 设置取消按钮的文字大小
     * @param cancelTextSize 文字大小（单位为px）
     */
    public void setCancelTextSize(int cancelTextSize) {
        this.cancelTextSize = cancelTextSize;
        cancelView.setTextSize(TypedValue.COMPLEX_UNIT_PX,cancelTextSize);
    }

    /**
     * 设置取消按钮的文字颜色
     */
    public void setCancelTextColor(@ColorInt int cancelTextColor) {
        this.cancelTextColor = cancelTextColor;
        cancelView.setTextColor(cancelTextColor);
    }

    public boolean isShowBackButton() {
        return showBackButton;
    }

    public void setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        backBtn.setVisibility(showBackButton?VISIBLE:GONE);
    }

    public boolean isShowSearchIcon() {
        return showSearchIcon;
    }

    public void setShowSearchIcon(boolean showSearchIcon) {
        this.showSearchIcon = showSearchIcon;
        showOrHideSearchIcon(searchEditText,showSearchIcon,searchIcon);
    }

    public boolean isShowClearButton() {
        return showClearButton;
    }

    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;
        clearBtn.setVisibility(showClearButton?VISIBLE:GONE);
    }

    public boolean isShowCancelButton() {
        return showCancelButton;
    }

    public void setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        cancelView.setVisibility(showCancelButton?VISIBLE:GONE);
    }


    /***************************设置监听器********************************/
    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public void setOnClearClickListener(OnClearClickListener onClearClickListener) {
        this.onClearClickListener = onClearClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public void setOnEditChangeListener(OnEditChangeListener onEditChangeListener) {
        this.onEditChangeListener = onEditChangeListener;
    }

    public void setOnEnterClickListener(FairySearchView.OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }

    //清除所有监听器
    public void clearListeners(){
        onBackClickListener=null;
        onCancelClickListener=null;
        onClearClickListener=null;
        onEditChangeListener=null;
        onEnterClickListener=null;
    }

    /**
     * 监听返回按钮点击事件
     */
    public interface OnBackClickListener{
        void onClick();
    }

    /**
     * 监听清除按钮点击事件
     */
    public interface OnClearClickListener{
        /**
         * @param oldContent 被删除的输入框内容
         */
        void onClick(String oldContent);
    }

    /**
     * 监听取消按钮点击事件
     */
    public interface OnCancelClickListener{
        void onClick();
    }

    /**
     * 监听输入框内容变化
     */
    public interface OnEditChangeListener{
        /**
         * @param nowContent 输入框当前的内容
         */
        void onEditChanged(String nowContent);
    }

    /**
     * 监听用户点击了虚拟键盘中右下角的回车/搜索键
     * 此时可以选择执行搜索操作
     */
    public interface OnEnterClickListener{
        /**
         * @param content 输入框中的内容
         */
        void onEnterClick(String content);
    }

}
