package cn.stylefeng.guns.core.request;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 返给前端的数据格式
 *
 * @author yaoliguo
 * @date 2019-05-04 13:26
 * ©2018 版权所有
 */
public class ResultData extends ResponseData {

    public static SuccessResponseData success(Object object) {

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pagination = new HashMap<>();

        if (object instanceof Page) {
            Page page = (Page) object;
            List records = page.getRecords();

            pagination.put("current", page.getCurrent());
            pagination.put("total", page.getTotal());
            pagination.put("pageSize", page.getSize());
            pagination.put("pages", page.getPages());

            map.put("pagination", pagination);
            map.put("list", records);
            return new SuccessResponseData(map);
        }

        return new SuccessResponseData(object);
    }

}
