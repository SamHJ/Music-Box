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

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.SongsAdapterViewHolder> {

    Context context;
    List<AdvertModel> advertModels;

    public AdvertAdapter(Context context, List<AdvertModel> advertModels) {
        this.context = context;
        this.advertModels = advertModels;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.buzz_layout,viewGroup, false);
        return  new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongsAdapterViewHolder holder, int i) {
        final AdvertModel advertModel = advertModels.get(i);
        holder.advert_title.setText(advertModel.getTitle());
        holder.advert_date.setText(advertModel.getDate_and_time());
        Picasso.with(context).load(advertModel.getAdvert_image_url()).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.advert_image,
                new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context).load(advertModel.getAdvert_image_url())
                                .placeholder(R.drawable.a).error(R.drawable.a).into(holder.advert_image);
                    }
                });


        holder.advert_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAdvertUrl = new Intent(context, WebViewLoader.class);
                openAdvertUrl.putExtra("advert_url", advertModel.getAdvert_url() );
                openAdvertUrl.putExtra("advert_title", advertModel.getTitle());
                Activity activity = (Activity) context;
                context.startActivity(openAdvertUrl);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertModels.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView advert_title, advert_date;
        ImageView advert_image;
        CardView advert_cardview;

        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
           advert_title = itemView.findViewById(R.id.advert_title);
           advert_date = itemView.findViewById(R.id.advert_date);
           advert_image = itemView.findViewById(R.id.advert_image);
           advert_cardview = itemView.findViewById(R.id.advert_card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
