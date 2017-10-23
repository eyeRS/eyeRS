package com.github.eyers.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.EyeRS;
import com.github.eyers.R;
import com.github.eyers.info.UserProfileInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.github.eyers.EyeRS.REQUEST_READ_EXTERNAL_STORAGE;

/**
 * This class will handle the user's profile settings event.
 */
public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Field & other declarations
     */
    private EditText txtUsername;
    private String username;
    /**
     * Camera declarations
     */
    private String img;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    private Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUsername = (EditText) findViewById(R.id.edtTxtProfileName);
        findViewById(R.id.saveProfileBtn).setOnClickListener(this);

        this.ivImage = (ImageView) findViewById(R.id.new_user_avatar);
        this.ivImage.setOnClickListener(this);

        try {

            if (ContextCompat.checkSelfPermission(ProfileSettingsActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileSettingsActivity.this,
                        Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(ProfileSettingsActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_READ_EXTERNAL_STORAGE);
                }
            }

        } catch (Exception ex) {

            Log.e("Camera permissions", ex.getMessage(), ex);
        }

    }

    public void addProfileInfo() {

        /**
         * Content resolver declaration
         */
        ContentResolver eyeRSContentResolver = this.getContentResolver();

        /**
         * Define an object to contain the new values to insert.
         */
        ContentValues profileValues = new ContentValues();

        profileValues.put(UserProfileInfo.ProfileInfo.USER_NAME, username); //username
        profileValues.put(UserProfileInfo.ProfileInfo.USER_AVATAR, img); //user avatar

        try {

            /**
             * Content resolver profile insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_USER_PROFILE,
                    profileValues);

            Toast.makeText(this, "Profile settings updated successfully", Toast.LENGTH_LONG).show();
            Log.e("DATABASE OPERATIONS", "...New item added to DB!");

            /**
             * Then clear the fields
             */
            txtUsername.setText("");
            ivImage.setImageBitmap(null);

        } catch (Exception ex) {

            Log.e("Profile update fail", ex.getMessage(), ex);
            Toast.makeText(this, "Unable to update profile", Toast.LENGTH_SHORT).show();
        }

    } //end void addProfileInfo()

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {

        try {

            switch (view.getId()) {

                case R.id.saveProfileBtn:

                    username = txtUsername.getText().toString();

                    /**
                     * Empty username
                     */
                    if (username.isEmpty()) {

                        Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    /**
                     * No image added
                     */
                    else if (img.isEmpty()) {

                        Toast.makeText(this, "Please add an image", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    addProfileInfo(); //Method to save user profile data
                    break;
                case R.id.new_user_avatar:

                    selectImage();
                    break;
            }
        } catch (Exception ex) {

            Log.e("Profile event handler", ex.getMessage(), ex);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Toast.makeText(this, requestCode + "", Toast.LENGTH_LONG).show();

        try {

            switch (requestCode) {
                case REQUEST_READ_EXTERNAL_STORAGE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (userChoosenTask.equals("Take Photo"))
                            cameraIntent();
                        else if (userChoosenTask.equals("Choose from Library"))
                            galleryIntent();
                    } else {
                        //code for deny
                    }
                    break;
            }
        } catch (Exception ex) {

            Log.e("Camera permissions", ex.getMessage(), ex);
        }
    }

    private void selectImage() {

        final String[] items = {"Take Photo", "Choose from Library", "Cancel"};

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    boolean result = EyeRS.checkPermission(ProfileSettingsActivity.this);

                    if (items[item].equals("Take Photo")) {
                        userChoosenTask = "Take Photo";
                        if (result) {
                            cameraIntent();
                        }
                    } else if (items[item].equals("Choose from Library")) {
                        userChoosenTask = "Choose from Library";
                        if (result)
                            galleryIntent();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        } catch (Exception ex) {

            Log.e("selectImage()", ex.getMessage(), ex);
        }
    }

    private void galleryIntent() {

        try {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

        } catch (Exception ex) {

            Log.e("galleryIntent()", ex.getMessage(), ex);
        }
    }

    private void cameraIntent() {

        try {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);

        } catch (Exception ex) {

            Log.e("cameraIntent()", ex.getMessage(), ex);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }

        } catch (Exception ex) {

            Log.e("onActivityResult()", ex.getMessage(), ex);
        }
    }

    private void onCaptureImageResult(Intent data) {

        try {

            thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            img = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

            ivImage.setImageBitmap(thumbnail);

        } catch (Exception ex) {

            Log.e("Image capture", ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        try {

            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ivImage.setImageBitmap(bm);

        } catch (Exception ex) {

            Log.e("Gallery selection", ex.getMessage(), ex);
        }
    }
}
