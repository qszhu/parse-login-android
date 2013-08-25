
package qszhu.parse.login.sample;

import android.os.Bundle;

import qszhu.parse.login.LoginActivity;

public class MyLoginActivity extends LoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSignUp(String username, String password, String email) throws Exception {
        super.onSignUp(username, password, email);
        if (password.length() < 8) {
            throw new Exception(getString(R.string.error_password_length));
        }
    }

}
