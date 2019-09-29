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

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.SongsAdapterViewHolder> {

    Context context;
    List<VideoModel> videoModels;

    public VideoAdapter(Context context, List<VideoModel> videoModels) {
        this.context = context;
        this.videoModels = videoModels;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_layout,viewGroup, false);
        return  new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongsAdapterViewHolder holder, int i) {
        final VideoModel videoModel = videoModels.get(i);
        holder.video_title.setText(videoModel.getVideo_title());
        holder.video_date.setText(videoModel.getVideo_date());
        holder.video_author.setText(videoModel.getVideo_author());
        Picasso.with(context).load(videoModel.getVideo_image_url()).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.video_image,
                new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context).load(videoModel.getVideo_image_url())
                                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.video_image);
                    }
                });


        holder.video_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(context, VideoPlayer.class);
                openAdvertUrl.putExtra("video_url", videoModel.getVideo_url() );
                openAdvertUrl.putExtra("video_title", videoModel.getVideo_title());
                openAdvertUrl.putExtra("price", videoModel.getPrice());
                Activity activity = (Activity) context;
                context.startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView video_title, video_date,video_author;
        ImageView video_image;
        CardView video_cardview;

        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            video_title = itemView.findViewById(R.id.video_title);
            video_author = itemView.findViewById(R.id.video_author);
            video_date = itemView.findViewById(R.id.video_date);
            video_image = itemView.findViewById(R.id.video_image);
            video_cardview = itemView.findViewById(R.id.advert_card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
