package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteDto, Integer> {
    @Override
    List<VoteDto> findAll();

    @Query(value = "select * from vote v where v.local_date_time between ?1 and ?2 ORDER BY ?#{#pageable}", nativeQuery = true)
    List<VoteDto> findByLocalDateTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
