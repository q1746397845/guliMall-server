package com.lt.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.lt.gulimall.common.validator.ListValue;
import com.lt.gulimall.common.validator.group.AddGroup;
import com.lt.gulimall.common.validator.group.Group;
import com.lt.gulimall.common.validator.group.UpdateGroup;
import com.lt.gulimall.common.validator.group.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 品牌
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(groups = {UpdateGroup.class,UpdateStatusGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "品牌log必须上传",groups = {AddGroup.class})
	@URL(message = "请输入合法的url" ,groups = {AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为空",groups = {AddGroup.class})
	@ListValue(vals = {0,1},groups = {AddGroup.class , UpdateStatusGroup.class,UpdateGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "首字母必须填写",groups = {AddGroup.class})
	@Pattern(regexp="^[a-zA-Z]$" ,message = "首字母必须是A-Z之前的字母",groups = {AddGroup.class,UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "序号必须填写",groups = {AddGroup.class})
	@Min(value = 0,message = "序号必须大于0",groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;

}
