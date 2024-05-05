package com.cwl.javafullstackapi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BibleStudyPlan {

    @Id
    private ObjectId _id;
    private TopicInfo topicInfo;
    private List<ReadingPlanEntry> readingPlan;

}
@Data
@AllArgsConstructor
@NoArgsConstructor
class TopicInfo {
    private String topic;
    private String daysRequested;
    private String daysFilled;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReadingPlanEntry {
    private int day;
    private String date;
    private List<String> verseIds;
}
