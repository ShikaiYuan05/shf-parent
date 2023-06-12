package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.en.HouseImageType;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 包名:com.atguigu.controller
 * 日期2023-06-03  11:23
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    private static final String PAGE_UPLOAD_SHOW = "house/upload";
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;

    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Integer houseId, @PathVariable("type") Integer type, Model model){
        //将houseId和type存入request域中
        model.addAttribute("houseId",houseId);
        model.addAttribute("type",type);
        //显示上传图片的页面
        return PAGE_UPLOAD_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable("houseId") Integer houseId, @PathVariable("type") Integer type){
        try {
            //1. 将客户端上传的图片保存到七牛云服务器:要求图片不能重名、要求图片保存的目录至少有两级随机目录
            //1.1 生成一个唯一的文件名
            String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
            //1.2 生成随机的两级目录
            String randomDirPath = FileUtil.getRandomDirPath(2, 3);
            //1.3 拼接图片在七牛云的保存路径
            String filePath = randomDirPath + uuidName;
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),randomDirPath + uuidName);
            //2. 将图片的访问路径保存到数据库的hse_house_image表中
            //2.1 获取图片的访问路径
            String imageUrl = QiniuUtils.getUrl(filePath);
            //2.2 调用业务层的方法将图片的访问路径保存到数据库中
            HouseImage houseImage = new HouseImage();
            houseImage.setHouseId(Long.valueOf(houseId));
            //设置图片在七牛云中存储的路径
            houseImage.setImageName(filePath);
            houseImage.setImageUrl(imageUrl);
            houseImage.setType(type);
            houseImageService.insert(houseImage);
            //3. 如果这个房源没有默认图片，则将这张上传的图片作为它的默认图片，只能是房源图片作为默认图片
            House house = houseService.findById(houseId);
            String defaultImageUrl = house.getDefaultImageUrl();
            if (type.equals(HouseImageType.HOUSE_SOURCE.getTypeCode())) {
                if (defaultImageUrl == null || "".equals(defaultImageUrl)) {
                    //说明当前房源没有默认图片
                    house.setDefaultImageUrl(imageUrl);
                    //修改
                    houseService.update(house);
                }
            }
            //4. 向客户端响应上传成功
            return Result.ok();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Integer houseId,@PathVariable("id") Integer id){
        //1. 根据id查询图片信息
        HouseImage houseImage = houseImageService.findById(id);
        System.out.println("此步正常houseImage = " + houseImage);
        //2. 调用七牛云的工具类的方法根据 图片在七牛云中的存储路径 去删除图片
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //3. 删除数据库中的图片信息
        houseImageService.delete(id);
        //4. 判断当前图片是否是房源的默认图片，如果是，则将房源的默认图片设置为null
        //4.1 根据houseId查询房源信息
        House house = houseService.findById(houseId);
        //4.2 判断当前图片是否是房源的默认图片
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            //说明当前图片是房源的默认图片
            house.setDefaultImageUrl("");
            //修改
            houseService.update(house);
        }
        //5. 重定向到房源图片详情页
        return "redirect:/house/"+houseId;
    }
}
