package com.rssfeed.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rssfeed.model.FeedItem;
import com.rssfeed.repository.FeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssFeedService {

    private static final Logger logger = LoggerFactory.getLogger(RssFeedService.class);
    private static final String IMAGE_EXTENSION = "jpg";

    @Value("${rss.feed.url}")
    private String url;

    @Value("${rss.feed.max.items}")
    private int maxNumberOfFeeds;

    @Autowired
    private FeedRepository feedRepository;

    public List<FeedItem> readRssFeeds() throws IOException, FeedException, IllegalArgumentException {

        logger.info("Start of reading RSS Feeds ");
        XmlReader reader = new XmlReader(new URL(url));
        SyndFeed feed = new SyndFeedInput().build(reader);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(feed);
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        logger.info("End of reading RSS Feeds");
        return populateFeedItemsList(feed);
    }

    @Transactional
    public void saveOrUpdateRssFeeds(List<FeedItem> itemList) {
        logger.info("Start of save or update feeds");
        for (FeedItem feedItem : itemList) {

            logger.info("Get feed from db with publication date = {}, title = {} and URI = {}",
                    feedItem.getPublicationDate(), feedItem.getTitle(), feedItem.getUri());

            List<FeedItem >feedItemListFromDb = feedRepository
                    .findAllByUri(feedItem.getUri());

            FeedItem feedItemFromDb = feedRepository
                    .findFeedItemByUri(feedItem.getUri());
            if (feedItemFromDb == null) {
                logger.info("New feed with URI = {} saved in DB", feedItem.getUri());
                feedRepository.save(itemList);
            } else {
                if (feedItem.getUpdateDate() != null) {
                    logger.info("Existing feed with  URI = {} updated in DB",
                            feedItem.getUri());
                    feedItem.setId(feedItemFromDb.getId());
                    feedRepository.save(feedItem);
                }
            }

        }
        logger.info("End of save or update feeds");
    }

    private List<FeedItem> populateFeedItemsList(SyndFeed feed) {
        List<FeedItem> itemList = new ArrayList<>();

        SyndEntry[] syndEntriesArray = feed.getEntries().toArray(new SyndEntry[feed.getEntries().size()]);

        int numberOfFeeds = syndEntriesArray.length < maxNumberOfFeeds ? syndEntriesArray.length : maxNumberOfFeeds;

        for (int i = 0; i < numberOfFeeds; i++) {

            FeedItem feedItem = initFeedItem(syndEntriesArray[i]);
            itemList.add(feedItem);
        }

        return itemList;
    }

    private FeedItem initFeedItem(SyndEntry feed) {
        FeedItem feedItem = new FeedItem();
        feedItem.setDescription(feed.getDescription().getValue());
        setImageFromURL(feedItem, feed);
        feedItem.setTitle(feed.getTitle());
        feedItem.setPublicationDate(feed.getPublishedDate());
        feedItem.setUpdateDate(feed.getUpdatedDate());
        feedItem.setUri(feed.getUri());
        return feedItem;
    }

    private void setImageFromURL(FeedItem feedItem, SyndEntry feed) {
        try {
            URL imageURL = new URL(feed.getEnclosures().get(0).getUrl());
            BufferedImage originalImage = ImageIO.read(imageURL);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, IMAGE_EXTENSION, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            feedItem.setLogo(imageInByte);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
