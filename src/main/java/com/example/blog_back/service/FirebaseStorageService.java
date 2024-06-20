package com.example.blog_back.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseStorageService {
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());
        
        URL signedUrl = blob.signUrl(365, TimeUnit.DAYS); // 1년간 유효한 URL 생성
        return signedUrl.toString();
    }
}
