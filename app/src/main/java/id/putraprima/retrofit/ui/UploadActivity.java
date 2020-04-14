package id.putraprima.retrofit.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.services.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private Uri imageUri;
    private Bitmap imageBitmap;
    private ImageView mRecipeImage;

    private String nama_resep, deskripsi, bahan, langkah_pembuatan;
    private EditText mBahanText;
    private EditText mLangkahText;
    private EditText mNamaResepText;
    private EditText mDeskripsiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mRecipeImage = findViewById(R.id.recipeImage);
        mBahanText = findViewById(R.id.bahanText);
        mLangkahText = findViewById(R.id.langkahText);
        mNamaResepText = findViewById(R.id.namaResepText);
        mDeskripsiText = findViewById(R.id.deskripsiText);
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + "_image.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public void doUpload() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        nama_resep = mNamaResepText.getText().toString();
        deskripsi = mDeskripsiText.getText().toString();
        bahan = mBahanText.getText().toString();
        langkah_pembuatan = mLangkahText.getText().toString();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("fk_user", createPartFromString("1"));
        map.put("nama_resep", createPartFromString(nama_resep));
        map.put("deskripsi", createPartFromString(deskripsi));
        map.put("bahan", createPartFromString(bahan));
        map.put("langkah_pembuatan", createPartFromString(langkah_pembuatan));
        map.put("token", createPartFromString(preference.getString("token", null)));

        File file = createTempFile(imageBitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBody> call = service.doUpload(body, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Upload sukses", Toast.LENGTH_SHORT).show();
                } else {
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getNama_resep() != null) {
                        Toast.makeText(UploadActivity.this, error.getError().getNama_resep().get(0), Toast.LENGTH_SHORT).show();
                    } else if (error.getError().getDeskripsi() != null) {
                        Toast.makeText(UploadActivity.this, error.getError().getDeskripsi().get(0), Toast.LENGTH_SHORT).show();
                    } else if (error.getError().getBahan() != null) {
                        Toast.makeText(UploadActivity.this, error.getError().getBahan().get(0), Toast.LENGTH_SHORT).show();
                    } else if (error.getError().getLangkah_pembuatan() != null) {
                        Toast.makeText(UploadActivity.this, error.getError().getLangkah_pembuatan().get(0), Toast.LENGTH_SHORT).show();
                    } else if (error.getError().getFoto() != null) {
                        Toast.makeText(UploadActivity.this, error.getError().getFoto().get(0), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == 1) {
            if (data != null) {
                imageUri = data.getData();
                imageBitmap = (Bitmap) data.getExtras().get("data");
                mRecipeImage.setImageBitmap(imageBitmap);
            }
        }else if (requestCode == 2) {
            if (data != null) {
                imageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRecipeImage.setImageBitmap(imageBitmap);
            }
        }
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        pickImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void pickImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle("Pick Image");
        builder.setMessage("Select image source");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                dialog.cancel();
            }
        });
        builder.show();


    }


    public void handleChooseImage(View view) {
        requestCameraPermission();
    }

    public void handleUpload(View view) {
        if (imageBitmap != null) {
            doUpload();
        } else {
            Toast.makeText(this, "Capture image first", Toast.LENGTH_SHORT).show();
        }
    }
}
