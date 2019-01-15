package com.xiaohes.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author by lei
 * @date 2019-1-14 19:13
 */
@RestController
public class helloController {

    @Value("${xiaohes.providerName}")
    String serviceId;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hi")
    public String hi(){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if(instances == null || instances.isEmpty()) {
            System.out.println("instances is null ================");
            return null;
        }
        ServiceInstance serviceInstance = instances.get(0);
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
        System.out.println("请求url:"+url);
        String name = restTemplate.getForObject(url + "/getName", String.class);
        return "Hello World!"+name;
    }

}
