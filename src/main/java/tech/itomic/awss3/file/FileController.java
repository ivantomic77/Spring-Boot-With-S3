package tech.itomic.awss3.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {
    @Autowired
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileModel>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @PostMapping
    public ResponseEntity<FileModel> uploadVideo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id) throws Exception {
        fileService.deleteFile(id);
        return ResponseEntity.ok("File has been deleted.");
    }
}
