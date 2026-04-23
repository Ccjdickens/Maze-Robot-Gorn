package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

public class PushBot extends RobotController {
    public PushBot(String robotName) {
        super(robotName);
    }

    @Override
    public void run() {
        mbot.avoidCrashing(15);
        mbot.forward(30);
        while (true) {
            SensorSnapshot sensor = awaitNewData();
            if (sensor.distance() <= 15) {   // fix typo distanc()
                mbot.stopBehavior("AVOID_CRASHING");
                mbot.pushObject();
                break;
            }
        }
    }

    public static void main(String[] args) {
        try (PushBot robot = new PushBot("Gorn")) {  // not ObstacleAvoider
            robot.run();
        }
    }
}