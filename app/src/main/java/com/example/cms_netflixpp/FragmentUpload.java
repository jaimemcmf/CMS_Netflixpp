package com.example.cms_netflixpp;

import static android.app.Activity.RESULT_OK;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class FragmentUpload extends Fragment {
    final static String uploadURL = "http://34.175.83.209:8080/upload/movie";

    TextView txt_pathShow;
    TextView txt_thumbnail_path;
    Button btn_videoPicker;
    Button btn_thumbnailPicker;
    Button btn_send;
    Intent myFileIntent;
    Intent myThumbnailIntent;
    EditText txt_title;

    byte[] video;
    byte[] thumb = null;

    View view;

    //@Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        txt_pathShow = view.findViewById(R.id.txt_path);
        txt_thumbnail_path = view.findViewById(R.id.thumbnail_path);
        btn_videoPicker = view.findViewById(R.id.uploadv);
        btn_thumbnailPicker = view.findViewById(R.id.uploadt);
        btn_send = view.findViewById(R.id.send);
        txt_title = view.findViewById(R.id.fileName);

        btn_videoPicker.setOnClickListener(view -> {
            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("video/*");
            startActivityForResult(myFileIntent, 10);
        });

        btn_thumbnailPicker.setOnClickListener(view -> {
            myThumbnailIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myThumbnailIntent.setType("image/*");
            startActivityForResult(myThumbnailIntent, 20);
        });

        btn_send.setOnClickListener(view -> {
            String title = txt_title.getText().toString();
            updateRequest(title);
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Context context = requireContext().getApplicationContext();
                    String path = uri.getPath();
                    txt_pathShow.setText(path);
                    video = videoSend(context, uri);
                }
                break;

            case 20:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Context context = requireContext().getApplicationContext();
                    String path = uri.getPath();
                    txt_thumbnail_path.setText(path);
                    thumb = thumbnailSend(context, uri);
                    System.out.println(txt_thumbnail_path.getText());
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    protected void updateRequest(String title) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadURL, response -> {
        }, Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", "a");
                params.put("pass", "a");
                params.put("fileName", title);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("upload", new DataPart("ola.mp4", video, "video/mp4"));
                if (thumb != null) {
                    params.put("thumbnail", new DataPart("file_cover.png", thumb, "image/png"));
                }
                return params;
            }
        };
        com.example.cms_netflixpp.VolleySingleton volleySingleton = new com.example.cms_netflixpp.VolleySingleton(requireActivity().getApplicationContext());
        volleySingleton.addToRequestQueue(multipartRequest);
        thumb = null;
    }

    @Nullable
    public static byte[] videoSend(@NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        String filePath = context.getApplicationInfo().dataDir + File.separator + "video.mp4";
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[104857600];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
            return buf;
        } catch (IOException ignore) {
            return null;
        }
    }

    @Nullable
    public static byte[] thumbnailSend(@NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;
        String filePath = context.getApplicationInfo().dataDir + File.separator + "thumbnail.png";
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1048576];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
            return buf;
        } catch (IOException ignore) {
            return null;
        }
    }
}