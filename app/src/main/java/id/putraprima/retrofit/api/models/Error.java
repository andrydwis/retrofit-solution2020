package id.putraprima.retrofit.api.models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("name")
    @Expose
    private List<String> name = null;
    public List<String> getName() {
        return name;
    }
    public void setName(List<String> name) {
        this.name = name;
    }

    @SerializedName("email")
    @Expose
    private List<String> email = null;
    public List<String> getEmail() {
        return email;
    }
    public void setEmail(List<String> email) {
        this.email = email;
    }

    @SerializedName("password")
    @Expose
    private List<String> password = null;
    public List<String> getPassword() {
        return password;
    }
    public void setPassword(List<String> password) {
        this.password = password;
    }

    @SerializedName("nama_resep")
    @Expose
    private List<String> nama_resep = null;
    public List<String> getNama_resep() {
        return nama_resep;
    }
    public void setNama_resep(List<String> nama_resep) {
        this.nama_resep = nama_resep;
    }

    @SerializedName("deskripsi")
    @Expose
    private List<String> deskripsi = null;
    public List<String> getDeskripsi() {
        return deskripsi;
    }
    public void setDeskripsi(List<String> deskripsi) {
        this.deskripsi = deskripsi;
    }

    @SerializedName("bahan")
    @Expose
    private List<String> bahan = null;
    public List<String> getBahan() {
        return bahan;
    }
    public void setBahan(List<String> bahan) {
        this.bahan = bahan;
    }

    @SerializedName("langkah_pembuatan")
    @Expose
    private List<String> langkah_pembuatan = null;
    public List<String> getLangkah_pembuatan() {
        return langkah_pembuatan;
    }
    public void setLangkah_pembuatan(List<String> langkah_pembuatan) {
        this.langkah_pembuatan = langkah_pembuatan;
    }

    @SerializedName("foto")
    @Expose
    private List<String> foto = null;
    public List<String> getFoto() {
        return foto;
    }
    public void setFoto(List<String> foto) {
        this.foto = foto;
    }
}