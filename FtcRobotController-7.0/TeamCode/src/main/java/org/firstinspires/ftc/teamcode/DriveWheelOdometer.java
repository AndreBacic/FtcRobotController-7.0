/*
Copyright 2019 FIRST Tech Challenge Team 14163
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.ArrayList;

public class DriveWheelOdometer {
    public double angle;
    public double angle_adjust;
    public double x;
    public double y;
    public final double CLICKS_PER_INCH = 560 / (3.5433 * Math.PI);
    /**
     * Distance from the left center wheel to the center of the robot.
     */
    public final double LEFT_DIS_FROM_CENTER = 7;
    /**
     * Distance from the right center wheel to the center of the robot.
     */
    public final double RIGHT_DIS_FROM_CENTER = 7;

    public DriveWheelOdometer(double angle, double x, double y) {
        this.angle = angle;
        this.x = x;
        this.y = y;
    }
    public BNO055IMU imu;
    public DcMotor left_encoder;
    public DcMotor right_encoder;

    public double last_angle;
    public double last_x;
    public double last_y;

    public double last_x_clicks = 0;
    public double last_y_clicks = 0;

    public void init(BNO055IMU imu, DcMotor left_encoder, DcMotor right_encoder) {
        this.imu = imu;
        this.left_encoder = left_encoder;
        this.right_encoder = right_encoder;
        this.angle_adjust = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - this.angle;
        this.left_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.right_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.left_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.right_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Calculates the robot's movement since this method was last called,
     * updating the current angle, x and y coordinates relative to where the robot started.
     * x and y are in inches.
     * @return The current angle, x and y coordinates relative to where the robot started.
     */
    public ArrayList<Double> getCurrentCoordinates() {
        // TODO: Implement drive wheel odometry algorithm
        ArrayList<Double> list = new ArrayList<Double>();
        list.add(this.angle);
        list.add(this.x);
        list.add(this.y);
        return list;
    }

    /**
     * Tracks position while delaying (sleeping) for ms milliseconds.
     * @param ms The time in milliseconds to delay
     */
    public void odSleep(int ms) {
        double start = (int)System.currentTimeMillis();
        ArrayList<Double> list = new ArrayList<Double>();
        while (start+ms > (int)System.currentTimeMillis()) {
            this.getCurrentCoordinates();
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
