package dk.itu.ragr.mandatory2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rasmus on 06-05-2015.
 */
public class ImageFragment extends Fragment {
    private String url;

    public static ImageFragment newInstance(String url) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setUrl(url);
        return imageFragment;
    }

    public ImageFragment() {
        // Necessary to handle orientation changes correctly
        setRetainInstance(true);
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.ivImage);
        ImageView backgroundView = (ImageView) rootView.findViewById(R.id.ivBackground);
        ImageProvider.putImage(getActivity(), imageView, url);
        ImageProvider.putImage(getActivity(), backgroundView, url);

        TextView textView = (TextView) rootView.findViewById(R.id.tvTitle);
        textView.setText(url);

        return rootView;
    }
}
