import java.util.TimerTask;

public class StaminaBarTimer extends TimerTask {
    public void run() {
        test.getObject().removeStamina();
    }
}
