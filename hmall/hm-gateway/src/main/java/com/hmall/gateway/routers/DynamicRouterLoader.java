package com.hmall.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicRouterLoader {

    private final NacosConfigManager nacosConfigManager;
    private final RouteDefinitionWriter routeDefinitionWriter;

    private final String dataId = "gateway-routes.json";

    private final static String group = "DEFAULT_GROUP";

    private final Set<String> routerIds = new HashSet<>();

    @PostConstruct
    public void initRouterConfigListener() {
        try {
            String configInfo = nacosConfigManager.getConfigService()
                    .getConfigAndSignListener(dataId, group, 5000, new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return null;
                        }

                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            updateConfigInfo(configInfo);
                        }
                    });
            updateConfigInfo(configInfo);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }


    }

    public void updateConfigInfo(String configInfo) {

        log.debug("监听到路由信息 {}", configInfo);

        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);

        for (String routerId : routerIds) {
            routeDefinitionWriter.delete(Mono.just(routerId)).subscribe();
        }
        routerIds.clear();

        for (RouteDefinition routeDefinition : routeDefinitions) {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            routerIds.add(routeDefinition.getId());

        }



    }

}
