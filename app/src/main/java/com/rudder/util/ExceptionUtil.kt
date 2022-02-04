package com.rudder.util

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend


class ExceptionUtil {
    companion object{

        //retrofit 통신에서만 사용할 것
        // 22 02 04 merge(main-tkl)
        suspend fun <FunctionParam,FunctionReturn,Api> retryWhenException(function: KFunction<Deferred<FunctionReturn>>, param:FunctionParam, instance: Api): FunctionReturn {

            return try {
                if (param==null){
                    function.callSuspend(instance).await()
                }else{
                    function.callSuspend(instance,param).await()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("here!",param.toString())
                retryWhenException(function,param,instance)
            }
        }
    }
}