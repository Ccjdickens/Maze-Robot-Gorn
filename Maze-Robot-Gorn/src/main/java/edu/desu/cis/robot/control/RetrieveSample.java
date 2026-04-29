package edu.desu.cis.robot.control;

public class RetrieveSample extends RobotController {

    private boolean sampleRetrieved = false;

    public RetrieveSample(String robotName) {
        super(robotName);
    }

    @Override
    public void run() {
        while (true) {
            String color = mbot.getColorObjectFromCamera();

            if (color != null) {
                color = color.trim().toUpperCase();
            }

            if (!sampleRetrieved && "RED".equals(color)) {
                sampleRetrieved = true;

                mbot.stop();
                mbot.forward(20, 0.9); // get closer to object to push
                mbot.stop();
                mbot.sampleFound();
                mbot.pushObject();
                mbot.doDance();
                mbot.flashLed(6, 255, 0, 255, 0.15);
                mbot.playJingle();
                break;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try (RetrieveSample robot = new RetrieveSample("Gorn")) {
            robot.run();
        }
    }
}