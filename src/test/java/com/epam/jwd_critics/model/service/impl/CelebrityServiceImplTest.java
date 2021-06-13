package com.epam.jwd_critics.model.service.impl;

import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.CelebrityServiceCode;
import com.epam.jwd_critics.model.entity.Celebrity;
import com.epam.jwd_critics.model.service.CelebrityService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CelebrityServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CelebrityServiceImplTest.class);
    private static final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();
    private static Celebrity celebrity = Celebrity.newBuilder()
            .setFirstName("testFirstName")
            .setLastName("testLastName")
            .setJobs(Collections.emptyMap())
            .build();

    @BeforeAll
    public static void initializeTest() {
        try {
            celebrity = celebrityService.create(celebrity);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @AfterAll
    public static void endTest() {
        try {
            celebrityService.delete(celebrity.getId());
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testFindEntityById() {
        try {
            Celebrity actualResult = celebrityService.getEntityById(celebrity.getId()).get();
            assertEquals(celebrity, actualResult);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            String oldFirstName = celebrity.getFirstName();
            celebrity.setFirstName("new name");
            celebrityService.update(celebrity);
            Celebrity actualResult = celebrityService.getEntityById(celebrity.getId())
                    .orElseThrow(() -> new ServiceException(CelebrityServiceCode.CELEBRITY_DOES_NOT_EXIST));
            assertEquals(celebrity, actualResult);
            celebrity.setFirstName(oldFirstName);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }
}