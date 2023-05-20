package org.crtlabs.streaming.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {

    private Logger logger = LoggerFactory.getLogger(S3Service.class);
    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${cloud.application.bucket.name}")
    private String bucketName;

    public String uploadFile(String fileName, MultipartFile file) {
        try {
            ObjectMetadata fileMetadata = new ObjectMetadata();
            fileMetadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), fileMetadata);
            return "File Uploaded: " + fileName;
        } catch (IOException ioe) {
            logger.error("IO Exception: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
            logger.info("Amazon Service Exception: " + ase.getMessage());
            throw ase;
        } catch (AmazonClientException ace) {

            logger.info("Amazon Client Exception: " + ace.getMessage());
            throw ace;
        }
        return "Unable to upload the file - " + fileName;
    }

}
