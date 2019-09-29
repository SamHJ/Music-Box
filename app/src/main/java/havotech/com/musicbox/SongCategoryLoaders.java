package havotech.com.musicbox;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class SongCategoryLoaders extends AppCompatActivity {

    Toolbar mToolbar;

    List<SongModel> songs;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SongsAdapter songsAdapter;
    ProgressBar loading_catgory_bar;
    RecyclerView category_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_category_loaders);
        mToolbar = findViewById(R.id.navigation_opener_toolbar);
        String title = getIntent().getStringExtra("title");
        String key = getIntent().getStringExtra("key");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);


        category_recyclerview = findViewById(R.id.category_recyclerview);
        loading_catgory_bar = findViewById(R.id.loading_catgory_bar);
        category_recyclerview.setHasFixedSize(true);
        category_recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        songs = new ArrayList<>();
        songsAdapter = new SongsAdapter(this, songs);
        category_recyclerview.setAdapter(songsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child(key);
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
                loading_catgory_bar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SongCategoryLoaders.this, ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_catgory_bar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_menu, menu);
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
        return super.onCreateOptionsMenu(menu);
    }


    private void firebaseMusicSearch(String query) {
        String key = getIntent().getStringExtra("key");
        databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child(key);
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
                loading_catgory_bar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SongCategoryLoaders.this, ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_catgory_bar.setVisibility(View.GONE);
            }
        });


    }


    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}
