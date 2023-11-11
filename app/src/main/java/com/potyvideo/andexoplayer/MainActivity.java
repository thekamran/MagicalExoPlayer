package com.potyvideo.andexoplayer;

import static com.potyvideo.library.utils.PublicValues.REQUEST_ID_MULTIPLE_PERMISSIONS;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.potyvideo.library.AndExoPlayerView;
import com.potyvideo.library.globalInterfaces.ExoPlayerCallBack;
import com.potyvideo.library.utils.PathUtil;
import com.potyvideo.library.utils.PublicFunctions;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AndExoPlayerView andExoPlayerView;

    private String TEST_URL_MP4 = "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4";

    private String TEST_URL_HLS = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";

    private String TEST_URL_MP3 = "https://host2.rj-mw1.com/media/podcast/mp3-192/Tehranto-41.mp3";

    private int req_code = 129;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        andExoPlayerView = findViewById(R.id.andExoPlayerView);

        findViewById(R.id.local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocaleVideo();
            }
        });

        findViewById(R.id.mp4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMP4ServerSide();
            }
        });

        findViewById(R.id.hls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHls();
            }
        });

        findViewById(R.id.mp3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMp3();
            }
        });

    }

    private void loadMp3() {
        andExoPlayerView.setSource(TEST_URL_MP3);
    }

    private void loadHls() {
        andExoPlayerView.setSource(TEST_URL_HLS);
    }

    private void loadMP4ServerSide() {
        andExoPlayerView.setSource(TEST_URL_MP4);
    }

    private void selectLocaleVideo() {
        if (checkAccessStoragePermission(this)) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), req_code);
        }
    }

    private static boolean checkAccessStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private void loadMP4Locale(String filePath) {
        andExoPlayerView.setSource(filePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req_code && resultCode == RESULT_OK) {
            Uri finalVideoUri = data.getData();
            String filePath = null;
            try {
                filePath = PathUtil.getPath(this, finalVideoUri);
                loadMP4Locale(filePath);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
