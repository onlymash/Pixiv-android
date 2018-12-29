package com.example.administrator.essim.utils;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.BookmarkAddResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils_re.LocalData;
import com.github.ybq.android.spinkit.style.Wave;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

/*
//       quu..__
//        $$$b  `---.__
//         "$$b        `--.                          ___.---uuudP
//          `$$b           `.__.------.__     __.---'      $$$$"              .
//            "$b          -'            `-.-'            $$$"              .'|
//              ".                                       d$"             _.'  |
//                `.   /                              ..."             .'     |
//                  `./                           ..::-'            _.'       |
//                   /                         .:::-'            .-'         .'
//                  :                          ::''\          _.'            |
//                 .' .-.             .-.           `.      .'               |
//                 : /'$$|           .@"$\           `.   .'              _.-'
//                .'|$u$$|          |$$,$$|           |  <            _.-'
//                | `:$$:'          :$$$$$:           `.  `.       .-'
//                :                  `"--'             |    `-.     \
//               :##.       ==             .###.       `.      `.    `\
//               |##:                      :###:        |        >     >
//               |#'     `..'`..'          `###'        x:      /     /
//                \                                   xXX|     /    ./
//                 \                                xXXX'|    /   ./
//                 /`-.                                  `.  /   /
//                :    `-  ...........,                   | /  .'
//                |         ``:::::::'       .            |<    `.
//                |             ```          |           x| \ `.:``.
//                |                         .'    /'   xXX|  `:`M`M':.
//                |    |                    ;    /:' xXXX'|  -'MMMMM:'
//                `.  .'                   :    /:'       |-'MMMM.-'
//                 |  |                   .'   /'        .'MMM.-'
//                 `'`'                   :  ,'          |MMM<
//                   |                     `'            |tbap\
//                    \                                  :MM.-'
//                     \                 |              .''
//                      \.               `.            /
//                       /     .:::::::.. :           /
//                      |     .:::::::::::`.         /
//                      |   .:::------------\       /
//                     /   .''               >::'  /
//                     `',:                 :    .'
//                                          `:.:'
*/


public class Common {




    public static AlphaAnimation getAnimation() {
        AlphaAnimation alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
        return alphaAnimationShowIcon;
    }

    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void postFollowUser(String auth, int userID, View view, String followType) {
        Call<BookmarkAddResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .postFollowUser(auth, userID, followType);
        call.enqueue(new Callback<BookmarkAddResponse>() {
            @Override
            public void onResponse(Call<BookmarkAddResponse> call, retrofit2.Response<BookmarkAddResponse> response) {
                if (followType.equals("public")) {
                    Snackbar.make(view, "关注成功~(公开的)", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "关注成功~(非公开的)", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookmarkAddResponse> call, Throwable throwable) {
            }
        });
    }

    public static void postUnFollowUser(String auth, int userID, View view) {
        Call<ResponseBody> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .postUnfollowUser(auth, userID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Snackbar.make(view, "取消关注~", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
            }
        });
    }

    //接收时间戳，格式化时间并返回
    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有日期数据哦";
    }

    public static String getTime(String time, int timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有数据";
    }

    //得到当前时间回退两天的日期
    public static String getLastDay() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -2);
        Date today = now.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(today);
    }

    //快速打LOG
    public static <T> void showLog(T t) {
        Log.d("a line of my log", String.valueOf(t));
    }

    //返回分页的页数
    public static int getPageCount(String itemCount) {
        if (Integer.valueOf(itemCount) < 20) {
            return 1;
        } else if ((Integer.valueOf(itemCount) / 20 < 20) && (Integer.valueOf(itemCount) / 20 >= 1)) {
            return Integer.valueOf(itemCount) / 20;
        } else {
            return 20;
        }
    }

    public static void clearLocalData(Context context) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //通知相册更新图片
    public static void sendBroadcast(Context context, File file) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }

    public static void sendBroadcast(Context context, Uri uri) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    public static void copyMessage(Context mContext, String tag) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", tag);
        assert cm != null;
        cm.setPrimaryClip(mClipData);
        Toast.makeText(mContext, tag + " 已复制到剪切板~", Toast.LENGTH_SHORT).show();
    }



    public static Wave getLoaderAnimation(Context context) {
        Wave wave = new Wave();
        wave.setColor(context.getResources().getColor(R.color.colorPrimary));
        return wave;
    }

    public static File generatePictureFile(Context context, IllustsBean illustsBean, int positionInIllust, int fileNameStyle, int downloadType) {
        //检验父文件夹是否存在在，若不存在则创建
        File parentFile = new File(LocalData.getDownloadPath());
        if (!parentFile.exists()) {
            parentFile.mkdir();
            Common.showToast("文件夹创建成功~");
        }
        //File file = new File(父文件夹路径， 文件名); 此格式生成具体的文件，File其他构造方法会生成文件夹无法写入
        if (illustsBean.getPage_count() == 1) {
            switch (fileNameStyle) {
                case 0:
                    return new File(parentFile.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".jpeg");
                case 1:
                    return new File(parentFile.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".png");
                case 2:
                    return new File(parentFile.getPath(), illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId() + "_" +
                            String.valueOf(positionInIllust) + ".jpeg");
                case 3:
                    return new File(parentFile.getPath(), illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId() + "_" +
                            String.valueOf(positionInIllust) + ".png");
            }
        } else if (downloadType == 0) {
            File secondParent = new File(parentFile.getPath() + "/" + illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId());
            if (!secondParent.exists()) {
                secondParent.mkdir();
            }
            showLog(secondParent.getPath());
            switch (fileNameStyle) {
                case 0:
                    return new File(secondParent.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".jpeg");
                case 1:
                    return new File(secondParent.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".png");
                case 2:
                    return new File(secondParent.getPath(), illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId() + "_" +
                            String.valueOf(positionInIllust) + ".jpeg");
                case 3:
                    return new File(secondParent.getPath(), illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId() + "_" +
                            String.valueOf(positionInIllust) + ".png");
            }
        } else if (downloadType == 1) {
            switch (fileNameStyle) {
                case 0:
                    return new File(parentFile.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".jpeg");
                case 1:
                    return new File(parentFile.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".png");
                case 2:
                    return new File(parentFile.getPath(), illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId() + "_" +
                            String.valueOf(positionInIllust) + ".jpeg");
                case 3:
                    return new File(parentFile.getPath(), illustsBean.getTitle().replace("/", " ") + "_" + illustsBean.getId() + "_" +
                            String.valueOf(positionInIllust) + ".png");
            }
        }
        return null;
    }



    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }



    private static Toast toast = null;

    /**
     * 应用内显示消息
     *
     * @param v
     * @param t
     * @param <T>
     */
    public static <T> void showSnack(View v, T t) {
        Snackbar.make(v, String.valueOf(t), Snackbar.LENGTH_SHORT).show();
    }



    /**
     * 应用内显示吐司
     *
     * @param context 上下文
     * @param t       泛型参数
     */
    public static <T> void showToast(Context context, T t) {
        if (toast == null) {
            toast = Toast.makeText(context, String.valueOf(t), Toast.LENGTH_SHORT);
        } else {
            toast.setText(String.valueOf(t));
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static <T> void showToast(T t) {
        if (toast == null) {
            toast = Toast.makeText(PixivApp.getContext(), String.valueOf(t), Toast.LENGTH_SHORT);
        } else {
            toast.setText(String.valueOf(t));
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
