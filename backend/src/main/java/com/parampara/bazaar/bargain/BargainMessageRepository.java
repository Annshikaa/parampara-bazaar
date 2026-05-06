package com.parampara.bazaar.bargain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BargainMessageRepository extends JpaRepository<BargainMessage, Long> {
    List<BargainMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
    List<BargainMessage> findTop10BySessionIdOrderByCreatedAtDesc(Long sessionId);
}

