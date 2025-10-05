
package com.hashedin.huspark.controller;

import com.hashedin.huspark.model.DocumentVersion;
import com.hashedin.huspark.repository.UserRepository;
import com.hashedin.huspark.service.DocumentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/subsections")
public class DocumentVersionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentVersionService docService;

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/course/{courseid}/section/{sectionid}/subsection/{subsectionId}/upload")
    public ResponseEntity<?> uploadPdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                       @PathVariable Long courseid,
                                       @PathVariable Long sectionid,
                                       @PathVariable Long subsectionId,
                                       @RequestParam("file") MultipartFile file) throws Exception {

        String token = authHeader.replace("Bearer ", "");

        docService.uploadPdf(token, courseid, sectionid, subsectionId, file);
        return ResponseEntity.ok("Uploaded");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/course/{courseid}/section/{sectionid}/subsection/{subsectionId}/revert")
    public ResponseEntity<?> revert(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                    @PathVariable Long courseid,
                                    @PathVariable Long sectionid,
                                    @PathVariable Long subsectionId) {

        String token = authHeader.replace("Bearer ", "");

        docService.revertToPreviousVersion(token, courseid, sectionid, subsectionId);
        return ResponseEntity.ok("Reverted to previous version");
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'STUDENT')")
    @GetMapping("/{subsectionId}/latest")
    public ResponseEntity<byte[]> downloadLatest(@PathVariable Long subsectionId) {

        DocumentVersion doc = docService.getLatestVersion(subsectionId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getFileType()))
                .body(doc.getData());
    }
}
