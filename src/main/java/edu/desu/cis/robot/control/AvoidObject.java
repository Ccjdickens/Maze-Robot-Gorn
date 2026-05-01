package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

public class AvoidObject extends RobotController {

    public AvoidObject(String robotName) {
        super(robotName);
    }

    @Override
    public void run() {
        mbot.followLine();
        mbot.avoidCrashing(15);

        boolean done = false;

        while (!done) {
            SensorSnapshot sensor = awaitNewData();

            if (sensor.distance() > 0 && sensor.distance() < 15) {
                mbot.stopAllBehaviors();

                mbot.steerAround(35, 40, 38);

                try {
                    Thread.sleep(2200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                mbot.stopBehavior("STEER_AROUND");

                mbot.turnRight(45);
                mbot.straight(20);

                mbot.followLine();
                mbot.avoidCrashing(15);

                done = true;
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mbot.stopAllBehaviors();
    }

    public static void main(String[] args) {
        try (AvoidObject robot = new AvoidObject("Gorn")) {
            robot.run();
        }
    }
}