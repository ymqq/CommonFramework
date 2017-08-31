package cn.ffcs.ms.crm_mobile_v20.login;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.itbg.itpd.diskcache.DiskCacheHelper;
import cn.ffcs.itbg.itpd.diskcache.FileUtils;
import cn.ffcs.itbg.itpd.zxing.ImageUtils;
import cn.ffcs.ms.crm_mobile_v20.R;
import cn.ffcs.ms.crm_mobile_v20.entities.User;
import cn.ffcs.ms.crm_mobile_v20.main.MainActivity;
import rx.functions.Action1;

/**
 * Created by Vic on 16/12/29.
 */

public class LoginActivity extends BaseAppCompatActivity implements LoginConstract.View {
    private LoginConstract.Presenter mPresenter;
    private EditText login_account_et, login_password_et;
    private Button login_login_btn, login_cancel_btn;
    private static final int REQUEST_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_login);
        login_account_et = (EditText) findViewById(R.id.login_account_et);
        login_password_et = (EditText) findViewById(R.id.login_password_et);
    }

    @Override
    protected void initEvents() {
        RxView.clicks(findViewById(R.id.login_login_btn))
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        User user = new User();
                        user.setAccount(login_account_et.getText().toString());
                        user.setPassword(login_password_et.getText().toString());
                        login(user);
                    }
                });
        RxView.clicks(findViewById(R.id.login_cancel_btn))
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_IMAGE);
                    }
                });
    }

    @Override
    protected void start() {
        initPresenter();

        DiskCacheHelper.getInstance().open(this, DiskCacheHelper.CACHE_TYPE.DATA, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void login(User user) {
        mPresenter.login(user, new LoginConstract.LoginCallback() {
            @Override
            public void onSuccessed(User user) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailed(String err) {
                showMessage(err);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        showSnackbar(message);
    }

    @Override
    public void initPresenter() {
        mPresenter = new LoginPresenter(this);
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        DiskCacheHelper.getInstance().flush().close();
    }

    private int count = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                final Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                count++;
                                String path = FileUtils.getAbsolutePath(LoginActivity.this, uri);
                                /**
                                 * 首先判断图片的大小,若图片过大,则执行图片的裁剪操作,防止OOM
                                 */
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true; // 先获取原大小
                                Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
                                options.inJustDecodeBounds = false; // 获取新的大小

                                options.inSampleSize = 2;
                                mBitmap = BitmapFactory.decodeFile(path, options);

                                List<Bitmap> bitmapList = ImageUtils.splitPicture(mBitmap, 2);
                                bitmapList.addAll(ImageUtils.splitPicture(mBitmap, 3));
                                for (int i = 0, length = bitmapList.size(); i < length; i++) {
                                    Bitmap bitmap = bitmapList.get(i);
                                    final String result = ImageUtils.analyzeBitmap(bitmap);
                                    if (!"Failed".equals(result)) {
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, "解析结果:" + result, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    ImageUtils.saveBitmapAsFile(LoginActivity.this, bitmap, "BitmapTest", "第" + count + "张_split_" + i + "_" + result + ".png");
                                }
                                final String result = ImageUtils.analyzeBitmap(mBitmap);
                                if (!"Failed".equals(result)) {
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "解析结果:" + result, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                ImageUtils.saveBitmapAsFile(LoginActivity.this, mBitmap, "BitmapTest", "第" + count + "张_" + result + ".png");
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "解析结束！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
