package com.rssfeed.service;

import com.rssfeed.model.FeedItem;
import com.rssfeed.repository.FeedRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RssFeedServiceTest {

    @Autowired
    private RssFeedService rssFeedService;

    @Autowired
    private FeedRepository feedRepository;

    @Test
    public void readRssFeeds() throws Exception {
        List<FeedItem> feedItems = rssFeedService.readRssFeeds();
        assertFalse(feedItems.isEmpty());

    }

    @Test
    public void saveOrUpdateRssFeeds() throws Exception {
        feedRepository.deleteAll();
        List<FeedItem> feedItems = rssFeedService.readRssFeeds();
        assertFalse(feedItems.isEmpty());
        rssFeedService.saveOrUpdateRssFeeds(feedItems);

        List<FeedItem> feedItemsfromDB = feedRepository.findAll();
        assertEquals(feedItems.size(),feedItemsfromDB.size());
        for(int i=0;i<feedItems.size();i++){
            assertEquals(feedItemsfromDB.get(i).getUri(), feedItems.get(i).getUri());
            assertEquals(feedItemsfromDB.get(i).getTitle(), feedItems.get(i).getTitle());
            assertEquals(feedItemsfromDB.get(i).getDescription(), feedItems.get(i).getDescription());
            assertEquals(feedItemsfromDB.get(i).getUpdateDate(), feedItems.get(i).getUpdateDate());

        }

    }


}