package com.coding.ticketingsystem.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TicketResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;
	public long ticket_id;
	public long agent_id;
	public String response;
	public String addedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date created_at;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(long ticket_id) {
		this.ticket_id = ticket_id;
	}
	public long getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(long agent_id) {
		this.agent_id = agent_id;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	public java.util.Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(java.util.Date created_at) {
		this.created_at = created_at;
	}
	
	
}
