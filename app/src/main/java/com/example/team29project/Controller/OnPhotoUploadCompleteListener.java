package com.example.team29project.Controller;

public interface OnPhotoUploadCompleteListener {
    void onPhotoUploadComplete(String uniqueId);
    void onPhotoUploadFailure(Exception e);

}
