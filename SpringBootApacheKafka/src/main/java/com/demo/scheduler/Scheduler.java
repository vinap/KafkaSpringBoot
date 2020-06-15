package com.demo.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.ehcache.Cache.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.demo.model.ServerDetail;

@Component
public class Scheduler {
	private final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	SimpMessagingTemplate template;
	
	@Scheduled(fixedRate = 50000)
	public void fixedRateSch() {
		final long start = System.currentTimeMillis();
		
		  DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
	      Date date = new Date(start);
	      logger.info("Start Time : "+dateFormat.format(date));
		long lastUpdateTimestamp;
		Cache cache = cacheManager.getCache("serverDetailCache");
		javax.cache.Cache<String, ServerDetail> nativCache = (javax.cache.Cache<String, ServerDetail>) cache.getNativeCache();
		int count=0;
		try {
			Iterator<javax.cache.Cache.Entry<String, ServerDetail>> itr=nativCache.iterator(); 
			while(itr.hasNext()){
				javax.cache.Cache.Entry<String, ServerDetail> entry=itr.next();
				ServerDetail serverDetails=entry.getValue();
				try {
					lastUpdateTimestamp=Long.parseLong(serverDetails.getLastUpdateTimestamp());
				} catch (NumberFormatException e) {
					lastUpdateTimestamp=0;
				}
				if(lastUpdateTimestamp==0 || start-lastUpdateTimestamp >= 300000 ) {
					logger.info("ServerName:" +serverDetails.getName()+ "  Last Updated" +serverDetails.getLastUpdateTimestamp());
					//template.convertAndSend("/topic/greetings", "servers not responded:"+serverDetails.toString());
					count++;
				}
				
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			Date endDate = new Date(System.currentTimeMillis());
		    logger.info("End Time : "+dateFormat.format(endDate));
		    logger.info("Total Servers not responded: "+count);
		}
	}
}
