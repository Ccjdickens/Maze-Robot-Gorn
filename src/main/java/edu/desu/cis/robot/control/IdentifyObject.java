package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

public class IdentifyObject extends RobotController {

    public IdentifyObject(String robotName) {
        super(robotName);
    }

    private boolean isTargetObject(String color) {
        return "RED".equals(color.trim().toUpperCase());
    }
    private void handleDetectedObject(String color) {
        switch (color.trim().toUpperCase()) {
            case "RED":
                System.out.println("TARGET SAMPLE found (red cup) — retrieving.");
                mbot.stop();
                mbot.forward(20, 0.9);
                mbot.stop();
                mbot.sampleFound();
                mbot.pushObject();
                mbot.doDance();
                mbot.flashLed(6, 255, 0, 255, 0.15);
                break;
            case "GREEN":
                System.out.println("Movable object detected (green) — pushing aside.");
                mbot.stopBehavior("AVOID_CRASHING");
                mbot.pushObject();
                break;
            case "YELLOW":
                System.out.println("Exit found (yellow) — mission complete.");
                mbot.stop();
                mbot.stopAllBehaviors();
                mbot.missionCompleted();
                break;
            case "BLUE":
                System.out.println("Immovable object detected (blue) — navigating around.");
                mbot.flashLed(2, 0, 0, 255, 0.2);
                break;
            default:
                System.out.println("Non-target object: " + color + " — ignoring.");
                break;
        }
    }

    @Override
    public void run() {
        mbot.avoidCrashing(15);
        mbot.forward(30);

        while (true) {
            SensorSnapshot sensors = awaitNewData();
            double distance = sensors.distance();

            if (distance > 0 && distance < 15) {
                mbot.stop();

                String detectedColor = mbot.getColorObjectFromCamera();

                if (detectedColor != null && !detectedColor.isEmpty()) {
                    handleDetectedObject(detectedColor);

                    String colorUpper = detectedColor.trim().toUpperCase();
                    if (!colorUpper.equals("YELLOW") && !colorUpper.equals("RED")) {
                        mbot.avoidCrashing(15);
                        mbot.turnRight(90);
                        SensorSnapshot afterTurn = awaitNewData();
                        if (afterTurn.distance() > 0 && afterTurn.distance() < 15) {
                            mbot.turnLeft(180);
                        }
                        mbot.forward(30);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try (MazeRobot amazin = new MazeRobot("Gorn")) {
            amazin.run();
        }
    }
}