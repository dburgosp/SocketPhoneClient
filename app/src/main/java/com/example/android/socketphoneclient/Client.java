package com.example.android.socketphoneclient;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, Void> {
    private static final String TAG = Client.class.getSimpleName();
    private String mDstAddress, mResponse = "";
    private int mDstPort;
    private boolean mConnected;
    private Context mContext;
    private TextView mResponseTextView;

    Client(String addr, int port, TextView textResponse, Context context) {
        mDstAddress = addr;
        mDstPort = port;
        mResponseTextView = textResponse;
        mContext = context;
        mConnected = false;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        Socket socket = null;

        try {
            socket = new Socket(mDstAddress, mDstPort);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            if (inputStream != null) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    stringBuilder.append(byteArrayOutputStream.toString("UTF-8"));
                }
                mResponse = stringBuilder.toString();
                mConnected = true;
            }

        } catch (UnknownHostException e) {
            mResponse = mContext.getResources().getString(R.string.response_unknown_host_exception)
                    + e.toString();
            Log.e(TAG, mResponse);
        } catch (IOException e) {
            mResponse = mContext.getResources().getString(R.string.response_io_exception)
                    + e.toString();
            Log.e(TAG, mResponse);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, mContext.getResources().getString(
                            R.string.response_unknown_host_exception) + e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Parse JSON response and show it.
        mResponseTextView.setText(parseJSONresponse(mResponse));
        super.onPostExecute(result);
    }

    public boolean isConnected() {
        return mConnected;
    }

    private String parseJSONresponse(String JSONresponse) {
        String response = "";
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject radioJSONObject = new JSONObject(JSONresponse);

            String IMEI = JSONUtils.getStringFromJSON(radioJSONObject, "IMEI");
            if (!IMEI.equals(""))
                response += "IMEI: " + IMEI + "\n";

            String softwareVersion = JSONUtils.getStringFromJSON(radioJSONObject, "softwareVersion");
            if (!softwareVersion.equals(""))
                response += "Software Version: " + softwareVersion + "\n";

            String phoneType = JSONUtils.getStringFromJSON(radioJSONObject, "phoneType");
            if (!phoneType.equals(""))
                response += "Phone type: " + phoneType + "\n";

            String serviceState = JSONUtils.getStringFromJSON(radioJSONObject, "serviceState");
            if (!serviceState.equals(""))
                response += "Service state: " + serviceState + "\n";

            String simState = JSONUtils.getStringFromJSON(radioJSONObject, "simState");
            if (!simState.equals(""))
                response += "SIM state: " + simState + "\n";

            String simSerialNumber = JSONUtils.getStringFromJSON(radioJSONObject, "simSerialNumber");
            if (!simSerialNumber.equals(""))
                response += "SIM serial number: " + simSerialNumber + "\n";

            String simOperatorName = JSONUtils.getStringFromJSON(radioJSONObject, "simOperatorName");
            if (!simOperatorName.equals(""))
                response += "SIM operator name: " + simOperatorName + "\n";

            String networkOperatorName = JSONUtils.getStringFromJSON(radioJSONObject, "networkOperatorName");
            if (!networkOperatorName.equals(""))
                response += "Net operator name: " + networkOperatorName + "\n";

            String line1Number = JSONUtils.getStringFromJSON(radioJSONObject, "line1Number");
            if (!line1Number.equals(""))
                response += "Line number: " + line1Number + "\n";

            String networkCountryIso = JSONUtils.getStringFromJSON(radioJSONObject, "networkCountryIso");
            if (!networkCountryIso.equals(""))
                response += "Net country ISO: " + networkCountryIso + "\n";

            // Get cells array.
            JSONArray cellsJSONArray = radioJSONObject.getJSONArray("cells");
            if (cellsJSONArray != null) {
                response += "Cells info:\n";
                JSONObject cellJSONObject;
                for (int n = 0; n < cellsJSONArray.length(); n++) {
                    cellJSONObject = cellsJSONArray.getJSONObject(n);

                    // Cell type.
                    String type = JSONUtils.getStringFromJSON(cellJSONObject, "type");
                    if (!type.equals(""))
                        response += "Type: " + type + "\n";

                    // Is cell registered?
                    String isRegistered = JSONUtils.getStringFromJSON(cellJSONObject, "isRegistered");
                    if (!isRegistered.equals(""))
                        response += "Is registered: " + isRegistered + "\n";

                    // Cell identity.
                    JSONObject identityJSONObject = cellJSONObject.getJSONObject("identity");
                    if (identityJSONObject != null) {
                        String cid = JSONUtils.getStringFromJSON(identityJSONObject, "cid");
                        if (!cid.equals(""))
                            response += "CID: " + cid + "\n";

                        String ci = JSONUtils.getStringFromJSON(identityJSONObject, "ci");
                        if (!ci.equals(""))
                            response += "CI: " + ci + "\n";

                        String psc = JSONUtils.getStringFromJSON(identityJSONObject, "psc");
                        if (!psc.equals(""))
                            response += "PSC: " + psc + "\n";

                        String lac = JSONUtils.getStringFromJSON(identityJSONObject, "lac");
                        if (!lac.equals(""))
                            response += "LAC: " + lac + "\n";

                        String sid = JSONUtils.getStringFromJSON(identityJSONObject, "sid");
                        if (!sid.equals(""))
                            response += "SID: " + sid + "\n";

                        String mcc = JSONUtils.getStringFromJSON(identityJSONObject, "mcc");
                        if (!mcc.equals(""))
                            response += "MCC: " + mcc + "\n";

                        String mccString = JSONUtils.getStringFromJSON(identityJSONObject, "mccString");
                        if (!mccString.equals(""))
                            response += "MCC string: " + mccString + "\n";

                        String mncString = JSONUtils.getStringFromJSON(identityJSONObject, "mncString");
                        if (!mncString.equals(""))
                            response += "MNC string: " + mncString + "\n";
                    }

                    // Cell signal strength.
                    JSONObject strengthJSONObject = cellJSONObject.getJSONObject("strength");
                    if (strengthJSONObject != null) {
                        String dbm = JSONUtils.getStringFromJSON(strengthJSONObject, "dbm");
                        if (!dbm.equals(""))
                            response += "DBM: " + dbm + "\n";

                        String timingAdvance = JSONUtils.getStringFromJSON(strengthJSONObject, "timingAdvance");
                        if (!timingAdvance.equals(""))
                            response += "Timing advance: " + timingAdvance + "\n";
                    }
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getTmdbMovies) Error parsing the JSON response: ", e);
        }

        return response;
    }
}

