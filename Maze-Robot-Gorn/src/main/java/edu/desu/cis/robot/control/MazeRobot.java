package edu.desu.cis.robot.control;

import edu.desu.cis.robot.service.SensorSnapshot;

/**
 * A specific implementation of a robot controller that navigates a maze,
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
        // need to implement.

        while (true) {

            double distance = mbot.readUltrasonic();

            // First check: the obstacle in front
            if (distance > 0 && distance < 15) {
                mbot.stop();

                // Turn right first
                mbot.turnRight(90);

                // Re-check distance after turning right
                double afterTurn = mbot.readUltrasonic();

                // If still blocked, turn left instead
                if (afterTurn > 0 && afterTurn < 15) {
                    mbot.turnLeft(180);  // 180 = undo right turn + turn left
                }

                continue;
            }

            // Otherwise move forward
            mbot.forward(30);

            try { Thread.sleep(100); } catch (Exception e) {}
        }
    }


    /**
     * The main entry point for the MazeRobot application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try (MazeRobot amazin = new MazeRobot("DickensCupid")) {
            amazin.run();
        }
    }
}
