package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="BOK TELEOP", group="BoKTele")
public class OoBTeleOpMode extends LinearOpMode{
    @Override
    public void runOpMode()  throws InterruptedException
    {
        boolean status = false;
        OoBHardwareBot robot = new OoBMecanumDT();
        OoBTele opMode = null;

        /*
         * Initialize the drive train and the robot subsystems variables.
         * The initHardware() method of the hardware class does all the work here
         */
        if (OoBHardwareBot.OoBHardwareStatus.OoB_HARDWARE_FAILURE == robot.initHardware(this)) {
            telemetry.addData("Status", "Hardware NOT initialized");
            telemetry.update();
        }
        else {
            // Send telemetry message to update hardware status
            telemetry.addData("Status", "Hardware initialized");
            telemetry.update();

            opMode = new OoBTele();
            if (OoBTele.OoBTeleStatus.OoB_TELE_FAILURE ==
                    opMode.initSoftware(this, robot)) {
                telemetry.addData("Status", "Software NOT initialized");
                telemetry.update();
            } else {
                status = true; // Hardware and software initialized!
                // Send telemetry message to update status
                telemetry.addData("Status", "Initialization complete. READY!");
                telemetry.update();
            }
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Run the teleop operation, if hardware and software is initialized
        // else do nothing
        if (status && opModeIsActive()) {

        }
    }
}
