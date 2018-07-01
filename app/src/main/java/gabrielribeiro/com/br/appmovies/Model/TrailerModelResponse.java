package gabrielribeiro.com.br.appmovies.Model;

public class TrailerModelResponse {
    private String id;

    private TrailerDetailModel[] results;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public TrailerDetailModel[] getResults ()
    {
        return results;
    }

    public void setResults (TrailerDetailModel[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", results = "+results+"]";
    }
}
