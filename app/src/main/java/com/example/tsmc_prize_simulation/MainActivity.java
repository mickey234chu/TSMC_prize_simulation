package com.example.tsmc_prize_simulation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public EditText input;
    public Button button;
    public ListView listView;
    public static String tag = "TEST";
    private static final int REQUEST_CODE_OPEN_FILE = 1;

    private PrizeItemAdapter mPrizeItemAdapter;
    public static JSONArray json_array_total = new JSONArray();
    public static JSONArray json_array_4card = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 打开文件选择器
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE);
        input = findViewById(R.id.input);
        button = findViewById(R.id.button1);
        listView = findViewById(R.id.listview);
        mPrizeItemAdapter = new PrizeItemAdapter(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardId = input.getText().toString();
                json_array_4card = Search4card(json_array_total, cardId);
                Log.d(tag,cardId + " : " + json_array_4card);
                if (json_array_4card.length() == 0) {
                    Toast.makeText(getApplicationContext(), "沒有中獎發票", Toast.LENGTH_SHORT).show();
                }
                Log.d(tag, "json_array_4card=" + json_array_4card.toString());
                for (int i = 0; i < json_array_4card.length(); i++) {
                    HistoryQueryResponseResult entry = new HistoryQueryResponseResult();
                    try {

                        JSONObject obj = (JSONObject) json_array_4card.get(i);
                        entry.amount = (String) obj.get("prizeamount");
                        String tmp = (String) obj.get("isPrinted");
                        entry.isPrinted = (String) obj.get("isPrinted");
                        //    entry.isPrinted = (tmp.equals("Yes")?"1":"0");
                        JSONObject objinv = (JSONObject) obj.get("Invoice");
                        entry.number = (String) objinv.get("Number");
                        String date = (String) objinv.get("Date");
                        date = new StringBuffer(date).insert(4, "/").toString();
                        date = new StringBuffer(date).insert(7, "/").toString();
                        String time = (String) objinv.get("Time");
                        time = new StringBuffer(time).insert(2, ":").toString();
                        time = new StringBuffer(time).insert(5, ":").toString();
                        //  entry.date="2021-11-16T07:12:09.000+0000";
                        entry.date = date + " " + time;
                        String pze = (String) obj.get("prizelevel");
                        entry.winningStatus = transWinningStatus(pze);
                    }
                    catch (JSONException e)
                    {
                        Log.e(tag, "Error message", e);
                    }

                    // if(entry.winningStatus.equals("none"))
                    //     continue;
                    try {
                        mPrizeItemAdapter.add(entry);
                        listView.setAdapter(mPrizeItemAdapter);
                    }
                    catch (Exception e)
                    {
                        Log.e(tag, "listView Error message", e);
                    }

                    //       btnPrizeSelected.requestFocus();
                    Log.d(tag, "entry==" + new Gson().toJson(entry).toString());
                }
            }

        });
    }
    public static String transWinningStatus(String status) {
        if (status.length() == 1) { // 中獎
            if ("A".equals(status)) {
                return "特別獎";
            } else if ("B".equals(status)) {
                return "雲端發票專屬兩千元獎";
            } else if ("C".equals(status)) {
                return "雲端發票專屬百萬元獎";
            } else if ("D".equals(status)) {
                return "雲端發票專屬五百元獎";
            } else if ("E".equals(status)) {
                return "雲端發票專屬八百元獎";
            } else if ("0".equals(status)) {
                return "特獎";
            } else if ("1".equals(status)) {
                return "頭獎";
            } else if ("2".equals(status)) {
                return "二獎";
            } else if ("3".equals(status)) {
                return "三獎";
            } else if ("4".equals(status)) {
                return "四獎";
            } else if ("5".equals(status)) {
                return "五獎";
            } else if ("6".equals(status)) {
                return "六獎";
            } else {
                // throw new IllegalArgumentException("unknow type: " + status);
                return "新增獎";
            }
        } else {
            throw new IllegalArgumentException("unknow type: " + status);
        }
    }
    public static JSONArray Search4card(JSONArray array, String cardnum) {
        JSONArray filtedArray = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = null;
            try {


                obj = (JSONObject) array.get(i);
                if (obj.get("cardnumber").equals(cardnum)) {
                    Log.d(tag, "toJSONString=" + obj.toString());
                    filtedArray.put(obj);
                }
                String nownum = (String) obj.get("cardnumber");
                if (nownum.length() > 6)
                    Log.d(tag, "nownum=" + nownum.toString());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return filtedArray;
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