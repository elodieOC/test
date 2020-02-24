package com.mUI.microserviceUI.proxies;

import com.mUI.microserviceUI.beans.*;
import com.mUI.microserviceUI.beansDTO.AddCategoryDTO;
import com.mUI.microserviceUI.beansDTO.AddShopDTO;
import com.mUI.microserviceUI.beansDTO.CategoryDTO;
import com.mUI.microserviceUI.beansDTO.EditCategoryDTO;
import com.mUI.microserviceUI.config.FeignConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h2>Proxy links clientui to microservice-merchants</h2>
 */
@FeignClient(name = "zuul-server", url = "localhost:9004",contextId = "merchantsProxy", configuration = FeignConfig.class)
@RibbonClient(name = "microservice-merchants")
public interface MicroserviceMerchantsProxy {
    @GetMapping(value = "microservice-merchants/Marchands")
    List<MerchantBean> listMerchants();

    @GetMapping(value = "microservice-merchants/Categories")
    List<CategoryBean> listCategories();

    @GetMapping(value = "microservice-merchants/Icons")
    List<CategoryIconBean> listIcons();

    @PostMapping(value = "microservice-merchants/Marchands/add-shop")
    MerchantBean addShop(@RequestBody AddShopDTO addShopDTO);

    @PostMapping(value = "microservice-merchants/Categories/add-category")
    CategoryBean addCategory(@RequestBody AddCategoryDTO addCategoryDTO);

    @PostMapping(value = "microservice-merchants/Categories/edit")
    CategoryBean editCategory(@RequestBody CategoryDTO categoryDTO);

    @GetMapping( value = "microservice-merchants/Marchands/{id}")
    MerchantBean showShop(@PathVariable("id") Integer id);

    @GetMapping( value = "microservice-merchants/Categories/{id}")
    CategoryBean showCategory(@PathVariable("id") Integer id);

    @GetMapping( value = "microservice-merchants/Category/{id}")
    CategoryBean getCategory(@PathVariable("id") Integer id);

    @GetMapping( value = "microservice-merchants/CategoryIcon/{id}")
    CategoryIconBean getCategoryIcon(@PathVariable("id") Integer id);

    @PostMapping( value = "microservice-merchants/Marchands/edit")
    MerchantBean editShop(@RequestBody MerchantBean merchantBean);

    @PostMapping(value = "microservice-merchants/Marchands/delete/{id}")
    void deleteShop(@PathVariable("id") Integer id);

    @PostMapping(value = "microservice-merchants/Categories/delete/{id}")
    void deleteCategory(@PathVariable("id") Integer id);


}
