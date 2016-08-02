package com.alphaxdream.mangaviewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alphaxdream.mangaviewer.R;
import com.alphaxdream.mangaviewer.thirdlibs.GestureImageView;
import com.alphaxdream.mangaviewer.thirdlibs.GestureImageViewTouchListener;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

public class ViewImageActivity extends Activity {

    private static final String TAG = "ViewImageActivity";
    ImageView[] viewContainer=null;
    ArrayList<String> filesName=new ArrayList<>();

    ZipFile zipFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        Log.i(TAG, "onCreate: "+getIntent().getData().getPath());


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ViewPager viewPager= (ViewPager) findViewById(R.id.image);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            int now=-1;
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        try {
            zipFile=new ZipFile(getIntent().getData().getPath(),"GBK");
            Enumeration<ZipEntry> entries=zipFile.getEntries();
            ZipEntry zipEntry=null;
            while(entries.hasMoreElements()){
                zipEntry=entries.nextElement();
                if(zipEntry.isDirectory())continue;
                filesName.add(zipEntry.getName());
            }
            Collections.sort(filesName, new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    if(str1.length()==str2.length()){
                        return str1.compareTo(str2);
                    }else if(str1.length()<str2.length()){
                        return -1;
                    }else if(str1.length()>str2.length()){
                        return 1;
                    }else
                        return 0;

                }
            });
            viewContainer=new ImageView[filesName.size()];
            for(String n:filesName) Log.i(TAG, "onCreate: "+n);
            for(int i=0;i<5;i++){
                viewContainer[i]=createImageView(i);
            }
            /*zipEntry=entries.nextElement();
            ImageView v=new ImageView(this);
            Bitmap p=new BitmapDrawable(zipFile.getInputStream(zipEntry)).getBitmap();
            v.setImageBitmap(p);
            viewContainer.add(v);
            zipEntry=entries.nextElement();
            v=new ImageView(this);
            p=new BitmapDrawable(zipFile.getInputStream(zipEntry)).getBitmap();
            v.setImageBitmap(p);
            viewContainer.add(v);*/

            /*while(entries.hasMoreElements()){
                zipEntry=entries.nextElement();
                ImageView v=new ImageView(this);
                Bitmap p=new BitmapDrawable(zipFile.getInputStream(zipEntry)).getBitmap();
                v.setImageBitmap(p);
                viewContainer.add(v);
                break;
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPager.setAdapter(new PagerAdapter() {
            int now=-1;
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.i(TAG, "destroyItem "+position);
                container.removeView(viewContainer[position]);
                ((BitmapDrawable)viewContainer[position].getDrawable()).getBitmap().recycle();
                viewContainer[position]=null;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.i(TAG, "instantiateItem "+position);
                viewContainer[position]=createImageView(position);
                container.addView(viewContainer[position]);
//                v=createImageView(position);
//                container.addView(v);
//                if(now==-1){now=position;return viewContainer.get(0);}
//                switch (now-position){
//                    case -1://左滑
//                        viewContainer.remove(0);
//                        viewContainer.add(createImageView(position));
//                        break;
//                    case 0:break;
//                    case 1://右滑
//                        viewContainer.remove(2);
//                        viewContainer.add(createImageView(position));
//                        break;
//                }
//
//                now=position;
                return viewContainer[position];
            }

            @Override
            public int getCount() {
                //Log.i(TAG, "getCount ");
                return filesName.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                //Log.i(TAG, "isViewFromObject ");
                return view == object;
            }
        });
    }
    private ImageView createImageView(int i){
        //ImageView v=new ImageView(this);
        GestureImageView v=new GestureImageView(this);

        try {
            Bitmap p=new BitmapDrawable(zipFile.getInputStream(zipFile.getEntry(filesName.get(i)))).getBitmap();
            v.setImageBitmap(p);
            v.setOnTouchListener(new GestureImageViewTouchListener(v,p.getWidth(),p.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

}
