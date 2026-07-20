package com.example.ragassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ragassistant.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}