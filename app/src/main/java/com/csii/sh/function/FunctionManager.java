package com.csii.sh.function;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by on 2017/10/15.
 * 添加  与  调用
 */

public class FunctionManager {

    private static final FunctionManager ourInstance = new FunctionManager();

    public static FunctionManager getInstance() {
        return ourInstance;
    }

    private FunctionManager() {
        mFunctionNoParamNoResult = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
        mFunctionWithPramOnly = new HashMap<>();
        mFunctionWithResultOnly = new HashMap<>();
    }


    private HashMap<String, FunctionNoParamNoResult> mFunctionNoParamNoResult;
    private HashMap<String, FunctionWithParamAndResult> mFunctionWithParamAndResult;
    private HashMap<String, FunctionWithPramOnly> mFunctionWithPramOnly;
    private HashMap<String, FunctionWithResultOnly> mFunctionWithResultOnly;


    public FunctionManager addFunction(FunctionNoParamNoResult functionNoParamNoResult) {
        mFunctionNoParamNoResult.put(functionNoParamNoResult.mFunctionName, functionNoParamNoResult);
        return this;
    }


    public void invokeFunc(String funcName) {

        if (TextUtils.isEmpty(funcName)) {
            return;
        }
        if (mFunctionNoParamNoResult != null) {
            FunctionNoParamNoResult f = mFunctionNoParamNoResult.get(funcName);
            if (f != null) {
                f.function();
            } else {
                try {
                    throw new FunctionException("has no function" + funcName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public FunctionManager addFunction(FunctionWithResultOnly functionWithResultOnly) {
        mFunctionWithResultOnly.put(functionWithResultOnly.mFunctionName, functionWithResultOnly);
        return this;
    }


    public <Result> Result invokeFunc(String funcName, Class<Result> c) {

        if (TextUtils.isEmpty(funcName)) {
            return null;
        }
        if (mFunctionWithResultOnly != null) {
            FunctionWithResultOnly f = mFunctionWithResultOnly.get(funcName);
            if (f != null) {
                if (c != null) {
                    return c.cast(f.function());
                } else {
                    f.function();
                }

            }

        } else {
            try {
                throw new FunctionException("has no function" + funcName);
            } catch (FunctionException e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    public FunctionManager addFunction(FunctionWithParamAndResult functionWithParamAndResult) {
        mFunctionWithParamAndResult.put(functionWithParamAndResult.mFunctionName, functionWithParamAndResult);
        return this;
    }

    public <Result, Param> Result invokeFunc(String funcName, Param data, Class<Result> c) {

        if (TextUtils.isEmpty(funcName)) {
            return null;
        }
        if (mFunctionWithParamAndResult != null) {
            FunctionWithParamAndResult f = mFunctionWithParamAndResult.get(funcName);
            if (f != null) {
                if (c != null) {
                    return c.cast(f.function(data));
                } else {
                    f.function(data);
                }

            }

        } else {
            try {
                throw new FunctionException("has no function" + funcName);
            } catch (FunctionException e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    public FunctionManager addFunction(FunctionWithPramOnly functionWithPramOnly) {
        mFunctionWithPramOnly.put(functionWithPramOnly.mFunctionName, functionWithPramOnly);
        return this;
    }

    public <Param> void invokeFunc(String funcName, Param data) {

        if (TextUtils.isEmpty(funcName)) {
            return;
        }
        if (mFunctionWithPramOnly != null) {
            FunctionWithPramOnly f = mFunctionWithPramOnly.get(funcName);
            if (f != null) {
                f.function(data);
            }

        } else {
            try {
                throw new FunctionException("has no function" + funcName);
            } catch (FunctionException e) {
                e.printStackTrace();
            }
        }
        return;

    }

}


