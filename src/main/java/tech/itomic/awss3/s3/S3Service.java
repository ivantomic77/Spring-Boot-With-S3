package tech.itomic.awss3.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket.name}")
    public String BUCKET_NAME;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file){
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID().toString();

        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(BUCKET_NAME, key, file.getInputStream(), metadata);
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception accured while uploading the file.");
        }

        amazonS3.setObjectAcl(BUCKET_NAME, key, CannedAccessControlList.PublicRead);

        return amazonS3.getUrl(BUCKET_NAME, key).toString();
    }


    public String deleteFile(final String fileName) {
        amazonS3.deleteObject(BUCKET_NAME, fileName);
        return "Deleted File: " + fileName;
    }
}
