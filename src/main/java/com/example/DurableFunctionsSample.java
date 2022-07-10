package com.example;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.durabletask.DurableTaskClient;
import com.microsoft.durabletask.OrchestrationRunner;
import com.microsoft.durabletask.Task;
import com.microsoft.durabletask.azurefunctions.DurableActivityTrigger;
import com.microsoft.durabletask.azurefunctions.DurableClientContext;
import com.microsoft.durabletask.azurefunctions.DurableClientInput;
import com.microsoft.durabletask.azurefunctions.DurableOrchestrationTrigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DurableFunctionsSample {
    // クライアント関数。オーケストレーター関数を開始する
    @FunctionName("StartHelloCities")
    public HttpResponseMessage startHelloCities(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET})HttpRequestMessage<Optional<String>> req,
            @DurableClientInput(name = "durableContext")DurableClientContext durableContext,
            final ExecutionContext context
            ) {
        DurableTaskClient client = durableContext.getClient();
//        String instanceId = client.scheduleNewOrchestrationInstance("HelloCities");
        String instanceId = client.scheduleNewOrchestrationInstance("FanOutFanIn");
        context.getLogger().info("Created new Java orchestration with instance ID = " + instanceId);
        return durableContext.createCheckStatusResponse(req, instanceId);
    }

    // オーケストレーター関数。アクションが実行される方法とアクセスの実行順序を書く
//    @FunctionName("HelloCities")
//    public String helloCitiesOrchestrator(@DurableOrchestrationTrigger(name = "runtimeState") String runtimeState) {
//        return OrchestrationRunner.loadAndRun(runtimeState, ctx -> {
//            String result = "";
//            result += ctx.callActivity("SayHello", "Tokyo", String.class).await() + ", ";
//            result += ctx.callActivity("SayHello", "London", String.class).await() + ", ";
//            result += ctx.callActivity("SayHello", "Seattle", String.class).await();
//            return result;
//        });
//    }

    @FunctionName("FanOutFanIn")
    public String fanOutFanInOrchestrator(
            @DurableOrchestrationTrigger(name = "runtimeState") String runtimeState
    ) {
        return OrchestrationRunner.loadAndRun(runtimeState, ctx -> {

            List<?> batch = ctx.callActivity("F1", List.class).await();

            List<Task<Integer>> parallelTasks = batch.stream()
                    .map(i -> ctx.callActivity("F2", i, Integer.class))
                    .collect(Collectors.toList());

            List<Integer> results = ctx.allOf(parallelTasks).await();
            return results.stream().reduce(0, Integer::sum);
        });
    }

    @FunctionName("F1")
    public List<Integer> f1(@DurableActivityTrigger(name = "f1name") String name) {
        return new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));
    }

    @FunctionName("F2")
    public Integer f2(@DurableActivityTrigger(name = "f2name") int i, final ExecutionContext context) {
        context.getLogger().info("F2 with i = [" + i + "]");
        return i * 3;
    }

    // アクティブティ関数。作業を実行し、必要に応じて値を返す。
//    @FunctionName("SayHello")
//    public String sayHello(@DurableActivityTrigger(name = "name") String name) {
//        return String.format("Hello %s!", name);
//    }
}
