package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_TYPE_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList(){
        String key = CACHE_TYPE_KEY ;
        //从redis查缓存
        String shopTypeJson = stringRedisTemplate.opsForValue().get(key);
        //判断是否存在
        if(StrUtil.isNotBlank(shopTypeJson)){
            //存在，直接返回
            List<ShopType> shopTypeList= JSONUtil.toList(shopTypeJson, ShopType.class);
            return Result.ok(shopTypeList);
        }
        //不存在，根据id查询数据库
        List<ShopType> shopTypeList = query().orderByAsc("sort").list();
        //如果数据库中还不存在，报错
//        if(shopTypeList == null){
//            return Result.fail("数据库中不存在店铺类型");
//        }
        //写入redis并返回
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopTypeList));
        return Result.ok(shopTypeList);
    }
}
