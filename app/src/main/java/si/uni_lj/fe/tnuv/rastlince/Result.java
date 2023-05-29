package si.uni_lj.fe.tnuv.rastlince;

import java.util.List;

public class Result {
    private double score;
    private Species species;
    private List<Image> images;
    private GBIF gbif;

    // Getters and setters
    public String getResultSpeciesName() {
        return species.getSpeciesName();
    }

    public String getResultSpeciesCommonName() {
        return species.getSpeciesCommonName();    }
}
