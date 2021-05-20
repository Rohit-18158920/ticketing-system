package com.coding.ticketingsystem.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.coding.ticketingsystem.repository.TicketRepository;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	private TicketRepository ticketRepository;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

//	@Scheduled(fixedRate = 5000)
//	public void reportCurrentTime() {
//		log.info("The time is now {}", dateFormat.format(new Date()));
//	}
	@Scheduled(cron = "0 0 6 * * *")
	public void updateResolvedStatus() {
		try {
			Date newDate = new Date();
			newDate.setTime(newDate.getTime() + 30 * 1000 * 60 * 60 * 24);
			String status= "resolved";
			String currentDate = dateFormat.format(new Date());
			ticketRepository.updateResolvedTickets(status,  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(currentDate));
			
		}catch(Exception e) {
			log.error(e.getMessage());
		}
	}
}
