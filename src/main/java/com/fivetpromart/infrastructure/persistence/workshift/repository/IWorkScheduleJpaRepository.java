package com.fivetpromart.infrastructure.persistence.workshift.repository;

import com.fivetpromart.infrastructure.persistence.workshift.WorkScheduleDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkScheduleJpaRepository extends JpaRepository<WorkScheduleDbo, String> {
    
    Optional<WorkScheduleDbo> findByWorkDateAndWorkShiftId(LocalDate workDate, String workShiftId);
    
    List<WorkScheduleDbo> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<WorkScheduleDbo> findByWorkDateBetweenAndWorkShiftId(LocalDate startDate, LocalDate endDate, String workShiftId);
    
    @Query("SELECT DISTINCT s FROM WorkScheduleDbo s JOIN s.assignments a " +
           "WHERE s.workDate BETWEEN :startDate AND :endDate " +
           "AND a.profileId = :profileId")
    List<WorkScheduleDbo> findByWorkDateBetweenAndProfileId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("profileId") String profileId
    );
    
    @Query("SELECT s FROM WorkScheduleDbo s JOIN s.assignments a " +
           "WHERE s.workDate = :workDate AND a.profileId = :profileId")
    List<WorkScheduleDbo> findByWorkDateAndProfileId(
            @Param("workDate") LocalDate workDate,
            @Param("profileId") String profileId
    );
    
    boolean existsByWorkDateAndWorkShiftId(LocalDate workDate, String workShiftId);
}
