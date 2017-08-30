package com.apps.cabpoint.cabigate.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Treehouse.std on 3/19/2017.
 */

public class utils {

    public static Boolean secondTime = false;
    public static String listID = "";

    public static String replaceAllSpecialCharsAndSpaces(String s) {
        return s.replaceAll("[^a-zA-Z0-9_-]", "");
    }

    public static boolean containSpecialCharacter(String s) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public static boolean isGPSEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("utf", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    //public static Boolean chepi = false;
    public static String getFacebookKeyHash(Context context) {
        String keyhash = "";

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", keyhash);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
        return keyhash;
    }

    @NonNull
    public static Boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.trim().matches(emailPattern);
    }

    @NonNull
    public static Boolean isEmailValid(EditText editText) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Boolean aBoolean = editText.getText().toString().trim().matches(emailPattern);
        if (!aBoolean) {
            editText.setError(editText.getHint());
        }
        return aBoolean;
    }

    @NonNull
    public static Boolean validInput(EditText editText) {
        if (editText.getText().toString().trim().isEmpty() || editText.getHint().toString().equals(editText.getText().toString())) {
            editText.setError(editText.getHint());
            return false;
        } else {
            return true;
        }
    }

    public static Boolean validTextView(TextView editText) {
        if (editText.getText().toString().trim().isEmpty() || editText.getHint().toString().equals(editText.getText().toString())) {
            editText.setError(editText.getHint());
            return false;
        } else {
            return true;
        }
    }

    @NonNull
    public static Boolean isValidNumber(EditText editText) {
        String s = editText.getText().toString();
        if (!s.equals("")) {
            if (TextUtils.isDigitsOnly(s)) {
                return true;
            } else {
                editText.setError(editText.getHint());
                return false;
            }
        } else {
            editText.setError(editText.getHint());
            return false;
        }
    }

    public static void setFont(String fontFamily, View[] views) {
        Typeface openSans = Typeface.createFromAsset(views[0].getContext().getAssets(), fontFamily);

        for (View view : views) {
            if (view instanceof TextView)
                ((TextView) view).setTypeface(openSans);
            if (view instanceof EditText)
                ((EditText) view).setTypeface(openSans);
            if (view instanceof Button)
                ((Button) view).setTypeface(openSans);
        }
    }

    //  public static int sign_in_RequestCode = 33;

    public static int getRand(int max, int min) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static int getRand() {
        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }

    public static String setDurationString(String duration) {
        int index = duration.indexOf(" ");
        return duration.substring(0, index) + "\n" + duration.substring(index, duration.length());
    }

    public static Bitmap getBitmapOfSize(Context context, int resource, int width, int height) {
        BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(resource);
        return Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), width, height, false);
    }

    public static Bitmap resizeMapIcon(Context context, int resource, int w, int h) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        Bitmap imageBitmap = getBitmapOfSize(context, resource, w, h);

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        float scaleWidth = metrics.scaledDensity;
        float scaleHeight = metrics.scaledDensity;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, width, height, matrix, true);
//        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public static Bitmap resizeBitMap(Context context, Bitmap imageBitmap, int w, int h) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, w, h, false);

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        float scaleWidth = metrics.scaledDensity;
        float scaleHeight = metrics.scaledDensity;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(imageBitmap, 0, 0, width, height, matrix, true);
    }

    public static BitmapDescriptor getShapeXmlIcon(Context context, int resource) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), resource, null);
        Canvas canvas = new Canvas();
        int width = context.getResources().getDimensionPixelOffset(R.dimen._10sdp);
        int height = context.getResources().getDimensionPixelOffset(R.dimen._10sdp);
        drawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String removeSpaces(String s) {
        return s.replaceAll("\\s+", "");
    }

    public static double getDistance(LatLng current, LatLng destination) {        // distance in KM
        Location locationA = new Location("point A");
        locationA.setLatitude(current.latitude);
        locationA.setLongitude(current.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(destination.latitude);
        locationB.setLongitude(destination.longitude);
        return locationA.distanceTo(locationB);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static String getImagePathFromURI(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();
        return picturePath;
    }

    public static void selectImage(Activity activity, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Profile Picture"), requestCode);
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static void animation(final Context context, final View viewicon, final View viewbk) {

        viewbk.setVisibility(View.GONE);
        viewicon.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_left_right);
        final Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.fadein);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewbk.setVisibility(View.VISIBLE);
                viewicon.setVisibility(View.VISIBLE);
                viewicon.startAnimation(anim2);
                viewbk.startAnimation(anim2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static void sendEmail(Context context, String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void showInfoDialog(Context context, String s, boolean isLong) {
        //  new InfoDialog(context, s, isLong).show();
    }

    public static boolean netCheckin(Context context) {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) context.getSystemService(
                    CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                    CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isMobileDataEnabled(Context context) {
        Object connectivityService = context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean) m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean getActiveInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null)
            return false;
        else if (!activeNetInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    @NonNull
    public static Boolean isValidInputWithUpdateUI(String editText, String hint, Button button) {
        if (editText.equals("") || editText.equals(hint)) {
            updateUI(button, hint);
            return false;
        } else {
            return true;
        }
    }

    public static void updateUI(final Button button, String label) {
        final String previousLabel = button.getText().toString();
        button.setText(label);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText(previousLabel);
            }
        }, 2000);
    }

    public static int lengthOfFile(String path) {
        File file = new File(path);
        int length;
        length = (int) file.length() / 1024;
        return length;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static File getFileFromBitmap(Context context, Bitmap bitmap) {
        //create a file to write bitmap data
        Date date = new Date();
        String name = date.toString().concat(".png");
        File f = new File(context.getCacheDir(), name);
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    public static void vibrate(Context context, int length) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(length);
    }

    public static Bitmap getResizedBitmapQuality(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public void changeTextSize(View view,int size){
        if (view != null) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                if(group.getChildAt(idx) instanceof ViewGroup){
                    changeTextSize(group.getChildAt(idx),size);
                }
                if(group.getChildAt(idx) instanceof TextView){
                    ((TextView) group.getChildAt(idx)).setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
                }
            }
        }
    }
}
