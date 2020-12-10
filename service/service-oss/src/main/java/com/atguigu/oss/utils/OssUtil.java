package com.atguigu.oss.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.StorageClass;
import com.atguigu.common_utils.ResultCode;
import com.atguigu.common_utils.exception.GuLiException;
import com.atguigu.oss.constant.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author Administrator
 * @CreateTime 2020-11-22
 * @Description
 */
public class OssUtil {

    // 获取阿里云存储相关的常量
    private static String endpoint = ConstantPropertiesUtil.ENDPOINT;
    private static String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
    private static String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
    private static String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
    private static String fileHost = ConstantPropertiesUtil.FILE_HOST;

    private static String url = "http://" + bucketName + "." + endpoint + "/";

    public static String upload2Aliyun(MultipartFile file) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String uploadUrl = null;

        try {
            // 判断bucket存储空间是否存在，存在直接获取，否则需要新建
            if (!ossClient.doesBucketExist(bucketName)) {
                // 创建CreateBucketRequest对象。
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);

                // 如果创建存储空间的同时需要指定存储类型以及数据容灾类型, 可以参考以下代码。
                // 此处以设置存储空间的存储类型为标准存储为例。
                createBucketRequest.setStorageClass(StorageClass.Standard);
                // 默认情况下，数据容灾类型为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请替换为DataRedundancyType.ZRS。
                // createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS)
                // 设置存储空间的权限为公共读，默认是私有。
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                // 设置存储空间的存储类型为低频访问类型，默认是标准类型。
                createBucketRequest.setStorageClass(StorageClass.IA);
                // 新建存储空间
                ossClient.createBucket(createBucketRequest);
            }

            // 获取文件上传流
            InputStream inputStream = file.getInputStream();

            //构建日期路径：avatar/2019/02/26/文件名
            String filePath = new DateTime().toString("yyyy/MM/dd");

            // 文件后缀名
            String filename = file.getOriginalFilename();
            String suffix = filename.substring(filename.lastIndexOf("."));
            // 设置文件名：UUID.后缀名
            String newName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            String fileUrl = fileHost + "/" + filePath + "/" + newName;

            // 文件上传到阿里云
            ossClient.putObject(bucketName, fileUrl, inputStream);

            // 获取文件url地址
            uploadUrl = url + fileUrl;

            ossClient.shutdown();
        } catch (IOException e) {
            throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR, "文件上传失败");
        }

        return uploadUrl;

    }

    public static void delete2Aliyun(String fileUrl) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String filename = fileUrl.replaceAll(url, "");
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹
        ossClient.deleteObject(bucketName, filename);

        // 关闭OSSClient
        ossClient.shutdown();

    }
}
