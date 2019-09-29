package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.FileReader;


public class OoBTele {
    // CONSTANTS
    //private static final double GAME_STICK_DEAD_ZONE_LEFT_STICK = 0.3;
    private static final double GAME_TRIGGER_DEAD_ZONE = 0.2;

    private OoBHardwareBot robot;
    private LinearOpMode opMode;
    private double speedCoef = robot.SPEED_COEFF_FAST;
    private double turnCoef = robot.SPEED_COEFF_TURN_FAST;

    public enum OoBTeleStatus {
        OoB_TELE_FAILURE, OoB_TELE_SUCCESS
    }
    public OoBTeleStatus initSoftware(LinearOpMode opMode, OoBHardwareBot robot) {
        this.opMode = opMode;
        this.robot = robot;
        robot.setModeForDTMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        return OoBTeleStatus.OoB_TELE_SUCCESS;
    }
while (opMode.opModeIsActive())

    {
        // GAMEPAD 1 CONTROLS:
        // Left & Right stick: Drive
        // A:                  Go in fast mode
        // Y:                  Go in slow mode

        moveRobot();

        if (opMode.gamepad1.y) {
            speedCoef = robot.SPEED_COEFF_SLOW;
        }

        if (opMode.gamepad1.a) {
            speedCoef = robot.SPEED_COEFF_FAST;
        }
    }


}
