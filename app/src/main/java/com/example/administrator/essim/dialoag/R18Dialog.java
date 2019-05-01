package com.example.administrator.essim.dialoag;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.essim.R;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;

public class R18Dialog extends DialogFragment {

    private AlertDialog mAlertDialog;
    private EditText mEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_download, null);
        mEditText = view.findViewById(R.id.song_size);
        view.findViewById(R.id.download_now).setOnClickListener(v -> setPwd());
        view.findViewById(R.id.cancel).setOnClickListener(v -> mAlertDialog.dismiss());
        builder.setView(view);
        mAlertDialog = builder.create();
        return mAlertDialog;
    }

    private void setPwd() {
        if (mEditText.getText().toString().trim().length() == 0) {
            Common.showToast(getActivity(), "密码不能为空");
        } else {
            LocalData.setR18Pwd(mEditText.getText().toString());
            Common.showToast(getActivity(), "保存成功！");
            mAlertDialog.dismiss();
        }
    }
}
