package com.coding.ticketingsystem.repository;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coding.ticketingsystem.model.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
	@Query("SELECT a FROM Agent a order by a.ticket_assigned asc")
	List<Agent> findLeastUsedAgent();
	
	@Modifying
	@Transactional
    @Query("update Agent a set a.ticket_assigned = :ticket_assigned where a.id = :id")
    void updateAgentTickets(@Param(value = "id") long id, @Param(value = "ticket_assigned") int ticket_assigned);
}
