package com.example.geekltdtest;


import java.util.ArrayList;
import java.util.List;

public class PhotoModel {
    public Race race;
    public int image;

    public PhotoModel(Race race, int image) {
        this.race = race;
        this.image = image;
    }
    public enum Race{
        JAPANESE,
        THAI,
        CHINESE,
        KOREAN
    }
    public static List<PhotoModel> createPhotoData(){
        List<PhotoModel> photoList = new ArrayList<>();
        photoList.add(new PhotoModel(Race.THAI,R.drawable.photo1));
        photoList.add(new PhotoModel(Race.CHINESE,R.drawable.photo2));
        photoList.add(new PhotoModel(Race.THAI,R.drawable.photo3));
        photoList.add(new PhotoModel(Race.JAPANESE, R.drawable.photo4));
        photoList.add(new PhotoModel(Race.THAI,R.drawable.photo5));
        photoList.add(new PhotoModel(Race.JAPANESE, R.drawable.photo6));
        photoList.add(new PhotoModel(Race.KOREAN,R.drawable.photo7));
        photoList.add(new PhotoModel(Race.KOREAN, R.drawable.photo8));
        photoList.add(new PhotoModel(Race.CHINESE,R.drawable.photo9));
        photoList.add(new PhotoModel(Race.KOREAN,R.drawable.photo10));
        return photoList;
    }
}
