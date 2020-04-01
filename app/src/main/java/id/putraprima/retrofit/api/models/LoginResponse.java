package id.putraprima.retrofit.api.models;

public class LoginResponse {

    public String token, token_type;
    public int expires_in;

    public LoginResponse(String token, String token_type, int expires_in) {
        this.token = token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
