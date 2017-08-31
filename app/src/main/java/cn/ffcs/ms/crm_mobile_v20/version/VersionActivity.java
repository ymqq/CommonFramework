package cn.ffcs.ms.crm_mobile_v20.version;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.itbg.itpd.diskcache.FileUtils;
import cn.ffcs.itbg.itpd.http.OkHttpUtils;
import cn.ffcs.ms.crm_mobile_v20.R;
import cn.ffcs.ms.crm_mobile_v20.httpUtils.Networks;
import rx.Observer;

/**
 * Created by chenqq on 16-12-15.
 */

public class VersionActivity extends BaseAppCompatActivity {

    private View selectFiles;
    private View uploadFile;
    private View uploadFiles;
    private TextView fileNameList;
    private List<String> pathList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_version);

        selectFiles = findViewById(R.id.selectFiles);
        uploadFile = findViewById(R.id.uploadFile);
        uploadFiles = findViewById(R.id.uploadFiles);
        fileNameList = (TextView) findViewById(R.id.fileNameList);
    }

    @Override
    protected void initEvents() {
        selectFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Networks.uploadFile("UploadFile", pathList.get(0), new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Logger.i("onNext %s.", s);
                    }
                });
            }
        });

        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Networks.uploadFiles("UploadFiles", pathList, new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Logger.i("onNext %s.", s);
                    }
                });

//                OkHttpUtils.uploadFile("http://192.168.58.140:9999/api/upload/photos",
//                        pathList.get(0), new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                Logger.e("onFailure");
//                                Logger.e(e.getMessage());
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                Logger.i("onResponse %s.", response.body().toString());
//                            }
//                        });

            }
        });
    }

    @Override
    protected void start() {
        pathList = new ArrayList<>();
        OkHttpUtils.getOkHttpClient(this, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            Uri uri = data.getData();
            String path = FileUtils.getAbsolutePath(this, uri);
            fileNameList.setText(fileNameList.getText().toString() + "\n" + path);
            pathList.add(path);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
