package com.example.nearestneedmerchant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nearestneedmerchant.Model.shopsmodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    EditText name, desc,phone,upi;
    Button submit;
    ImageView img;
    String userid,lat,lon;

    private Uri ImageUri;
    public static final int GalleryPick = 1;
    private StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=findViewById(R.id.editTextTextPersonName);
        desc=findViewById(R.id.editTextTextPersonName2);
        phone=findViewById(R.id.editTextPhone);
        submit=findViewById(R.id.submitbutton);
        upi=findViewById(R.id.upi);
        lat=getIntent().getStringExtra("lat");
        lon=getIntent().getStringExtra("lon");
        img=findViewById(R.id.shopimage);
        userid=getIntent().getStringExtra("id");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("Images").child("Profiles");
        databaseReference = FirebaseDatabase.getInstance().getReference("Merchants").child("Profile").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name.setText(snapshot.child("shopname").getValue().toString());
                    desc.setText(snapshot.child("shopdesc").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                    upi.setText(snapshot.child("upi").getValue().toString());
                    Glide.with(ProfileActivity.this).load(snapshot.child("shopimage").getValue().toString()).into(img);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){

                }else if (desc.getText().toString().isEmpty()){

                }else if (phone.getText().toString().isEmpty()){

                }else{
                    UploadImage();
                }
            }
        });
    }


    public void UploadImage() {

        if (ImageUri != null) {


            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(ImageUri));
            storageReference2.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempshopName = name.getText().toString().trim();

                            String Tempshopdesc = desc.getText().toString().trim();
                            String Tempphone =phone.getText().toString().trim();
                            String tempupi = upi.getText().toString().trim();


                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    shopsmodel userProfileInfo = new shopsmodel(TempshopName,Tempshopdesc,Tempphone, url,userid,lat,lon,tempupi);

                                    databaseReference.child("Profile").child(userid).setValue(userProfileInfo);

                                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_LONG).show();


                                }
                            });

//
                        }
                    });
        }
        else {

            Toast.makeText(ProfileActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            img.setImageURI(ImageUri);
        }
    }

    private void OpenGallery()
    {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GalleryPick);
    }
}