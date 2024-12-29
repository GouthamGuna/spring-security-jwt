package in.dev.ggs.dto;

public class AuthResponseEntity {

    private Object token;

    public AuthResponseEntity(Object token) {
        this.token = token;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }
}
