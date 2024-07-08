package com.example.blog_back.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private final String firebaseConfigPath = "C:\\Users\\dlthw\\backend\\blog_back\\src\\main\\resources\\bbanghoon-project-firebase-adminsdk-2uxpz-99296c19b4.json";

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    @PostConstruct
    public void initializeFirebaseApp() {
        try {
            FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(firebaseBucket)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("파이어베이스 application has been initialized");
            }
        } catch (IOException e) {
            System.out.println("Failed to initialize 파이어베이스 application: " + e.getMessage());
        }
    }
}
