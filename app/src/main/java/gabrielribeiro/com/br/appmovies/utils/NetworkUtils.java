package gabrielribeiro.com.br.appmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String PARAM_KEY= "api_key";
    private final static String PARAM_POPULAR = "popular";
    private final static String PARAM_TOP_RATED = "top_rated";
    private final static String PARAM_TRAILER = "videos";


    private final static String MOVIEDB_BASE_URL =
            "http://api.themoviedb.org/3/movie";



    public static URL buildUrl(String queryType, String  API_KEY, String movieId ) {

        Uri builtUri = null;
        String queryParam = PARAM_POPULAR;

        if(queryType.equals("popular")){
            queryParam = PARAM_POPULAR;
        }
        if(queryType.equals("top_rated")){
            queryParam = PARAM_TOP_RATED;
        }
        if(queryType.equals("trailer")){
            queryParam = movieId;
        }
        if(movieId != null){
            builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon().appendPath(queryParam)
                    .appendPath(PARAM_TRAILER)
                    .appendQueryParameter(PARAM_KEY,API_KEY)
                    .build();
        } else {
            builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon().appendPath(queryParam)
                    .appendQueryParameter(PARAM_KEY,API_KEY)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
