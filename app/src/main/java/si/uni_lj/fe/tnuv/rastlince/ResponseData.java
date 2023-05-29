package si.uni_lj.fe.tnuv.rastlince;

import java.util.List;

public class ResponseData {
    private Query query;
    private String language;
    private String preferredReferential;
    private String switchToProject;
    private String bestMatch;
    private List<Result> results;
    private int remainingIdentificationRequests;
    private String version;

    // Getters and setters
    public String getFirstRespnseDataResultsSpeciesName() {
        return results.get(0).getResultSpeciesName();
    }

    public String getFirstResponseDataResultsSpeciesCommonName() {
        return results.get(0).getResultSpeciesCommonName();
    }
}

