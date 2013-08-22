
package qszhu.parse.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgressFragment extends Fragment {

    private static final String ARG_MSG = "arg_message";

    public static ProgressFragment getInstance(String message) {
        ProgressFragment res = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MSG, message);
        res.setArguments(args);
        return res;
    }

    private String mMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString(ARG_MSG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.progress, container, false);
        ((TextView) v.findViewById(R.id.progress_message)).setText(mMessage);
        return v;
    }

}
