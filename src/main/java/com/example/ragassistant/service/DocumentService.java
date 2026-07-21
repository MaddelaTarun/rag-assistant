package com.example.ragassistant.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ragassistant.entity.Document;
import com.example.ragassistant.repository.DocumentRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TextExtractionService textExtractionService;

    @Autowired
    private ChunkingService chunkingService;

    @Autowired
    private VectorStoreService vectorStoreService;

    private final String UPLOAD_DIR = "uploads/";

    public Document uploadDocument(MultipartFile file) throws Exception {

        // Create uploads directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save uploaded file
        Path filePath = uploadPath.resolve(file.getOriginalFilename());

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Extract text from the uploaded document
        String extractedText = textExtractionService.extractText(filePath.toFile());
        List<String> chunks = chunkingService.createChunks(extractedText);

        vectorStoreService.storeChunks(chunks, file.getOriginalFilename());

        System.out.println("Number of Chunks : " + chunks.size());

        for (int i = 0; i < chunks.size(); i++) {

            System.out.println("-----------------------------");
            System.out.println("Chunk " + (i + 1));
            System.out.println("-----------------------------");

            System.out.println(chunks.get(i));
        }

        // Print extracted text in the console (for testing)
        System.out.println("\n========== EXTRACTED TEXT ==========");
        System.out.println(extractedText);
        System.out.println("====================================\n");

        // Save document details in MySQL
        Document document = new Document();

        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setUploadDate(LocalDateTime.now());

        return documentRepository.save(document);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
