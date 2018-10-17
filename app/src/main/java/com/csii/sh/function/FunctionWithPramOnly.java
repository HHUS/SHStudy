package com.csii.sh.function;

/**
 * Created by on 2017/10/15.
 */

public abstract class FunctionWithPramOnly<Param> extends AbsFunction {
    public FunctionWithPramOnly(String functionName) {
        super(functionName);
    }

    public abstract void function(Param data);
}
