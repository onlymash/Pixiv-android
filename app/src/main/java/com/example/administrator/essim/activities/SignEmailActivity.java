package com.example.administrator.essim.activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.network.AccountPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.UpdateInfoResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignEmailActivity extends BaseActivity {

    private EditText mEditText, mEditText2, mEditText3;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_email);

        initView();
    }

    private void initView() {
        mProgressBar = findViewById(R.id.progress);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar.setVisibility(View.INVISIBLE);
        mEditText = findViewById(R.id.email);
        mEditText2 = findViewById(R.id.pixiv_id);
        mEditText3 = findViewById(R.id.password);

        mTextView = findViewById(R.id.update);
        mTextView.setOnClickListener(view -> {
            if (mEditText.getText().toString().length() != 0) {
                if (mEditText2.getText().toString().length() != 0) {
                    if (mEditText3.getText().toString().length() != 0) {
                        updateUserInfo();
                    } else {
                        Common.showToast(mContext, "请输入密码");
                    }
                } else {
                    Common.showToast(mContext, "请输入Pixiv ID");
                }
            } else {
                Common.showToast(mContext, "请输入邮箱地址");
            }
        });
    }


    private void updateUserInfo() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<UpdateInfoResponse> call = RestClient
                .retrofit_Account
                .create(AccountPixivService.class)
                .updateUserInfo(LocalData.getToken(),
                        mEditText.getText().toString(),
                        mEditText2.getText().toString(),
                        LocalData.getUserPwd(),
                        mEditText3.getText().toString());
        call.enqueue(new Callback<UpdateInfoResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateInfoResponse> call,
                                   @NotNull Response<UpdateInfoResponse> response) {
                if (!response.isSuccessful()) {
                    ResponseBody responseBody = response.errorBody();
                    Common.showLog("00000000");
                    try {
                        Gson gson = new Gson();
                        UpdateInfoResponse updateInfoResponse = gson.fromJson(responseBody.string(), UpdateInfoResponse.class);
                        if (updateInfoResponse != null && updateInfoResponse.getBody() != null) {
                            Common.showLog("11111111111111");
                            if (updateInfoResponse.getBody().isIs_succeed()) {
                                Common.showToast(mContext, "更新成功");
                                Common.showLog("222222222222222");
                                mProgressBar.setVisibility(View.INVISIBLE);
                            } else {
                                Common.showLog("33333333333333");
                                if (updateInfoResponse.getBody().getValidation_errors() != null) {
                                    Common.showLog("4444444444444444");
                                    if (updateInfoResponse.getBody().getValidation_errors().getMail_address() != null) {
                                        if (updateInfoResponse.getBody().getValidation_errors().getMail_address().equals("このメールアドレスはすでに登録されています")) {
                                            Common.showToast(mContext, "该邮箱已被占用~");
                                            Common.showLog("555555555555555555");
                                        } else {
                                            Common.showToast(mContext, updateInfoResponse.getBody().getValidation_errors().getMail_address());
                                            Common.showLog("6666666666666666");
                                        }
                                    }
                                    if (updateInfoResponse.getBody().getValidation_errors().getPixiv_id() != null) {
                                        if (updateInfoResponse.getBody().getValidation_errors().getPixiv_id().equals("このpixiv IDは既に取得されています")) {
                                            Common.showLog("77777777777777777");
                                            Common.showToast(mContext, "该Pixiv ID已被占用~");
                                        } else {
                                            Common.showLog("888888888888888888");
                                            Common.showToast(mContext, updateInfoResponse.getBody().getValidation_errors().getPixiv_id());
                                        }
                                    }
                                    if (updateInfoResponse.getBody().getValidation_errors().getMessage() != null) {
                                        if (updateInfoResponse.getBody().getValidation_errors().getMessage().equals("これ以上、pixiv IDを変更することができません")) {
                                            Common.showLog("99999999999999999");
                                            Common.showToast(mContext, "Pixiv ID只允许变更一次，你已经变动过");
                                        } else {
                                            Common.showLog("10101010010101010");
                                            Common.showToast(mContext, updateInfoResponse.getBody().getValidation_errors().getMessage());
                                        }
                                    }
                                    if (updateInfoResponse.getBody().getValidation_errors().getPassword() != null) {
                                        if (updateInfoResponse.getBody().getValidation_errors().getPassword().equals("現在のパスワードと同じものは指定できません")) {
                                            Common.showLog("99999999999999999");
                                            Common.showToast(mContext, "新密码不能和老密码相同");
                                        } else {
                                            Common.showLog("10101010010101010");
                                            Common.showToast(mContext, updateInfoResponse.getBody().getValidation_errors().getPassword());
                                        }
                                    }
                                }
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    } catch (IOException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    UpdateInfoResponse updateInfoResponse = response.body();
                    if (updateInfoResponse != null && updateInfoResponse.getBody() != null) {
                        if (updateInfoResponse.getBody().isIs_succeed()) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Common.showToast(mContext, "操作成功，请根据邮箱内容完成注册！");
                        }
                    }else {
                        Common.showToast(mContext, "操作失败！");
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateInfoResponse> call, Throwable t) {
                Common.showToast(mContext, "操作失败！");
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
