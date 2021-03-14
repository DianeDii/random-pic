package com.diane.oss.controller;
import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import java.net.URL;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dianedi
 * @date 2021/3/14 12:21
 * @Destription
 */
@RestController
public class OssController {

    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "xxx";
    private static String accessKeySecret = "xxxx";
    private static String bucketName = "dianedii-weekly";

    private static String firstKey = "my-first-key";

    @RequestMapping("/random")
    public String getOssPicLink(){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
// 设置参数
            listObjectsRequest.setDelimiter("/");
            listObjectsRequest.setPrefix("test/");
            ObjectListing listing = ossClient.listObjects(listObjectsRequest);
            int sum = listing.getObjectSummaries().size();
//           获取一个从1到sum的随机数
            int randomNum = (int) (Math.random() * (sum -1) +1);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName,listing.getObjectSummaries().get(randomNum).getKey(), HttpMethod.GET);
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 365 );
            req.setExpiration(expiration);
            URL signedUrl = ossClient.generatePresignedUrl(req);
//            System.out.println(signedUrl);
            return signedUrl.toString();


        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
    @RequestMapping("/pic")
    public ModelAndView toPic(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("image");
        return modelAndView;
    }
}
