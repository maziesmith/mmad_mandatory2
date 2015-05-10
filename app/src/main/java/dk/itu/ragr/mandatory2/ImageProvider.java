package dk.itu.ragr.mandatory2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Rasmus on 06-05-2015.
 */
public class ImageProvider {
    private static final String baseUrl = "http://www.itu.dk/people/jacok/MMAD/services/images/";
    private static HashMap<String, Bitmap> cache = new HashMap<>();

    public static void putImage(Context context, ImageView view, String relativeUrl){
        if (cache.containsKey(relativeUrl)){
            view.setImageBitmap(cache.get(relativeUrl));
            return;
        }
        ImageLoaderTask task = new ImageLoaderTask(view, context);
        task.execute(relativeUrl);
    }

    private static class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{

        ImageView view;
        Context context;
        public ImageLoaderTask(ImageView view, Context context){
            this.context = context;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            view.setImageDrawable(context.getResources().getDrawable(R.drawable.loader));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            HttpURLConnection connection = null;
            try {
                Thread.sleep(1000);
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
