package havotech.com.musicbox;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {


    Toolbar navigation_opener_toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    List<SongModel> songs;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SongsAdapter songsAdapter;
    ProgressBar loading_music_bar;
    RecyclerView music_recyclerview;

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View musicFragmentView =  inflater.inflate(R.layout.fragment_music, container, false);

        navigation_opener_toolbar = musicFragmentView.findViewById(R.id.navigation_opener_toolbar);

        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).setSupportActionBar(navigation_opener_toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Music");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);


        drawerLayout = musicFragmentView.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close);

        loading_music_bar = musicFragmentView.findViewById(R.id.loading_music_bar);
        music_recyclerview = musicFragmentView.findViewById(R.id.music_recyclerview);
        music_recyclerview.setHasFixedSize(true);
        music_recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        songs = new ArrayList<>();
        songsAdapter = new SongsAdapter(getContext(), songs);
        music_recyclerview.setAdapter(songsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child("Musics");
        databaseReference.keepSynced(true);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_music_bar.setVisibility(View.GONE);
            }
        });


        return musicFragmentView;
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
