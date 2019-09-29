package havotech.com.musicbox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainNavContainer extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    FrameLayout mMainFrame;
    HomeFragment homeFragment;
    MusicFragment musicFragment;
    LibraryFragment libraryFragment;
    BuzzFragment buzzFragment;
    VideosFragment videosFragment;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_container);

        mToolbar = findViewById(R.id.userhome_navigation_opener_include);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainNavContainer.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation_drawer_nav_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);



        homeFragment = new HomeFragment();
        musicFragment = new MusicFragment();
        libraryFragment = new LibraryFragment();
        buzzFragment = new BuzzFragment();
        videosFragment = new VideosFragment();

        setFragment(homeFragment);

        //bottom navigation on selected item listener
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                                         @Override
                                                         public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                                             switch (menuItem.getItemId()) {

                                                                 case R.id.nav_home :
                                                                     setFragment(homeFragment); //loads the feeds Fragment
                                                                     return true;

                                                                 case R.id.nav_music :
                                                                     setFragment(musicFragment); //loads the posts Fragment
                                                                     return true;

                                                                 case R.id.nav_library :
                                                                     setFragment(libraryFragment); //loads the messages Fragment
                                                                     return true;

                                                                 case R.id.nav_buzz :

                                                                     setFragment(buzzFragment); //loads the friends Fragment
                                                                     return true;

                                                                 case R.id.nav_videos :
                                                                     setFragment(videosFragment); //loads the requests Fragment
                                                                     return true;

                                                                 default:
                                                                     return false;
                                                             }
                                                         }


                                                     }
        );



        //navigation view on selected item listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                userMenuSelector(menuItem);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void userMenuSelector(MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.nav_drawer_settings:
                break;

            case R.id.nav_drawer_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey! check out this app";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Music Box");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.nav_drawer_rate:
                showRateDialog(this);
                break;

            case R.id.nav_drawer_donate:
                break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.search_view_menu, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
//                    adapter.filter("");
//                    listView.clearTextFilter();
                } else {
//                    adapter.filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public static void showRateDialog(final Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + "Music Box");

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + "Music Box" + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + "Music Box");
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "havotech.com.musicbox")));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppRater.app_launched(this);
    }
}
