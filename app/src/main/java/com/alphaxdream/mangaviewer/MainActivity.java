package com.alphaxdream.mangaviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alphaxdream.mangaviewer.thirdlibs.GestureImageView;
import com.alphaxdream.mangaviewer.thirdlibs.GestureImageViewTouchListener;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.Intersect;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.zip.*;

public class MainActivity extends Activity {


    private static final String TAG = "MainActivity";
    ImageView[] viewContainer=null;
    ArrayList<String> filesName=new ArrayList<>();
    ZipFile zipFile;

    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton= (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "此功能开发中！", Toast.LENGTH_SHORT).show();
                showFileChooser();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){

            if(resultCode==RESULT_OK){
                //Intent intent=new Intent(this,ViewImageActivity.class);
                data.setClass(this,ViewImageActivity.class);
                startActivity(data);
            }
        }
    }

    private void showFileChooser(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"select a zip file!"),1);
    }
}
