package com.example.android.socketphoneclient;


import android.content.Context;
import android.content.res.Resources;
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
    private Resources mResources;

    Client(String addr, int port, TextView textResponse, Context context) {
        mDstAddress = addr;
        mDstPort = port;
        mResponseTextView = textResponse;
        mContext = context;
        mConnected = false;
        mResources = mContext.getResources();
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
            mResponse = mResources.getString(R.string.response_unknown_host_exception) + " "
                    + e.toString();
            Log.e(TAG, mResponse);
        } catch (IOException e) {
            mResponse = mResources.getString(R.string.response_io_exception) + " " + e.toString();
            Log.e(TAG, mResponse);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, mResources.getString(R.string.response_unknown_host_exception)
                            + " " + e);
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

            String imei = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_imei));
            if (!imei.equals(""))
                response += mResources.getString(R.string.imei) + ": " + imei + "\n";

            String softwareVersion = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_software_version));
            if (!softwareVersion.equals(""))
                response += mResources.getString(R.string.software_version) + ": " +
                        softwareVersion + "\n";

            String phoneType = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_phone_type));
            if (!phoneType.equals(""))
                response += mResources.getString(R.string.phone_type) + ": " + phoneType + "\n";

            String serviceState = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_service_state));
            if (!serviceState.equals(""))
                response += mResources.getString(R.string.service_state) + ": " + serviceState +
                        "\n";

            String simState = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_sim_state));
            if (!simState.equals(""))
                response += mResources.getString(R.string.sim_state) + ": " + simState + "\n";

            String simSerialNumber = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_sim_serial_number));
            if (!simSerialNumber.equals(""))
                response += mResources.getString(R.string.sim_serial_number) + ": " +
                        simSerialNumber + "\n";

            String simOperatorName = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_sim_operator_name));
            if (!simOperatorName.equals(""))
                response += mResources.getString(R.string.sim_operator_name) + ": " +
                        simOperatorName + "\n";

            String networkOperatorName = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_network_operator_name));
            if (!networkOperatorName.equals(""))
                response += mResources.getString(R.string.network_operator_name) + ": " +
                        networkOperatorName + "\n";

            String line1Number = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_line_1_number));
            if (!line1Number.equals(""))
                response += mResources.getString(R.string.line_number) + ": " + line1Number + "\n";

            String networkCountryIso = JSONUtils.getStringFromJSON(radioJSONObject,
                    mResources.getString(R.string.json_key_network_country_iso));
            if (!networkCountryIso.equals(""))
                response += mResources.getString(R.string.network_country_iso) + ": " +
                        networkCountryIso + "\n";

            // Get cells array.
            JSONArray cellsJSONArray =
                    radioJSONObject.getJSONArray(mResources.getString(R.string.json_key_cells));
            if (cellsJSONArray != null) {
                JSONObject cellJSONObject;
                for (int n = 0; n < cellsJSONArray.length(); n++) {
                    cellJSONObject = cellsJSONArray.getJSONObject(n);
                    response += "\n" + mResources.getString(R.string.cell) + n + 1 + ":" + "\n";

                    // Cell type.
                    String type = JSONUtils.getStringFromJSON(cellJSONObject,
                            mResources.getString(R.string.json_key_type));
                    if (!type.equals(""))
                        response += "  " + mResources.getString(R.string.type) + ": " + type + "\n";

                    // Is cell registered?
                    String isRegistered = JSONUtils.getStringFromJSON(cellJSONObject,
                            mResources.getString(R.string.json_key_is_registered));
                    if (!isRegistered.equals(""))
                        response += "  " + mResources.getString(R.string.is_registered) + ": " +
                                isRegistered + "\n";

                    // Cell identity.
                    JSONObject identityJSONObject = cellJSONObject.getJSONObject(
                            mResources.getString(R.string.json_key_identity));
                    if (identityJSONObject != null) {
                        String cid = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_cid));
                        if (!cid.equals(""))
                            response += "  " + mResources.getString(R.string.cid) + ": " + cid
                                    + "\n";

                        String ci = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_ci));
                        if (!ci.equals(""))
                            response += "  " + mResources.getString(R.string.ci) + ": " + ci + "\n";

                        String psc = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_psc));
                        if (!psc.equals(""))
                            response += "  " + mResources.getString(R.string.psc) + ": " + psc
                                    + "\n";

                        String lac = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_lac));
                        if (!lac.equals(""))
                            response += "  " + mResources.getString(R.string.lac) + ": " + lac
                                    + "\n";

                        String sid = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_sid));
                        if (!sid.equals(""))
                            response += "  " + mResources.getString(R.string.sid) + ": " + sid
                                    + "\n";

                        String mcc = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_mcc));
                        if (!mcc.equals(""))
                            response += "  " + mResources.getString(R.string.mcc) + ": " + mcc
                                    + "\n";

                        String mccString = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_mcc_string));
                        if (!mccString.equals(""))
                            response += "  " + mResources.getString(R.string.mcc_string) + ": "
                                    + mccString + "\n";

                        String mncString = JSONUtils.getStringFromJSON(identityJSONObject, 
                                mResources.getString(R.string.json_key_mnc_string));
                        if (!mncString.equals(""))
                            response += "  " + mResources.getString(R.string.mnc_string) + ": "
                                    + mncString + "\n";
                    }

                    // Cell signal strength.
                    JSONObject strengthJSONObject = cellJSONObject.getJSONObject(
                            mResources.getString(R.string.json_key_strength));
                    if (strengthJSONObject != null) {
                        String dbm = JSONUtils.getStringFromJSON(strengthJSONObject, 
                                mResources.getString(R.string.json_key_dbm));
                        if (!dbm.equals(""))
                            response += "  " + mResources.getString(R.string.dbm) + ": " + dbm
                                    + "\n";

                        String timingAdvance = JSONUtils.getStringFromJSON(strengthJSONObject,
                                mResources.getString(R.string.json_key_timing_advance));
                        if (!timingAdvance.equals(""))
                            response += "  " + mResources.getString(R.string.timing_advance) + ": "
                                    + timingAdvance + "\n";
                    }
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, mResources.getString(R.string.json_error) + ": " + e);
        }

        return response;
    }
}

