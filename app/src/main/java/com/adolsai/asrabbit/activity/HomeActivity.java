package com.adolsai.asrabbit.activity;

import android.content.Context;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.fragment.FavouriteFragment;
import com.adolsai.asrabbit.fragment.HistoryFragment;
import com.adolsai.asrabbit.fragment.HomeFragment;
import com.adolsai.asrabbit.utils.ScreenUtil;
import com.adolsai.asrabbit.utils.StatusBarCompat;
import com.adolsai.asrabbit.utils.VersionUtil;
import com.ht.baselib.arcanimator.ArcAnimator;
import com.ht.baselib.arcanimator.Side;
import com.ht.baselib.utils.LogUtils;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class HomeActivity extends AppCompatActivity implements OnClickListener {
    private Context context;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private String hideTag;

    private HomeFragment mHomeFragment;
    private HistoryFragment mCategoryFragment;
    private FavouriteFragment mSubscribeFragment;
    private SupportAnimator mAnimator;

    public static final String TAG_HOME = "Home";
    public static final String TAG_HISTORY = "History";
    public static final String TAG_FAVOURITE = "Favourite";

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.fl_content_home)
    FrameLayout fl_home;
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


    private static boolean isHistory = false;

    private static boolean isHome = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        context = this;
        StatusBarCompat.compat(this, getResources().getColor(R.color.base_sys_bar_bg));
        init();
        handFab(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("sharing", "HomeActivity onResume" + isHistory);
        if (isHistory) {
            handFab(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
//            startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            handFab(true);
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                handFab(true);
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 先隐藏搜索框
            if (viewHide.isShown()) {
                viewHide.performClick();
            } else {
                showCloseDialog();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /**
     * 初始化直接面框架的fragment布局
     */
    private void init() {
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

        mHomeFragment = HomeFragment.newInstance();
        switchFragment(TAG_HOME, mHomeFragment);

        handFabPathAndSearch();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LogUtils.e("sharing", "item onclick");
                return false;
            }
        });

    }

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
                                isHistory = false;

                                if (mHomeFragment == null) {
                                    mHomeFragment = HomeFragment.newInstance();
                                }
                                switchFragment(TAG_HOME, mHomeFragment);
                                break;
                            case R.id.nav_history:

                                setTitle("历史记录");
                                isHome = false;
                                fab.setImageResource(R.mipmap.ic_action_search);
                                isHistory = true;
                                handFab(false);
                                if (mCategoryFragment == null) {
                                    mCategoryFragment = HistoryFragment.newInstance();
                                }
                                switchFragment(TAG_HISTORY, mCategoryFragment);

                                break;
                            case R.id.nav_favourite:
                                if (mSubscribeFragment == null) {
                                    mSubscribeFragment = FavouriteFragment.newInstance();
                                }
                                setTitle("个人收藏");
                                handFab(true);
//                                switchFragment(TAG_FAVOURITE, new CardViewPagerFragment());
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
     * 处理浮层是否隐藏
     *
     * @param isFabHide 是否隐藏
     */
    public void handFab(boolean isFabHide) {
        LogUtils.e("sharing", "handFab fab is " + fab);
        if (isFabHide) {
            hideFab();
        } else {
            showFab();
        }
    }

    /**
     * 浮层按钮的点击事件
     */
    private void handFabPathAndSearch() {
        iv_bottom_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                ArcAnimator.createArcAnimator(fab, ScreenUtil.getScreenWidth(context) - fab.getWidth() / 2 - ScreenUtil.dip2px(context, 16), ScreenUtil.getScreenHeight(context) - fab.getHeight() - ScreenUtil.dip2px(context, 16), 45.0f, Side.LEFT)
                                        .setDuration(500)
                                        .start();
                            } else {
                                ArcAnimator.createArcAnimator(fab, ScreenUtil.getScreenWidth(context) - fab.getWidth() / 2 - ScreenUtil.dip2px(context, 16), ScreenUtil.getScreenHeight(context) - fab.getHeight() / 2 - ScreenUtil.dip2px(context, 16), 45.0f, Side.LEFT)
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
                    final ObjectAnimator animator = ObjectAnimator.ofFloat(iv_bottom_search, "translationX", curTranslationX, cx / 2 - ScreenUtil.dip2px(context, 24));
                    animator.setDuration(600);
                    float radius = r(mCardView.getWidth(), mCardView.getHeight());
                    mAnimator = ViewAnimationUtils.createCircularReveal(mCardView, cx / 2, cy - ScreenUtil.dip2px(context, 32), ScreenUtil.dip2px(context, 20), radius);
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
//            fab.animate().scaleX(1.0f);
//            fab.animate().scaleY(1.0f);
//            fab.animate().translationX(0);
        }
    }

    /**
     * 隐藏浮层按钮
     */
    public void hideFab() {
        if (fab != null) {
//            fab.animate().scaleX(0.1f);
//            fab.animate().scaleY(0.1f);
//            fab.animate().translationX(200);
            fab.setVisibility(View.GONE);
        }
    }


    /**
     * 显示退出的弹出窗口
     */
    private void showCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("小伙伴,您确定要离开我吗");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    static float r(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }
}
