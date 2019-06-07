import java.util.TimerTask;

public class ClockTimer extends TimerTask {
    public void run() {
        test.getObject().addTime();
        test.getObject().setScore();
    }
}
