package id.putraprima.retrofit.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import id.putraprima.retrofit.api.models.Error;

public class ApiError {

    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}