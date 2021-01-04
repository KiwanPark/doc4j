package com.s2d5.doc4j4;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.docx4j.TextUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView license;
    TextView output;

    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        license = findViewById(R.id.licenseTV);
        output = findViewById(R.id.outputTV);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            Log.e("version", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        license.setText(version);
        getDoc4j();
    }

    private void getDoc4j() {
        try {
            // Create WordprocessingMLPackage
            InputStream inputStream = getResources().getAssets().open("sample.docx");
            WordprocessingMLPackage w = WordprocessingMLPackage.load(inputStream);

            List<Object> lists = w.getMainDocumentPart().getContent();
            Log.e("lists", lists.size() + "");
            StringBuilder outputStr = new StringBuilder();
            for (Object obj : lists) {
                String text = TextUtils.getText(obj).replaceAll(getResources().getString(R.string.docx_garbage_mapper), System.lineSeparator());
                text = text.replace("\\h", "");
                outputStr.append(text).append(System.lineSeparator());
                //Log.e("obj", TextUtils.getText(obj));
            }
//
            output.setText(outputStr.toString());
            Log.e("TAG", "done!");

        } catch (IOException | Docx4JException e) {
            e.printStackTrace();
        }
    }
}

