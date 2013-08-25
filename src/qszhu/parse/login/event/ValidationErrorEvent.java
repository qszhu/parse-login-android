
package qszhu.parse.login.event;

public class ValidationErrorEvent {

    private Exception mException;

    public ValidationErrorEvent(Exception exception) {
        mException = exception;
    }

    public Exception getException() {
        return mException;
    }

}
