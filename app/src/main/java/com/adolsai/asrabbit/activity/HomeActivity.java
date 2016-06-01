package com.adolsai.asrabbit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.app.GlobalStaticData;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.fragment.FavouriteFragment;
import com.adolsai.asrabbit.fragment.HistoryFragment;
import com.adolsai.asrabbit.fragment.HomeFragment;
import com.adolsai.asrabbit.utils.DialogUtils;
import com.adolsai.asrabbit.utils.ScreenUtil;
import com.adolsai.asrabbit.utils.StatusBarCompat;
import com.adolsai.asrabbit.utils.VersionUtil;
import com.ht.baselib.arcanimator.ArcAnimator;
import com.ht.baselib.arcanimator.Side;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.SoftInputMethodUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class HomeActivity extends AsRabbitBaseActivity implements OnClickListener {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private String hideTag;

    private SupportAnimator mAnimator;

    public static final String TAG_HOME = "Home";
    public static final String TAG_HISTORY = "History";
    public static final String TAG_FAVOURITE = "Favourite";

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.card_search)
    CardView mCardView;
    @Bind(R.id.iv_bottom_search)
    ImageView iv_bottom_search;
    @Bind(R.id.edit_text_search)
    EditText edit_text_search;
    @Bind(R.id.view_hide)
    View viewHide;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.iv_nav_head)
    ImageView ivNavHead;

    private static boolean isHistory = false;
    private static boolean isFavourite = false;
    private static boolean isHome = true;

    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private FavouriteFragment favouriteFragment;


    //***********************生命周期区**************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_home, savedInstanceState);
    }

    @Override
    protected void initViews() {
        StatusBarCompat.compat(this, getResources().getColor(R.color.base_sys_bar_bg));
        viewHide.setOnClickListener(this);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.mipmap.ic_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
            mNavigationView.setCheckedItem(R.id.nav_home);
        }
        homeFragment = HomeFragment.getInstance();
        switchFragment(TAG_HOME, homeFragment);

        handFabPathAndSearch();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    LogUtils.e("点击关于");
                    handFab(true);
                    return true;
                } else if (id == R.id.action_fav) {
                    LogUtils.e("点击fav清除");
                    handFab(true);
                    DialogUtils.showDialog(mContext, "温馨提示", "小伙伴，你确定要清除全部收藏吗", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //清除全部收藏数据
                        }
                    });
                    return true;
                } else if (id == R.id.action_history) {
                    LogUtils.e("点击his清除");
                    handFab(true);
                    DialogUtils.showDialog(mContext, "温馨提示", "小伙伴，你确定要清除全部历史吗", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //清除全部历史数据
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            }
        });
        handFab(true);
        ivNavHead.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isHistory) {
            handFab(false);
        } else {
            handFab(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。 返回true则显示该menu,false 则不显示;
         * (只会在第一次初始化菜单时调用) Inflate the menu; this adds items to the action bar
         * if it is present.
         */
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /**
         * 在onCreateOptionsMenu执行后，菜单被显示前调用；如果菜单已经被创建，则在菜单显示前被调用。 同样的，
         * 返回true则显示该menu,false 则不显示; （可以通过此方法动态的改变菜单的状态，比如加载不同的菜单等） TODO
         * Auto-generated method stub
         */
        LogUtils.e("onPrepareOptionsMenu");
        menu.clear();
        if (isHome) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        } else if (isHistory) {
            getMenuInflater().inflate(R.menu.menu_history, menu);
        } else if (isFavourite) {
            getMenuInflater().inflate(R.menu.menu_favourite, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * 菜单项被点击时调用，也就是菜单项的监听方法。
         * 通过这几个方法，可以得知，对于Activity，同一时间只能显示和监听一个Menu 对象。 TODO Auto-generated
         * method stub
         */
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //点击拉开侧边栏
            handFab(true);
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //************************事件处理区***************************************************************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (isHome) {
                    //在主界面点击浮层按钮
                } else {
                    viewHide.setVisibility(View.VISIBLE);
                    ArcAnimator.createArcAnimator(fab, ScreenUtil.getScreenWidth(getApplicationContext()) / 2, ScreenUtil.getStatusHeight(getApplicationContext()) + (toolbar.getHeight() / 2), 45.0f, Side.LEFT)
                            .setDuration(500)
                            .start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCardView.setVisibility(View.VISIBLE);
                            fab.setVisibility(View.GONE);
                            iv_bottom_search.performClick();
                        }
                    }, 600);
                }
                break;

            case R.id.view_hide:
                edit_text_search.setText("");
                iv_bottom_search.performClick();
                break;
            case R.id.iv_nav_head:
                if (!Hawk.get(GlobalStaticData.IS_LOGIN, false)) {
                    //未登陆，跳转到登陆界面
                    ActivityUtil.startActivity(activity, LoginActivity.class);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 先隐藏搜索框
            if (viewHide.isShown()) {
                viewHide.performClick();
            } else {
                DialogUtils.showDialog(mContext, "温馨提示", "小伙伴，你确定要离开我吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /**
     * 侧边栏点击事件
     *
     * @param navigationView 侧边栏的view
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        // 关闭搜索框、选择器
                        if (viewHide.isShown()) {
                            viewHide.performClick();
                        }
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                setTitle("最常查看");
                                handFab(true);
                                fab.setImageResource(R.mipmap.ic_add_white_24dp);
                                isHome = true;
                                isFavourite = false;
                                isHistory = false;
                                switchFragment(TAG_HOME, homeFragment);
                                break;
                            case R.id.nav_history:
                                setTitle("历史记录");
                                isHome = false;
                                isFavourite = false;
                                fab.setImageResource(R.mipmap.ic_action_search);
                                isHistory = true;
                                handFab(false);
                                if (historyFragment == null) {
                                    historyFragment = HistoryFragment.getInstance();
                                }
                                switchFragment(TAG_HISTORY, historyFragment);

                                break;
                            case R.id.nav_favourite:
                                isFavourite = true;
                                isHome = false;
                                isHistory = false;
                                setTitle("个人收藏");
                                handFab(false);
                                if (favouriteFragment == null) {
                                    favouriteFragment = FavouriteFragment.getInstance();
                                }
                                switchFragment(TAG_FAVOURITE, favouriteFragment);
                                break;
                            case R.id.help:
                                Toast.makeText(HomeActivity.this, "帮助", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                                break;
                            case R.id.setting:
                                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                                break;
                        }
                        return true;
                    }
                });
    }

    /**
     * 浮层按钮的点击事件
     */
    private void handFabPathAndSearch() {
        iv_bottom_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //收回软键盘
                SoftInputMethodUtils.hideSoftInputMethod(mContext, edit_text_search);
                if (mAnimator != null && !mAnimator.isRunning()) {
                    mAnimator = mAnimator.reverse();
                    float curTranslationX = iv_bottom_search.getTranslationX();
                    final ObjectAnimator animator = ObjectAnimator.ofFloat(iv_bottom_search, "translationX", curTranslationX, 0);
                    animator.setDuration(600);
                    mAnimator.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {
                            animator.start();
                        }

                        @Override
                        public void onAnimationEnd() {
                            mAnimator = null;
                            fab.setVisibility(View.VISIBLE);
                            mCardView.setVisibility(View.GONE);
                            if (VersionUtil.checkVersionIntMoreThan19()) {
                                ArcAnimator.createArcAnimator(fab, ScreenUtil.getScreenWidth(mContext)
                                                - fab.getWidth() / 2 - ScreenUtil.dip2px(mContext, 16),
                                        ScreenUtil.getScreenHeight(mContext) - fab.getHeight()
                                                - ScreenUtil.dip2px(mContext, 16), 45.0f, Side.LEFT)
                                        .setDuration(500)
                                        .start();
                            } else {
                                ArcAnimator.createArcAnimator(fab, ScreenUtil.getScreenWidth(mContext)
                                                - fab.getWidth() / 2 - ScreenUtil.dip2px(mContext, 16),
                                        ScreenUtil.getScreenHeight(mContext) - fab.getHeight() / 2
                                                - ScreenUtil.dip2px(mContext, 16), 45.0f, Side.LEFT)
                                        .setDuration(500)
                                        .start();
                            }
                            viewHide.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchData();
                                }
                            }, 500);

                        }

                        @Override
                        public void onAnimationCancel() {

                        }

                        @Override
                        public void onAnimationRepeat() {

                        }
                    });
                } else if (mAnimator != null) {
                    mAnimator.cancel();
                    return;
                } else {
                    int cx = mCardView.getRight();
                    int cy = mCardView.getBottom();
                    float curTranslationX = iv_bottom_search.getTranslationX();
                    final ObjectAnimator animator = ObjectAnimator.ofFloat(iv_bottom_search, "translationX", curTranslationX, cx / 2 - ScreenUtil.dip2px(mContext, 24));
                    animator.setDuration(600);
                    float radius = r(mCardView.getWidth(), mCardView.getHeight());
                    mAnimator = ViewAnimationUtils.createCircularReveal(mCardView, cx / 2, cy - ScreenUtil.dip2px(mContext, 32), ScreenUtil.dip2px(mContext, 20), radius);
                    mAnimator.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {
                            animator.start();
                        }

                        @Override
                        public void onAnimationEnd() {

                        }

                        @Override
                        public void onAnimationCancel() {

                        }

                        @Override
                        public void onAnimationRepeat() {

                        }
                    });
                }
                mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator.setDuration(600);
                mAnimator.start();
            }
        });
    }


    //****************自定义方法区*********************************************************************

    /**
     * 根据不同的tag选择不同的Fragment
     *
     * @param tag       标签
     * @param mFragment 被选择的fragment
     */
    public void switchFragment(String tag, Fragment mFragment) {
        if (hideTag == tag) {
            return;
        }
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        Fragment tagFragment = mFragmentManager.findFragmentByTag(tag);

        if (tagFragment == null) {
            mFragmentTransaction.add(R.id.fl_content_home, mFragment, tag);
        } else {
            mFragmentTransaction.show(tagFragment);
        }

        tagFragment = mFragmentManager.findFragmentByTag(hideTag);

        if (tagFragment != null) {
            mFragmentTransaction.hide(tagFragment);
        }

        hideTag = tag;
        mFragmentTransaction.commit();
    }

    /**
     * 设置titleBar的标题
     *
     * @param str 字符串
     */
    public void setTitle(String str) {
        if (toolbar != null) {
            toolbar.setTitle(str);
        }
    }

    /**
     * 处理浮层是否隐藏
     *
     * @param isFabHide 是否隐藏
     */
    public void handFab(boolean isFabHide) {
        if (isFabHide) {
            hideFab();
        } else {
            showFab();
        }
    }

    /**
     * 搜索
     */
    private void searchData() {
        String result = edit_text_search.getText().toString().trim();
        edit_text_search.setText("");

        if (!TextUtils.isEmpty(result)) {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("search", result);
            startActivity(intent);
        } else {
            Toast.makeText(HomeActivity.this, "请输入需要搜索的帖子信息", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 显示浮层按钮
     */
    public void showFab() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.animate().scaleX(1.0f);
            fab.animate().scaleY(1.0f);
            fab.animate().translationX(0);
        }
    }

    /**
     * 隐藏浮层按钮
     */
    public void hideFab() {
        if (fab != null) {
            fab.animate().scaleX(0.1f);
            fab.animate().scaleY(0.1f);
            fab.animate().translationX(200);
            fab.setVisibility(View.GONE);
        }
    }


    static float r(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }
}
