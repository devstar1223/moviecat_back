package com.moviecat.www;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
class JasyptConfigAESTest {

    @Test
    @DisplayName("DB 정보 암호화 테스트")
    void stringEncryptor() {
        String url = "test";
        String username = "test";
        String password = "test";

        System.out.println(jasyptEncoding(url));
        System.out.println(jasyptEncoding(username));
        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        String key = System.getenv("encryptorKey");
        stringEncryptor.setPassword(key);
        System.out.println(key);
        stringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        stringEncryptor.setPassword(key);
        stringEncryptor.setIvGenerator(new RandomIvGenerator());

        return stringEncryptor.encrypt(value);
    }
}