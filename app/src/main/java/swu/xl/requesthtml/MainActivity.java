package swu.xl.requesthtml;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private EditText request_text;
    private Button request_btn;
    private TextView show_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        request_text = findViewById(R.id.request_text);
        request_btn = findViewById(R.id.request_btn);
        show_text = findViewById(R.id.show_text);

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //异步执行  `
                new RequestNetworkDataTask().execute(request_text.getText().toString());
            }
        });
    }

    /**
     * 获取URL
     * @return
     */
    private URL getURL(String urlString){
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();

            Toast.makeText(this, "URL是非法的", Toast.LENGTH_SHORT).show();
        }
        
        return null;
    }

    /**
     * 请求数据
     */
    private String requestData(String urlString){
        //获取URL
        URL url = getURL(urlString);

        //判断URL
        if (url == null){
            return null;
        }

        //使用URL
        try {
            //打开连接
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //设置响应时间
            connection.setConnectTimeout(30000);
            //设置请求方式
            connection.setRequestMethod("GET");

            //开始请求
            connection.connect();

            //响应代码
            int responseCode = connection.getResponseCode();
            //响应消息
            String responseMessage = connection.getResponseMessage();

            //响应成功
            if (responseCode == HttpURLConnection.HTTP_OK){
                //响应流
                InputStream inputStream = connection.getInputStream();

                //转化为字符串
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                char[] buffer = new char[1024];
                reader.read(buffer);
                String content = new String(buffer);
                return content;
            }

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(this, "读写错误", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /**
     * 异步类
     */
    class RequestNetworkDataTask extends AsyncTask<String,Integer,String>{

        //在后台work之前
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //主线程
            //UI Loading
        }


        //在后台work
        @Override
        protected String doInBackground(String... params) {
            String result = requestData(params[0]);

            return result;
        }

        //在后台work完毕了
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //执行完之后在主线程更新

            show_text.setText(result);
        }
    }
}
