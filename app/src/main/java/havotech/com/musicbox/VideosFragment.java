package havotech.com.musicbox;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
public class VideosFragment extends Fragment {

    Toolbar navigation_opener_toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    List<VideoModel> videoModels;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    VideoAdapter videoAdapter;
    ProgressBar loading_video_bar;
    RecyclerView video_recyclerview;


    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View videoFragmentView =  inflater.inflate(R.layout.fragment_videos, container, false);


        navigation_opener_toolbar = videoFragmentView.findViewById(R.id.navigation_opener_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(navigation_opener_toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Videos");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        drawerLayout = videoFragmentView.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close);


        loading_video_bar = videoFragmentView.findViewById(R.id.loading_video_bar);
        video_recyclerview = videoFragmentView.findViewById(R.id.video_recyclerview);
        video_recyclerview.setHasFixedSize(true);
        video_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        videoModels = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(), videoModels);
        video_recyclerview.setAdapter(videoAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference.keepSynced(true);
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    VideoModel songModel = snapshot.getValue(VideoModel.class);
                    songModel.setmKey(dataSnapshot.getKey());
                    videoModels.add(songModel);
                }
                videoAdapter.notifyDataSetChanged();
                loading_video_bar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_video_bar.setVisibility(View.GONE);
            }
        });



        return  videoFragmentView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    firebaseVideosSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseVideosSearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void firebaseVideosSearch(String query) {
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference.keepSynced(true);
        Query firebaseVideoSearchQuery = databaseReference.orderByChild("video_title").startAt(query.toUpperCase()).endAt(query + "\uf0ff");

        valueEventListener = firebaseVideoSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    VideoModel songModel = snapshot.getValue(VideoModel.class);
                    songModel.setmKey(dataSnapshot.getKey());
                    videoModels.add(songModel);
                }
                videoAdapter.notifyDataSetChanged();
                loading_video_bar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_video_bar.setVisibility(View.GONE);
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
