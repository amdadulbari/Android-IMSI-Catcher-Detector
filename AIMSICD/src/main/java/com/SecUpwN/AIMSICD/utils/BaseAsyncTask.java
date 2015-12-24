/* Android IMSI-Catcher Detector | (c) AIMSICD Privacy Project
 * -----------------------------------------------------------
 * LICENSE:  http://git.io/vki47 | TERMS:  http://git.io/vki4o
 * -----------------------------------------------------------
 */
package com.SecUpwN.AIMSICD.utils;


import android.app.Activity;
import android.os.AsyncTask;

import com.SecUpwN.AIMSICD.AppAIMSICD;

import java.lang.ref.WeakReference;

import io.freefair.android.injection.app.InjectionAppCompatActivity;

public abstract class BaseAsyncTask<TParams, TProgress, TResult> extends AsyncTask<TParams, TProgress, TResult> {
    protected AppAIMSICD mApp;
    protected WeakReference<InjectionAppCompatActivity> mWeakReferenceActivity;

    public BaseAsyncTask(InjectionAppCompatActivity activity) {
        mWeakReferenceActivity = new WeakReference<>(activity);
        mApp = (AppAIMSICD) activity.getApplication();
        activity.getInjector().inject(this);
    }

    public void setActivity(InjectionAppCompatActivity activity) {
        if (activity == null) {
            mWeakReferenceActivity.clear();
            onActivityDetached();
        } else {
            onActivityAttached();
            mWeakReferenceActivity = new WeakReference<>(activity);
        }
    }

    protected void onActivityAttached() {
    }

    protected void onActivityDetached() {
    }

    @Override
    protected void onPreExecute() {
        mApp.addTask(mWeakReferenceActivity.get(), this);
    }

    @Override
    protected void onPostExecute(TResult result) {
        mApp.removeTask(this);
    }

    @Override
    protected void onCancelled() {
        mApp.removeTask(this);
    }

    protected Activity getActivity() {
        return mWeakReferenceActivity.get();
    }

}
