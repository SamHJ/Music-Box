package havotech.com.musicbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroViewPageAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private int[] layouts;

    IntroViewPageAdapter( int[] layouts ,Context mContext){
        this.mContext = mContext;
        this.layouts = layouts;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layoutscreen = inflater.inflate(layouts[position],container, false);

        container.addView(layoutscreen);

        return layoutscreen;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View)object);
    }
}
