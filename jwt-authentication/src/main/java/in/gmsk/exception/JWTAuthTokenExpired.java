package in.gmsk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_GATEWAY)
public class JWTAuthTokenExpired extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String resourcename;
    private String fieldname;
    private Object fieldvalue;

    public JWTAuthTokenExpired(String resourcename, String fieldname, Object fieldvalue) {
        super(String.format("%s Login once more. %s : '%s'",resourcename, fieldname, fieldvalue));
        this.resourcename = resourcename;
        this.fieldname = fieldname;
        this.fieldvalue = fieldvalue;
    }

    public String getResourcename() {
        return resourcename;
    }

    public String getFieldname() {
        return fieldname;
    }

    public Object getFieldvalue() {
        return fieldvalue;
    }
}
