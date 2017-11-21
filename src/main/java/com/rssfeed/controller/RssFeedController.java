package com.rssfeed.controller;

import com.rometools.rome.io.FeedException;
import com.rssfeed.model.FeedItem;
import com.rssfeed.service.RssFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RssFeedController {

    private static final Logger logger = LoggerFactory.getLogger(RssFeedController.class);

    @Autowired
    private RssFeedService rssFeedService;

    @Scheduled(fixedRate = 300000)
    public void rssFeedsJob() {
        logger.info("Read and save RSS feeds job started");
        List<FeedItem> feedItems;

        try {
            feedItems = rssFeedService.readRssFeeds();
        } catch (IOException e) {
            logger.info("IOException in reading RSS feeds");
            logger.error("IOException in reading RSS feeds {}", e.getMessage());
            return;

        } catch (FeedException e) {
            logger.info("FeedException in reading RSS feeds");
            logger.error("FeedException in reading RSS feeds {}", e.getMessage());
            return;
        }

        rssFeedService.saveOrUpdateRssFeeds(feedItems);
        logger.info("End and save RSS feeds job started");
    }

}
