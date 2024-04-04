package com.cwl.javafullstackapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BibleStudyService {

    //initialize the class
    @Autowired
    private VersesRepository versesRepository;
    public List<BibleStudyPlan> allPlans(){
    return versesRepository.findAll(); //mongodb method
    }

}
