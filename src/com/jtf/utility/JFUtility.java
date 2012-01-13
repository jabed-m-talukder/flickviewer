package com.jtf.utility;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JFUtility {

    public JFUtility() {

    }

    public String executeCmd(String cmd, String args) {
        try {
            // Executes the command.
            ProcessBuilder pb = new ProcessBuilder(cmd, args);
            Process process = pb.start();

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String executeCmdRuntime(String cmd) {
        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec(cmd);

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            process.waitFor();

            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String pkgExplorer(Context context) {
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        StringBuffer strPkg = new StringBuffer();

        for(ApplicationInfo pkgInfo : packages) {
//            strPkg.append(pkgInfo.packageName + ":" + pm.getLaunchIntentForPackage(pkgInfo.packageName));
            strPkg.append(pkgInfo.packageName);
            strPkg.append("\n");
//            if (pm.getLaunchIntentForPackage(pkgInfo.packageName) != null) {
//                strPkg.append(pm.getLaunchIntentForPackage(pkgInfo.packageName));
//                strPkg.append("\n");
//            }
        }
        System.out.println(strPkg);
        return strPkg.toString();
    }

}
