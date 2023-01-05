package com.xinnn.reggie.utils;

import com.aliyun.dm20151123.Client;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;

/**
 * 阿里云邮件推送
 */
public class AliyunEmailCode {
    private static final String accessKeyId = "LTAI5t9ivLkyxRSqF8rLo1J3";
    private static final String accessKeySecret = "xEEvK3UWFNrb8tW5FdvbzuGdfDWFkg";
    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static Client createClient() throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dm.aliyuncs.com";
        return new Client(config);
    }

    public static void main(String emailAddress, String code) throws Exception {
        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        Client client = AliyunEmailCode.createClient();
        SingleSendMailRequest singleSendMailRequest = new SingleSendMailRequest()
                .setAccountName("admin@email.zeroxn.com")
                .setAddressType(1)
                .setTagName("checkCode")
                .setToAddress(emailAddress)
                .setSubject("网站验证码")
                .setFromAlias("admin")
                .setReplyAddress("2775714150@qq.com")
                .setReplyToAddress(true)
                .setTextBody("【瑞吉外卖】您的网页验证码为：code，5分钟内有效！".replaceAll("code", code));
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.singleSendMailWithOptions(singleSendMailRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }
    }
}
