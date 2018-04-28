package model;

import controller.history.ICmd;
import controller.history.RecordCmd;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utils.UndoException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class RecordCmdTest {
    int testNo = 0;


    class TestCmd implements ICmd {

        public boolean done = false;

        @Override
        public void execute() {
            done = true;
            RecordCmd.getInstance().saveCmd(this);
        }

        @Override
        public void undo() throws UndoException {
            done = false;
        }

        @Override
        public void redo() throws UndoException {
            done = true;
        }
    }

    /** Le logger qu de cette classe */
    private static final Logger LOG = Logger.getLogger(RecordCmd.class.getName());

    @BeforeEach
    public void setUp() {
        testNo++;
        LOG.log(Level.INFO, "Starting test number " + testNo);
    }

    @AfterEach
    public void tearDown() {
        LOG.log(Level.INFO, "End test number " + testNo);
    }

    @Test
    public void getInstanceIsNotNull() {

        // test que getInstance() crée l'objet
        RecordCmd recordCmd = RecordCmd.getInstance();

        assertNotNull(recordCmd);
    }

    @Test
    public void getInstanceIsAlwaysSame() {

        // test que getInstance retournes toujours la même référence
        RecordCmd recordCmd1 = RecordCmd.getInstance();
        RecordCmd recordCmd2 = RecordCmd.getInstance();

        assertSame(recordCmd1,recordCmd2);
    }

    @Test
    public void saveCmdUndoRedoWorks() {

        // création d'une commande de test
        TestCmd cmd = new TestCmd();

        // exécution de la commande et sauvegarde dans le recordCmd
        cmd.execute();

        // test que la cmd est bien exécutée
        assertTrue(cmd.done);

        // application du undo
        RecordCmd.getInstance().undo();

        // test que le undo a fonctionné
        assertFalse(cmd.done);

        RecordCmd.getInstance().redo();

        // test que le redo a bien fonctionné
        assertTrue(cmd.done);
    }
}