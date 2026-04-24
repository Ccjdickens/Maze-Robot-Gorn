package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

/**
 * A specific implementation of a robot controller that# navigates a maze,
 * identifies objects, and performs actions based on the object's color.
 *
 */
public class MazeRobot extends RobotController {

    /**
     * Constructs a new MazeRobot.
     */
    public MazeRobot(String robotName) {
        super(robotName);
    }


    @Override
    public void run() {

        while (true) {

            double forward = mbot.readUltrasonic();

            // If forward is clear → go forward
            if (forward > 20) {
                mbot.forward(40);
                pause(200);
                continue;
            }

            // Forward blocked → scan left
            mbot.stop();
            pause(150);

            mbot.turnLeft(90);
            pause(300);
            double left = mbot.readUltrasonic();
            mbot.turnRight(90);
            pause(300);

            // Scan right
            mbot.turnRight(90);
            pause(300);
            double right = mbot.readUltrasonic();
            mbot.turnLeft(90);
            pause(300);

            // Choose direction
            if (left > 20) {
                mbot.turnLeft(90);
                pause(300);
                mbot.forward(40);
                pause(300);
            } else if (right > 20) {
                mbot.turnRight(90);
                pause(300);
                mbot.forward(40);
                pause(300);
            } else {
                // Dead end → turn around
                mbot.turnLeft(180);
                pause(400);
                mbot.forward(40);
                pause(300);
            }
        }
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    /**
     * The main entry point for the MazeRobot application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try (MazeRobot amazin = new MazeRobot("Wisp")) {
            amazin.run();
        }
    }
}
