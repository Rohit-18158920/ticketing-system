package com.coding.ticketingsystem.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.coding.ticketingsystem.model.Agent;
import com.coding.ticketingsystem.model.Ticket;
import com.coding.ticketingsystem.model.TicketResponse;
import com.coding.ticketingsystem.repository.AgentRepository;
import com.coding.ticketingsystem.repository.TicketRepository;
import com.coding.ticketingsystem.repository.TicketResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
//import com.sendgrid.helpers.mail.Mail;
//import com.sendgrid.helpers.mail.objects.Content;
//import com.sendgrid.helpers.mail.objects.Email;
@Service
public class TicketService {
	@Autowired
	private TicketRepository ticketRepository;
	
	@Value("${custom.api_key}")
	private String apiKey;

	@Autowired
	private AgentRepository agentRepository;
	
	@Autowired
	private TicketResponseRepository ticketResponseRepository;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public List<Ticket> list() throws Exception {
		return ticketRepository.findAll();
	}

	public String addTicket(Map<String, Object> reqBody) throws ParseException {
		// TODO Auto-generated method stub
		Integer temp = 6;
		if(((reqBody.get("created_by_user")).getClass())!=temp.getClass()) {
			System.out.println((reqBody.get("created_by_user")).getClass());
			return "Created by user should be Numeric";
		}
		String currentDate = dateFormat.format(new Date());
		reqBody.put("created_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		reqBody.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		ObjectMapper mapper = new ObjectMapper();
		Ticket newTicket = mapper.convertValue(reqBody, Ticket.class);
		newTicket.setStatus("open");
//		String status = newTicket.getStatus();
//		if (!(status.equals("open") || status.equals("waiting on customer") || status.equals("customer responded")
//				|| status.equals("resolved") || status.equals("closed")))
//			return "Invalid Status";
		
		ticketRepository.save(newTicket);
		return "Success";
	}

	public List<Object> getTicketsFiltered(String filterName, String filterValue) {
		// TODO Auto-generated method stub
		if (filterName.toLowerCase().equals("assigned_agent"))
			return ticketRepository.findAllTicketByAssignedAgent(filterValue);
		else if (filterName.toLowerCase().equals("customer"))
			return ticketRepository.findAllTicketByCustomer(filterValue);
		else if (filterName.toLowerCase().equals("status"))
			return ticketRepository.findAllTicketByStatus(filterValue);
		return new ArrayList<Object>();
	}

	public List<Object> getTicketList() throws Exception {
		return ticketRepository.findAllTicket();
	}

	public Ticket getTicketDetails(long id) {
		// TODO Auto-generated method stub
		Optional<Ticket> optTicket = ticketRepository.findById(id);
		if(optTicket.isPresent()) {
			Ticket ticket = optTicket.get();
			List<TicketResponse> respList = ticketResponseRepository.getResponseList(id);
			ticket.setResponseList(respList);
			return ticket;
		}
			return null; 
	}

	public String updateTicketDetails(Map<String, Object> reqBody) throws Exception {
		if (!reqBody.containsKey("id"))
			return "Ticket Id not present";
		Integer temp =6;
		if(((reqBody.get("id")).getClass())!=temp.getClass() ||(reqBody.containsKey("created_by_user")&& ((reqBody.get("created_by_user")).getClass())!=temp.getClass())) {
			return "Id should be Numeric and created by user should be Numeric if present ";
		}
		int i = (int) reqBody.get("id");
		long id= i;
		Optional<Ticket> opt = ticketRepository.findById(id);
		if (!opt.isPresent())
			return "No such ticket exists";
		Ticket oldTicket = opt.get();
		if (!reqBody.containsKey("type"))
			reqBody.put("type", oldTicket.getType());
		if (!reqBody.containsKey("description"))
			reqBody.put("description", oldTicket.getDescription());
		if (!reqBody.containsKey("title"))
			reqBody.put("title", oldTicket.getTitle());
		if (!reqBody.containsKey("customer"))
			reqBody.put("customer", oldTicket.getCustomer());
		if (!reqBody.containsKey("priority"))
			reqBody.put("priority", oldTicket.getPriority());
		if (!reqBody.containsKey("created_by_user"))
			reqBody.put("created_by_user", oldTicket.getCreated_by_user());
		if (!reqBody.containsKey("assigned_to_user"))
			reqBody.put("assigned_to_user", oldTicket.getAssigned_to_user());
		String currentDate = dateFormat.format(new Date());
		reqBody.put("created_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		reqBody.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		ObjectMapper mapper = new ObjectMapper();
		Ticket newTicket = mapper.convertValue(reqBody, Ticket.class);
		if (oldTicket.equals(newTicket))
			return "No changes present";
		if (!newTicket.getType().equals(oldTicket.getType()))
			ticketRepository.updateTicketType(id, newTicket.getType(), newTicket.getUpdated_at());
		if (!newTicket.getDescription().equals(oldTicket.getDescription()))
			ticketRepository.updateTicketDescription(id, newTicket.getDescription(), newTicket.getUpdated_at());
		if (!newTicket.getTitle().equals(oldTicket.getTitle()))
			ticketRepository.updateTicketTitle(id, newTicket.getTitle(), newTicket.getUpdated_at());
		if (!newTicket.getCustomer().equals(oldTicket.getCustomer()))
			ticketRepository.updateTicketCustomer(id, newTicket.getCustomer(), newTicket.getUpdated_at());
		if (!newTicket.getPriority().equals(oldTicket.getPriority()))
			ticketRepository.updateTicketPriority(id, newTicket.getPriority(), newTicket.getUpdated_at());
		return "Success";
	}

	public String updateStatus(Map<String, Object> reqBody) throws Exception {
		// TODO Auto-generated method stub
		if (!reqBody.containsKey("id") || !reqBody.containsKey("status"))
			return "Invalid request body";
		Integer temp =6;
		if(((reqBody.get("id")).getClass())!=temp.getClass())
			return "Id should be Numeric";
		int i = (int) reqBody.get("id");
		long id= i;
		String status = reqBody.get("status").toString();
		if (!(status.equals("open") || status.equals("waiting on customer") || status.equals("customer responded")
				|| status.equals("resolved") || status.equals("closed")))
			return "Invalid Status";
		Optional<Ticket> opt = ticketRepository.findById(id);
		if (!opt.isPresent())
			return "No such ticket exists";
		Ticket oldTicket = opt.get();
		if (oldTicket.getStatus().equals(status))
			return "Same Status is present";
		if (status.equals("resolved")) {
			Optional<Agent> optAgent = agentRepository.findById(oldTicket.getAssigned_to_user());
			if (!optAgent.isPresent()) {
				return "Agent Not present";
			}
			Agent agent = optAgent.get();
			long agentId = agent.getId();
			int ticketAssigned = agent.getTicket_assigned() -1;
			ticketAssigned = ticketAssigned<0?0:ticketAssigned;
			agentRepository.updateAgentTickets(agentId, ticketAssigned);
		}
		String currentDate = dateFormat.format(new Date());
		ticketRepository.updateTicketStatus(id, status, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));

		return "Success";
	}

	public String assignAgent(Map<String, Object> reqBody) throws Exception {
		// TODO Auto-generated method stub
		if (!reqBody.containsKey("id"))
			return "Invalid request body";
		Integer temp =6;
		if(((reqBody.get("id")).getClass())!=temp.getClass())
			return "Id should be Numeric";
		int i = (int) reqBody.get("id");
		long ticketId= i;
		Optional<Ticket> opt = ticketRepository.findById(ticketId);
		if (!opt.isPresent())
			return "No such ticket exists";
		Ticket oldTicket = opt.get();
		if (oldTicket.getAssigned_to_user() != 0 && !oldTicket.getStatus().equals("resolved")) {
			Optional<Agent> optAgent = agentRepository.findById(oldTicket.getAssigned_to_user());
			if (optAgent.isPresent()) {
				return "Already assigned an Agent";
			}
		}
		List<Agent> agentList = agentRepository.findLeastUsedAgent();
		Agent agent = agentList.get(0);
		long agentId = agent.getId();
		String currentDate = dateFormat.format(new Date());
		if(oldTicket.getStatus().equals("open"))
			ticketRepository.updateTicketStatus(ticketId, "waiting on customer", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		ticketRepository.updateTicketAssignedAgent(ticketId, agentId,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		int ticketAssigned = agent.getTicket_assigned() + 1;
		agentRepository.updateAgentTickets(agentId, ticketAssigned);
		return "Success";
	}

	public List<Agent> getAgents() {
		// TODO Auto-generated method stub
		return agentRepository.findAll();
	}
	
	public String deleteTicket(Map<String, Object> reqBody) throws Exception{
		if (!reqBody.containsKey("id"))
			return "Invalid request body";
		Integer temp =6;
		if(((reqBody.get("id")).getClass())!=temp.getClass())
			return "Id should be Numeric";
		int i = (int) reqBody.get("id");
		long ticketId= i;
		
		Optional<Ticket> opt = ticketRepository.findById(ticketId);
		if (!opt.isPresent())
			return "No such ticket exists";
		Ticket oldTicket = opt.get();
		ticketRepository.deleteById(ticketId);
		if (oldTicket.getAssigned_to_user() != 0 && !oldTicket.getStatus().equals("resolved")) {
			Optional<Agent> optAgent = agentRepository.findById(oldTicket.getAssigned_to_user());
			if (optAgent.isPresent()) {
				Agent agent = optAgent.get();
				long agentId = agent.getId();
				int ticketAssigned = agent.getTicket_assigned() -1;
				ticketAssigned = ticketAssigned<0?0:ticketAssigned;
				agentRepository.updateAgentTickets(agentId, ticketAssigned);
			}
		}
		
		return "Success";
	}
	public String addResponse(Map<String, Object> reqBody) throws Exception{
		if (!reqBody.containsKey("id") || !reqBody.containsKey("addedBy"))
			return "Invalid request body";
		Integer temp =6;
		if(((reqBody.get("id")).getClass())!=temp.getClass())
			return "Id should be Numeric";
		int i = (int) reqBody.get("id");
		long ticketId= i;
		String currentDate = dateFormat.format(new Date());
		String addedBy = reqBody.get("addedBy").toString();
		Optional<Ticket> opt = ticketRepository.findById(ticketId);
		if (!opt.isPresent())
			return "No such ticket exists";
		Ticket oldTicket = opt.get();
		if (oldTicket.getAssigned_to_user() != 0 && !oldTicket.getStatus().equals("resolved")) {
			Optional<Agent> optAgent = agentRepository.findById(oldTicket.getAssigned_to_user());
			if (!optAgent.isPresent()) {
				return "Agent not yet assigned";
			}
		}
		TicketResponse ticketResponse = new TicketResponse();
		ticketResponse.setTicket_id(ticketId);
		ticketResponse.setAgent_id(oldTicket.getAssigned_to_user());
		ticketResponse.setResponse(reqBody.get("response").toString());
		ticketResponse.setAddedBy(addedBy);
		ticketResponse.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		ticketResponseRepository.save(ticketResponse);
		String status = "waiting on customer";
		if(addedBy.toLowerCase().equals("customer")) {
			status = "customer responded";
		}else {
			//added by agent send email
			sendResponse(ticketId,oldTicket.getCustomer(),ticketResponse.getResponse());
			
		}
		ticketRepository.updateTicketStatus(ticketId, status, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
		return "Success";
	}
	
	public String sendResponse(long ticketId, String customer, String resp) throws Exception{
		Email from = new Email("yogesh@sinecycle.com");
	    String subject = "Response to the Ticket : "+ticketId;
	    Email to = new Email(customer);
	    Content content = new Content("text/plain", resp);
	    Mail mail = new Mail(from, subject, to, content);
	    System.out.println("SENDGRID_API_KEY:: "+apiKey);
	    SendGrid sg = new SendGrid(apiKey);
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());
	      return response.toString();
	    } catch (IOException ex) {
	      throw ex;
	    }
	  }

}
