package pl.edu.ur.pnes.petriNet.simulator;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.MainApp;
import pl.edu.ur.pnes.editor.Mode;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.petriNet.Transition;

import java.util.Optional;
import java.util.function.DoubleConsumer;

public class SimulatorFacade {

    private final Session session;
    private NetSnapshot lastSnapshot = null;

    public enum AutoStepState {
        STOPPED,
        PAUSED,
        DONE,
        RUNNING
    }

    private final Simulator simulator;
    private final Logger logger = LogManager.getLogger(SimulatorFacade.class);
    private Thread autoStepThread = null;

//    private boolean stopThread = false;


    private final DoubleProperty autoStepWaitDuration = new SimpleDoubleProperty(1000);
    private final double waitTime = Math.round(1000 / 30.0);
    public final DoubleBinding partCount = autoStepWaitDuration.divide(waitTime);
    private int stepCooldownCounter = (int) Math.floor(partCount.get());
    private final DoubleBinding autoStepDurationPartTime = autoStepWaitDuration.divide(partCount);
    private DoubleConsumer progressCallback = __ -> {
    };
    private final ObjectProperty<AutoStepState> autoStepState = new SimpleObjectProperty<>(AutoStepState.STOPPED);

    public AutoStepState getAutoStepState() {
        return autoStepState.getValue();
    }

    public ReadOnlyObjectProperty<AutoStepState> autoStepProperty() {
        return autoStepState;
    }

    private void setAutoStepState(AutoStepState autoStepState) {
        this.autoStepState.set(autoStepState);
    }

    public double getAutoStepWaitDuration() {
        return autoStepWaitDuration.get();
    }

    public DoubleProperty autoStepWaitDurationProperty() {
        return autoStepWaitDuration;
    }


    /*
    TODO: Add event dispatcher and turn step into events:
        Via event filters you can intercept event before occurring
        Via event handlers you can invoke actions after event occurred
     */


    SimulatorFacade(Simulator simulator, Session session) {
        this.simulator = simulator;
        this.session = session;
        autoStepWaitDuration.addListener((observable, oldValue, newValue) -> stepCooldownCounter = (int) Math.floor(partCount.get()));
    }

    private void restoreLastSnapshotIfPossible() {
        this.simulator.restoreSnapshotIfPossible(lastSnapshot);
    }


    private void startClock() {
        if (simulator.checkIfDone()) {
            setAutoStepState(AutoStepState.DONE);
            displaySimulationDoneMessage();
            fullStepIndicator();
            return;
        }

        resetStepIndicator();

        if (getAutoStepState() == AutoStepState.RUNNING) {
            throw new IllegalStateException("Thread already running");
        }

        resumeClock();
    }

    private void displaySimulationDoneMessage() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Nie można uruchomić już żadnej tranzycji");
        a.showAndWait();
    }

    private void resumeClock() {
        if (simulator.checkIfDone()) {
            setAutoStepState(AutoStepState.DONE);

            displaySimulationDoneMessage();
            fullStepIndicator();
            return;
        }

        if (autoStepThread != null && !autoStepThread.isInterrupted())
            autoStepThread.interrupt();

        autoStepThread = new Thread(() -> {
            do {
                try {
                    Thread.sleep(((int) autoStepDurationPartTime.get()));
                } catch (InterruptedException e) {
                    break;
                }

                if (autoStepState.get() == AutoStepState.DONE) {
                    fullStepIndicator();
                    stopClock();
                    return;
                }

                stepCooldownCounter--;
                updateStepIndicator();

                if (stepCooldownCounter == 0) {
                    try {
                        simulator.automaticStep(true);
                    } catch (TransitionCannotBeActivatedException e) {
                        setAutoStepState(AutoStepState.DONE);
                        fullStepIndicator();
                        stopClock();
                        return;
                    }
                    stepCooldownCounter = (int) partCount.get();
                }

            } while (autoStepWaitDuration.get() != 0);
        });

        autoStepThread.start();
        setAutoStepState(AutoStepState.RUNNING);
    }

    private void resetStepIndicator() {
        stepCooldownCounter = (int) partCount.get();
        progressCallback.accept(0);
    }

    private void fullStepIndicator() {
        progressCallback.accept(1);
    }

    private void updateStepIndicator() {
        progressCallback.accept(1 - Math.max(stepCooldownCounter / partCount.get(), 0.001));
    }

    public void startAutoStep() {
        // STOPPED -> RUNNING
        // PAUSED -> RUNNING
        startAutoStep(autoStepWaitDuration.get());
    }

    public void startAutoStep(double duration) {
        if (autoStepState.getValue() == AutoStepState.PAUSED || autoStepState.getValue() == AutoStepState.STOPPED) {
            // Create snapshot when starting new simulation
            if (getAutoStepState() == AutoStepState.STOPPED) {
                // first step
                restoreLastSnapshotIfPossible();
                this.lastSnapshot = this.simulator.getSnapshot();
                simulator.calculateTransitionsThatCanBeActivated();
            }

            autoStepWaitDuration.set(duration);
            startClock();
        }
    }


    public void stopAutoStep() {
        if (getAutoStepState() == AutoStepState.STOPPED) {
            if (Optional.ofNullable(lastSnapshot).map(NetSnapshot::isAlreadyRestored).orElse(false)) {
                // When "STOP" is clicked and the net was already restored -> exit RUN mode
                // that handles triple stop click
                MainApp.mainController.getFocusedSession().mode().set(Mode.EDIT);
                return;
            } else
                // that handles double stop click
                restoreLastSnapshotIfPossible();
        }

        resetStepIndicator();
        stopClock();
        autoStepThread = null;
        setAutoStepState(AutoStepState.STOPPED);
    }

    public void startOrPauseAutoStep() {
        switch (this.autoStepState.getValue()) {
            case RUNNING -> pauseAutoStep();
            case STOPPED, PAUSED -> startAutoStep();
            case DONE -> {
                fullStepIndicator();
                displaySimulationDoneMessage();
            }
        }
    }

    public void setProgressCallback(DoubleConsumer progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void pauseAutoStep() {
        if (getAutoStepState() == AutoStepState.DONE)
            return;

        stopClock();
        setAutoStepState(AutoStepState.PAUSED);
    }

    private void stopClock() {
        if (autoStepThread != null)
            autoStepThread.interrupt();
        setAutoStepState(AutoStepState.PAUSED);
    }

    public void singleAutomaticStep() {
        if (autoStepState.getValue() == AutoStepState.STOPPED) {
            restoreLastSnapshotIfPossible();
            this.lastSnapshot = simulator.getSnapshot();
        }
        if (autoStepState.getValue() == AutoStepState.PAUSED || autoStepState.getValue() == AutoStepState.STOPPED) {
            if (simulator.checkIfDone()) {
                setAutoStepState(AutoStepState.DONE);
                displaySimulationDoneMessage();
                fullStepIndicator();
                return;
            }

            if (autoStepState.getValue() == AutoStepState.STOPPED) {
                // first step, calculate possible transitions and exit
                simulator.calculateTransitionsThatCanBeActivated();
                setAutoStepState(AutoStepState.PAUSED);
                return;
            }

            simulator.automaticStep();
        }
    }

    public void singleManualStep(Transition transitionToBeActivated) {

        // If it is not paused nor stopped, then do nothing
        if (!getAutoStepState().equals(AutoStepState.PAUSED) && !getAutoStepState().equals(AutoStepState.STOPPED))
            return;

        logger.info("manual step fired on transition {}", transitionToBeActivated.getName());
        try {
            simulator.fireTransition(transitionToBeActivated);
            setAutoStepState(AutoStepState.PAUSED);
        } catch (TransitionCannotBeActivatedException e) {
            logger.error(e.getMessage());
        }
        simulator.calculateTransitionsThatCanBeActivated();
    }

}
