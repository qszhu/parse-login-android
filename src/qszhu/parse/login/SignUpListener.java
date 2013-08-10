
package qszhu.parse.login;

public interface SignUpListener {
    boolean onSignUp(String username, String password, String email);

    void onSignUpCompleted();

    void onSignUpError(Exception e);

    void onSignUpCancelled();
}
