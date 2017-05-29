package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.entities.MenteeMentorGoal;
import com.livingprogress.mentorme.entities.MenteeMentorGoalSearchCriteria;
import com.livingprogress.mentorme.entities.MenteeMentorProgram;
import com.livingprogress.mentorme.entities.Paging;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.*;
import com.livingprogress.mentorme.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Date;



/**
 * The mentee  mentor goal REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/menteeMentorGoals")
@NoArgsConstructor
public class MenteeMentorGoalController {
    /**
     * The mentee mentor goal service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private MenteeMentorGoalService menteeMentorGoalService;

    /**
     * The mentee mentor program service used to create mentee mentor program
     */
    @Autowired
    private MenteeMentorProgramService menteeMentorProgramService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(menteeMentorGoalService, "menteeMentorGoalService");
    }


    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public MenteeMentorGoal get(@PathVariable long id) throws MentorMeException {
        return menteeMentorGoalService.get(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MenteeMentorGoal create(@RequestBody MenteeMentorGoal entity) throws MentorMeException {
        return menteeMentorGoalService.create(entity);
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public MenteeMentorGoal update(@PathVariable long id, @RequestBody MenteeMentorGoal entity) throws
            MentorMeException {

        MenteeMentorGoal updated = menteeMentorGoalService.update(id, entity);;
       
        MenteeMentorProgram menteeMentorProgram = updated.getMenteeMentorProgram();
        List<MenteeMentorGoal> goals = menteeMentorProgram.getGoals();
        boolean programCompleted = true; 
        for (MenteeMentorGoal goal : goals) {
            if(!goal.isCompleted())
            {
                programCompleted = false;
                break;
            }
        }            
        menteeMentorProgram.setCompleted(programCompleted);
        menteeMentorProgram.setCompletedOn(programCompleted ? new Date() : null);                
        menteeMentorProgramService.update(menteeMentorProgram.getId(), menteeMentorProgram);
           
        
        return updated;
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) throws MentorMeException {
        menteeMentorGoalService.delete(id);
    }

    /**
     * This method is used to search for entities by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<MenteeMentorGoal> search(@ModelAttribute MenteeMentorGoalSearchCriteria criteria,
            @ModelAttribute Paging paging) throws MentorMeException {
        return menteeMentorGoalService.search(criteria, paging);
    }
}