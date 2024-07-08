package com.example.blog_back.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseStorageService {

    @Value("${app.firebase-bucket}")
    private String firebaseStorageUrl;

    // 기존 uploadFile 메서드
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());

        URL signedUrl = blob.signUrl(365, TimeUnit.DAYS); // 1년간 유효한 URL 생성
        return signedUrl.toString();
    }

    public String uploadFirebaseBucket(MultipartFile multipartFile, String fileName) throws IOException {
        System.out.println("받아온 Firebase Bucket: " + firebaseStorageUrl);
        
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);
        Blob blob = bucket.create(fileName, multipartFile.getInputStream(), multipartFile.getContentType());
        return blob.getMediaLink(); // 파이어베이스에 저장된 파일 url
    }    
    
    // 파일 삭제
    public void deleteFirebaseBucket(String key) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);
        
        bucket.get(key).delete();
    }
}
