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

    @Value("${rss.feed.url}")
    private String url;

    @Value("${rss.feed.max.items}")
    private int maxNumberOfFeeds;

    private FeedRepository feedRepository;

    public RssFeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public List<FeedItem> readRssFeeds() throws IOException, FeedException, IllegalArgumentException {

        logger.info("Start of reading RSS Feeds ");
        XmlReader reader = new XmlReader(new URL(url));
        SyndFeed feed = new SyndFeedInput().build(reader);
        return convertToFeedItemsList(feed);
    }

    public void saveOrUpdateRssFeeds(List<FeedItem> itemList) {
        logger.info("Start of save or update feeds");
        for (FeedItem feedItem : itemList) {

            FeedItem feedItemFromDb = getExistingFeedItem(feedItem);

            saveOrUpdateRssFeed(itemList, feedItem, feedItemFromDb);
        }

    }

    @Transactional(readOnly = true)
    protected FeedItem getExistingFeedItem(FeedItem feedItem) {
        logger.debug("Get feed from db with publication date = {}, title = {} and URI = {}",
                feedItem.getPublicationDate(), feedItem.getTitle(), feedItem.getUri());

        return feedRepository.findFeedItemByUri(feedItem.getUri());
    }

    @Transactional
    protected void saveOrUpdateRssFeed(List<FeedItem> itemList, FeedItem feedItem, FeedItem feedItemFromDb) {
        if (feedItemFromDb == null) {
            logger.debug("New feed with URI = {} saved in DB", feedItem.getUri());
            feedRepository.save(itemList);
        } else {
            logger.debug("Existing feed with  URI = {} updated in DB", feedItem.getUri());
            updateDbFeedItem(feedItem, feedItemFromDb);
            feedRepository.save(feedItemFromDb);
        }
    }

    private void updateDbFeedItem(FeedItem feedItem, FeedItem feedItemFromDb) {
        feedItemFromDb.setUpdateDate(feedItem.getUpdateDate());
        feedItemFromDb.setDescription(feedItem.getDescription());
        feedItemFromDb.setLogo(feedItem.getLogo());
        feedItemFromDb.setPublicationDate(feedItem.getPublicationDate());
        feedItemFromDb.setTitle(feedItem.getTitle());
        feedItemFromDb.setUri(feedItem.getUri());
    }

    private List<FeedItem> convertToFeedItemsList(SyndFeed feed) {

        List<FeedItem> itemList = new ArrayList<>();

        SyndEntry[] syndEntriesArray = feed.getEntries().toArray(new SyndEntry[feed.getEntries().size()]);

        int numberOfFeeds = Math.min(syndEntriesArray.length, maxNumberOfFeeds);

        for (int i = 0; i < numberOfFeeds; i++) {
            FeedItem feedItem = convertToFeedItem(syndEntriesArray[i]);
            itemList.add(feedItem);
        }

        return itemList;
    }

    private FeedItem convertToFeedItem(SyndEntry feed) {
        FeedItem feedItem = new FeedItem();
        feedItem.setDescription(feed.getDescription().getValue());
        feedItem.setLogo(getImageFromURL(feed));
        feedItem.setTitle(feed.getTitle());
        feedItem.setPublicationDate(feed.getPublishedDate());
        feedItem.setUpdateDate(feed.getUpdatedDate());
        feedItem.setUri(feed.getUri());
        return feedItem;
    }

    private byte[] getImageFromURL(SyndEntry feed) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            URL imageURL = new URL(feed.getEnclosures().get(0).getUrl());
            BufferedImage originalImage = ImageIO.read(imageURL);
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("Could not read image ", e);
        }
        return null;
    }

}
