package tech.itomic.awss3.file;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.itomic.awss3.s3.S3Service;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    @Autowired
    private final S3Service s3Service;

    @Autowired
    private final FileRepository fileRepository;

    public List<FileModel> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileModel uploadFile(MultipartFile file) {
        String url = s3Service.uploadFile(file);
        FileModel fileModel = new FileModel();

        fileModel.setUrl(url);
        fileModel.setUpload_timestamp(LocalDateTime.now());

        return fileRepository.save(fileModel);
    }

    public void deleteFile(Long id) throws Exception {
        FileModel fileModel = fileRepository.getReferenceById(id);
        URL url = new URL(fileModel.getUrl());
        String filename = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);

        // delete file from DB and S3
        s3Service.deleteFile(filename);
        fileRepository.delete(fileModel);
    }

}
