package in.dev.ggs.dto;

public class ErrorResponseEntity {

    private String error;

    public ErrorResponseEntity(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
