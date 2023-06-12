package si.uni_lj.fe.tnuv.rastlince;

public class RastlinaModel {
    String rastlinaIme;
    String rastlinaVrsta;
    String rastlinaVrstaLat;
    int image;

    public RastlinaModel(String rastlinaIme, String rastlinaVrsta, String rastlinaVrstaLat, int image) {
        this.rastlinaIme = rastlinaIme;
        this.rastlinaVrsta = rastlinaVrsta;
        this.rastlinaVrstaLat = rastlinaVrstaLat;
        this.image = image;
    }

    public String getRastlinaIme() {
        return rastlinaIme;
    }

    public String getRastlinaVrsta() {
        return rastlinaVrsta;
    }

    public String getRastlinaVrstaLat() {
        return rastlinaVrstaLat;
    }

    public int getImage() {
        return image;
    }
}
