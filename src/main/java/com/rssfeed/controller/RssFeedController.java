package com.rssfeed.controller;

import com.rometools.rome.io.FeedException;
import com.rssfeed.model.FeedItem;
import com.rssfeed.service.RssFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RssFeedController {

    private static final Logger logger = LoggerFactory.getLogger(RssFeedController.class);

    private RssFeedService rssFeedService;

    public RssFeedController(RssFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    @Scheduled(fixedRate = 5*60*1000)
    public void rssFeedsJob() {
        logger.info("Read and save RSS feeds job started");
        List<FeedItem> feedItems;

        try {
            feedItems = rssFeedService.readRssFeeds();
            rssFeedService.saveOrUpdateRssFeeds(feedItems);
            logger.info("End and save RSS feeds job end");
        } catch (IOException e) {
            logger.error("Can not read RSS feeds ", e);
        } catch (FeedException e) {
            logger.error("Can not read RSS feeds ", e);
        } catch (RuntimeException e) {
            logger.error("Unexpected error ", e);
        }

    }

}
