package com.demo.scheduler;

import java.util.Iterator;

import org.ehcache.Cache.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	@Scheduled(fixedRate = 100000)
	public void fixedRateSch() {
		final long start = System.currentTimeMillis();
		long lastUpdateTimestamp;
		Cache cache = cacheManager.getCache("serverDetailCache");
		javax.cache.Cache<String, ServerDetail> nativCache = (javax.cache.Cache<String, ServerDetail>) cache.getNativeCache();
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
				if(lastUpdateTimestamp==0 || lastUpdateTimestamp - start < 300000 ) {
					logger.info("{} servers not responded.More server details : {}",serverDetails.getName(),serverDetails.toString());
					template.convertAndSend("/topic/greetings", "servers not responded:"+serverDetails.toString());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally {
			//nativCache.close();
		}
	}
}
