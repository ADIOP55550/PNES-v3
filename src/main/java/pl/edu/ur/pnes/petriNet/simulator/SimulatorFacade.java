package pl.edu.ur.pnes.petriNet.simulator;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.petriNet.Transition;


import java.util.function.DoubleConsumer;

public class SimulatorFacade {

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
    private final DoubleBinding autoStepDurationPartTime = autoStepWaitDuration.divide(partCount.get());
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


    SimulatorFacade(Simulator simulator) {
        this.simulator = simulator;
        autoStepWaitDuration.addListener((observable, oldValue, newValue) -> {
            stepCooldownCounter = (int) Math.floor(partCount.get());
        });
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
        showDoneMessage();
    }

    private void showDoneMessage() {
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

                if (simulator.checkIfDone()) {
                    setAutoStepState(AutoStepState.DONE);
                    fullStepIndicator();
                    stopClock();
                    return;
                }

                stepCooldownCounter--;
                updateStepIndicator();

                if (stepCooldownCounter == 0) {
                    simulator.automaticStep();
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
                restoreLastSnapshotIfPossible();
                this.lastSnapshot = this.simulator.getSnapshot();
            }

            autoStepWaitDuration.set(duration);
            startClock();
        }
    }


    public void stopAutoStep() {
        if (getAutoStepState() == AutoStepState.STOPPED)
            restoreLastSnapshotIfPossible();

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
                showDoneMessage();
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
            simulator.automaticStep();
        }
    }

    public void singleManualStep(Transition transitionToBeActivated) {
        logger.info("manual step fired on transition {}", transitionToBeActivated.getName());
        try {
            simulator.fireTransition(transitionToBeActivated);
        } catch (TransitionCannotBeActivatedException e) {
            e.printStackTrace();
        }
        simulator.calculateTransitionsThatCanBeActivated();
    }

}
