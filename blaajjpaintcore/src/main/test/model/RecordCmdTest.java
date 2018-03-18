package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class RecordCmdTest {
    int testNo = 0;

    /** Le logger qu de cette classe */
    private static final Logger LOG = Logger.getLogger(RecordCmd.class.getName());

    @BeforeEach
    void setUp() {
        testNo++;
        LOG.log(Level.INFO, "Starting test number " + testNo);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
    }

    @Test
    void undo() {
    }

    @Test
    void redo() {
    }

    @Test
    void saveCmd() {
    }
}