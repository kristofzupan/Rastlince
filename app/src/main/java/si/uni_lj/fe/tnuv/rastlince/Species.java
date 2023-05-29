package si.uni_lj.fe.tnuv.rastlince;

import java.util.List;

public class Species {
    private String scientificNameWithoutAuthor;
    private String scientificNameAuthorship;
    private String scientificName;
    private Genus genus;
    private Family family;
    private List<String> commonNames;

    // Getters and setters
    public String getSpeciesName() {
        return scientificName;
    }

    public String getSpeciesCommonName() {
        return commonNames.get(0);
    }
}
