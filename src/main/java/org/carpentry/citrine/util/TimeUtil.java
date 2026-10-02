package org.carpentry.citrine.util;

public class TimeUtil {
    public static int sec2tick(double s) {return (int) s*20;}
    public static double tick2sec(int t) {return (double) t /20;}
    public static int min2tick(double m) {return (int) m*20*60;}
    public static double tick2min(int t) {return (double) t/20/60;}
    public static int hour2tick(double h) {return (int) h*20*60*60;}
    public static double tick2hour(int t) {return (double) t/20/60/60;}
}