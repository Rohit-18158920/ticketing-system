package com.coding.ticketingsystem.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coding.ticketingsystem.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query("SELECT t.id FROM Ticket t WHERE t.assigned_to_user = :assignedUser and t.status!='closed'")
	List<Object> findAllTicketByAssignedAgent(@Param("assignedUser") String assignedUser);
	@Query("SELECT t.id FROM Ticket t WHERE t.customer = :customer and t.status!='closed'")
	List<Object> findAllTicketByCustomer(@Param("customer") String customer);
	@Query("SELECT t.id FROM Ticket t WHERE t.status = :status and t.status!='closed'")
	List<Object> findAllTicketByStatus(@Param("status") String status);
	@Query("SELECT t.id FROM Ticket t where t.status!='closed'")
	List<Object> findAllTicket();
	
	@Query("SELECT t FROM Ticket t where t.status = 'resolved'")
	List<Ticket> findAllTicketResolved();
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.type = :type, t.updated_at= :updated_at where t.id = :id")
    int updateTicketType(@Param(value = "id") long id, @Param(value = "type") String type,@Param(value = "updated_at")  Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.description = :description, t.updated_at= :updated_at where t.id = :id")
    void updateTicketDescription(@Param(value = "id") long id, @Param(value = "description") String description,@Param(value = "updated_at") Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.title = :title, t.updated_at= :updated_at where t.id = :id")
    void updateTicketTitle(@Param(value = "id") long id, @Param(value = "title") String title,@Param(value = "updated_at") Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.customer = :customer, t.updated_at= :updated_at where t.id = :id")
    void updateTicketCustomer(@Param(value = "id") long id, @Param(value = "customer") String customer,@Param(value = "updated_at") Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.priority = :priority, t.updated_at= :updated_at where t.id = :id")
    void updateTicketPriority(@Param(value = "id") long id, @Param(value = "priority") String priority,@Param(value = "updated_at") Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.status = :status, t.updated_at= :updated_at where t.id = :id")
    void updateTicketStatus(@Param(value = "id") long id, @Param(value = "status") String status,@Param(value = "updated_at") Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.assigned_to_user = :assignedAgent, t.updated_at= :updated_at where t.id = :id")
    void updateTicketAssignedAgent(@Param(value = "id") long id, @Param(value = "assignedAgent") long assignedAgent,@Param(value = "updated_at") Date updated_at);
	
	@Modifying
	@Transactional
    @Query("update Ticket t set t.status ='closed' where t.status = :status and t.updated_at<= :updated_at")
	int updateResolvedTickets(@Param(value = "status") String status, @Param(value = "updated_at") Date updated_at);
	
}
