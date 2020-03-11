package com.lab.dealpos;
import android.content.Context;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.Log;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

@NativePlugin()
public class epsonionic extends Plugin {

    private static final int REQUEST_PERMISSION = 100;
    private static final int DISCONNECT_INTERVAL = 500;//millseconds
    public static Printer  mPrinter = null;
    private Context mContext = null;
    public static boolean mDrawer = false;
    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }


    @PluginMethod()
    private boolean printData(PluginCall call) {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter(call)) {
            mPrinter.clearCommandBuffer();
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            mPrinter.clearCommandBuffer();
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }


    private boolean connectPrinter(PluginCall call) {
        if (mPrinter == null) {
            return false;
        }

        try {
            String target = call.getString("IP");
            mPrinter.connect(target, Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        return true;
    }

    @PluginMethod()
    public void disconnectPrinter(PluginCall call) {
        if (mPrinter == null) {
            return;
        }

        while (true) {
            try {
                mPrinter.disconnect();
                break;
            } catch (final Exception e) {
                if (e instanceof Epos2Exception) {
                    //Note: If printer is processing such as printing and so on, the disconnect API returns ERR_PROCESSING.
                    if (((Epos2Exception) e).getErrorStatus() == Epos2Exception.ERR_PROCESSING) {
                        try {
                            Thread.sleep(DISCONNECT_INTERVAL);
                        } catch (Exception ex) {
                        }
                    }else{
                        bridge.getActivity().runOnUiThread(new Runnable() {
                            public synchronized void run() {
                                ShowMsg.showException(e, "disconnect", mContext);
                            }
                        });
                        break;
                    }
                }else{
                    bridge.getActivity().runOnUiThread(new Runnable() {
                        public synchronized void run() {
                            ShowMsg.showException(e, "disconnect", mContext);
                        }
                    });
                    break;
                }
            }
        }

        mPrinter.clearCommandBuffer();
    }

    @PluginMethod()
    public boolean createReceiptData(PluginCall call) {
        String value = call.getString("Text");

        String method = "";

        StringBuilder textData = new StringBuilder();

        if (mPrinter == null) {
            return false;
        }

        try {

            if(mDrawer) {
                method = "addPulse";
                mPrinter.addPulse(Printer.PARAM_DEFAULT,
                        Printer.PARAM_DEFAULT);
            }

            textData.append(value);
            method = "addText";
            mPrinter.addText(textData.toString());

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
        }
        catch (Exception e) {
            mPrinter.clearCommandBuffer();
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        textData = null;

        return true;
    }
}
