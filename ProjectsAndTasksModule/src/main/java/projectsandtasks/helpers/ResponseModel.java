package projectsandtasks.helpers;

/**
 * Created by Vejsil on 28.03.2017..
 */
public class ResponseModel {

    public ResponseModel() {
    }

    public ResponseModel(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}