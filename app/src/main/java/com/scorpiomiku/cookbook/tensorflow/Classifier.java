package com.scorpiomiku.cookbook.tensorflow;

import com.baidu.aip.client.BaseClient;
import com.baidu.aip.error.AipError;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/17.
 */

public class Classifier extends BaseClient {
    public Classifier(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }

    public JSONObject dishDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);

        String base64Content = Base64Util.encode(image);

        request.addBody("image", base64Content);

        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.DISH_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 菜品识别接口
     * 该请求用于菜品识别。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的菜品名称、卡路里信息、置信度。
     *
     * @param image   - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     *                options - options列表:
     *                top_num 返回预测得分top结果数，默认为5
     * @return JSONObject
     */
    public JSONObject dishDetect(String image, HashMap<String, String> options) {
        try {
            byte[] imgData = Util.readFileByBytes(image);
            return dishDetect(imgData, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 植物识别接口
     * 该请求用于识别一张图片。即对于输入的一张图片（可正常解码，且长宽比适宜），输出植物识别结果。
     *
     * @param image   - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     *                options - options列表:
     * @return JSONObject
     */
    public JSONObject plantDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);

        String base64Content = Base64Util.encode(image);

        request.addBody("image", base64Content);

        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.PLANT_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 植物识别接口
     * 该请求用于识别一张图片。即对于输入的一张图片（可正常解码，且长宽比适宜），输出植物识别结果。
     *
     * @param image   - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     *                options - options列表:
     * @return JSONObject
     */
    public JSONObject plantDetect(String image, HashMap<String, String> options) {
        try {
            byte[] imgData = Util.readFileByBytes(image);
            return plantDetect(imgData, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }
}
