package id.putraprima.retrofit.api.models;

public class Recipe {

    private int id;
    private String nama_resep;
    private String deskripsi;
    private String bahan;
    private String langkah_pembuatan;
    private String foto;

    public Recipe(int id, String nama_resep, String deskripsi, String bahan, String langkah_pembuatan, String foto) {
        this.id = id;
        this.nama_resep = nama_resep;
        this.deskripsi = deskripsi;
        this.bahan = bahan;
        this.langkah_pembuatan = langkah_pembuatan;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_resep() {
        return nama_resep;
    }

    public void setNama_resep(String nama_resep) {
        this.nama_resep = nama_resep;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getLangkah_pembuatan() {
        return langkah_pembuatan;
    }

    public void setLangkah_pembuatan(String langkah_pembuatan) {
        this.langkah_pembuatan = langkah_pembuatan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
