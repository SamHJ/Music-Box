package havotech.com.musicbox;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class OnboardingIntroSlider extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPageAdapter introViewPageAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    Button btnGetStarted;
    int position = 0;
    Animation btnAnimate;

    private int[] layouts = {R.layout.intro_one, R.layout.intro_two, R.layout.intro_three,
    R.layout.intro_four, R.layout.intro_five};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //as this activity is about to be launched, check if the user has already viewed the intro slide

        if(restorePrefData()){
            Intent openAdvertUrl = new Intent(OnboardingIntroSlider.this, SplashScreen.class);
            Activity activity = (Activity) OnboardingIntroSlider.this;
            OnboardingIntroSlider.this.startActivity(openAdvertUrl);
            activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();


        }

//        //initialize Calligraphy font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Comic_Neue.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_onboarding_intro_slider);

        //init views
        btnNext = findViewById(R.id.next_button);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnimate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);


        // setup Viewpager
        screenPager= findViewById(R.id.screen_viewpager);
        introViewPageAdapter = new IntroViewPageAdapter(layouts, this);
        screenPager.setAdapter(introViewPageAdapter);


        tabIndicator.setupWithViewPager(screenPager);

        //nextBtn click listener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < layouts.length){
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == layouts.length-1){
                    //when we reach the last screen slide
                    // show the get started button and hide the indicator and next button
                    loadLastScreen();
                }
            }
        });



        //tab layout add chang listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == layouts.length-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //get started button clicklistener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(OnboardingIntroSlider.this, SplashScreen.class);
                Activity activity = (Activity) OnboardingIntroSlider.this;
                OnboardingIntroSlider.this.startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                //use sharedpreferences to prevent the intro slide from showing up again
                savePrefsData();

                finish();
            }
        });


    }

    private boolean restorePrefData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("intro_slide_pref", MODE_PRIVATE);
        return preferences.getBoolean("isIntroOpened", false);
    }

    private void savePrefsData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("intro_slide_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }

    // show the get started button and hide the indicator and next button
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        //add animation to get started button
        //setup the animation
        btnGetStarted.setAnimation(btnAnimate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
