package com.alex.greenroute.presentation.screens.tutorial;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.alex.greenroute.R;
import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.DataRepository;
import com.alex.greenroute.data.util.Constants;
import com.alex.greenroute.presentation.screens.main.MainActivity;
import com.alex.greenroute.presentation.screens.tutorial.fragments.FragmentInfo;
import com.alex.greenroute.presentation.screens.tutorial.fragments.FragmentLogin;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.button_skip)
    Button skip_button;

    @BindView(R.id.button_use_now)
    Button use_now_button;

    @BindView(R.id.button_next)
    ImageButton next_button;

    @BindView(R.id.flexibleIndicator)
    ExtensiblePageIndicator extensiblePageIndicator;

    @Inject
    DataRepository dataRepository;

    int numberOfViewPagerChildren = 4;

    Integer[] colors;
    ArgbEvaluator argbEvaluator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ButterKnife.bind(this);

        TutorialComponent component = DaggerTutorialComponent.builder()
                .appComponent(GreenApplication.component())
                .build();
        component.inject(this);

        setupViewPager();
    }

    @OnClick({R.id.button_skip, R.id.button_use_now})
    void onSkip() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.button_next)
    void onNext() {
        int currentItem = viewPager.getCurrentItem();
        if(currentItem < numberOfViewPagerChildren) {
            viewPager.setCurrentItem(currentItem + 1, true);
        }
    }

    private void setupViewPager() {
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        extensiblePageIndicator.initViewPager(viewPager);

        colors = new Integer[]{
                ContextCompat.getColor(this, R.color.tutorial_color_2),
                ContextCompat.getColor(this, R.color.tutorial_color_3),
                ContextCompat.getColor(this, R.color.tutorial_color_1),
                ContextCompat.getColor(this, R.color.tutorial_color_4)};

        argbEvaluator = new ArgbEvaluator();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < (numberOfViewPagerChildren - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == (numberOfViewPagerChildren - 1)) {
                    use_now_button.setVisibility(View.VISIBLE);
                    next_button.setVisibility(View.INVISIBLE);
                } else {
                    use_now_button.setVisibility(View.INVISIBLE);
                    next_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    private class MyAdapter extends FragmentStatePagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Bundle extras = new Bundle();

            FragmentInfo fragment;

            switch (i) {
                case 0:
                    extras.putInt(Constants.EXTRAS_TITLE_REFERENCE, R.string.welcome_page_title_1);
                    extras.putInt(Constants.EXTRAS_CONTENT_REFERENCE, R.string.welcome_page_content_1);
                    extras.putInt(Constants.EXTRAS_IMAGE_REFERENCE, R.drawable.tutorial_test2);

                    fragment = new FragmentInfo();
                    fragment.setArguments(extras);
                    return fragment;
                case 1:
                    extras.putInt(Constants.EXTRAS_TITLE_REFERENCE, R.string.welcome_page_title_2);
                    extras.putInt(Constants.EXTRAS_CONTENT_REFERENCE, R.string.welcome_page_content_2);
                    extras.putInt(Constants.EXTRAS_IMAGE_REFERENCE, R.drawable.tutorial_test3);

                    fragment = new FragmentInfo();
                    fragment.setArguments(extras);
                    return fragment;
                case 2:
                    extras.putInt(Constants.EXTRAS_TITLE_REFERENCE, R.string.welcome_page_title_3);
                    extras.putInt(Constants.EXTRAS_CONTENT_REFERENCE, R.string.welcome_page_content_3);
                    extras.putInt(Constants.EXTRAS_IMAGE_REFERENCE, R.drawable.tutorial_test1);

                    fragment = new FragmentInfo();
                    fragment.setArguments(extras);
                    return fragment;
                case 3:
                    return new FragmentLogin();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return numberOfViewPagerChildren;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if(object instanceof FragmentInfo){
                view.setTag(1);
            }
            if(object instanceof FragmentLogin){
                view.setTag(0);
            }
            return super.isViewFromObject(view, object);
        }
    }
}
