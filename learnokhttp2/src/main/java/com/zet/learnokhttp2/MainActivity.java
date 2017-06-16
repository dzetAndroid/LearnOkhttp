package com.zet.learnokhttp2;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

import static com.zet.learnokhttp2.app.GlobalApplication.okHttpClient;
import static com.zet.learnokhttp2.global.GlobalField.mBasePostFileUrl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";

    private Button mButtonDoGet;
    private TextView mTextViewShowInfo;
    private Button mButtonDoPost;

    private final Handler mHandler = new Handler();
    private Button mButtonDoPostString;
    private Button mButtonDoPostFile;
    private Button mGetAsyncHttp;
    private Button mGetSyncHttp;
    private Button mBtnCancel;
    private Button mOpenGallery;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                postFile();
            }
        }, 100);
    }

//    /**
//     * 异步Get 请求
//     */
//    private void getAsyncHttp()
//    {
//
//        // 构造Request
//        final Request request = new Request.Builder()
//                .url(piaotianBaseUrl)
//                .get()
//                .cacheControl(CacheControl.FORCE_NETWORK)
//                .build();
//
//        // 构造Call
//        Call call = okHttpClient.newCall(request);
//
//        // 请求   异步
//        call.enqueue(new Callback()
//        {
//            @Override
//            public void onFailure(Request request, IOException e)
//            {
//                Log.e(TAG, "onFailure: ");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException
//            {
//                if (response.cacheResponse() != null)
//                {
//                    String toString1 = response.cacheResponse().toString();
//                    Log.e(TAG, "onResponse: 1" + toString1);
//                }
//                if (response.networkResponse() != null)
//                {
//                    String toString2 = response.networkResponse().toString();
//                    Log.e(TAG, "onResponse: 2" + toString2);
//                }
//
//                // 运行非主线程
//                String string3 = response.body().string();
//                Log.e(TAG, "onResponse: 3" + string3.substring(0, 100));
//
////                MainActivity.this.runOnUiThread(new Runnable()
////                {
////                    @Override
////                    public void run()
////                    {
////                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
////                    }
////                });
//
//            }
//        });
//    }

//    /**
//     * 同步Get 请求
//     */
//    class MySyncTask extends AsyncTask<String, Integer, String>
//    {
//        public MySyncTask()
//        {
//            super();
//        }
//
//        /**
//         * 执行前
//         */
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//        }
//
//        /**
//         * 执行后
//         *
//         * @param s
//         */
//        @Override
//        protected void onPostExecute(String s)
//        {
//            super.onPostExecute(s);
//            mTextViewShowInfo.setText(s);
//        }
//
//        /**
//         * 进度更新
//         *
//         * @param values
//         */
//        @Override
//        protected void onProgressUpdate(Integer... values)
//        {
//            super.onProgressUpdate(values);
//            Log.e(TAG, "onProgressUpdate: " + values[0]);
//        }
//
//        /**
//         * 取消时
//         *
//         * @param s
//         */
//        @Override
//        protected void onCancelled(String s)
//        {
//            super.onCancelled(s);
//        }
//
//        /**
//         * 取消时
//         */
//        @Override
//        protected void onCancelled()
//        {
//            super.onCancelled();
//        }
//
//        /**
//         * 后台任务 请求中
//         *
//         * @param params
//         * @return
//         */
//        @Override
//        protected String doInBackground(String... params)
//        {
//            String string = null;
//            // 构造Request
//            Request request = new Request.Builder()
//                    .url(piaotianBaseUrl)
//                    .get()
//                    .build();
//
//            // 构造Call
//            Call call = okHttpClient.newCall(request);
//
//            // 请求   同步
//            try
//            {
//                Response response = call.execute();
//                if (response != null)
//                {
//
//                    if (response.isSuccessful())
//                    {
//                        string = response.body().string();
//                        Log.e(TAG, "doInBackground: " + string);
//                    } else
//                    {
//                        Log.e(TAG, "doInBackground: " + "failed");
//                    }
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//
//            return string;
//        }
//    }

//    /**
//     * post 异步 请求
//     */
//    private void postAsyncHttp()
//    {
//
//        RequestBody requestBody = new FormEncodingBuilder()
//                .add("username", "sdfsdgasg")
//                .build();
//
//        Request request = new Request.Builder()
//                .url(mBaseUrl + "login")
//                .post(requestBody)
//                .build();
//
//        dealCall(request);
//    }

    /**
     * findviewbyid
     */
    private void initView()
    {
        mButtonDoGet = (Button) findViewById(R.id.mButtonDoGet);
        mButtonDoGet.setOnClickListener(this);
        mTextViewShowInfo = (TextView) findViewById(R.id.mTextViewShowInfo);
        mButtonDoPost = (Button) findViewById(R.id.mButtonDoPost);
        mButtonDoPost.setOnClickListener(this);
        mButtonDoPostString = (Button) findViewById(R.id.mButtonDoPostString);
        mButtonDoPostString.setOnClickListener(this);
        mButtonDoPostFile = (Button) findViewById(R.id.mButtonDoPostFile);
        mButtonDoPostFile.setOnClickListener(this);
        mGetAsyncHttp = (Button) findViewById(R.id.mGetAsyncHttp);
        mGetAsyncHttp.setOnClickListener(this);
        mGetSyncHttp = (Button) findViewById(R.id.mGetSyncHttp);
        mGetSyncHttp.setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.mBtnCancel);
        mBtnCancel.setOnClickListener(this);
        mOpenGallery = (Button) findViewById(R.id.mOpenGallery);
        mOpenGallery.setOnClickListener(this);
        mImg = (ImageView) findViewById(R.id.mImg);
        mImg.setOnClickListener(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        okHttpClient.cancel(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.mButtonDoGet:
                break;
            case R.id.mButtonDoPost:
                break;
            case R.id.mButtonDoPostString:
                break;
            case R.id.mButtonDoPostFile:
                break;
            case R.id.mGetAsyncHttp:
                break;
            case R.id.mGetSyncHttp:
                break;
            case R.id.mBtnCancel:
                break;
            case R.id.mOpenGallery:
                break;
        }
    }

//    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//
//    private void cancel()
//    {
//        final Request request = new Request.Builder()
//                .url(piaotianBaseUrl)
//                .get()
//                .cacheControl(CacheControl.FORCE_NETWORK)
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        final Call finalCall = call;
//
//        // 100毫秒后取消call
//        executorService.schedule(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                finalCall.cancel();
//            }
//        }, 1, TimeUnit.MICROSECONDS); // TUM
//
//        call.enqueue(new Callback()
//        {
//            @Override
//            public void onFailure(Request request, IOException e)
//            {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException
//            {
//                if (response.cacheResponse() != null)
//                {
//                    String s = response.cacheResponse().toString();
//                    Log.e(TAG, "onResponse: " + s);
//                }
//                if (response.networkResponse() != null)
//                {
//                    String s = response.networkResponse().toString();
//                    Log.e(TAG, "onResponse: " + s);
//                }
//            }
//        });
//        Log.e(TAG, "onResponse: " + "success");
//    }


    public void postFile()
    {
        try
        {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();

            File file = new File(canonicalPath, "xjy.jpg");
            Log.e(TAG, "postFile: " + file.getCanonicalPath());
            if (file.exists() && file.length() > 0)
            {
                final AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("profile_picture", file);
                client.post(mBasePostFileUrl, params, new AsyncHttpResponseHandler()
                {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                    {
                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error)
                    {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_LONG).show();
                    }
                });
            } else
            {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * post
     * file
     */
    private void doPostFile()
    {
        Log.e(TAG, "doPostFile: " + Environment.getExternalStorageDirectory());
//        File file = new File(Environment.getExternalStorageDirectory(), "cc.jpg");
        String canonicalPath = null;
        try
        {
            canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            Log.e(TAG, "doPostFile: canonicalPath " + canonicalPath);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

//        File file = Environment.getExternalStorageDirectory();
        File file = new File(canonicalPath, "cc.jpg");
        try
        {
            Log.e(TAG, "doPostFile: " + file.getCanonicalPath());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (!file.exists())
        {
            Log.e(TAG, "doPostFile: " + "file not exist!");
            return;
        }


//        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream;"), file);
//        Log.e(TAG, "doPostFile: " + file.getAbsolutePath());
//
////        new MultipartBuilder()
////                .type(MultipartBuilder.FORM)
////                .addPart(Headers.of())
//
//        Request.Builder builder = new Request.Builder();
//        Request request = builder
//                .url(mBasePostStringUrl)
//                .post(fileBody)
//                .build();
//
//        dealCall(request);
    }

    private void dealCall(Request request)
    {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {

            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                // 获取字符串
                final String result = response.body().string();
                // 获取响应
                Log.e(TAG, "onResponse: " + result);
                // 运行主线程
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mTextViewShowInfo.setText(result);
                    }
                });
            }
        });
    }

//    /**
//     * post
//     * string
//     */
//    private void doPostString()
//    {
//        User user = new User("user1", "pw1");
//        String json = JSON.toJSON(user).toString();
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), json);
//
//        Request.Builder builder = new Request.Builder();
//        Request request = builder
//                .url(mBasePostStringUrl)
//                .post(requestBody)
//                .build();
//
//        dealCall(request);
//
//    }
//
//
//    /**
//     * post
//     * param
//     */
//    private void doPost()
//    {
//        // 2. 构造Request
//        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
//        RequestBody requestBody = formEncodingBuilder
//                .add("username", "zetman")
//                .add("password", "778899")
//                .build();
//
//        Request.Builder builder = new Request.Builder();
//        Request request = builder
//                .url(mBaseLoginUrl)
//                .post(requestBody)
//                .build();
//
//        // 3. 构造call
//        dealCall(request);
//
//    }
//
//    /**
//     * get
//     */
//    private void doGet()
//    {
//        try
//        {
//            // 2. 构造Request
//            Request.Builder builder = new Request.Builder();
//            Request request = builder
//                    .url(mBaseUrl + "login?username=hmj&password=123")
//                    .get()
//                    .build();
//
//            // 3. 构造Call
//            dealCall(request);
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }


}
