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
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        pager = (ViewPager)findViewById(R.id.viewPager);

        ImageListDownloader downloader = new ImageListDownloader();
        downloader.execute();
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

            try {
                pager.setAdapter(new PagerAdaptor(getSupportFragmentManager(), strings));
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection connection = null;
            try {
                Thread.sleep(5000);
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
        String[] urls; /*= new String[]{
            "img/2608-1273524021cCGQ.jpg",
            "img/church-silhouette-8712964986014Kb.jpg",
            "img/ivy-on-house-in-autumn-112881926976ezt.jpg",
            "img/night-in-the-city-21851292200793awk.jpg",
            "img/old-castle-gate-11281969200foh6.jpg",
            "img/shack-29211280016930gtkz.jpg"
        };
*/

        public PagerAdaptor(FragmentManager fm, String[] urls) throws IOException {
            super(fm);
            this.urls = urls;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(urls[position]);
        }

        @Override
        public int getCount() {
            return urls.length;
        }
    }

}
