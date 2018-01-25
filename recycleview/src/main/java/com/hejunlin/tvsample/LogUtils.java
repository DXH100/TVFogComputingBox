
package com.hejunlin.tvsample;

import android.util.Log;

/**
 * ClassName:LogUtils <br/>
 * Date: 2016年6月7日 下午9:13:31 <br/>
 *
 * @author Dhx
 * @version
 */
public final class LogUtils {
    /**
     * Log default tag.
     */
    private static String sTagDefault = "GLC";

    /**
     * Log toggle for release, default value is false.
     */
    private static boolean sToggleRelease = false;

    /**
     * Log toggle for print Throwable new1, default value is true.
     */
    private static boolean sToggleThrowable = true;

    /**
     * Log toggle for print thread name, default value is false.
     */
    private static boolean sToggleThread = true;

    /**
     * Log toggle for print class name and method name, default value is false.
     */
    private static boolean sToggleClassMethod = true;

    /**
     * Log toggle for print file name and code line number, default value is false.
     */
    private static boolean sToggleFileLineNumber = true;

    public static void e(String tag, String msg, Throwable e) {
        printLog(Log.ERROR, tag, msg, e);
    }

    public static void e(String msg, Throwable e) {
        printLog(Log.ERROR, null, msg, e);
    }

    public static void e(String msg) {
        if (msg==null){
            printLog(Log.ERROR, null, "Rnull", null);
            return;
        }
        printLog(Log.ERROR, null, msg, null);
    }

    public static void e(Exception msg) {
        printLog(Log.ERROR, null, msg.toString(), null);
    }

    public static void e(int msg) {
        printLog(Log.ERROR, null, msg+"", null);
    }

    public static void e(long msg) {
        printLog(Log.ERROR, null, msg+"", null);
    }

    public static void e(boolean msg) {
        printLog(Log.ERROR, null, msg+"", null);
    }
    public static void e(Object msg) {
        printLog(Log.ERROR, null, msg.toString(), null);
    }

    public static void w(String tag, String msg, Throwable e) {
        printLog(Log.WARN, tag, msg, e);
    }

    public static void w(String msg, Throwable e) {
        printLog(Log.WARN, null, msg, e);
    }

    public static void w(String msg) {
        printLog(Log.WARN, null, msg, null);
    }

    public static void i(String tag, String msg, Throwable e) {
        printLog(Log.INFO, tag, msg, e);
    }

    public static void i(String msg) {
        printLog(Log.INFO, null, msg, null);
    }

    public static void d(String tag, String msg, Throwable e) {
        printLog(Log.DEBUG, tag, msg, e);
    }

    public static void d(String msg, Throwable e) {
        printLog(Log.DEBUG, null, msg, e);
    }

    public static void d(String tag, String msg) {
        printLog(Log.DEBUG, tag, msg, null);
    }

    public static void d(String msg) {
        printLog(Log.DEBUG, null, msg, null);
    }

    public static void v(String tag, String msg, Throwable e) {
        printLog(Log.VERBOSE, tag, msg, e);
    }

    public static void v(String tag, String msg) {
        printLog(Log.VERBOSE, tag, msg, null);
    }

    public static void v(String msg) {
        printLog(Log.VERBOSE, null, msg, null);
    }

    private static void printLog(int logType, String tag, String msg, Throwable e) {
        String tagStr = (tag == null) ? sTagDefault : tag;
        if (sToggleRelease)return;
        if (sToggleRelease) {
            if (logType < Log.INFO) {
                return;
            }
            String msgStr =
                    (e == null) ? msg : (msg + "\n" + Log.getStackTraceString(e));

            switch (logType) {
                case Log.ERROR:
                    Log.e(tagStr, msgStr);

                    break;
                case Log.WARN:
                    Log.w(tagStr, msgStr);

                    break;
                case Log.INFO:
                    Log.i(tagStr, msgStr);

                    break;
                default:
                    break;
            }

        } else {
            StringBuilder msgStr = new StringBuilder();

            if (sToggleThread || sToggleClassMethod || sToggleFileLineNumber) {
                Thread currentThread = Thread.currentThread();

                if (sToggleThread) {
                    msgStr.append("<");
                    msgStr.append(currentThread.getName());
                    msgStr.append("> ");
                }

                if (sToggleClassMethod) {
                    StackTraceElement ste = currentThread.getStackTrace()[4];
                    String className = ste.getClassName();
                    msgStr.append("[");
                    msgStr.append(className == null ? null
                            : className.substring(className.lastIndexOf('.') + 1));
                    msgStr.append("--");
                    msgStr.append(ste.getMethodName());
                    msgStr.append("] ");
                }

                if (sToggleFileLineNumber) {
                    StackTraceElement ste = currentThread.getStackTrace()[4];
                    msgStr.append("(");
                    msgStr.append(ste.getFileName());
                    msgStr.append(":");
                    msgStr.append(ste.getLineNumber());
                    msgStr.append(") ");
                }
            }

            msgStr.append(msg);
            if (e != null && sToggleThrowable) {
                msgStr.append('\n');
                msgStr.append(Log.getStackTraceString(e));
            }

            switch (logType) {
                case Log.ERROR:
                    Log.e(tagStr, msgStr.toString());

                    break;
                case Log.WARN:
                    Log.w(tagStr, msgStr.toString());

                    break;
                case Log.INFO:
                    Log.i(tagStr, msgStr.toString());

                    break;
                case Log.DEBUG:
                    Log.d(tagStr, msgStr.toString());

                    break;
                case Log.VERBOSE:
                    Log.v(tagStr, msgStr.toString());

                    break;
                default:
                    break;
            }
        }
    }

}
