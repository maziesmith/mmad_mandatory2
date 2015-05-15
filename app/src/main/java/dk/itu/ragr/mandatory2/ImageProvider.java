package dk.itu.ragr.mandatory2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rasmus on 06-05-2015.
 */
public class ImageProvider {
    private static final String baseUrl = "http://www.itu.dk/people/jacok/MMAD/services/images/";
    private static HashMap<String, Bitmap> cache = new HashMap<>();
    private static HashMap<String, ArrayList<ImageView>> waiting = new HashMap<>();

    /**
     * Put an image in a ImageView.
     * This method always return immediately.
     * Must be called on UI thread, since it manipulates the UI.
     * If the image is not cached it will be downloaded and put in place when the download is complete
     * @param context
     * @param view
     * @param relativeUrl
     */
    public static void putImage(Context context, ImageView view, String relativeUrl){
        if (cache.containsKey(relativeUrl)){
            view.setImageBitmap(cache.get(relativeUrl));
            return;
        }
        if (!waiting.containsKey(relativeUrl))
            waiting.put(relativeUrl, new ArrayList<ImageView>());
        //Safe because it's executed on the UI thread
        waiting.get(relativeUrl).add(view);
        ImageLoaderTask task = new ImageLoaderTask();
        task.execute(relativeUrl);
    }

    private static class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{
        String url;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //Safe because it's executed on the UI thread
            for (ImageView v : waiting.get(url))
                v.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            this.url = params[0];
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(baseUrl+url).openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                cache.put(url, bitmap);
                return bitmap;
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
            finally {
                if (connection != null) connection.disconnect();
            }
        }
    }

}
