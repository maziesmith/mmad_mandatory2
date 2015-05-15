package dk.itu.ragr.mandatory2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Rasmus on 13-05-2015.
 */
public class GridViewDialogFragment extends DialogFragment {
    String[] imageList;
    private GridView gridView;
    private ClickListener listener;
    private static final String EXTRA_IMAGE_LIST = "EXTRA_IMAGE_LIST";

    public interface ClickListener{
        void onClick(int position);
    }

    public static GridViewDialogFragment newInstance(String[] imageList){
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_IMAGE_LIST, imageList);
        GridViewDialogFragment instance = new GridViewDialogFragment();
        instance.imageList = imageList;
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            imageList = savedInstanceState.getStringArray(EXTRA_IMAGE_LIST);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(EXTRA_IMAGE_LIST, imageList);
    }

    public void setOnItemClickListener(ClickListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);
        gridView = (GridView)view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(imageList, getActivity()));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    private class ImageAdapter extends BaseAdapter{
        String[] imageList;
        Context context;
        public ImageAdapter(String[] imageList, Context context)
        {
            this.context = context;
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            return imageList.length;
        }

        @Override
        public Object getItem(int position) {
            return imageList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ImageView view;
            if (convertView == null) {
                view = new ImageView(context);
                view.setLayoutParams(new GridView.LayoutParams(200, 200));
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setPadding(5, 5, 5, 5);
            } else {
                view = (ImageView) convertView;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });
            ImageProvider.putImage(context, view, imageList[position]);
            return view;
        }
    }
}
