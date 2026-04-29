package edu.desu.cis.robot.control;

public class Exit extends RobotController {

    public Exit(String robotName) {
        super(robotName);
    }

    @Override
    public void run() {
        while (true) {
            String color = mbot.getColorObjectFromCamera();

            if (color.equals("YELLOW")) {
                mbot.stop();
                mbot.stopAllBehaviors();
                mbot.missionCompleted();
                break;
            }

            try {
                Thread.sleep(100);
            } catch (Exception e) {}
        }
    }

    public static void main(String[] args) {
        try (Exit robot = new Exit("Gorn")) {
            robot.run();
        }
    }
}