package nju.tb.net;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import nju.tb.Commen.MyAppContext;
import nju.tb.entity.ProgressMultipartEntity;

@SuppressWarnings("deprecation")
public class HttpImage extends AsyncTask<String, Integer, String> {
    private final String POST_URL = Net.URL_PREFIX + "/user/uploadPicture";
    private Context context;
    private HttpClient httpClient;
    private File file;
    private long totalSize;
    private ProgressDialog pd;
    private PostOver postOver;

    public HttpImage(Context context, File file) {
        this.context = context;
        this.file = file;
        httpClient = ((MyAppContext) this.context.getApplicationContext()).getHttpClient();
    }

    public void setPostOver(PostOver postOver) {
        this.postOver = postOver;
    }

    //post请求 将手机本地图片上传，返回图片URL
    private String doUpload(File f) {
        if (!MyAppContext.getIsConnected()) {
            return "netwrong";
        }

        HttpPost httpPost = new HttpPost(POST_URL);

        FileBody pic = new FileBody(f);
        ProgressMultipartEntity multipartEntity = new ProgressMultipartEntity(
                new ProgressMultipartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                }
        );
        multipartEntity.addPart("file", pic);
        totalSize = multipartEntity.getContentLength();
        try {
            httpPost.setEntity(multipartEntity);
            if (!MyAppContext.getIsConnected()) {
                return "netwrong";
            }
            HttpResponse response = httpClient.execute(httpPost);
            while (response == null) {
                if (!MyAppContext.getIsConnected()) {
                    return "netwrong";
                }
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            for (line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            String linkurl = data.getString("url");
            if (linkurl.equals("")) {
                response.getEntity().consumeContent();
                return "wrong";
            }
            response.getEntity().consumeContent();
            return linkurl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "wrong";
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("Uploading Picture...");
        pd.setCancelable(false);
        pd.show();
    }


    @Override
    protected String doInBackground(String... params) {
        String s = doUpload(file);
        return s;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        pd.setProgress((int) (progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
        postOver.over(result);
    }

    @Override
    protected void onCancelled() {

    }

    public interface PostOver {
        void over(String s);
    }

}
