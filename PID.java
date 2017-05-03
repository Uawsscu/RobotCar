
/**
 *
 * @author tong_Uawsscu
 */
import java.util.*;

public class PID {

    double input, output, SV; //sv=Setpoint
    double error, lastErr;
    double kP, kI, kD, Bias=50;
    double P, I, D;
    double pid;

    PID(double _kP, double _kI, double _kD, double _Bias) {
        kP = _kP;
        kI = _kI;
        kD = _kD;
        Bias = _Bias;
    }


    int process(int sensor1, int sensor2) {
        //float now = millis();
        int gain=10;
        float timeChange = 1;//(double)(now - lastTime);
        ///error =setPoint-PV
        input = sensor1*(-gain) + sensor2*(gain);//////// int input = sensor1 * -gain + sensor2 * gain; ????
        error = SV - input;
        /// P
        P = error * kP;
        /// I
        I += (error * kI) * timeChange;
        /// D
        D = (error - lastErr) * kD / timeChange;
        lastErr = error;
        // lastTime = now;

        pid = P + I + D + Bias;
        System.out.format("%3d %3d %3d %3d %3d %3d", (int) input, (int) error, (int) P, (int) I, (int) D, (int) pid);
        return (int) pid;
    }

}
