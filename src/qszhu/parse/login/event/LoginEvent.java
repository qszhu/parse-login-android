
package qszhu.parse.login.event;

public class LoginEvent {

    private String mUsername, mPassword;

    public LoginEvent(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

}
