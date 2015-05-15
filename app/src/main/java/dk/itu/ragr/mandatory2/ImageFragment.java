package dk.itu.ragr.mandatory2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    private String[] urls;
    ImageView imageView;

    public static ImageFragment newInstance(String url, String[] urls) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.url = url;
        imageFragment.urls = urls;
        return imageFragment;
    }

    public ImageFragment() {
        // Necessary to handle orientation changes correctly
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.ivImage);

        ImageView backgroundView = (ImageView) rootView.findViewById(R.id.ivBackground);
        ImageProvider.putImage(getActivity(), imageView, url);
        ImageProvider.putImage(getActivity(), backgroundView, url);

        TextView textView = (TextView) rootView.findViewById(R.id.tvTitle);
        textView.setText(url);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //The click listeners must be updated on every start
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                final GridViewDialogFragment newFragment = GridViewDialogFragment.newInstance(urls);
                newFragment.setOnItemClickListener(new GridItemClickListener(newFragment));
                newFragment.show(ft, "dialog");
            }
        });

        final GridViewDialogFragment gridDialog = (GridViewDialogFragment)getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (gridDialog != null) {
            gridDialog.setOnItemClickListener(new GridItemClickListener(gridDialog));
        }
    }

    private class GridItemClickListener implements GridViewDialogFragment.ClickListener{
        GridViewDialogFragment dialogFragment;
        public GridItemClickListener(GridViewDialogFragment dialogFragment){
            this.dialogFragment = dialogFragment;
        }

        @Override
        public void onClick(int position) {
            if (getActivity() instanceof GalleryActivity) {
                ((GalleryActivity) getActivity()).scrollToItem(position);
                dialogFragment.dismiss();
            }
        }
    }

}
