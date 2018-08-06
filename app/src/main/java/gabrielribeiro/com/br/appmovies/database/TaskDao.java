package gabrielribeiro.com.br.appmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM movie ORDER BY voteAverage")
    List<MovieEntry> loadAllMovies();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Query("DELETE  FROM movie WHERE idMovie LIKE :movieId")
    void deleteMovie(String movieId);

    @Query("SELECT * FROM movie WHERE idMovie LIKE :movieId")
    List<MovieEntry> searchMovie(String movieId);
}
