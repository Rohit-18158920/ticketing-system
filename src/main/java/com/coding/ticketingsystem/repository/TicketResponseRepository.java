package com.coding.ticketingsystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coding.ticketingsystem.model.TicketResponse;


@Repository
public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long>  {
	@Modifying
	@Transactional
    @Query("delete TicketResponse r where r.ticket_id = :id")
    int deleteTicketResponse(@Param(value = "id") long id);
	
	@Query("SELECT r FROM TicketResponse r where r.ticket_id = :id")
	List<TicketResponse> getResponseList(@Param(value = "id") long id);
}
	