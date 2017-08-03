-- phpMyAdmin SQL Dump
-- version 4.6.3
-- https://www.phpmyadmin.net/
--
-- Host: 10.0.0.51
-- Generation Time: 2017-08-03 10:35:12
-- 服务器版本： 10.1.16-MariaDB
-- PHP Version: 5.6.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dmp`
--

-- --------------------------------------------------------

--
-- 表的结构 `admin`
--

CREATE TABLE `admin` (
  `id` int(32) UNSIGNED NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '用户密码',
  `channel_id` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `channel_id`) VALUES
(1, 'admin', 'admin', ''),
(2, 'user', 'user', 'index');

-- --------------------------------------------------------

--
-- 表的结构 `admin_role`
--

CREATE TABLE `admin_role` (
  `admin_id` int(32) UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` int(32) UNSIGNED NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `admin_role`
--

INSERT INTO `admin_role` (`admin_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- 表的结构 `app_channel`
--

CREATE TABLE `app_channel` (
  `id` int(10) UNSIGNED NOT NULL,
  `app_channel_name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `app_channel`
--

INSERT INTO `app_channel` (`id`, `app_channel_name`) VALUES
(11, 'yuzhuang'),
(12, 'meijie01'),
(13, 'meijie02'),
(14, 'meijie03'),
(15, 'meijie04'),
(16, 'meijie05'),
(17, 'meijie06'),
(18, 'vivo'),
(19, 'jinshan');

-- --------------------------------------------------------

--
-- 表的结构 `app_event_set`
--

CREATE TABLE `app_event_set` (
  `id` int(32) UNSIGNED NOT NULL,
  `app_event_name` varchar(100) NOT NULL,
  `app_event_remark` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `app_event_set`
--

INSERT INTO `app_event_set` (`id`, `app_event_name`, `app_event_remark`) VALUES
(30, 'to_notes', '进入手记频道'),
(31, 'to_notes_release', '进入手记发布'),
(32, 'to_notes_details', '进入手记详情'),
(33, 'submit_notes_release', '提交手记发布'),
(34, 'notes_qq_share', '手记分享到企鹅'),
(35, 'notes_wx_share', '手记分享到微信'),
(36, 'notes_wb_share', '手记分享到微博'),
(37, 'notes_like', '手记点赞'),
(38, 'click_notes_comment', '点击手记评论框'),
(39, 'submit_notes_comment', '提交手记评论'),
(40, 'details_notes_comment', '查看手记评论详情'),
(41, 'to_diet_guide', '进入膳食指南'),
(42, 'to_recommend_food', '进入推荐食材'),
(43, 'to_nutrition_recipes', '进入营养食谱'),
(44, 'nutrition_recipes_search', '营养食谱搜索按钮'),
(45, 'diet_wx_share', '周报微信分享'),
(46, 'diet_wxpyq_share', '周报微信朋友圈分享'),
(47, 'diet_qq_share', '周报QQ分享'),
(48, 'diet_qqkj_share', '周报QQ空间分享'),
(49, 'diet_wb_share', '周报微博分享'),
(50, 'to_weekly', '进入周报'),
(51, 'diet_check', '勾选操作'),
(52, 'to_weekly_h5_share_page', '进入周报的H5分享页'),
(53, 'download_weekly_h5_share_page', '点击周报H5分享页的“立即下载”'),
(54, 'diet_task', '完成膳食任务'),
(55, 'wap_index_001', '取名广告(wap首页)'),
(56, 'wap_detaile_top', '取名广告(文章详情头部)'),
(57, 'wap_detaile_bottom', '取名广告(文章详情底部浮动)'),
(58, 'home_page_search', '点击首页“搜索框”'),
(59, 'first_tool', '首页第一个工具'),
(60, 'second_tool', '首页第二个工具'),
(61, 'third_tool', '首页第三个工具'),
(62, 'more_tool', '首页"更多工具"'),
(63, 'home_page_ad_detail', '首页"广告详情"'),
(64, 'cartoon_list', '首页进入漫画列表'),
(65, 'atlas_list', '首页进入图集列表'),
(66, 'recipes_detail', '首页进入食谱详情'),
(67, 'recipes_list', '首页进入食谱列表'),
(68, 'dmgf_detail', '首页进入豆妈工坊详情'),
(69, 'dmgf_list', '首页进入豆妈工坊列表'),
(70, 'feedback', '首页进入意见反馈'),
(71, 'video_index', '进入视频频道页'),
(72, 'video_index_ad_detail', '视频频道——查看广告位详情'),
(73, 'video_index_column', '视频频道——进入栏目列表页'),
(74, 'video_index_detail', '视频频道——进入视频详情页'),
(75, 'video_more', '视频频道——查看更多视频'),
(76, 'video_comment', '视频频道——点击“评论输入框”'),
(77, 'ad_346', '你真的选对沐浴露了吗？'),
(78, 'ad_349', '为宝宝起个好名字');

-- --------------------------------------------------------

--
-- 表的结构 `app_version`
--

CREATE TABLE `app_version` (
  `id` int(10) UNSIGNED NOT NULL,
  `app_version_name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `app_version`
--

INSERT INTO `app_version` (`id`, `app_version_name`) VALUES
(13, '2.2.0'),
(14, '2.0.1'),
(15, '1.2.0'),
(16, '2.1.0'),
(17, '3.2.0'),
(18, '3.3.0'),
(19, '3.0.0'),
(20, '3.1.0'),
(21, '1.1.0'),
(22, '1.0.5');

-- --------------------------------------------------------

--
-- 表的结构 `channel`
--

CREATE TABLE `channel` (
  `id` int(32) UNSIGNED NOT NULL,
  `channel_id` varchar(32) NOT NULL,
  `channel_name` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `channel`
--

INSERT INTO `channel` (`id`, `channel_id`, `channel_name`) VALUES
(10, 'index', 'jybb');

-- --------------------------------------------------------

--
-- 表的结构 `data`
--

CREATE TABLE `data` (
  `id` int(32) UNSIGNED NOT NULL,
  `data_name` varchar(100) DEFAULT NULL COMMENT '数据名',
  `proportion` float(5,2) DEFAULT NULL COMMENT '占比',
  `data_item_id` int(32) UNSIGNED DEFAULT NULL COMMENT '关联数据项id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `data`
--

INSERT INTO `data` (`id`, `data_name`, `proportion`, `data_item_id`) VALUES
(127, 'Windows 7', 40.00, 74),
(128, '1024,768', 30.00, 76),
(130, 'Windows 8', 5.00, 74),
(131, 'Windows xp', 30.00, 74),
(132, '1440,900', 20.00, 76),
(133, '1920,1080', 20.00, 76),
(134, 'zh-CN', 70.00, 77),
(135, 'zh-TW', 8.00, 77),
(139, 'Chrome', 40.00, 79),
(140, 'IE 6', 20.00, 79),
(141, 'IE 7', 10.00, 79),
(142, 'IE 8', 15.00, 79),
(143, '16', 10.00, 80),
(144, '24', 60.00, 80),
(145, '32', 20.00, 80),
(146, '1366,768', 15.00, 76),
(147, '真', 90.00, 84),
(148, '假', 10.00, 84),
(149, '真', 90.00, 83),
(150, '假', 10.00, 83),
(151, '真', 90.00, 85),
(152, '假', 10.00, 85),
(153, '真', 90.00, 86),
(154, '假', 10.00, 86),
(156, 'Windows 10', 5.00, 74),
(159, 'Unknow', 2.00, 74),
(160, 'Macintosh', 3.00, 74),
(161, 'Windows', 15.00, 74),
(162, 'en-US', 20.00, 77),
(163, 'Unknow', 2.00, 77),
(164, 'Safari', 5.00, 79),
(165, 'Unknow', 10.00, 79),
(166, '0', 10.00, 80),
(167, '1600,900', 15.00, 76),
(168, '北京', 3.00, 88),
(169, '天津', 3.00, 88),
(170, '上海', 3.00, 88),
(171, '重庆', 4.00, 88),
(172, '河北', 3.00, 88),
(173, '山西', 3.00, 88),
(174, '辽宁', 3.00, 88),
(175, '吉林', 3.00, 88),
(176, '黑龙江', 3.00, 88),
(177, '江苏', 3.00, 88),
(178, '浙江', 3.00, 88),
(179, '安徽', 3.00, 88),
(180, '福建', 3.00, 88),
(181, '江西', 3.00, 88),
(182, '山东', 3.00, 88),
(183, '河南', 3.00, 88),
(184, '湖北', 3.00, 88),
(185, '湖南', 3.00, 88),
(186, '广东', 3.00, 88),
(187, '海南', 3.00, 88),
(188, '四川', 3.00, 88),
(189, '贵州', 3.00, 88),
(190, '云南', 3.00, 88),
(191, '陕西', 3.00, 88),
(192, '甘肃', 3.00, 88),
(194, '青海', 3.00, 88),
(195, '台湾', 3.00, 88),
(196, '内蒙古', 3.00, 88),
(197, '广西', 3.00, 88),
(198, '西藏', 2.00, 88),
(199, '新疆', 2.00, 88),
(200, '香港', 1.00, 88),
(201, '澳门', 1.00, 88),
(202, '宁夏', 3.00, 88),
(203, '其他', 2.00, 88),
(204, '女', 30.00, 90),
(205, '男', 40.00, 90),
(207, '未填写', 30.00, 90),
(208, '20岁以下', 5.00, 91),
(209, '20-25岁', 15.00, 91),
(210, '25-30岁', 10.00, 91),
(211, '30-35岁', 8.00, 91),
(212, '35-40岁', 7.00, 91),
(213, '40岁以上', 5.00, 91),
(214, '备孕中', 20.00, 92),
(215, '怀孕中', 30.00, 92),
(216, '已出生', 30.00, 92),
(217, '北京', 3.00, 93),
(218, '天津', 3.00, 93),
(219, '上海', 3.00, 93),
(220, '重庆', 4.00, 93),
(221, '河北', 3.00, 93),
(222, '山西', 3.00, 93),
(223, '辽宁', 3.00, 93),
(224, '吉林', 3.00, 93),
(225, '黑龙江', 3.00, 93),
(226, '江苏', 3.00, 93),
(227, '浙江', 3.00, 93),
(228, '安徽', 3.00, 93),
(229, '福建', 3.00, 93),
(230, '江西', 3.00, 93),
(231, '山东', 3.00, 93),
(232, '河南', 3.00, 93),
(233, '湖北', 3.00, 93),
(234, '湖南', 3.00, 93),
(235, '广东', 3.00, 93),
(236, '海南', 3.00, 93),
(237, '四川', 3.00, 93),
(238, '贵州', 3.00, 93),
(239, '云南', 3.00, 93),
(240, '陕西', 3.00, 93),
(241, '甘肃', 3.00, 93),
(242, '青海', 3.00, 93),
(243, '台湾', 3.00, 93),
(244, '内蒙古', 3.00, 93),
(245, '广西', 3.00, 93),
(246, '西藏', 2.00, 93),
(247, '新疆', 2.00, 93),
(248, '香港', 1.00, 93),
(249, '澳门', 1.00, 93),
(250, '宁夏', 3.00, 93),
(251, '其他', 1.00, 93),
(252, '未填写', 50.00, 91),
(253, '未填写', 20.00, 92),
(254, '未填写', 1.00, 93);

-- --------------------------------------------------------

--
-- 表的结构 `data_item`
--

CREATE TABLE `data_item` (
  `id` int(32) UNSIGNED NOT NULL,
  `data_item_name` varchar(100) DEFAULT NULL COMMENT '数据项名称',
  `hbase_name` varchar(100) DEFAULT NULL COMMENT 'hbase对应列名',
  `exception_range` float(5,2) DEFAULT NULL COMMENT '异常范围，百分比',
  `data_source` varchar(100) DEFAULT NULL COMMENT '数据来源',
  `state` int(1) DEFAULT NULL COMMENT '数据项类型标记 1-软件数据标准，2-硬件数据标准，3-用户属性数据标准'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `data_item`
--

INSERT INTO `data_item` (`id`, `data_item_name`, `hbase_name`, `exception_range`, `data_source`, `state`) VALUES
(74, '操作系统', 'j_sys', 10.00, '家有宝宝', 1),
(76, '分辨率', 'j_resolution', 20.00, '家有宝宝', 2),
(77, '语言环境', 'j_language', 10.00, '家有宝宝', 1),
(79, '浏览器', 'j_broser', 10.00, '家有宝宝', 1),
(80, '屏幕颜色', 'j_color_depth', 10.00, '家有宝宝', 1),
(83, '真假浏览器', 'j_has_lied_browser', 10.00, '家有宝宝', 1),
(84, '真假语言', 'j_has_lied_languages', 10.00, '家有宝宝', 1),
(85, '真假系统', 'j_has_lied_os', 10.00, '家有宝宝', 1),
(86, '真假分辨率', 'j_has_lied_resolution', 10.00, '家有宝宝', 1),
(88, '城市分布', 'province', 10.00, '家有宝宝', 1),
(90, '性别', 'sex', 10.00, '家有宝宝', 3),
(91, '年龄', 'birthyear,birthmonth,birthday', 10.00, '家有宝宝', 3),
(92, '状态', 'state', 10.00, '家有宝宝', 3),
(93, '地域', 'areaprovince', 10.00, '家有宝宝', 3);

-- --------------------------------------------------------

--
-- 表的结构 `disable`
--

CREATE TABLE `disable` (
  `id` int(32) UNSIGNED NOT NULL,
  `website_id` int(32) UNSIGNED DEFAULT NULL,
  `channel_id` varchar(32) DEFAULT NULL,
  `channel_name` varchar(100) DEFAULT NULL,
  `disable_link` varchar(100) DEFAULT NULL,
  `state` tinyint(1) DEFAULT '0' COMMENT '0禁用，1启用',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `enable_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `disable_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `dmp_uv_201612`
--

CREATE TABLE `dmp_uv_201612` (
  `country` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `ctime` varchar(100) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `j_broser` varchar(100) DEFAULT NULL,
  `j_color_depth` varchar(100) DEFAULT NULL,
  `j_has_lied_browser` varchar(100) DEFAULT NULL,
  `j_has_lied_languages` varchar(100) DEFAULT NULL,
  `j_has_lied_os` varchar(100) DEFAULT NULL,
  `j_has_lied_resolution` varchar(100) DEFAULT NULL,
  `j_language` varchar(100) DEFAULT NULL,
  `j_resolution` varchar(100) DEFAULT NULL,
  `j_sys` varchar(100) DEFAULT NULL,
  `j_timezone_offset` varchar(100) DEFAULT NULL,
  `j_referrer` varchar(1000) DEFAULT NULL,
  `j_url` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `extension`
--

CREATE TABLE `extension` (
  `id` int(32) UNSIGNED NOT NULL,
  `website_id` int(32) UNSIGNED DEFAULT NULL,
  `channel_id` varchar(32) DEFAULT NULL,
  `channel_name` varchar(100) DEFAULT NULL,
  `extension_link` varchar(100) DEFAULT NULL,
  `state` tinyint(1) DEFAULT '0' COMMENT '0禁用，1启用',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `enable_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `disable_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `extension`
--

INSERT INTO `extension` (`id`, `website_id`, `channel_id`, `channel_name`, `extension_link`, `state`, `add_time`, `enable_time`, `disable_time`) VALUES
(19, 26, 'index', '首页', 'http://www.jbaobao.com/', 1, '2017-01-09 03:05:03', '2017-01-09 03:12:15', '2017-01-09 03:05:03'),
(20, 27, 'index', '社区', 'http://bbs.jbaobao.com/', 1, '2017-01-09 03:05:17', '2017-01-09 03:12:16', '2017-01-09 03:05:17'),
(21, 28, 'index', '电商', 'http://hfx.jbaobao.com/', 1, '2017-01-09 03:18:46', '2017-01-09 03:18:59', '2017-01-09 03:18:46'),
(22, 29, 'index', 'WAP首页', 'https://m.jbaobao.com', 1, '2017-02-28 02:20:40', '2017-02-28 02:20:55', '2017-02-28 02:20:40'),
(23, 30, 'index', '30天瘦回孕前', 'http://www.wapzo.cn/H5/index.html', 0, '2017-04-07 08:30:03', '2017-04-07 08:30:25', '2017-07-10 03:54:33'),
(24, 31, 'index', '胖脸婆', 'http://www.wapzo.cn/demo/index.html', 0, '2017-04-13 01:47:58', '2017-04-13 01:48:13', '2017-07-10 03:54:35');

-- --------------------------------------------------------

--
-- 表的结构 `hbase_disable`
--

CREATE TABLE `hbase_disable` (
  `id` bigint(64) UNSIGNED NOT NULL,
  `disable_id` int(32) DEFAULT NULL,
  `disable_link` varchar(1000) DEFAULT NULL,
  `pv` int(32) DEFAULT NULL,
  `uv` int(32) DEFAULT NULL,
  `ip` int(32) DEFAULT NULL,
  `start` bigint(64) DEFAULT NULL,
  `end` bigint(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `model`
--

CREATE TABLE `model` (
  `id` int(32) UNSIGNED NOT NULL COMMENT '模块ID',
  `model_name` varchar(100) NOT NULL COMMENT '模块名',
  `model_url` varchar(100) DEFAULT NULL COMMENT '模块url'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `model`
--

INSERT INTO `model` (`id`, `model_name`, `model_url`) VALUES
(1, '用户管理', 'admin/toAdmin.do'),
(3, '模块管理', 'model/toModel.do'),
(8, '角色管理', 'role/toRole.do'),
(9, '软件标准数据', 'softwareStandard/toSoftwareStandard.do'),
(10, '硬件标准数据', 'hardwareStandard/toHardwareStandard.do'),
(11, '行为标准数据', 'behaviorStandard/toBehaviorStandard.do'),
(12, '网站管理', 'website/toWebsite.do'),
(13, '推广链接', 'extension/toExtensio.do'),
(14, '数据监控', 'monitor/toMonitor.do'),
(15, '渠道管理', 'channel/toChannel.do'),
(16, '禁用链接', 'disable/toDisable.do'),
(18, '用户属性', 'memberAttribute/toMemberAttribute'),
(19, '属性标准数据', 'memberAttributeStandard/toMemberAttributeStandard.do'),
(21, 'APP渠道', 'appChannel/toAppChannel'),
(22, 'APP版本', 'appVersion/toAppVersion'),
(23, '事件设置', 'appEventSet/toAppEventSet');

-- --------------------------------------------------------

--
-- 表的结构 `privilege`
--

CREATE TABLE `privilege` (
  `id` int(32) UNSIGNED NOT NULL,
  `privilege_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `privilege`
--

INSERT INTO `privilege` (`id`, `privilege_name`) VALUES
(1, '增加'),
(2, '删除'),
(3, '修改'),
(4, '访问');

-- --------------------------------------------------------

--
-- 表的结构 `privilege_model`
--

CREATE TABLE `privilege_model` (
  `id` int(32) UNSIGNED NOT NULL,
  `model_id` int(32) UNSIGNED NOT NULL,
  `privilege_id` int(32) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `privilege_model`
--

INSERT INTO `privilege_model` (`id`, `model_id`, `privilege_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(9, 3, 1),
(10, 3, 2),
(11, 3, 3),
(12, 3, 4),
(34, 8, 1),
(33, 8, 2),
(35, 8, 3),
(19, 8, 4),
(38, 9, 1),
(37, 9, 2),
(39, 9, 3),
(36, 9, 4),
(42, 10, 1),
(41, 10, 2),
(43, 10, 3),
(40, 10, 4),
(46, 11, 1),
(45, 11, 2),
(47, 11, 3),
(44, 11, 4),
(50, 12, 1),
(49, 12, 2),
(51, 12, 3),
(48, 12, 4),
(54, 13, 1),
(53, 13, 2),
(55, 13, 3),
(52, 13, 4),
(56, 14, 4),
(59, 15, 1),
(58, 15, 2),
(60, 15, 3),
(57, 15, 4),
(63, 16, 1),
(62, 16, 2),
(64, 16, 3),
(61, 16, 4),
(73, 18, 4),
(76, 19, 1),
(75, 19, 2),
(77, 19, 3),
(74, 19, 4),
(84, 21, 1),
(83, 21, 2),
(85, 21, 3),
(82, 21, 4),
(88, 22, 1),
(87, 22, 2),
(89, 22, 3),
(86, 22, 4),
(92, 23, 1),
(91, 23, 2),
(93, 23, 3),
(90, 23, 4);

-- --------------------------------------------------------

--
-- 表的结构 `role`
--

CREATE TABLE `role` (
  `id` int(32) UNSIGNED NOT NULL COMMENT '角色ID',
  `role_name` varchar(100) NOT NULL COMMENT '角色名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `role`
--

INSERT INTO `role` (`id`, `role_name`) VALUES
(1, '超级管理员'),
(2, '访客');

-- --------------------------------------------------------

--
-- 表的结构 `role_privilege_model`
--

CREATE TABLE `role_privilege_model` (
  `id` int(32) UNSIGNED NOT NULL COMMENT '角色权限模型id',
  `role_id` int(32) UNSIGNED NOT NULL COMMENT '角色ID',
  `privilege_model_id` int(32) UNSIGNED NOT NULL COMMENT '权限id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `role_privilege_model`
--

INSERT INTO `role_privilege_model` (`id`, `role_id`, `privilege_model_id`) VALUES
(899, 2, 56),
(900, 2, 73),
(1005, 1, 46),
(1006, 1, 45),
(1007, 1, 47),
(1008, 1, 44),
(1009, 1, 34),
(1010, 1, 33),
(1011, 1, 35),
(1012, 1, 19),
(1013, 1, 59),
(1014, 1, 58),
(1015, 1, 60),
(1016, 1, 57),
(1017, 1, 54),
(1018, 1, 53),
(1019, 1, 55),
(1020, 1, 52),
(1021, 1, 1),
(1022, 1, 2),
(1023, 1, 3),
(1024, 1, 4),
(1025, 1, 9),
(1026, 1, 10),
(1027, 1, 11),
(1028, 1, 12),
(1029, 1, 92),
(1030, 1, 91),
(1031, 1, 93),
(1032, 1, 90),
(1033, 1, 84),
(1034, 1, 83),
(1035, 1, 85),
(1036, 1, 82),
(1037, 1, 38),
(1038, 1, 37),
(1039, 1, 39),
(1040, 1, 36),
(1041, 1, 56),
(1042, 1, 63),
(1043, 1, 62),
(1044, 1, 64),
(1045, 1, 61),
(1046, 1, 73),
(1047, 1, 88),
(1048, 1, 87),
(1049, 1, 89),
(1050, 1, 86),
(1051, 1, 50),
(1052, 1, 49),
(1053, 1, 51),
(1054, 1, 48),
(1055, 1, 76),
(1056, 1, 75),
(1057, 1, 77),
(1058, 1, 74),
(1059, 1, 42),
(1060, 1, 41),
(1061, 1, 43),
(1062, 1, 40);

-- --------------------------------------------------------

--
-- 表的结构 `website`
--

CREATE TABLE `website` (
  `id` int(32) UNSIGNED NOT NULL,
  `name` varchar(100) NOT NULL,
  `domain_name` varchar(100) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `code` text,
  `state` tinyint(1) DEFAULT '0' COMMENT '0-禁用 1-启用',
  `enable_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `disable_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `website`
--

INSERT INTO `website` (`id`, `name`, `domain_name`, `add_time`, `code`, `state`, `enable_time`, `disable_time`) VALUES
(26, '家有宝宝首页', 'http://www.jbaobao.com', '2017-01-09 03:04:11', '无', 1, '2017-01-09 03:12:10', '2017-01-09 03:04:11'),
(27, '家有宝宝社区', 'http://bbs.jbaobao.com', '2017-01-09 03:04:45', '无', 1, '2017-01-09 03:12:11', '2017-01-09 03:04:45'),
(28, '惠发现', 'http://hfx.jbaobao.com', '2017-01-09 03:18:19', '无', 1, '2017-01-09 03:18:56', '2017-01-09 03:18:19'),
(29, 'WAP家宝宝首页', 'https://m.jbaobao.com', '2017-02-28 02:19:05', '无', 1, '2017-02-28 02:19:29', '2017-02-28 02:19:05'),
(30, '30天瘦回孕前', 'http://www.wapzo.cn/H5/index.html', '2017-04-07 08:29:31', '无', 0, '2017-04-07 08:30:19', '2017-07-10 03:54:33'),
(31, '免费的30天产后减肥课 我不再做“胖脸婆”', 'http://www.wapzo.cn/demo/index.html', '2017-04-13 01:47:21', '无', 0, '2017-04-13 01:48:10', '2017-07-10 03:54:35');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `admin_role`
--
ALTER TABLE `admin_role`
  ADD UNIQUE KEY `admin_role_adminId_roleId_uk` (`admin_id`,`role_id`),
  ADD KEY `admin_role_role_id_fk` (`role_id`);

--
-- Indexes for table `app_channel`
--
ALTER TABLE `app_channel`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_event_set`
--
ALTER TABLE `app_event_set`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_version`
--
ALTER TABLE `app_version`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `channel_id` (`channel_id`);

--
-- Indexes for table `data`
--
ALTER TABLE `data`
  ADD PRIMARY KEY (`id`),
  ADD KEY `data_item_id` (`data_item_id`);

--
-- Indexes for table `data_item`
--
ALTER TABLE `data_item`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `disable`
--
ALTER TABLE `disable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `extension_ibfk_1` (`website_id`);

--
-- Indexes for table `extension`
--
ALTER TABLE `extension`
  ADD PRIMARY KEY (`id`),
  ADD KEY `extension_ibfk_1` (`website_id`);

--
-- Indexes for table `hbase_disable`
--
ALTER TABLE `hbase_disable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `model`
--
ALTER TABLE `model`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `privilege`
--
ALTER TABLE `privilege`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `privilege_model`
--
ALTER TABLE `privilege_model`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `model_id` (`model_id`,`privilege_id`),
  ADD KEY `privilege_model_model_id_fk` (`model_id`),
  ADD KEY `privilege_model_privilege_id_fk` (`privilege_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role_privilege_model`
--
ALTER TABLE `role_privilege_model`
  ADD PRIMARY KEY (`id`),
  ADD KEY `role_provilege_model_role_id_fk` (`role_id`),
  ADD KEY `role_provilege_model_prvilege_model_id_fk` (`privilege_model_id`);

--
-- Indexes for table `website`
--
ALTER TABLE `website`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID', AUTO_INCREMENT=3;
--
-- 使用表AUTO_INCREMENT `app_channel`
--
ALTER TABLE `app_channel`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- 使用表AUTO_INCREMENT `app_event_set`
--
ALTER TABLE `app_event_set`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;
--
-- 使用表AUTO_INCREMENT `app_version`
--
ALTER TABLE `app_version`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
--
-- 使用表AUTO_INCREMENT `channel`
--
ALTER TABLE `channel`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- 使用表AUTO_INCREMENT `data`
--
ALTER TABLE `data`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=255;
--
-- 使用表AUTO_INCREMENT `data_item`
--
ALTER TABLE `data_item`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;
--
-- 使用表AUTO_INCREMENT `disable`
--
ALTER TABLE `disable`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `extension`
--
ALTER TABLE `extension`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- 使用表AUTO_INCREMENT `hbase_disable`
--
ALTER TABLE `hbase_disable`
  MODIFY `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `model`
--
ALTER TABLE `model`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '模块ID', AUTO_INCREMENT=24;
--
-- 使用表AUTO_INCREMENT `privilege`
--
ALTER TABLE `privilege`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- 使用表AUTO_INCREMENT `privilege_model`
--
ALTER TABLE `privilege_model`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;
--
-- 使用表AUTO_INCREMENT `role`
--
ALTER TABLE `role`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID', AUTO_INCREMENT=3;
--
-- 使用表AUTO_INCREMENT `role_privilege_model`
--
ALTER TABLE `role_privilege_model`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色权限模型id', AUTO_INCREMENT=1063;
--
-- 使用表AUTO_INCREMENT `website`
--
ALTER TABLE `website`
  MODIFY `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- 限制导出的表
--

--
-- 限制表 `admin_role`
--
ALTER TABLE `admin_role`
  ADD CONSTRAINT `admin_role_admin_id_fk` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  ADD CONSTRAINT `admin_role_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

--
-- 限制表 `data`
--
ALTER TABLE `data`
  ADD CONSTRAINT `data_ibfk_1` FOREIGN KEY (`data_item_id`) REFERENCES `data_item` (`id`);

--
-- 限制表 `extension`
--
ALTER TABLE `extension`
  ADD CONSTRAINT `extension_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `website` (`id`) ON DELETE CASCADE;

--
-- 限制表 `privilege_model`
--
ALTER TABLE `privilege_model`
  ADD CONSTRAINT `privilege_model_model_id_fk` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`),
  ADD CONSTRAINT `privilege_model_privilege_id_fk` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`);

--
-- 限制表 `role_privilege_model`
--
ALTER TABLE `role_privilege_model`
  ADD CONSTRAINT `role_provilege_model_prvilege_model_id_fk` FOREIGN KEY (`privilege_model_id`) REFERENCES `privilege_model` (`id`),
  ADD CONSTRAINT `role_provilege_model_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

/*Hbase----Table*/;

Table dmp_pv is ENABLED                                                                                                                              
dmp_pv                                                                                                                                               
COLUMN FAMILIES DESCRIPTION                                                                                                                          
{NAME => 'user', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', VERSIONS => '1', COMPRESSION => 'NONE', MIN_VERSIONS 
=> '0', TTL => 'FOREVER', KEEP_DELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true'}

Table dmp_uv is ENABLED                                                                                                                                                                                                                                                       
dmp_uv                                                                                                                                                                                                                                                                        
COLUMN FAMILIES DESCRIPTION                                                                                                                                                                                                                                                   
{NAME => 'info', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', VERSIONS => '1', COMPRESSION => 'NONE', MIN_VERSIONS => '0', TTL => 'FOREVER', KEEP_DELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true'
}  

Table points is ENABLED                                                                                                                                                                                                                                                       
points                                                                                                                                                                                                                                                                        
COLUMN FAMILIES DESCRIPTION                                                                                                                                                                                                                                                   
{NAME => 'info', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', VERSIONS => '1', COMPRESSION => 'NONE', MIN_VERSIONS => '0', TTL => 'FOREVER', KEEP_DELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true'
} 
