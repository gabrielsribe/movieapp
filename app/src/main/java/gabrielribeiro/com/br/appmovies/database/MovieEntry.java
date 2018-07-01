package gabrielribeiro.com.br.appmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "movie")
public class MovieEntry {

    //CONSTRUCT TABLE TO MOVIE ROW
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String idMovie;
    private String originalTitle;
    //DESCOBRIR COMO SALVAR O POSTER
    private String sinopse;
    private String voteAverage;
    private String releaseDate;

    @Ignore
    public MovieEntry(String originalTitle, String idMovie, String sinopse, String voteAverage,
                      String releaseDate) {
        this.originalTitle = originalTitle;
        this.idMovie = idMovie;
        this.sinopse = sinopse;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public MovieEntry(int id, String originalTitle, String idMovie, String sinopse, String voteAverage,
                      String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.idMovie = idMovie;
        this.sinopse = sinopse;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
