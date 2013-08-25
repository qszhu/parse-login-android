
package qszhu.parse.login;

public interface SignUpListener {
    void onSignUp(String username, String password, String email) throws Exception;

    void onSignUpCompleted(String username, String password);

    void onSignUpError(Exception e);

    void onSignUpCancelled();
}
