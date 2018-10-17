package com.csii.sh.function;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.csii.sh.BlankFragment;

/**
 * Created by on 2017/10/15.
 */

public class TestInterfaceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void setFunctionForFragment(String tag){

        FunctionManager manager = FunctionManager.getInstance();
        if(tag.equals(BlankFragment.class.getName())){

            manager.addFunction(new FunctionNoParamNoResult(BlankFragment.INTERFACE) {
                @Override
                public void function() {

                    //成功调用无参无返回值的方法
                }
            });
        }else if(tag.equals(BlankFragment.class.getName())){

            manager.addFunction(new FunctionWithResultOnly<String>(BlankFragment.INTERFACE) {
                @Override
                public String function() {
                    return "AAAAAA";
                }
            });
        }else if(tag.equals(BlankFragment.class.getName())){

            manager.addFunction(new FunctionWithParamAndResult<String,String>(BlankFragment.INTERFACE) {

                @Override
                public String function(String data) {

                    return null;
                }
            });
        }


    }
}
