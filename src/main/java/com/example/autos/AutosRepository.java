package com.example.autos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutosRepository extends JpaRepository<Automobile, Long> {
}
