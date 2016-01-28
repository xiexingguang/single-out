package com.ec.singleOut.util;

import java.util.List;

/**
 * Created by ecuser on 2015/12/31.
 */
public class StringUtil {

    public static boolean isNullString(String input){
        return (input==null||input.trim().length()<=0);
    }

    public static boolean stringIsContainStringArrays(String[] tags, String s) {
        for (String ss : tags) {
            if (ss.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] tags =  {"2494808434","werwer"};
     /*   System.out.println(tags[0]);
        System.out.println(tags[1]);
        for (String s : tags) {
            System.out.println(s);
        }*/
        System.out.println(stringIsContainStringArrays(tags,"2494808434"));
    }

    @SuppressWarnings("rawtypes")
    public static String listToString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

}
