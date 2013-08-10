
package qszhu.parse.login;

public interface LoginListener {
    boolean onLogin(String username, String password);

    void onLoginCompleted(Object user);

    void onLoginError(Exception e);

    void onLoginCancelled();
}
