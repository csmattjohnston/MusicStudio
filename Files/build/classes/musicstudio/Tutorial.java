package musicstudio;

import java.awt.AWTException;
import java.awt.Robot;

public class Tutorial {

    public Robot addInstrument() {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(100);
            robot.mouseMove(10, 680);
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);//press 1
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);//press 2
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
            robot.mouseMove(10, 580);   //move mouse
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);//press 1
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {

            e.printStackTrace();
        }
        return null;
    }

    public Robot removeInstrument() {
        try {
            Robot remove = new Robot();
            remove.setAutoDelay(200);
            remove.mouseMove(50, 380); //move mouse
            remove.mousePress(java.awt.event.InputEvent.BUTTON1_MASK); //click
            remove.mousePress(java.awt.event.InputEvent.BUTTON3_MASK); //right click
            remove.mouseRelease(java.awt.event.InputEvent.BUTTON3_MASK); //richt click release
            remove.mousePress(java.awt.event.InputEvent.BUTTON3_MASK); //right click again to show
            remove.mouseRelease(java.awt.event.InputEvent.BUTTON3_MASK); //richt click release
            remove.mouseMove(70, 400); //move mouse
            remove.mousePress(java.awt.event.InputEvent.BUTTON1_MASK); //click
            remove.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK); // click
        } catch (AWTException e) {

            e.printStackTrace();
        }
        return null;
    }
}
