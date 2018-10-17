package com.csii.sh.function;

/**
 * Created by on 2017/10/15.
 */

public abstract class FunctionWithResultOnly<Result> extends AbsFunction {

    public FunctionWithResultOnly(String functionName) {
        super(functionName);
    }


    public abstract Result function();

}
