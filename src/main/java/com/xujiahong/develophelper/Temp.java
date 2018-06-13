package com.xujiahong.develophelper;

/**
 * description
 *
 * @author xujiahong
 * @date 2018/5/2
 */
public class Temp {

    public static void main(String[] args) {
        String loopSwagger = "(abc),\n";
        if (loopSwagger.length() > 0) {
            if (loopSwagger.charAt(loopSwagger.length() - 2) == ',') {
                loopSwagger = loopSwagger.substring(0, loopSwagger.length() - 2) + loopSwagger.substring(loopSwagger
                        .length()-1);
            }
        }
        System.out.println(loopSwagger);
    }
}
