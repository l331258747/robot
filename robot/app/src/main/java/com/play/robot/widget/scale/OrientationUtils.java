package com.play.robot.widget.scale;

public class OrientationUtils {
     /**
     * 根据角度计算方位角度（方向传感器）
     *
     * @param degree 角度
     * @return 方位角度
     */
    public static String calculateDegree(int degree) {
        if (degree > 0 && degree < 90) {
            if (degree == 45) {
                return "东北";
            }
            return degree + "°";
        } else if (degree > 90 && degree < 180) {
            if (degree == 135) {
                return "东南";
            }
            return (degree - 90) + "°";
        } else if (degree > 180 && degree < 270) {
            if (degree == 225) {
                return "西南";
            }
            return (degree - 180) + "°";
        } else if (degree > 270 && degree < 360) {
            if (degree == 315) {
                return "西北";
            }
            return (degree - 270) + "°";
        } else if (degree == 0 || degree == 360) {
            return "正北";
        } else if (degree == 90) {
            return "正东";
        } else if (degree == 180) {
            return "正南";
        } else if (degree == 270) {
            return "正西";
        }

        return "";
    }
 }