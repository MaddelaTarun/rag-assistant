package com.example.ragassistant.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

@Service
public class TextExtractionService {

    public String extractText(File file) throws Exception {

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".txt")) {
            return Files.readString(file.toPath());
        }

        if (fileName.endsWith(".pdf")) {

            PDDocument document = Loader.loadPDF(file);

            PDFTextStripper stripper = new PDFTextStripper();

            String text = stripper.getText(document);

            document.close();

            return text;
        }

    if (fileName.endsWith(".docx")) {

        try (FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            return extractor.getText();
        }
    }
        return "Unsupported File Type";
    }
}