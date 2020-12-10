package com.atguigu.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.utils.OssUtil;
import org.junit.Test;

/**
 * @Author Administrator
 * @CreateTime 2020-11-23
 * @Description
 */
public class OssTest {

    @Test
    public void test01(){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build("oss-cn-shenzhen.aliyuncs.com", "LTAI4G3qzhwRdA8hDEagoTRs", "hb2c1Vnx1W6biaB0IsCwaVwcvXnvUm");
        String url = "http://gili-online.oss-cn-shenzhen.aliyuncs.com/avatar/2020/11/23/0d3eff33fe3643fbb996daad5f397145.png";
        String fileUrl = url.replace("http://gili-online.oss-cn-shenzhen.aliyuncs.com/","");
        System.out.println(fileUrl);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹
        ossClient.deleteObject("gili-online", fileUrl);
        // 关闭OSSClient
        ossClient.shutdown();

    }
}
