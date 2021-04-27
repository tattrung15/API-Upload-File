package com.hithaui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hithaui.configs.APIClient;
import com.hithaui.models.FileUpload;
import com.hithaui.services.ImageService;
import com.hithaui.utils.RealPathUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private final static int SELECT_IMAGE_CODE = 1;
    Button btnSelect, btnPost;
    ImageView imageView;
    String realPathImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelect = findViewById(R.id.btnSelect);
        btnPost = findViewById(R.id.btnPost);
        imageView = findViewById(R.id.imageView);

        Retrofit retrofit = APIClient.getClient();
        ImageService imageService = retrofit.create(ImageService.class);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tạo task pick image qua action intent
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realPathImage != null) {
                    // tạo đối tượng file từ real path
                    File file = new File(realPathImage);

                    // tạo đối tượng RequestBody để gửi dữ liệu (file được truyền vào) lên api
                    RequestBody requestBody = RequestBody.create(MediaType.parse("/*"), file);

                    /* tạo đối tượng MultipartBody có kiểu form data, name (file) truyền vào phương thức
                       createFormData map (trùng) với key nhận dữ liệu trên server
                     */
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    imageService.uploadImage(fileToUpload)
                            .enqueue(new Callback<FileUpload>() {
                                @Override
                                public void onResponse(Call<FileUpload> call, Response<FileUpload> response) {
                                    // upload thành công
                                    if (response.body() != null) {
                                        System.err.println(response.body().getFileName());
                                    } else {
                                        System.err.println("RESPONSE BODY NULL");
                                    }
                                }

                                @Override
                                public void onFailure(Call<FileUpload> call, Throwable t) {
                                    // upload thất bại
                                    System.err.println("UPDATE PHOTO FAIL " + t.getMessage());
                                }
                            });
                } else {
                    // chưa chọn ảnh
                    Toast.makeText(getBaseContext(), "Bạn chưa chọn ảnh", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            realPathImage = RealPathUtil.getRealPathFromURI(uri, getBaseContext());
        }
    }
}