package havotech.com.musicbox;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {


    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    ProgressBar loading_library_bar;
    RecyclerView library_recyclerview;

    List<LibraryModel> libraryModels;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    LibraryAdapter libraryAdapter;

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View libraryFragmentView = inflater.inflate(R.layout.fragment_library, container, false);

        mToolbar = libraryFragmentView.findViewById(R.id.navigation_opener_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Library");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        drawerLayout = libraryFragmentView.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close);

        loading_library_bar = libraryFragmentView.findViewById(R.id.loading_library_bar);
        library_recyclerview = libraryFragmentView.findViewById(R.id.library_recyclerview);
        library_recyclerview.setHasFixedSize(true);
        library_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        libraryModels = new ArrayList<>();
        libraryAdapter = new LibraryAdapter(getContext(), libraryModels);
        library_recyclerview.setAdapter(libraryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Library");
        databaseReference.keepSynced(true);
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                libraryModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LibraryModel songModel = snapshot.getValue(LibraryModel.class);
                    songModel.setmKey(dataSnapshot.getKey());
                    libraryModels.add(songModel);
                }
                libraryAdapter.notifyDataSetChanged();
                loading_library_bar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
                loading_library_bar.setVisibility(View.GONE);
            }
        });


        return libraryFragmentView;
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
