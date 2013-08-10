
package qszhu.parse.login.sample;

import android.os.Bundle;

import qszhu.parse.login.LoginActivity;

public class MyLoginActivity extends LoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSignUp(String username, String password, String email) {
        if (!super.onSignUp(username, password, email)) {
            return false;
        }
        if (password.length() < 8) {
            this.showErrorDialog(R.string.error, R.string.error_password_length);
            return false;
        }
        return true;
    }

}
