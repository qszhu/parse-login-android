
package qszhu.parse.login.event;

public class ShowProgressEvent {

    private String mMessage;

    public ShowProgressEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

}
