package com.reqven.kayu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends Fragment {
    private FragmentAccountListener listener;

    public interface FragmentAccountListener {
        void onInputAccountSent(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentAccountListener) {
            listener = (FragmentAccountListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAccountListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
