package com.demomvvm.school.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.FloatRange;
import android.view.View;

import com.demomvvm.school.SchoolApplication;
import com.demomvvm.school.R;
import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;


public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // startService(new Intent(getApplicationContext(), CurrentLocation.class));

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.first_slide_background)
                .buttonsColor(R.color.first_slide_buttons)
                .image(R.drawable.ic_intro_one)
                .title(getString(R.string.save_time_with_us))
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.first_slide_background)
                .buttonsColor(R.color.first_slide_buttons)
                .image(R.drawable.ic_intro_two)
                .title(getString(R.string.book_from_anywhere))
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.first_slide_background)
                .buttonsColor(R.color.first_slide_buttons)
                .image(R.drawable.ic_intro_four)
                .title(getString(R.string.satisfaction_gurantee))
                .build());
    }


    @Override
    public void onFinish() {
        SchoolApplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_isIntro), true);
        super.onFinish();
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
