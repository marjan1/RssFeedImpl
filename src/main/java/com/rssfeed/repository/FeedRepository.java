package com.rssfeed.repository;

import com.rssfeed.model.FeedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface FeedRepository extends JpaRepository<FeedItem, Short> {

    FeedItem findFeedItemByPublicationDateAndUri(Date publicationDate, String uri);

}
