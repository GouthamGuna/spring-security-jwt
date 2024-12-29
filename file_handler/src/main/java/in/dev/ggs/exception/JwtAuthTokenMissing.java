package in.dev.ggs.exception;

public class JwtAuthTokenMissing {

    private String error;

    public JwtAuthTokenMissing(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}