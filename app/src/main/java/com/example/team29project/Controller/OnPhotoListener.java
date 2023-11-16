package com.example.team29project.Controller;

public interface OnPhotoListener {
    void onPhotoUrlReady(String photoUrl);

    void onFailure(Exception e);

}
