package com.example.tvshows.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tvshows.R;
import com.example.tvshows.adapters.EpisodesAdapter;
import com.example.tvshows.adapters.ImageSliderAdapter;
import com.example.tvshows.databinding.ActivityMainBinding;
import com.example.tvshows.databinding.ActivityTvShowDetailsBinding;
import com.example.tvshows.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tvshows.viewmodels.TvShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class TvShowDetailsActivity extends AppCompatActivity {

    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TvShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog bottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityTvShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details);
        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        activityTvShowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        getTvShowDetails();
    }

    private void getTvShowDetails() {
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvShowDetailsViewModel.getTvShowDetails(tvShowId).observe(this, m -> {
            activityTvShowDetailsBinding.setIsLoading(false);
            if (m.getTvShowDetails() != null) {
                if (m.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(m.getTvShowDetails().getPictures());
                }
                //      MAIN IMAGE OF TV SHOW
                activityTvShowDetailsBinding.setTvShowImageURL(
                        m.getTvShowDetails().getImagePath()
                );
                activityTvShowDetailsBinding.imageTvShow.setVisibility(View.VISIBLE);

                //      DESCRIPTION AND READ MORE BUTTON
                activityTvShowDetailsBinding.setDescription(
                        String.valueOf(HtmlCompat.fromHtml(
                                m.getTvShowDetails().getDescription(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY)
                        )
                );
                activityTvShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (activityTvShowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                            activityTvShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                            activityTvShowDetailsBinding.textDescription.setEllipsize(null);
                            activityTvShowDetailsBinding.textReadMore.setText(R.string.read_less);
                        } else {
                            activityTvShowDetailsBinding.textDescription.setMaxLines(4);
                            activityTvShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                            activityTvShowDetailsBinding.textReadMore.setText(R.string.read_more);
                        }
                    }
                });
                //      WEBSITE BUTTON
                activityTvShowDetailsBinding.buttonWebsite.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(m.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });
                activityTvShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);

                //      EPISODES BUTTON
                activityTvShowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.buttonEpisodes.setOnClickListener(view -> {
                    if (bottomSheetDialog == null) {
                        bottomSheetDialog = new BottomSheetDialog(TvShowDetailsActivity.this);
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(TvShowDetailsActivity.this),
                                R.layout.layout_episodes_bottom_sheet,
                                findViewById(R.id.episodesContainer),
                                false
                        );
                        bottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                        layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                new EpisodesAdapter(m.getTvShowDetails().getEpisodes())
                        );
                        layoutEpisodesBottomSheetBinding.textTitle.setText(
                                String.format("Episodes | %s", getIntent().getStringExtra("name"))
                        );
                        layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> bottomSheetDialog.dismiss());

//                        //-----     OPTIONAL
//                        FrameLayout frameLayout = bottomSheetDialog.findViewById(
//                                com.google.android.material.R.id.design_bottom_sheet
//                        );
//                        if (frameLayout != null) {
//                            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
//                            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
//                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        }
//                        //-----     OPTIONAL

                        bottomSheetDialog.show();
                    }
                });

                loadBasicTVShowDetails();
            }
//            Toast.makeText(
//                    getApplicationContext(),
//                    "Url: " + m.getTvShowDetails().getUrl(),
//                    Toast.LENGTH_SHORT).show();
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        activityTvShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);

        setUpSliderIndicator(sliderImages.length);
        activityTvShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setUpSliderIndicator(int count) {
        ImageView[] indicators = new ImageView[count];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < count; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);

            activityTvShowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvShowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvShowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.background_slider_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.background_slider_indicator_inactive
                ));
            }
        }

    }

    private void loadBasicTVShowDetails() {
        activityTvShowDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTvShowDetailsBinding.setNetworkCountry(
                getIntent().getStringExtra("network") + "(" +
                        getIntent().getStringExtra("country") + ")"
        );
        activityTvShowDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityTvShowDetailsBinding.setStartDate(getIntent().getStringExtra("startDate"));
        activityTvShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }

}