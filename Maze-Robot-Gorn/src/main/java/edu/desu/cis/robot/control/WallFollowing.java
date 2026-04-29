package edu.desu.cis.robot.control;


public class WallFollowing extends RobotController {

    public WallFollowing(String robotName) {
        super(robotName);
    }

    @Override
    public void run() {
        mbot.followLine();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mbot.stopBehavior("FOLLOW_LINE");
        mbot.stop();
    }

    public static void main(String[] args) {
        try (WallFollowing robot = new WallFollowing("Gorn")) {
            robot.run();
        }
    }
}