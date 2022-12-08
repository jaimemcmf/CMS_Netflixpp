/*package com.example.cms_netflixpp;

import com.android.volley.Request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Threads extends Thread{
    @Override
    public void run() {
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
                    try {
                        params.put("upload", new DataPart("ola.mp4", Files.readAllBytes(Paths.get(filePathv)), "multipart/form-data"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
    }
}
*/