package com.example.tsmc_prize_simulation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_FILE = 1;

    public static JSONArray json_array_total = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 打开文件选择器
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_OPEN_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    Log.d("TEST", "sucesss");
                    JSONObject jsonObject = loadJSONFromUri(uri);
                    if (jsonObject != null) {
                        json_array_total = null;
                        json_array_total = (JSONArray) jsonObject.get("Result");
                        Log.d("TEST", "json_array_total=" + json_array_total);
                        //jWriteFileAfterDelete(json_array_total.toString(), Global.ctx_main);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("TEST", "fail");
                }
                // 在这里使用选中的文件URI进行后续操作
                // 例如，读取文件内容或进行文件处理等
                Toast.makeText(this, "Selected file: " + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private JSONObject loadJSONFromUri(Uri uri) throws JSONException {
        JSONObject jsonObject = null;

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String jsonString = stringBuilder.toString();
            jsonObject = new JSONObject(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}