package com.alex.greenroute.presentation.screens.tutorial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alex.greenroute.R;
import com.alex.greenroute.data.util.Constants;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 18.10.2015.
 */
public class FragmentInfo extends Fragment {

    @BindView(R.id.heading)
    TextView title;

    @BindView(R.id.blurb)
    TextView content;

    @BindView(R.id.tutorial_image)
    ImageView image;

    @BindView(R.id.rootLayoutTutorial)
    RelativeLayout root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle extras = getArguments();
        if (extras == null) {
            return;
        }

        int ref1 = extras.getInt(Constants.EXTRAS_TITLE_REFERENCE);
        int ref2 = extras.getInt(Constants.EXTRAS_CONTENT_REFERENCE);
        int ref3 = extras.getInt(Constants.EXTRAS_IMAGE_REFERENCE, -1);
        int ref4 = extras.getInt(Constants.EXTRAS_BG_COLOR_REFERENCE, -1);

        title.setText(ref1);
        content.setText(ref2);

        if(ref3 != -1) {
            Glide.with(this)
                    .load(ref3)
                    .into(image);
        }

        if(ref4 != -1) {
            root.setBackgroundResource(ref4);
        }
    }
}
