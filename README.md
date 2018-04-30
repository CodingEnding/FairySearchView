# FairySearchView

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![](https://jitpack.io/v/CodingEnding/FairySearchView.svg)](https://jitpack.io/#CodingEnding/FairySearchView)
[![MinSdk](https://img.shields.io/badge/MinSDK-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

FairySearchView是经过封装的通用搜索控件，可以根据需求切换不同的布局模式。内置多种事件监听器，可以满足不同场景的业务需求，使用方式非常灵活。

## 效果预览

| 显示所有控件（无输入内容）  | 显示所有控件（有输入内容） |
| :----------: |:-------:|
| ![](https://i.imgur.com/x632L77.png)| ![](https://i.imgur.com/6cM7Rl8.png) |

| 点击了回车/搜索 | 不显示返回按钮 |
| :----------: |:-------:|
| ![](https://i.imgur.com/pa17mY5.png) | ![](https://i.imgur.com/WQnbTbI.png) |

| 不显示取消按钮 | 不显示返回/取消按钮 |
| :----------: |:-------:|
| ![](https://i.imgur.com/ydK7T59.png) | ![](https://i.imgur.com/ogotbYb.png) |

## Gradle

[![](https://jitpack.io/v/CodingEnding/FairySearchView.svg)](https://jitpack.io/#CodingEnding/FairySearchView)

```
//根项目下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

//主项目下的build.gradle
dependencies {
    implementation 'com.github.CodingEnding:FairySearchView:1.01'
}
```

## Usage

### 基本使用

搜索栏一般嵌套在Toolbar中使用，如下：

```
<!-- 将FairySearView嵌套在Toolbar中使用（也可以单独使用） -->
<android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    android:background="?attr/colorPrimary"
    android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
    app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
    app:contentInsetStart="0dp">

    <com.codingending.library.FairySearchView
        android:id="@+id/search_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:searchViewHeight="55dp"
        app:showSearchIcon="true"
        app:showBackButton="true"
        app:showClearButton="true"
        app:showCancelButton="true" />

</android.support.v7.widget.Toolbar>
```

**提示：** Toolbar默认有`16dp`的左边距，这会影响UI效果，可以通过设置`app:contentInsetStart="0dp"`取消这个边距。

当然，FairySearchView也可以作为一个普通控件使用。注意，FairySearchView默认并没有设置背景，如果将其作为普通控件请设置`android:background`属性。

### 相关属性

| 属性名  | 类型  | 默认值 | 说明 |
| :---------- |:-------|:-----|:-----|
| app:searchViewHeight | dimension | 52dp | 中间输入区域的高度 |
| app:maxSearchLength | integer | x | 输入框的最大文字长度 |
| app:showBackButton | boolean | false | 是否显示左侧返回按钮 |
| app:backIcon | drawable | x | 左侧返回按钮的图标 |
| app:showSearchIcon | boolean | true | 是否显示输入框左侧的搜索按钮 |
| app:backIcon | drawable | x | 输入框左侧的搜索按钮图标 |
| app:showClearButton | boolean | true | 是否显示输入框右侧的清除按钮 |
| app:clearIcon | drawable | x | 输入框右侧的清除按钮图标 |
| app:showCancelButton | boolean | true | 是否显示右侧的取消按钮 |
| app:cancelText | string | 取消 | 右侧取消按钮显示的文字 |
| app:cancelTextSize | dimension | 16sp | 右侧取消按钮的文字大小 |
| app:cancelTextColor | color | #fff | 右侧取消按钮的文字颜色 |
| app:searchText | string | x | 输入框的内容 |
| app:searchTextSize | dimension | 14sp | 输入框的的文字大小 |
| app:searchTextColor | color | #212121 | 输入框的的文字颜色 |
| app:searchHint | string | x | 输入框的提示文字 |
| app:searchHintColor | color | #aaa | 输入框的的提示文字颜色 |

### 监听器

FairySearchView内置了多个事件监听器，可以根据实际需求灵活设置。

**监听左侧返回按钮的点击事件：**

```
fairySearchView.setOnBackClickListener(new FairySearchView.OnBackClickListener() {
    @Override
    public void onClick() {
        //TODO
    }
});
```

**监听清除按钮的点击事件：**

```
fairySearchView.setOnClearClickListener(new FairySearchView.OnClearClickListener() {
    @Override
    public void onClick(String oldContent) {
        //oldContent：被清除的内容
    }
});
```

**说明：** 默认情况下点击清除按钮会清空输入框中的内容，如果没有特殊的需求请不要设置这个监听器。

**监听右侧取消按钮的点击事件：**

```
fairySearchView.setOnCancelClickListener(new FairySearchView.OnCancelClickListener() {
    @Override
    public void onClick() {
        //TODO
    }
});
```

**监听输入框内容的变化：**

```
fairySearchView.setOnEditChangeListener(new FairySearchView.OnEditChangeListener() {
    @Override
    public void onEditChanged(String nowContent) {
        //nowContent：输入框中的内容
    }
});
```

**监听虚拟键盘的右下角回车/搜索按键点击事件（此时可以执行搜索）：**

```
fairySearchView.setOnEnterClickListener(new FairySearchView.OnEnterClickListener() {
    @Override
    public void onEnterClick(String content) {
        //content：输入框中的内容
    }
});
```

### 相关方法

FairySearchView为大部分属性提供了对应的getter/setter方法，下面列出其中的一部分，其他的方法也基本类似。

| 方法名  | 返回值| 说明 |
| :---------|:-----|:-----|
| setSearchText(String text) | void | 设置输入内容 |
| getSearchText | string | 获得输入内容 |
| setSearchTextSize(int searchTextSize) | void | 设置输入文字大小（px） |
| setSearchTextColor(int searchTextColor) | void | 设置输入文字颜色 |
| setSearchHint(String searchHint) | void | 设置提示文字 |
| setSearchHintColor(int searchHintColor) | void | 设置提示文字颜色 |
| setSearchViewHeight(int searchViewHeight) | void | 设置输入区域高度（px） |
| setMaxSearchLength(int maxSearchLength) | void | 限制输入内容的最大长度 |
| setBackIcon(int backIcon) | void | 设置返回按钮的图标 |
| setShowBackButton(boolean showBackButton) | void | 设置是否显示返回按钮 |
| ......... | .......... | .......... |

## Blog

欢迎访问我的博客：

> [https://blog.csdn.net/codingending](https://blog.csdn.net/codingending)

## License


    Copyright CodingEnding

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
