package com.report.reportingsystem;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

class ScannerConstants {
    public static String user="employee";//to delete account or update username/password and display above home page
    public static String member_post="moderator";
    public static int jsonArraySize=0;
    public static int index=0;
    public static String id="";
    public static String issue_for_this_member="";
    public static String ip="192.168.0.102";
    public static String data_table="data";
    public static String task_table="task";
    public static String login_data_table="login_data";
    public static String under_review_table="under_review";
    public static String sql="";
    public static  Bitmap image;
    public static String image_name="";
    public static ArrayList<String> task_to_remove = new ArrayList<String>();
    public static ArrayList<String> USERID = new ArrayList<String>();
    public static ArrayList<String> USER_ISSUE = new ArrayList<String>();
    public static ArrayList<String> USER_REPORT = new ArrayList<String>();
    public static ArrayList<String> issue = new ArrayList<String>();
    public static ArrayList<String> selectedImageName = new ArrayList<String>();
    public static int num=0;

}