package com.fly.jmeter;

import com.dxy.keygen.core.DefaultKeyGenerator;
import com.dxy.keygen.core.KeyGenerator;
import com.dxy.keygen.core.config.MachineConfig;
import com.dxy.keygen.core.config.ZookeeperMachineConfig;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * jmeter自定义脚本
 * @author: peijiepang
 * @date 2019-01-16
 * @Description:
 */
public class KeygenJmeterTest extends AbstractJavaSamplerClient {

    private SampleResult results;

    private KeyGenerator keyGenerator;

    @Override
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);
        results = new SampleResult();
        MachineConfig machineConfig = new ZookeeperMachineConfig();
        ((ZookeeperMachineConfig) machineConfig).setConnectionInfo("zk1.host.dxy:2181,zk2.host.dxy:2181,zk3.host.dxy:2181");
        ((ZookeeperMachineConfig) machineConfig).setEnv("dev");
        ((ZookeeperMachineConfig) machineConfig).setBusinessCode("test");
        keyGenerator = new DefaultKeyGenerator(machineConfig);

    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        Long id = keyGenerator.generateKey();
        if(null != id){
            results.setResponseData(id.toString(),"utf-8");
            results.setSuccessful(true);
        }else{
            results.setSuccessful(false);
        }
        return results;
    }
}
