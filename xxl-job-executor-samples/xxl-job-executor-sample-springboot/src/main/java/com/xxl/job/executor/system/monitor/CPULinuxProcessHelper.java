package com.xxl.job.executor.system.monitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

// Sample Java program to print highest CPU process in a linux system
// Note that this program itself may be reported as the top process!
public class CPULinuxProcessHelper {

    public static double getTotalCpuRate() throws Exception {
        Process process = Runtime.getRuntime().exec("ps -e -o pcpu");
        BufferedReader r =  new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        double result = 0.0;
        r.readLine(); // first line skip
        while((line=r.readLine())!=null) {
            result += Double.parseDouble(line.trim());
        }
        return result;
    }
}
