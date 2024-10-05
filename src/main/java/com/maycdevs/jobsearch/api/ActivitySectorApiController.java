package com.maycdevs.jobsearch.api;


import com.maycdevs.jobsearch.model.ActivitySector;
import com.maycdevs.jobsearch.service.ActivitySectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/activitysector")
public class ActivitySectorApiController {

    @Autowired
    private ActivitySectorService activitySectorService;

    /**
     * Read - read an activity sector
     * @return - An object of the new saved activity sector
     */
    @GetMapping(value = {"","/"})
    public List<ActivitySector> listAllActivitySectors() {
        return activitySectorService.getAll();
    }

    /**
     * Write - Save an activity sector
     * @param sector - The activitySector object to be created
     * @return - An object of the new saved activity sector
     */
    @PostMapping(value = {"","/"})
    public ActivitySector createActivitySector(@RequestBody ActivitySector sector) throws Exception {
        return activitySectorService.create(sector);
    }

    /**
     * Read - read an activity sector
     * @param id - The id of the activitySector object to update
     * @return - An object of the new saved activity sector
     */
    @GetMapping("/{id}")
    public ActivitySector getActivitySector(@PathVariable String id) throws Exception {
        return activitySectorService.getOneById(id);
    }

    /**
     * Update - Update an existing employee
     * @param id - The id of the activitySector object to update
     * @param sector - The activitySector object updated
     * @return
     */
    @PutMapping("/{id}")
    public ActivitySector updateActivitySector(
            @PathVariable final String id,
            @RequestBody ActivitySector sector
    ) throws Exception {
        return activitySectorService.update(id, sector);
    }

    /**
     * Delete - Save an activity sector
     * @param id - The id of the activitySector object to update
     * @return - A boolean, true if action is successed
     */
    @DeleteMapping("/{id}")
    public Boolean deleteActivitySector(@PathVariable String id) throws Exception {
        return activitySectorService.delete(id);
    }
}
