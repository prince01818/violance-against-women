package com.example.prince.violanceagainstwomen;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SABBIR on 4/7/2016.
 */
public class Utility {
    Context context;
    Activity activity;
    String AppNname;
    String DATEformat = "dd-MMM-yyyy hh:mm:ss aa";

    public Utility(Context context) {
        this.context = context;
        // activityfromCOntext();
        AppNname = getString(R.string.app_name);
    }

    public void activityfromCOntext() {
        activity = (Activity) context;
    }

    public String Classname() {
        return context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName())
                .getComponent().getClassName();
    }

    public void getDeviceIfo() {
        int versionCode = android.os.Build.VERSION.SDK_INT;
        String strlayouts[] = {"Undefined", "SMALL", "NORMAL", "LARGE", "XLARGE"};
        String layout = strlayouts[(context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)];
        int intdrawable[] = {120, 160, 240, 320, 480, 640};
        String strdrawable[] = {"l", "m", "h", "x", "xx", "xxx"};
        String drawable = "ldpi";
        for (int i = 0; i < intdrawable.length; i++) {
            if( context.getResources().getDisplayMetrics().densityDpi ==intdrawable[i])
            {
                drawable = strdrawable[i]+ "dpi";
                break;
            }
        }
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        AlertDialog("versionCode " + versionCode + "\nlayout " + layout + "\ndrawable  " + drawable+"\nDimention "+height+"*"+width);
    }

    public int getResId(String resName, Class<?> className) {

        try {
            java.lang.reflect.Field idField = className
                    .getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }

    public String getString(int ID) {
        return context.getResources().getString(ID);
    }

    public Drawable getDrawable(int ID) {
        return context.getResources().getDrawable(ID);
    }

    public Bitmap getBitmap(int ID) {
        try {
            return BitmapFactory.decodeResource(context.getResources(), ID);
        } catch (Exception e) {
            return null;
        }
    }

    public String latestImgUserLocation() {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};
        final Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        }
        return null;
    }

    public Bitmap ImgUserLocation(String imagelocation) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagelocation, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagelocation, options);
    }

    public String Bitmap2String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    public String getSYSDate() {
        return getSYSDate(0);
    }

    public String getSYSDate(int type) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEformat);
        Date date = new Date();
        String now = sdf.format(date.getTime());
        String[] parts = now.split(" ");
        if (type == 1)
            return parts[0];
        if (type == 2)
            return parts[1] + " " + parts[2];
        return now;

    }

    public int getcolor(String colorname) {
        return Color.parseColor(colorname);
    }

    public int getcolorTransparent() {
        return Color.TRANSPARENT;
    }

    public static boolean EmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean PhoneNumberValid(String phoneNumber) {
        String expression = "^[0-9-+]{9,15}$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return (matcher.matches()) ? true : false;
    }

    public boolean network_connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String RemoveExtention(String name) {
        if (name.indexOf(".") > 0)
            name = name.substring(0, name.lastIndexOf("."));
        return name;
    }

    public String getExtention(String name) {

        String filenameArray[] = name.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
        return extension;
    }

    public static String addSlashes(String string) {
        string = string.replaceAll("\\\\", "\\\\\\\\");
        string = string.replaceAll("\\n", "\\\\n");
        string = string.replaceAll("\\r", "\\\\r");
        string = string.replaceAll("\\00", "\\\\0");
        string = string.replaceAll("'", "\\\\'");
        return string;
    }

    public static String removeSlashes(String string) {
        string = string.replaceAll("\\\\'", "'");
        string = string.replaceAll("\\\\0", "\\00");
        string = string.replaceAll("\\\\r", "\\r");
        string = string.replaceAll("\\\\n", "\\n");
        string = string.replaceAll("\\\\\\\\", "\\\\");
        return string;
    }

    public String StringRightJustify(String givenString, int number) {
        String postString = "";
        int count = number - givenString.length();
        for (int i = 0; i < count; i++) {
            postString += " ";
        }
        return givenString + postString;
    }

    public String StringLeftJustify(String givenString, int number) {
        String postString = "";
        int count = number - givenString.length();
        for (int i = 0; i < count; i++) {
            postString += " ";
        }
        return postString + givenString;
    }

    public String capitalize(String source) {
        source = source.toLowerCase();
        StringBuffer res = new StringBuffer();
        String[] strArr = source.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }
        return res.toString().trim();
    }

    public String[] Arraylist2Array(ArrayList<String> arrayList) {
        int size = arrayList.size();
        String strings[] = new String[size];
        for (int i = 0; i < size; i++)
            strings[i] = arrayList.get(i);
        return strings;
    }

    public ArrayList<String> Arraylist2Array(String[] strings) {
        int size = strings.length;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < size; i++)
            arrayList.add(strings[i]);
        return arrayList;
    }

    public String double2dec(double number) {
        DecimalFormat sf = new DecimalFormat("0.00");
        return sf.format(number);
    }

    public String double2dec(String number) {
        DecimalFormat sf = new DecimalFormat("00.00");
        return sf.format(Double.parseDouble(number));
    }

    public String Addressname(double latitude, double longitude)
            throws IOException {
        String addres = "";

        Geocoder geocoder;
        List<Address> add;
        geocoder = new Geocoder(context, Locale.getDefault());

        add = geocoder.getFromLocation(latitude, longitude, 1);
        addres = add.get(0).getAddressLine(0);

        return addres;
    }

    public void makeToast(String text) {
        Toast start = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        start.setGravity(Gravity.CENTER, 0, 0);
        start.show();
    }

    public void makeToast(int number) {
        makeToast(number + "");
    }

    public void makeToastNoInternet() {
        makeToast("Please connect internet or wifi");
    }

    public AlertDialog.Builder AlertDialogBuiLd(String Text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, R.style.AppTheme));
        alertDialogBuilder.setTitle(AppNname);
        TextView textView = new TextView(context);
        textView.setText("\n" + Text);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextAppearance(context,
                android.R.style.TextAppearance_Medium);
        textView.setMovementMethod(new ScrollingMovementMethod());
        alertDialogBuilder.setView(textView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        return alertDialogBuilder;
    }

    public void AlertDialog(String Text) {
        AlertDialog.Builder alertDialog = AlertDialogBuiLd(Text);
        alertDialog.show();

    }

    public void AlertDialog(int num) {
        AlertDialog(num + "");
    }

    public void AlertDialogNoInternetConnection() {
        AlertDialog("No Internet Connection");
    }

    public AlertDialog.Builder AlertDialogInputFieldSingle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        builder.setView(input);
        return builder;
    }

    public void ZertAlertDialog(String Title, String Body, int icon) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, R.style.AppTheme));
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setIcon(context.getResources().getDrawable(icon));
        TextView textView = new TextView(context);
        textView.setText("\n" + Body);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextAppearance(context,
                android.R.style.TextAppearance_Medium);
        textView.setMovementMethod(new ScrollingMovementMethod());
        alertDialogBuilder.setView(textView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public ProgressDialog ProgressLoading(String Text) {
        return ProgressLoading(AppNname, Text);
    }

    public ProgressDialog ProgressLoading(String Title, String Text) {

        ProgressDialog barProgressDialog = new ProgressDialog(
                new ContextThemeWrapper(context, R.style.AppTheme));
        barProgressDialog.setTitle(Title);
        barProgressDialog.setMessage(Text);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(false);
        return barProgressDialog;
    }

    public String ContactNameFromNumber(String phoneNumber) {
        ContentResolver cResolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cResolver.query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = "";
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    public void smsSend(String phn, String sms) {
        SmsManager message = SmsManager.getDefault();
        message.sendTextMessage(phn, null, sms, null, null);
    }

    /*public void CalllNumber(String phn) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phn));
        ac.startActivity(intent);
    }*/

    public String smsId(String message, String number) {
        try {

            Uri uriSms = Uri.parse("content://sms/inbox");
            String searchQuery = "address like '%" + number
                    + "%' and body like '%" + message + "%'";
            String ordered = "_id DESC";
            Cursor cursor = context.getContentResolver().query(uriSms,
                    new String[]{"_id"}, searchQuery, null, ordered);
            int size = cursor.getCount();
            if (size == 0)
                return null;
            cursor.moveToFirst();
            String read = "";
            cursor.moveToFirst();
            cursor.moveToFirst();
            long id = cursor.getLong(0);
            return id + "";
        } catch (Exception e) {
            return null;
        }
    }

    public void smsDELETE(String smsId) {
        try {
            context.getContentResolver().delete(
                    Uri.parse("content://sms/" + smsId), null, null);
        } catch (Exception ex) {
        }
    }

    public static void keyboardhide(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void ShareIt(String body) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = body;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    public int GetRingtoneVolumeCurrent() {
        AudioManager amanager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = amanager.getStreamVolume(AudioManager.STREAM_RING);
        return currentVolume;
    }

    public int GetRingtonetVolumeMax() {
        AudioManager amanager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int max = amanager.getStreamMaxVolume(AudioManager.STREAM_RING);
        return max;
    }

    public void SetRingtoneVolumeCurrent(int volume) {
        AudioManager amanager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamVolume(AudioManager.STREAM_RING, volume,
                AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

    }

    public void setRingingMode(int ringerMode) {
        int RingingMode[] = {AudioManager.RINGER_MODE_SILENT,
                AudioManager.RINGER_MODE_VIBRATE,
                AudioManager.RINGER_MODE_NORMAL};
        AudioManager amanager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        amanager.setRingerMode(ringerMode);
    }

    public int getRingingMode() {
        AudioManager amanager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return amanager.getRingerMode();
    }

    public SpannableString UnderlineText(String text) {
        SpannableString underlineText = new SpannableString(text);
        underlineText
                .setSpan(new UnderlineSpan(), 0, underlineText.length(), 0);
        return underlineText;
    }

    public boolean nullvalue(String string) {
        if (string == null)
            return false;
        if (string.length() < 1)
            return false;
        if (string.contains("null"))
            return false;

        if (string.equals("null"))
            return false;
        if (string.equals(" "))
            return false;
        if (string.equals("  "))
            return false;
        return true;
    }

    public boolean Check_emptyvalues(ArrayList<String> item) {
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i).equals(""))
                return true;
        }
        return false;

    }

    public boolean Check_ALLemptyvalues(ArrayList<String> item) {
        for (int i = 0; i < item.size(); i++) {
            if (!item.get(i).equals(""))
                return false;
        }
        return true;

    }

    public String getArrayListValues(ArrayList<ArrayList<String>> item) {
        String data = "";
        if (item == null || item.size() == 0)
            return "null";
        for (int i = 0; i < item.size(); i++) {
            data += i + "->";
            for (int j = 0; j < item.get(i).size(); j++)
                data += item.get(i).get(j) + " ";
            data += "\n";
        }
        return data;
    }

    public String getArrayListValue(ArrayList<String> item) {
        String data = "";
        if (item == null || item.size() == 0)
            return "null";
        for (int i = 0; i < item.size(); i++) {
            data += i + " ";
            data += item.get(i);
            data += "\n";
        }
        return data;
    }

    public String getCursorValues(Cursor cursor) {
        String data = "";
        int size = cursor.getCount();

        int columnnumber = cursor.getColumnCount();
        for (int i = 0; i < size; i++) {
            data += i + "-->";
            if (i == 0)
                cursor.moveToFirst();
            else
                cursor.moveToNext();
            for (int j = 0; j < columnnumber; j++) {
                try {
                    data += cursor.getString(j) + " ";
                } catch (Exception e) {
                    try {
                        data += cursor.getInt(j) + " ";
                    } catch (Exception e1) {
                        try {
                        } catch (Exception e2) {
                            data += cursor.getFloat(j) + " ";

                        }
                    }
                }
            }
            data += "\n";
        }
        return data;
    }

    public String twelveto24(String time1) {
        String time2 = "";
        int i = 0, day = 0, mon = 0, y = 0;
        int PM = 0;

        if (time1.contains("P")) {
            PM = 1;
        }
        for (i = 0; ; i++) {
            if (time1.charAt(i) == ':')
                break;
            y = y * 10 + Character.getNumericValue(time1.charAt(i));
        }
        for (i++; ; i++) {
            if (time1.charAt(i) == ':')
                break;
            mon = mon * 10 + Character.getNumericValue(time1.charAt(i));
        }
        for (i++; ; i++) {
            if (time1.charAt(i) == ' ')
                break;
            day = day * 10 + Character.getNumericValue(time1.charAt(i));
        }
        if (PM == 1 && y != 12)
            y += 12;
        String yy = "", mm = "", dd = "";
        if (y < 10)
            yy = "0";
        if (mon < 10)
            mm = "0";
        if (day < 10)
            dd = "0";
        time2 = yy + y + ":" + mm + mon + ":" + dd + day;
        return time2;
    }

    public long day_number_from_date(long y, long m, long d) {
        m = (m + 9) % 12;
        y = y - m / 10;
        return 365 * y + y / 4 - y / 100 + y / 400 + (m * 306 + 5) / 10
                + (d - 1);
    }

    public int Time_difference(int y, int m, long d, int y1, int m1, int d1,
                               int mi, int h, int mi1, int h1) {
        long Daydif = day_number_from_date(y1, m1, d1)
                - day_number_from_date(y, m, d);

        int timedef = h1 - h;
        if (Daydif == 1 && timedef <= 0) {
            return 1;

        }
        if (Daydif == 0 && timedef >= 0) {
            return 1;
        }
        return 0;
    }

    public int Time_difference3(int y, int m, long d, int y1, int m1, int d1,
                                int mi, int h, int mi1, int h1) {
        long Daydif = day_number_from_date(y1, m1, d1)
                - day_number_from_date(y, m, d);

        int timedef = h1 - h;
        if (Daydif == 1 && timedef <= -21) {
            return 1;

        }
        if (Daydif == 0 && timedef >= 0 && timedef <= 3) {
            return 1;
        }
        return 0;
    }

    public int timedifference(String time1, String time2) {
        int difference = 0;
        int hour1 = (time1.charAt(0) - 48) * 10 + (time1.charAt(1) - 48);
        int min1 = (time1.charAt(3) - 48) * 10 + (time1.charAt(4) - 48);
        int sec1 = (time1.charAt(6) - 48) * 10 + (time1.charAt(7) - 48);

        int hour2 = (time2.charAt(0) - 48) * 10 + (time2.charAt(1) - 48);
        int min2 = (time2.charAt(3) - 48) * 10 + (time2.charAt(4) - 48);
        int sec2 = (time2.charAt(6) - 48) * 10 + (time2.charAt(7) - 48);

        int start = (hour1 * 60 + min1) * 60 + sec1;
        int end = (hour2 * 60 + min2) * 60 + sec2;

        difference = Math.abs(start - end);
        return difference;
    }

    public String time_adding(String time1, int second) {
        int hour1 = (time1.charAt(0) - 48) * 10 + (time1.charAt(1) - 48);
        int min1 = (time1.charAt(3) - 48) * 10 + (time1.charAt(4) - 48);
        int sec1 = (time1.charAt(6) - 48) * 10 + (time1.charAt(7) - 48);

        int new_time = hour1 * 3600 + min1 * 60 + sec1 + second;
        hour1 = new_time / 3600;
        min1 = (new_time - hour1 * 3600) / 60;
        sec1 = new_time - hour1 * 3600 - min1 * 60;
        String new_time1 = "";
        if (hour1 < 10)
            new_time1 += "0";
        new_time1 += hour1 + ":";
        if (min1 < 10)
            new_time1 += "0";
        new_time1 += min1 + ":";
        if (sec1 < 10)
            new_time1 += "0";
        new_time1 += sec1;
        return new_time1;
    }

    public String dateformat_SFORCE(int day, int month, int year) {
        String[] Months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        String Dates = "";
        if (day < 10)
            Dates = "0";
        Dates += day + "-" + Months[month].toUpperCase() + "-" + year % 100;
        return Dates;
    }

    public InputFilter[] ETblock() {
        final String blockCharacterSet = "~#^|$%&*!)(/";
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        return new InputFilter[]{filter};
    }

    public boolean ServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void goToApp() {
        String AppLink = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AppLink)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + AppLink)));
        }
    }

    public void goToApp(String AppLink) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(AppLink);
        try {
            context.startActivity(launchIntent);
        } catch (Exception e1) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AppLink)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + AppLink)));
            }
        }
    }

    public void recommendApp(Activity activity, int ImageId, String Body, String Subject) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_TEXT, (Body + "\n\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);

        if (ImageId != 0) {
            saVeBanner(ImageId);
            File file = null;
            file = new File(Environment.getExternalStorageDirectory().toString(),
                    "/.sadman/Bannar_" + "cricscore" + ".png");


            Uri fileUri = FileProvider.getUriForFile(context, "com.zertinteractive.cricscore.fileprovider", file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        }

        activity.startActivityForResult(Intent.createChooser(shareIntent, "Share Cric Score"), 1);
    }

    public void saVeBanner(int ImageId) {
        File filebase = new File(Environment.getExternalStorageDirectory().toString() + "/.sadman");
        File file = new File(Environment.getExternalStorageDirectory().toString(),
                "/.sadman/Bannar_" + "cricscore" + ".png");
        if (!filebase.exists()) {
            AlertDialog(filebase.mkdir() + "");
        }
        if (file.exists()) {
            return;
        }
        Bitmap bb = BitmapFactory.decodeResource(context.getResources(), ImageId);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        try {
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), ImageId);
            b = Bitmap.createScaledBitmap(b, b.getWidth() / 3, b.getHeight() / 3, true);
            OutputStream fOut = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), file.getName());
            b.recycle();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog("" + e);

        }
    }
}
