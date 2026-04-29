package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

public class MazeRobot extends RobotController {

    public MazeRobot(String robotName) {
        super(robotName);
    }

    private boolean isTargetObject(String color) {
        return "red".equalsIgnoreCase(color);
    }

    private void handleDetectedObject(String color) {
        switch (color.toLowerCase()) {
            case "red":
                System.out.println("TARGET SAMPLE found (red cup)");
                mbot.flashLed(3, 255, 0, 0, 0.2);
                break;
            case "green":
                System.out.println("Movable object detected (green) — pushing aside.");
                mbot.flashLed(2, 0, 255, 0, 0.2);
                mbot.forward(30); // push it
                break;
            case "blue":
                System.out.println("Immovable object detected (blue) — navigating around.");
                mbot.flashLed(2, 0, 0, 255, 0.2);
                break;
            case "yellow":
                System.out.println("Insertion point found (yellow ball) — stopping.");
                mbot.flashLed(3, 255, 255, 0, 0.2);
                mbot.stop();
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

                handleDetectedObject(detectedColor);

                if (!detectedColor.equalsIgnoreCase("yellow") &&
                        !detectedColor.equalsIgnoreCase("green")) {
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

    public static void main(String[] args) {
        try (MazeRobot amazin = new MazeRobot("Gorn")) {
            amazin.run();
        }
    }
}