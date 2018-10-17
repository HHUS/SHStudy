package com.csii.sh.function;

/**
 * Created by on 2017/10/15.
 */

public abstract class FunctionWithParamAndResult<Result,Params> extends AbsFunction {
    public FunctionWithParamAndResult(String functionName) {
        super(functionName);
    }


    public abstract Result function(Params data);
}
