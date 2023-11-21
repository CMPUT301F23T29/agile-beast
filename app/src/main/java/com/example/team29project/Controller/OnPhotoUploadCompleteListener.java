package com.example.team29project.Controller;

public interface OnPhotoUploadCompleteListener {
    void onPhotoUploadComplete(int position);
    void onPhotoUploadFailure(Exception e);

}
