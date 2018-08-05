package gabrielribeiro.com.br.appmovies.utils;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import gabrielribeiro.com.br.appmovies.Model.TrailerModelResponse;

public class TrailerSearch {

    TrailerModelResponse trailerResponse;


    public TrailerModelResponse ParseJsonToTrailerList(String jsonData){


        Gson gson = new Gson();
        try{
            TrailerModelResponse response = gson.fromJson( jsonData, TrailerModelResponse.class );
            return response;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public class trailerQuerySearch extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieDbSearchResults = null;
            try {
                movieDbSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieDbSearchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {

            if (searchResults != null && !searchResults.equals("")) {

                ParseJsonToTrailerList(searchResults);

            }
        }
    }


}


