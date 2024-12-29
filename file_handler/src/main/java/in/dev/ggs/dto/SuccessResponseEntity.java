package in.dev.ggs.dto;

public class SuccessResponseEntity {

    private Object response;

    public SuccessResponseEntity(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
