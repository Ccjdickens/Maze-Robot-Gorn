package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

public class MazeRobot extends RobotController {

    private enum State {
        CRUISING,
        IDENTIFY_OBJECT,
        AVOID_OBJECT,
        PUSH_OBJECT,
        COLLECT_SAMPLE,
        MISSION_COMPLETED
    }

    public MazeRobot(String robotName) {
        super(robotName);
    }

    @Override
    public void run() {
        State currentState = State.CRUISING;
        boolean missionDone = false;

        mbot.followLine();
        mbot.avoidCrashing(15);

        while (!missionDone) {
            SensorSnapshot sensor = awaitNewData();

            switch (currentState) {
                case CRUISING:
                    if (sensor.distance() < 15) {
                        currentState = State.IDENTIFY_OBJECT;
                    }
                    break;

                case IDENTIFY_OBJECT:
                    mbot.stopAllBehaviors();
                    String color = mbot.getColorObjectFromCamera();

                    if (color != null) {
                        color = color.trim().toUpperCase();
                    }

                    if ("BLUE".equals(color)) {
                        mbot.stopAllBehaviors();
                        mbot.steerAround(35, 40, 35); // let run longer then steer more
                        currentState = State.AVOID_OBJECT;
                    } else if ("GREEN".equals(color)) {
                        mbot.stopAllBehaviors();
                        currentState = State.PUSH_OBJECT;
                    } else if ("RED".equals(color)) {
                        mbot.stopAllBehaviors();
                        currentState = State.COLLECT_SAMPLE;
                    } else if ("YELLOW".equals(color)) {
                        mbot.stopAllBehaviors();
                        System.out.println("mission completed");
                        currentState = State.MISSION_COMPLETED;
                    } else {
                        mbot.followLine();
                        mbot.avoidCrashing(15);
                        currentState = State.CRUISING;
                    }
                    break;

                case AVOID_OBJECT: // adjust for maze
                    if(sensor.distance() > 40) {
                        mbot.stopBehavior("STEER_AROUND");
                        mbot.turnRight(90);
                        mbot.straight(10);
                        mbot.followLine();
                        mbot.avoidCrashing(15);
                        currentState = State.CRUISING;
                        break;
                    }

                case PUSH_OBJECT:// completed
                    mbot.forward(20, 1.5);
                    mbot.stop();
                    mbot.pushObject();
                    mbot.followLine();
                    mbot.avoidCrashing(15);
                    currentState = State.CRUISING;
                    break;

                case COLLECT_SAMPLE: // completed
                    mbot.forward(20, 1.5);
                    mbot.stop();
                    mbot.sampleFound();
                    mbot.pushObject();
                    mbot.flashLed(6, 255, 0, 255, 0.15);
                    mbot.playJingle();
                    mbot.followLine();
                    mbot.avoidCrashing(15);
                    currentState = State.CRUISING;
                    break;

                case MISSION_COMPLETED: // completed
                    mbot.missionCompleted();
                    missionDone = true;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        try (MazeRobot robot = new MazeRobot("Blue")) {
            robot.run();
        }
    }
}