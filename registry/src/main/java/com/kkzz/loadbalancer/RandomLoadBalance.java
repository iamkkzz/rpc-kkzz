package com.kkzz.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalancer {
    @Override
    public Instance select(List<Instance> instances) {
        Random random = new Random();
        return instances.get(random.nextInt(instances.size()));
    }
}
