package dk.itu.ragr.mandatory2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE_LIST = "EXTRA_IMAGE_LIST";
    private ViewPager pager;
    private String[] imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        pager = (ViewPager)findViewById(R.id.viewPager);
        if (savedInstanceState == null || savedInstanceState.getStringArray(EXTRA_IMAGE_LIST) == null) {
            new ImageListDownloader().execute();
        }
        else{
            setImageList(savedInstanceState.getStringArray(EXTRA_IMAGE_LIST));
        }
    }

    private void setImageList(String[] images)
    {
        imageList = images;
        try {
            pager.setAdapter(new PagerAdaptor(getSupportFragmentManager()));
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(EXTRA_IMAGE_LIST, imageList);
    }

    public void scrollToItem(int position){
        pager.setCurrentItem(position, true);
    }

    private class ImageListDownloader extends AsyncTask<Void, Void, String[]> {
        private static final String baseUrl = "http://www.itu.dk/people/jacok/MMAD/services/images/";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(GalleryActivity.this);
            pd.setTitle("Loading images");
            pd.setMessage("Loading images from server");
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            pd.dismiss();
            setImageList(strings);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection connection = null;
            try {
                Thread.sleep(1000); //We can wait a bit.. ;)
                ArrayList<String> items = new ArrayList<>();
                connection = (HttpURLConnection) new URL(baseUrl).openConnection();
                JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(connection.getInputStream())));
                reader.beginArray();
                while (reader.peek() != JsonToken.END_ARRAY){
                    items.add(reader.nextString());
                }
                return items.toArray(new String[items.size()]);
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
            finally {
                if (connection != null) connection.disconnect();
            }
        }
    }

    private class PagerAdaptor extends FragmentPagerAdapter{

        public PagerAdaptor(FragmentManager fm) throws IOException {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            return ImageFragment.newInstance(imageList[position], imageList);
        }

        @Override
        public int getCount() {
            return imageList.length;
        }
    }

}
