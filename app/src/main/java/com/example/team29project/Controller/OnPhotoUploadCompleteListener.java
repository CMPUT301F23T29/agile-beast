package com.example.team29project.Controller;

/**
 * Callback Interface deals after photoUploading is done
 */
public interface OnPhotoUploadCompleteListener {
    void onPhotoUploadComplete(int position);
    void onPhotoUploadFailure(Exception e);

}
