package in.dev.ggs.exception;

public class JWTAuthTokenExpired {

    private String error;

    public JWTAuthTokenExpired(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
