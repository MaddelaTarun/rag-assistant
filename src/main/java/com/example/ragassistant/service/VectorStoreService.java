package com.example.ragassistant.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VectorStoreService {

    private final VectorStore vectorStore;

    public VectorStoreService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void storeChunks(List<String> chunks, String fileName) {

        System.out.println("VectorStoreService Called");

        List<Document> documents = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            Document document = new Document(
                    chunks.get(i),
                    Map.of(
                            "fileName", fileName,
                            "chunkNumber", i + 1
                    )
            );

            documents.add(document);
        }

        System.out.println("Adding " + documents.size() + " documents to ChromaDB...");

        vectorStore.add(documents);
        System.out.println("Succesfully stored "+ documents.size()+" chunks in ChromaDB");
    }
}
