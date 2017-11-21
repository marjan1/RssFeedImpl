package com.rssfeed.repository;

import com.rssfeed.model.FeedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface FeedRepository extends JpaRepository<FeedItem, Short> {

    FeedItem findFeedItemByPublicationDateAndUriAndTitle(Date publicationDate, String uri, String title);

}
