package havotech.com.musicbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.SongsAdapterViewHolder> {

    Context context;
    List<LibraryModel> libraryModels;

    public LibraryAdapter(Context context, List<LibraryModel> libraryModels) {
        this.context = context;
        this.libraryModels = libraryModels;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.library_layout,viewGroup, false);
        return  new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongsAdapterViewHolder holder, int i) {
        final LibraryModel advertModel = libraryModels.get(i);
        holder.library_name.setText(advertModel.getName());
        holder.no_of_songs_in_library.setText(String.valueOf(advertModel.getNo_of_songs()));
        Picasso.get().load(advertModel.getImage_url()).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.library_image,
                new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(advertModel.getImage_url())
                                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.library_image);
                    }


                });


        holder.library_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(context, LibrarySongCategoryLoader.class);
                openAdvertUrl.putExtra("library_title", advertModel.getName() );
                openAdvertUrl.putExtra("library_key", advertModel.getKey());
                Activity activity = (Activity) context;
                context.startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libraryModels.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView library_name, no_of_songs_in_library;
        ImageView library_image;
        CardView library_card_view;

        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            library_name = itemView.findViewById(R.id.library_name);
            no_of_songs_in_library = itemView.findViewById(R.id.no_of_songs_in_library);
            library_image = itemView.findViewById(R.id.library_image);
            library_card_view = itemView.findViewById(R.id.library_card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
