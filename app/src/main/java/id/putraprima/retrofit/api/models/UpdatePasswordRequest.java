package id.putraprima.retrofit.api.models;

public class UpdatePasswordRequest {

    public String password;
    public String password_confirmation;

    public UpdatePasswordRequest(String password, String password_confirmation) {
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }
}
