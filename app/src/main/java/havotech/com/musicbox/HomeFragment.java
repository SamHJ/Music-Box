package havotech.com.musicbox;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageButton favourites_btn;
    ImageButton top_songs_btn;
    ImageButton random_btn;
    RecyclerView recent_tracks_recyclerview;
    ProgressBar loading_music_bar;

    List<SongModel> songs;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SongsAdapter songsAdapter;
    CardView cardview;
    TextView home_featured_text;

    int[] images = {R.drawable.home_header_img, R.drawable.header_home,R.drawable.home_2, R.drawable.home_3, R.drawable.home_4,
            R.drawable.home_5,R.drawable.home_6};
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeFragmentView =  inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        mToolbar = homeFragmentView.findViewById(R.id.userhome_navigation_opener_include);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        Random r = new Random();
        int image_int = r.nextInt(7);
        mToolbar.setBackgroundResource(images[image_int]);

        drawerLayout = homeFragmentView.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close);
        recent_tracks_recyclerview = homeFragmentView.findViewById(R.id.recent_tracks_recyclerview);
        loading_music_bar = homeFragmentView.findViewById(R.id.loading_music_bar);
        home_featured_text = homeFragmentView.findViewById(R.id.home_featured_text);
        cardview = homeFragmentView.findViewById(R.id.cardview);

        recent_tracks_recyclerview.setHasFixedSize(true);
        recent_tracks_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        songs = new ArrayList<>();
        songsAdapter = new SongsAdapter(getContext(), songs);
        recent_tracks_recyclerview.setAdapter(songsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child("RecentTracks");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 songs.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                     SongModel songModel = snapshot.getValue(SongModel.class);
                     songModel.setmKey(dataSnapshot.getKey());
                     songs.add(songModel);
                 }
                 songsAdapter.notifyDataSetChanged();
                 loading_music_bar.setVisibility(View.GONE);
                 home_featured_text.setText(songs.get(2).getSong_name()+" By "+songs.get(2).getArtiste_name());
                 cardview.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent openMusicPlayer = new Intent(getContext(), MusicPlayer.class);
                         openMusicPlayer.putExtra("song_url", songs.get(2).getSong_url() );
                         openMusicPlayer.putExtra("song_name", songs.get(2).getSong_name());
                         openMusicPlayer.putExtra("artiste_name", songs.get(2).getArtiste_name());
                         openMusicPlayer.putExtra("price", songs.get(2).getPrice());
                         openMusicPlayer.putExtra("song_image", songs.get(2).getSong_image_url());
                         Activity activity = (Activity) getContext();
                         getContext().startActivity(openMusicPlayer);
                         activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                     }
                 });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_music_bar.setVisibility(View.GONE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();

        }

        favourites_btn = homeFragmentView.findViewById(R.id.favourites_btn);
        top_songs_btn = homeFragmentView.findViewById(R.id.top_songs_btn);
        random_btn = homeFragmentView.findViewById(R.id.random_btn);
        favourites_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(getContext(), SongCategoryLoaders.class);
                openAdvertUrl.putExtra("title", "Favourites" );
                openAdvertUrl.putExtra("key","Favourites");
                Activity activity = (Activity) getContext();
                getContext().startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        top_songs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(getContext(), SongCategoryLoaders.class);
                openAdvertUrl.putExtra("title", "Top Songs" );
                openAdvertUrl.putExtra("key","TopSongs");
                Activity activity = (Activity) getContext();
                getContext().startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        random_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(getContext(), SongCategoryLoaders.class);
                openAdvertUrl.putExtra("title", "Random" );
                openAdvertUrl.putExtra("key","Random");
                Activity activity = (Activity) getContext();
                getContext().startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        return homeFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseMusicSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseMusicSearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void firebaseMusicSearch(String query) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child("Musics");
        databaseReference.keepSynced(true);
        Query firebaseMusicSearchQuery = databaseReference.orderByChild("song_name").startAt(query.toUpperCase()).endAt(query + "\uf0ff");

        valueEventListener = firebaseMusicSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SongModel songModel = snapshot.getValue(SongModel.class);
                    songModel.setmKey(dataSnapshot.getKey());
                    songs.add(songModel);
                }
                songsAdapter.notifyDataSetChanged();
                loading_music_bar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_music_bar.setVisibility(View.GONE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}
