package net.odiak.instagramviewer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImagesAdapter extends ArrayAdapter<InstagramImage> {

    LayoutInflater mInflater;

    public ImagesAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflateView(parent);
        }
        ImageViewHolder holder = (ImageViewHolder) convertView.getTag();

        InstagramImage image = getItem(position);
        String url = image.getImageUrl();

        Picasso.with(getContext()).load(url).into(holder.imageView);

        return convertView;
    }

    private View inflateView(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.image, parent, false);
        view.setTag(getViewHolder(view));
        return view;
    }

    private ImageViewHolder getViewHolder(View view) {
        ImageViewHolder holder = new ImageViewHolder();
        holder.imageView = (SquaredImageView) view.findViewById(R.id.imageView);
        return holder;
    }

    public static class ImageViewHolder {
        SquaredImageView imageView;
    }
}
