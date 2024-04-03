package com.cwl.javafullstackapi;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//setup  to reference db document file
@Document(collection = "verses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class verses {

    @Id
    private ObjectId _id;
    private TopicInfo topicInfo;
    private List<ReadingPlanEntry> readingPlan;

}

class TopicInfo {
    private String topic;
    private String daysRequested;
    private String daysFilled;
}

class ReadingPlanEntry {
    private int day;
    private String date;
    private List<String> verseIds;
}
