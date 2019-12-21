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
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsAdapterViewHolder> {

    Context context;
    List<SongModel> songModels;

    public SongsAdapter(Context context, List<SongModel> songModels) {
        this.context = context;
        this.songModels = songModels;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.tracks_layout,viewGroup, false);
        return  new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongsAdapterViewHolder holder, int i) {
        final SongModel songModel = songModels.get(i);
        holder.song_name.setText(songModel.getSong_name());
        if(songModel.getPrice().equals("Free")){
            holder.song_price.setVisibility(View.GONE);
        }else {
            holder.song_price.setText("N "+songModel.getPrice());
        }
        holder.artiste_name.setText(songModel.getArtiste_name());
        Picasso.get().load(songModel.getSong_image_url()).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.song_image,
                new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(songModel.getSong_image_url())
                                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.song_image);
                    }
                });
        holder.song_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMusicPlayer = new Intent(context, MusicPlayer.class);
                openMusicPlayer.putExtra("song_url", songModel.getSong_url() );
                openMusicPlayer.putExtra("song_name", songModel.getSong_name());
                openMusicPlayer.putExtra("artiste_name", songModel.getArtiste_name());
                openMusicPlayer.putExtra("price", songModel.getPrice());
                openMusicPlayer.putExtra("song_image", songModel.getSong_image_url());
                Activity activity = (Activity) context;
                context.startActivity(openMusicPlayer);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songModels.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView song_name, artiste_name,song_price;
        ImageView song_image;
        CardView song_cardView;

        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            song_image = itemView.findViewById(R.id.song_image);
            song_name = itemView.findViewById(R.id.song_name);
            song_price = itemView.findViewById(R.id.song_price);
            artiste_name = itemView.findViewById(R.id.artiste_name);
            song_cardView = itemView.findViewById(R.id.song_card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
