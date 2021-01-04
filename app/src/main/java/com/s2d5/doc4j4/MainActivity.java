package com.s2d5.doc4j4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sun.xml.bind.v2.runtime.JAXBContextImpl;

import org.docx4j.TextUtils;
import org.docx4j.XmlUtils;
import org.docx4j.convert.out.html.HtmlExporterNonXSLT;
import org.docx4j.jaxb.Context;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.model.images.ConversionImageHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import static org.docx4j.jaxb.Context.getJaxbImplementation;

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
            Log.e("TAG", "about to create package");

            // org.apache.harmony.xml.parsers.SAXParserFactoryImpl throws SAXNotRecognizedException
            // for feature http://javax.xml.XMLConstants/feature/secure-processing
            // so either disable XML security, or use a different parser.  Here we disable it.
            org.docx4j.jaxb.ProviderProperties.getProviderProperties().put(JAXBContextImpl.DISABLE_XML_SECURITY, Boolean.TRUE);

            // Can we init the Context?
            // You can delete this if you want...
            Log.e("TAG", String.valueOf(getJaxbImplementation()));
            Log.e("TAG", Context.jc.getClass().getName());

            // Create WordprocessingMLPackage
            InputStream inputStream = getResources().getAssets().open("sample.docx");
            WordprocessingMLPackage w = WordprocessingMLPackage.load(inputStream);

            String xml = w.getMainDocumentPart().getXML();
            Log.e("xml", xml);

            List<Object> lists = w.getMainDocumentPart().getContent();
            Log.e("lists", lists.size() + "");
            String outputStr = "";
            for (Object obj : lists) {
                outputStr += obj + System.lineSeparator();
                Log.e("obj", TextUtils.getText(obj));
            }

            output.setText(outputStr);
            Log.e("TAG", "done!");

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

