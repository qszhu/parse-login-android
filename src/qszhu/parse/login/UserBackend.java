
package qszhu.parse.login;

public interface UserBackend {
    interface Callback {
        void success(Object result);

        void error(Exception e);
    }

    void login(String username, String password, Callback callback);

    void signUp(String username, String password, String email, Callback callback);
}
