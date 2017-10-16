package com.github.eyers.activities.todo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.github.eyers.activities.UserProfileInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.github.eyers.EyeRS.REQUEST_READ_EXTERNAL_STORAGE;

/**
 * This class will handle the user's profile settings event.
 */
public class ProfileSettings extends AppCompatActivity implements View.OnClickListener {

    /**
     * Field & other declarations
     */
    private EditText txtUsername;
    String img;
    /**
     * Camera declarations
     */
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUsername = (EditText) findViewById(R.id.edtTxtProfileName);
        findViewById(R.id.saveProfileBtn).setOnClickListener(this);

        this.ivImage = (ImageView) findViewById(R.id.new_item_image);
        this.ivImage.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(ProfileSettings.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileSettings.this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(ProfileSettings.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

    }

    public void addProfileInfo() {

        String username = txtUsername.getText().toString();

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

            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_USER_PROFILE, profileValues);

            //Display a message to the user
            Toast.makeText(this, "Profile settings updated successfully", Toast.LENGTH_LONG).show();

            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New item added to DB!");

        } catch (SQLiteException ex) {
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

        switch (view.getId()) {

            case R.id.saveProfileBtn:

                addProfileInfo(); //Method to save user profile data
                break;
            case R.id.new_user_avatar:
                selectImage();
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Toast.makeText(this, requestCode + "", Toast.LENGTH_LONG).show();
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
    }

    private void selectImage() {
        final String[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = EyeRS.checkPermission(ProfileSettings.this);

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
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        img = "data:image/jpg;base64," + Base64.encodeToString(bytes.toByteArray(), 16);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }
}
