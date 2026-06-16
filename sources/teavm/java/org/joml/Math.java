package org.joml;

public final class Math {

    public static float sin(float angle) {
        return (float) java.lang.Math.sin(angle);
    }

    public static float cos(float angle) {
        return (float) java.lang.Math.cos(angle);
    }

    public static float tan(float angle) {
        return (float) java.lang.Math.tan(angle);
    }

    public static float sqrt(float value) {
        return (float) java.lang.Math.sqrt(value);
    }

    public static float abs(float value) {
        return java.lang.Math.abs(value);
    }

    public static int abs(int value) {
        return java.lang.Math.abs(value);
    }

    public static float min(float a, float b) {
        return java.lang.Math.min(a, b);
    }

    public static int min(int a, int b) {
        return java.lang.Math.min(a, b);
    }

    public static long min(long a, long b) {
        return java.lang.Math.min(a, b);
    }

    public static float max(float a, float b) {
        return java.lang.Math.max(a, b);
    }

    public static int max(int a, int b) {
        return java.lang.Math.max(a, b);
    }

    public static long max(long a, long b) {
        return java.lang.Math.max(a, b);
    }

    public static float floor(float value) {
        return (float) java.lang.Math.floor(value);
    }

    public static float ceil(float value) {
        return (float) java.lang.Math.ceil(value);
    }

    public static int floorToInt(float value) {
        return (int) java.lang.Math.floor(value);
    }

    public static int ceilToInt(float value) {
        return (int) java.lang.Math.ceil(value);
    }

    public static float round(float value) {
        return java.lang.Math.round(value);
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static int clamp(int value, int min, int max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static float toRadians(float degrees) {
        return (float) java.lang.Math.toRadians(degrees);
    }

    public static float toDegrees(float radians) {
        return (float) java.lang.Math.toDegrees(radians);
    }

    public static float fma(float a, float b, float c) {
        return a * b + c;
    }

    public static float pow(float a, float b) {
        return (float) java.lang.Math.pow(a, b);
    }

    public static float asin(float value) {
        return (float) java.lang.Math.asin(value);
    }

    public static float acos(float value) {
        return (float) java.lang.Math.acos(value);
    }

    public static float atan2(float y, float x) {
        return (float) java.lang.Math.atan2(y, x);
    }

    public static float atan(float value) {
        return (float) java.lang.Math.atan(value);
    }

    public static float exp(float value) {
        return (float) java.lang.Math.exp(value);
    }

    public static float log(float value) {
        return (float) java.lang.Math.log(value);
    }

    public static float signum(float value) {
        return java.lang.Math.signum(value);
    }

    public static float copySign(float magnitude, float sign) {
        return java.lang.Math.copySign(magnitude, sign);
    }

    public static float nextAfter(float start, float direction) {
        return java.lang.Math.nextAfter(start, direction);
    }

    public static boolean isFinite(float f) {
        return java.lang.Float.isFinite(f);
    }

    private Math() {
        // prevent instantiation
    }

    public static float cosFromSin(float sin, float angle) {
        return (float) Math.cos(angle);
    }

    public static float invsqrt(float value) { return 1.0f / (float) java.lang.Math.sqrt(value); }
}
