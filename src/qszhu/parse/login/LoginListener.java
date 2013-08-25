
package qszhu.parse.login;

public interface LoginListener {
    void onLogin(String username, String password) throws Exception;

    void onLoginCompleted(Object user);

    void onLoginError(Exception e);

    void onLoginCancelled();
}
