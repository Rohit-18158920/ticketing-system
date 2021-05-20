package com.coding.ticketingsystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.coding.ticketingsystem.model.Agent;
import com.coding.ticketingsystem.model.Ticket;
import com.coding.ticketingsystem.service.ScheduledTasks;
import com.coding.ticketingsystem.service.TicketService;

@RestController
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@GetMapping(value ="/getTickets", produces = "application/json")
    public @ResponseBody List<Object> getTickets() {
        try {
			return ticketService.getTicketList();
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ArrayList<Object>();
			
		}
    }
	@PostMapping(value="/addTicket", produces= "application/json")
	public @ResponseBody String addTicket(@RequestBody Map<String, Object> reqBody) {
		try {
		return ticketService.addTicket(reqBody);
		}catch(Exception e) {
			log.error(e.getMessage());
			return "Failed to add Ticket";
		}
	}
	@GetMapping(value ="/getFilteredListTicket/{filterName}/{filterValue}", produces = "application/json")
    public @ResponseBody List<Object> getTicketsFiltered(@PathVariable String filterName, @PathVariable String filterValue) {
        return ticketService.getTicketsFiltered(filterName, filterValue);
    }
	@GetMapping(value ="/getTicketDetails/{id}", produces = "application/json")
	public @ResponseBody Ticket getTicketDetails(@PathVariable int id) {
				return ticketService.getTicketDetails(id);
	}
	@PostMapping(value="/updateTicket", produces= "application/json")
	public @ResponseBody String updateTicket(@RequestBody Map<String, Object> reqBody) {
		try {
		return ticketService.updateTicketDetails(reqBody);
		}catch(Exception e) {
			log.error(e.getMessage());
			System.out.println(e);
			return "Failed to update Ticket";
		}
	}
	@PostMapping(value="/updateStatus", produces= "application/json")
	public @ResponseBody String updateStatus(@RequestBody Map<String, Object> reqBody) {
		try {
		return ticketService.updateStatus(reqBody);
		}catch(Exception e) {
			log.error(e.getMessage());
			return "Failed to update status";
		}
	}
	@GetMapping(value ="/getAgents", produces = "application/json")
	public @ResponseBody List<Agent> getAgents() {
				return ticketService.getAgents();
	}
	@PostMapping(value="/assignAgent", produces= "application/json")
	public @ResponseBody String assignAgent(@RequestBody Map<String, Object> reqBody) {
		try {
		return ticketService.assignAgent(reqBody);
		}catch(Exception e) {
			log.error(e.getMessage());
			return "Failed to assign agent";
		}
	}
	@PostMapping(value="/addResponse", produces= "application/json")
	public @ResponseBody String addResponse(@RequestBody Map<String, Object> reqBody) {
		try {
		return ticketService.addResponse(reqBody);
		}catch(Exception e) {
			log.error(e.getMessage());
			return "Failed to add Response";
		}
	}
	@PostMapping(value="/deleteTicket", produces= "application/json")
	public @ResponseBody String deleteTicket(@RequestBody Map<String, Object> reqBody) {
		try {
		return ticketService.deleteTicket(reqBody);
		}catch(Exception e) {
			log.error(e.getMessage());
			return "Failed to delete Ticket";
		}
	}
	
}
