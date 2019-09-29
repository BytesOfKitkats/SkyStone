package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ReadWriteFile;
import java.io.File;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;
import android.util.Log;

public abstract class OoBHardwareBot {
    // CONSTANTS
    protected static final int OPMODE_SLEEP_INTERVAL_MS_SHORT  = 10;
    protected static final double SPEED_COEFF_SLOW = 0.35;
    protected static final double SPEED_COEFF_MED = 0.5;
    protected static final double SPEED_COEFF_FAST = 0.8;
    protected static final double SPEED_COEFF_TURN = 0.3;
    protected static final double SPEED_COEFF_TURN_FAST = 0.4;
    protected static final double GAME_STICK_DEAD_ZONE = 0.1;

    // Sensors
    private static final String IMU_TOP = "imu";        // IMU

    LinearOpMode opMode; // current opMode

    // Sensors
    protected BNO055IMU imu;

    private Orientation angles;

    // waitForTicks
    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime runTime  = new ElapsedTime();

    // return status
    protected enum OoBHardwareStatus
    {
        OoB_HARDWARE_FAILURE,
        OoB_HARDWARE_SUCCESS
    }

    /*
     * The initHardware() method initializes the hardware on the robot including the drive train.
     * It calls the abstract initDriveTrainMotors() and initMotorsAndSensors() methods.
     * Returns BOK_SUCCESS if the initialization is successful, BOK_FAILURE otherwise.
     */
    protected OoBHardwareStatus initHardware(LinearOpMode opMode)
    {
        this.opMode = opMode;
        // First initialize the drive train
        OoBHardwareStatus rc = initDriveTrainMotors();
        if (rc == OoBHardwareStatus.OoB_HARDWARE_SUCCESS) {
            // Next initialize the other motors and sensors
            rc = initMotorsAndSensors();
        }
        return rc;
    }

    // Initialization of drive train is protected but abstract
    protected abstract OoBHardwareStatus initDriveTrainMotors();

    private OoBHardwareStatus initMotorsAndSensors()
    {
        // Sensors
        imu = opMode.hardwareMap.get(BNO055IMU.class, IMU_TOP);
        if(imu == null){
            return OoBHardwareStatus.OoB_HARDWARE_FAILURE;
        }

        return OoBHardwareStatus.OoB_HARDWARE_SUCCESS;
    }

    protected void initializeImu()
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        //angles = new Orientation();
//        imu.initialize(parameters);
    }

    // Using the drive train is public
    protected abstract void testDTMotors();
    protected abstract void resetDTEncoders();
    protected abstract boolean areDTMotorsBusy();

    protected abstract void setPowerToDTMotors(double power);
    protected abstract void setPowerToDTMotors(double power, boolean forward);
    protected abstract void setPowerToDTMotors(double leftPower, double rightPower);
    protected abstract void setPowerToDTMotorsStrafe(double power, boolean right);
    protected abstract void setModeForDTMotors(DcMotor.RunMode runMode);
    protected abstract void setOnHeading(double leftPower, double rightPower);

    // Autonomous driving
    protected abstract int startMove(double leftPower,
                                     double rightPower,
                                     double inches,
                                     boolean backward);

    protected abstract void startEncMove(double leftPower,
                                         double rightPower,
                                         int encCount,
                                         boolean forward);


    protected abstract int startStrafe(double power, double rotations,
                                       boolean right) throws UnsupportedOperationException;

    protected abstract int startStrafeWEnc(double power, double rotations,
                                           boolean right) throws UnsupportedOperationException;

    protected abstract void stopMove();

    protected abstract double getTargetEncCount(double targetDistanceInches);
    protected abstract double getAvgEncCount();
    protected abstract int getLFEncCount();
    protected abstract int getRFEncCount();

    // Teleop driving
    protected abstract void moveRobotTele(double speedCoef, double turnCoef, boolean endGame);

    /*
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     *b The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    protected void waitForTick(long periodMs)
    {
        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            }
            catch (InterruptedException e) {
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    protected double getAngle (){

        //      angles = imu.getAngularOrientation(AxesReference.INTRINSIC,
        //AxesOrder.XYZ;
        //            AngleUnit.DEGREES);
        return angles.thirdAngle;
    }


}