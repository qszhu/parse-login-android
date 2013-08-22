
package qszhu.parse.login.event;

public class SignUpEvent {

    private String mUsername, mPassword, mEmail;

    public SignUpEvent(String username, String password, String email) {
        mUsername = username;
        mPassword = password;
        mEmail = email;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

}
