package gabrielribeiro.com.br.appmovies.Model;

public class MovieModel {

    private String Title;

    private String PosterPath;

    public String getTitle() {
        return Title;
    }



    public MovieModel(String title, String posterPath){

        this.Title = title;
        this.PosterPath = posterPath;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }
}
