package com.umeng.soexample.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.core.StaticValue;
import com.android.core.base.AbsBaseFragment;
import com.android.core.listener.ThemeChangeListener;
import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.heaton.liulei.utils.utils.SPUtils;
import com.heaton.liulei.utils.utils.ScreenUtils;
import com.umeng.soexample.App;
import com.umeng.soexample.Constants;
import com.umeng.soexample.R;
import com.umeng.soexample.activity.SetPatternActivity;
import com.umeng.soexample.activity.SuggestActivity;
import com.umeng.soexample.custom.TextDrawable;
import com.umeng.soexample.custom.ToggleButton;
import com.umeng.soexample.manager.ConfigManage;
import com.umeng.soexample.utils.GlideCacheUtil;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author: liulei
 * @date: 2016-10-24 09:06
 */
public class SetFragment extends AbsBaseFragment implements View.OnClickListener {

    @Bind(R.id.cache_size)
    TextView cache_size;
    @Bind(R.id.text_exit)
    TextDrawable exit;
    @Bind(R.id.appbar_setting)
    AppBarLayout mAppbarSetting;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toggle_mode)
    ToggleButton toggle_mode;
    @Bind(R.id.toggle_show_img)
    ToggleButton toggle_show;


    ThemeChangeListener listener;

    public void setThemeChangeLisenter(ThemeChangeListener lisenter){
        this.listener = lisenter;
    }

    private CompositeSubscription mSubscriptions;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ThemeChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    protected void onInitView() {
        initToolBar();
        mSubscriptions = new CompositeSubscription();
//        long size = DataCleanManager.getPhotoCacheSize(getContext());
        cache_size.setText(getCachedSize());
        exit.setTextColor(StaticValue.color);

        toggle_mode.setToggleOn(false);
        toggle_mode.setOnColor(StaticValue.color);
        toggle_mode.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    showMessage("暂未开发夜间模式,敬请期待");
                }else {
                    showMessage("切换到正常模式");
                }
            }
        });

        if(!ConfigManage.INSTANCE.isListShowImg()){
            toggle_show.setToggleOn(false);
        }
        toggle_show.setOnColor(StaticValue.color);
        toggle_show.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    ConfigManage.INSTANCE.setListShowImg(getActivity(),true);
                }else {
                    ConfigManage.INSTANCE.setListShowImg(getActivity(),false);
                }
            }
        });
    }

    //自己新添加的
    private void initToolBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mAppbarSetting.setPadding(
//                    mAppbarSetting.getPaddingLeft(),
//                    mAppbarSetting.getPaddingTop() + ScreenUtils.getStatusBarHeight(getActivity()),
//                    mAppbarSetting.getPaddingRight(),
//                    mAppbarSetting.getPaddingBottom());
//        }
        mAppbarSetting.setBackgroundColor(StaticValue.color);
        toolbar.setBackgroundColor(StaticValue.color);
        if (toolbar != null) {
            ((TextView) toolbar.findViewById(com.android.core.R.id.toolbar_title)).setText("设置");
        }
    }

    /**
     * 获取 Glide 已经缓存的大小
     */
    private String getCachedSize() {
        return GlideCacheUtil.getInstance().getCacheSize(App.getInstance());
    }

    @OnClick(R.id.rl_language)
    void language() {
        Snackbar.make(cache_size,"该功能尚未开发",Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rl_app_update)
    void update() {
        Snackbar.make(cache_size,"该功能尚未开发",Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rl_theme)
    void theme() {
        final AlertDialog dialog = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .initialColor(StaticValue.color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(9)
                .showColorPreview(true)
                .lightnessSliderOnly()
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int color, Integer[] allColors) {
                        SPUtils.put(getActivity(), StaticValue.THEME_COLOR, color);
                        StaticValue.color = color;
                        listener.onThemeChanged();
                        exit.setTextColor(StaticValue.color);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(StaticValue.color);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(StaticValue.color);
            }
        });

        dialog.show();
    }

    AlertDialog About_dialog;
    @OnClick(R.id.rl_about)
    void about() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);
        About_dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .create();
        TextView autho = (AppCompatTextView) dialogView.findViewById(R.id.tv_author_github);
        TextView blog = (AppCompatTextView) dialogView.findViewById(R.id.tv_author_blog);
        TextView address = (AppCompatTextView) dialogView.findViewById(R.id.tv_open_address);
        TextView sure = (AppCompatTextView) dialogView.findViewById(R.id.tv_affirm);
        autho.setTextColor(StaticValue.color);
        blog.setTextColor(StaticValue.color);
        address.setTextColor(StaticValue.color);
        sure.setTextColor(StaticValue.color);
        setUnderline(autho);
        setUnderline(blog);
        setUnderline(address);
        autho.setOnClickListener(this);
        blog.setOnClickListener(this);
        address.setOnClickListener(this);
        sure.setOnClickListener(this);
        About_dialog.show();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_author_github:
                viewIntent("https://github.com/liulei-0911");
                break;
            case R.id.tv_author_blog:
                viewIntent("http://blog.csdn.net/liulei823581722");
                break;
            case R.id.tv_open_address:
                viewIntent("https://github.com/liulei-0911/LLApp");
                break;
            case R.id.tv_affirm:
                About_dialog.cancel();
                break;
        }
    }

    private void viewIntent(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    private void setUnderline(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    @OnClick(R.id.add_psw)
    void add_psw() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("温馨提示")
                .setMessage("当打开加密图案后，每次进入APP都要进行验证，是否继续")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(SetPatternActivity.class);
                    }
                }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(StaticValue.color);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(StaticValue.color);
            }
        });
        dialog.show();
    }

//    @OnClick(R.id.switch_theme)
//    void switch_theme() {
//        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("选择主题")
//                .setSingleChoiceItems(new String[]{"标准模式", "夜间模式"}, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        //暂时注释
//                        Snackbar.make(cache_size,"该功能尚未开发",Snackbar.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("取消", null).create();
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(StaticValue.color);
//            }
//        });
//
//        dialog.show();
//    }

    @OnClick(R.id.clear_cache)
    void clear() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("确定要删除缓存图片?")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deteleCache();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(StaticValue.color);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(StaticValue.color);
            }
        });
        dialog.show();
    }

    private void deteleCache() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Glide.get(App.getInstance()).clearDiskCache();
                subscriber.onNext(null);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object object) {
                        cache_size.setText(getCachedSize());
                        Snackbar.make(cache_size, "清理缓存完成", Snackbar.LENGTH_LONG).show();
                    }
                });
        mSubscriptions.add(subscription);
    }

    @OnClick(R.id.rl_suggest)
    void suggest() {
        startActivity(SuggestActivity.class);
    }

    @OnClick(R.id.text_exit)
    void exit() {
        SPUtils.remove(getContext(), Constants.APP_PSW);
        SPUtils.remove(getContext(), Constants.APP_COUNT);
        SPUtils.remove(getContext(), StaticValue.IS_PSW_OPEN);
        getActivity().finish();
    }

    public int getDarkColor(int color) {
        int darkColor = 0;

        int r = Math.max(Color.red(color) - 25, 0);
        int g = Math.max(Color.green(color) - 25, 0);
        int b = Math.max(Color.blue(color) - 25, 0);

        darkColor = Color.rgb(r, g, b);

        return darkColor;
    }
}
